package ru.ekazantsev.nsd_fake_class_generator.nsd_connector.dto

/**
 * DTO с информацией по группе атрибутов
 */
 class AttributeGroupDto {
    var title: String? = ""
    var code: String = ""
    var attributes: List<String> = listOf()
}