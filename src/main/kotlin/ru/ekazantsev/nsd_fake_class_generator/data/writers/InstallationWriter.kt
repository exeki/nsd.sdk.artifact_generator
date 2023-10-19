package ru.ekazantsev.nsd_fake_class_generator.data.writers

import ru.ekazantsev.nsd_fake_class_generator.data.DbAccess
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import ru.ekazantsev.nsd_basic_api_connector.ConnectorParams
import java.util.Date

/**
 * Служба, записывающая инсталляции в хранилище
 */
class InstallationWriter {
    private val db: DbAccess = DbAccess.getInstance()
    private val dao = db.installationDao
    fun createOrUpdate(connectorParams: ConnectorParams) : Installation {
        var inst: Installation? = dao.queryForEq("host", connectorParams.host).lastOrNull()
        if (inst == null) inst = Installation()
        inst.host = connectorParams.host
        inst.lastUpdateDate = Date()
        dao.createOrUpdate(inst)
        return inst
    }
}