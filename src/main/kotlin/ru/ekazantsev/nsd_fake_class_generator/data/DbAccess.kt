package ru.ekazantsev.nsd_fake_class_generator.data

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import ru.ekazantsev.nsd_fake_class_generator.data.dto.*

/**
 * Класс, инициализирующий связь с базой и предоставляющий доступ к данным
 */
class DbAccess private constructor(val dbFilePath: String) {
    companion object {
        private var instance: DbAccess? = null
        /**
         * Получить экземпляр класса
         * @return имеющийся экземпляр или новый, если ранее еще не был создан
         */
        @JvmStatic
        fun getInstance(): DbAccess {
            if(instance == null) instance = DbAccess(System.getProperty("user.home") + "\\nsd_sdk\\data\\sdk_meta_store")
            return instance as DbAccess
        }
    }

    val connection: JdbcConnectionSource = JdbcConnectionSource("jdbc:h2:file:$dbFilePath")
    val installationDao: Dao<Installation, Long>
    val metaClassDao: Dao<MetaClass, Long>
    val attributeDao: Dao<Attribute, Long>
    val attributeGroupDao: Dao<AttributeGroup, Long>
    val attributeAndGroupLinkDao: Dao<AttributeAndGroupLink, Long>

    init {
        TableUtils.createTableIfNotExists(connection, Installation::class.java)
        TableUtils.createTableIfNotExists(connection, MetaClass::class.java)
        TableUtils.createTableIfNotExists(connection, Attribute::class.java)
        TableUtils.createTableIfNotExists(connection, AttributeGroup::class.java)
        TableUtils.createTableIfNotExists(connection, AttributeAndGroupLink::class.java)
        installationDao = DaoManager.createDao(connection, Installation::class.java)
        metaClassDao = DaoManager.createDao(connection, MetaClass::class.java)
        attributeDao = DaoManager.createDao(connection, Attribute::class.java)
        attributeGroupDao = DaoManager.createDao(connection, AttributeGroup::class.java)
        attributeAndGroupLinkDao = DaoManager.createDao(connection, AttributeAndGroupLink::class.java)
    }
}