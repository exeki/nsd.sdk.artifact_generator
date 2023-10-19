package ru.ekazantsev.nsd_fake_class_generator.data.writers

import ru.ekazantsev.nsd_fake_class_generator.data.DbAccess
import ru.ekazantsev.nsd_fake_class_generator.data.dto.AttributeAndGroupLink
import ru.ekazantsev.nsd_fake_class_generator.data.dto.AttributeGroup
import ru.ekazantsev.nsd_fake_class_generator.data.dto.MetaClass
import ru.ekazantsev.nsd_fake_class_generator.nsd_connector.dto.AttributeGroupDto

/**
 * Служба, записывающая группы атрибутов в хранилище
 */
class AttributeGroupWriter {
    private val db: DbAccess = DbAccess.getInstance()
    private val groupDao = db.attributeGroupDao
    private val attrDao = db.attributeDao
    private val attrGroupLinkDao = db.attributeAndGroupLinkDao

    fun createOrUpdate(meta: MetaClass, groupDto: AttributeGroupDto): AttributeGroup {
        var group: AttributeGroup? = groupDao.queryBuilder()
            .where()
            .eq("code", groupDto.code)
            .and()
            .eq("metaClass_id", meta.id)
            .query()
            .firstOrNull()
        if (group == null) group = AttributeGroup()
        group.code = groupDto.code
        group.title = groupDto.title
        group.metaClass = meta
        groupDao.createOrUpdate(group)
        groupDto.attributes.forEach {
            val attr = attrDao.queryBuilder()
                .where()
                .eq("code", it)
                .and()
                .eq("metaClass_id", meta.id)
                .query()
                .firstOrNull()
            if (attr == null) throw RuntimeException("Cant find attribute $it in database from group ${group.code}")
            var attrGroupLink = attrGroupLinkDao.queryBuilder()
                .where()
                .eq("attribute_id", attr.id)
                .and()
                .eq("group_id", group)
                .query()
                .firstOrNull()
            if(attrGroupLink == null) attrGroupLink = AttributeAndGroupLink(attr, group)
            attrGroupLinkDao.createOrUpdate(attrGroupLink)
        }
        return group
    }
}