package ru.ekazantsev.nsd_fake_class_generator.services

import org.slf4j.LoggerFactory
import ru.ekazantsev.nsd_fake_class_generator.data.DbAccess
import ru.ekazantsev.nsd_fake_class_generator.data.writers.AttributeGroupWriter
import ru.ekazantsev.nsd_fake_class_generator.data.writers.AttributeWriter
import ru.ekazantsev.nsd_fake_class_generator.data.writers.InstallationWriter
import ru.ekazantsev.nsd_fake_class_generator.data.writers.MetaClassWriter
import ru.ekazantsev.nsd_fake_class_generator.nsd_connector.FakeApiConnector
import ru.ekazantsev.nsd_basic_api_connector.ConnectorParams
import ru.ekazantsev.nsd_basic_api_connector.HttpException
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import java.util.*

//TODO сделать удаление и архивацию метаклассов и полей
/**
 * Служба, направленная на получение и помещение в хранилище метаинформации из инсталляции
 * @param connectorParams  параметры коннектора, из них будет извлечена и сохранена инсталляция
 * @param metaClassCodes коды метаклассов, которые будут затянуты и сохранены в хранилище
 */
class MetainfoUpdateService(connectorParams: ConnectorParams, val metaClassCodes: MutableSet<String>) {
    private val db: DbAccess = DbAccess.getInstance()
    private val logger = LoggerFactory.getLogger(javaClass)
    private val connector: FakeApiConnector
    private val metaWriter = MetaClassWriter()
    private val attrWriter = AttributeWriter()
    private val groupWriter = AttributeGroupWriter()
    private var irrelevanceTime: Int = 0
    private val constantMeta: Set<String> = setOf("file")
    private val installation: Installation = InstallationWriter().createOrUpdate(connectorParams)
    private val fetchedMeta: MutableSet<String> = mutableSetOf()

    init {
        this.connector = FakeApiConnector(connectorParams)
        this.connector.setInfoLogging(false)
        this.metaClassCodes.addAll(constantMeta)
    }

    fun setIrrelevanceTime(minutes: Int): MetainfoUpdateService {
        this.irrelevanceTime = minutes
        return this
    }

    /**
     * Затянуть и метакласс и все его типы
     */
    private fun fetchMetaClassBranch(code: String) {
        val existed = db.metaClassDao.queryForEq("fullCode", code).lastOrNull()
        if (existed != null) {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.MINUTE, -irrelevanceTime)
            if (calendar.time < existed.lastUpdateDate) {
                logger.info("MetaClass ${existed.fullCode} already relevant")
                return
            }
        }
        try {
            val metaDto = connector.getMetaClassInfo(code)
            val meta = metaWriter.createOrUpdate(installation, metaDto)
            logger.info("MetaClass ${meta.fullCode} written")
            metaDto.attributes.forEach {
                val attr = attrWriter.createOrUpdate(meta, it)
                logger.debug("Attribute ${attr.code} written")
                if (it.relatedMetaClass != null && !metaClassCodes.contains(it.relatedMetaClass) && !fetchedMeta.contains(it.relatedMetaClass)) {
                    logger.info("Adding another metaclass ${it.relatedMetaClass} to fetch from meta: $code, attr: ${attr.code}")
                    metaClassCodes.add(it.relatedMetaClass!!)
                }
            }
            metaDto.attributeGroups.forEach {
                val group = groupWriter.createOrUpdate(meta, it)
                logger.debug("Group ${group.code} written")
            }
            metaDto.children.forEach {
                fetchMetaClassBranch(it)
            }
        } catch (e: HttpException) {
            logger.error("Caught error while getting info about meta $code: ${e.message}")
        } finally {
            fetchedMeta.add(code)
            metaClassCodes.remove(code)
        }
    }

    /**
     * Затянуть метаинформацию по инсталляции
     */
    fun fetchMeta() {
        while (metaClassCodes.size != 0) {
            fetchMetaClassBranch(metaClassCodes.first())
        }
    }
}