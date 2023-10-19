package ru.ekazantsev.nsd_fake_class_generator.data.dto

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "attribute_groups")
class AttributeGroup {
    @DatabaseField(generatedId = true)
    var id: Long = 0

    @DatabaseField
    var title: String? = ""

    @DatabaseField
    var code: String = ""

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    var metaClass: MetaClass = MetaClass()

    @ForeignCollectionField(eager = false)
    var attributes: MutableCollection<AttributeAndGroupLink> = mutableListOf()
}