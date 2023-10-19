package ru.ekazantsev.nsd_fake_class_generator.src_generation

/**
 * Служба, хранящая основные константы сгенерируемого проекта
 */
class PackageService {

    /**
     * Постфикс для всех сгенерированных классов
     */
    var classNamePostfix = "ScriptDtObject"

    /**
     * Пакет, куда будут сгружаться все сгенерированные классы
     */
    var packageName: String = "ru.naumen.core.server.script.spi"

    /**
     * Имя для класса, который будет являться хранилищем метаинформации
     */
    var generatedMetaClassName = "GeneratedMeta"

    /**
     * Пакет, куда складывается метаинформация
     */
    var generatedMetaClassPackage = "ru.ekazantsev.nsd_generated_fake_classes"

    /**
     * Имя папки создаваемого проекта
     */
    var projectFolder = "nsd_fake_classes"

    /**
     * Получить наименование для класса на основании кода метакласса NSD
     */
    fun getClassName(code: String): String {
        val strings: MutableList<String> = mutableListOf()
        code.split("$").forEach {
            strings.add(it[0].uppercase() + it.substring(1, it.length))
        }
        strings.add(classNamePostfix)
        return strings.joinToString("\$")
    }

}