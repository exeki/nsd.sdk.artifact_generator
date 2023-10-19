package ru.ekazantsev.nsd_fake_class_generator.data.writers

import ru.ekazantsev.nsd_fake_class_generator.data.DbAccess
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import ru.ekazantsev.nsd_fake_class_generator.data.dto.MetaClass
import ru.ekazantsev.nsd_fake_class_generator.nsd_connector.dto.MetaClassWrapperDto
import java.util.*

/**
 * Служба, записывающая метаклассы в хранилище
 */
class MetaClassWriter {
    private val db: DbAccess = DbAccess.getInstance()
    private val dao = db.metaClassDao
    fun createOrUpdate(inst: Installation, metaDto: MetaClassWrapperDto): MetaClass {
        var meta: MetaClass? = dao.queryBuilder()
            .where()
            .eq("fullCode", metaDto.fullCode)
            .and()
            .eq("installation_id", inst.id)
            .query()
            .firstOrNull()
        if (meta == null) meta = MetaClass()
        meta.installation = inst
        meta.caseCode = metaDto.caseCode
        meta.classCode = metaDto.classCode
        meta.fullCode = metaDto.fullCode
        meta.title = metaDto.title
        meta.lastUpdateDate = Date()
        meta.description = metaDto.description
        meta.hardcoded = metaDto.hardcoded
        meta.hasResponsible = metaDto.hasResponsible
        meta.hasWorkflow = metaDto.hasWorkflow
        meta.parent = dao.queryBuilder()
            .where()
            .eq("fullCode", metaDto.parent)
            .and()
            .eq("installation_id", inst.id)
            .query().firstOrNull()
        dao.createOrUpdate(meta)
        return meta
    }
}