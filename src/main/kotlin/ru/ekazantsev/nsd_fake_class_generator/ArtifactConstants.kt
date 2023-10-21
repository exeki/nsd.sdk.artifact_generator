package ru.ekazantsev.nsd_fake_class_generator

/**
 * Служба, хранящая основные константы генерируемого артефакта
 */
class ArtifactConstants {

    /**
     * Постфикс для всех сгенерированных классов
     */
    var classNamePostfix = "ScriptDtObject"
        private set

    /**
     * Пакет, куда будут сгружаться все сгенерированные классы
     */
    var packageName: String = "ru.naumen.core.server.script.spi"
        private set

    /**
     * Имя для класса, который будет являться хранилищем метаинформации
     */
    var generatedMetaClassName = "GeneratedMeta"
        private set

    /**
     * Пакет, куда складывается метаинформация
     */
    var generatedMetaClassPackage = "ru.ekazantsev.nsd_generated_fake_classes"
        private set

    /**
     * Имя папки создаваемого проекта
     */
    var projectFolderName = "nsd_fake_classes"
        private set

    /**
     * Путь до попки, в которой будет находиться проект
     */
    var projectPath: String = System.getProperty("user.home") + "\\nsd_sdk\\data"
        private set

    /**
     * Путь до папки проекта
     */
    var projectFolder: String = "$projectPath\\$projectFolderName"
        private set

    /**
     * Папка исходников сгенерированного проекта
     */
    var generatedProjectSrcPath = "$projectFolder\\src\\main\\java"
        private set

    /**
     * Путь до собранного jar в проекте файла
     */
    var newJarPath = "$projectFolder\\build\\libs\\nsd_fake_classes-1.0.0.jar"
        private set

    /**
     * Путь, куда будет доставлен генерируемый jar файл
     */
    var targetJarPath = System.getProperty("user.home") + "\\nsd_sdk\\out\\nsd_fake_classes-1.0.0.jar"
        private set

    /**
     * Получить наименование для класса на основании кода метакласса NSD
     */
    fun getClassNameFromMetacode(code: String): String {
        val strings: MutableList<String> = mutableListOf()
        code.split("$").forEach {
            strings.add(it[0].uppercase() + it.substring(1, it.length))
        }
        strings.add(classNamePostfix)
        return strings.joinToString("\$")
    }

}