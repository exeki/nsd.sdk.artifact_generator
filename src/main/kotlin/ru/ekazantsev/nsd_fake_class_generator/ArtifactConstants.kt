package ru.ekazantsev.nsd_fake_class_generator

/**
 * Служба, хранящая основные константы генерируемого артефакта
 */
class ArtifactConstants {

    constructor() {
        this.targetJarFolder = "${System.getProperty("user.home")}\\nsd_sdk\\out"
        this.targetJarPath = "$targetJarFolder\\$targetJarName-$targetJarVersion.jar"
    }

    constructor(targetJarFolder: String) {
        this.targetJarFolder = targetJarFolder
        this.targetJarPath = "$targetJarFolder\\$targetJarName-$targetJarVersion.jar"
    }

    /**
     * Постфикс для всех сгенерированных классов
     */
    val classNamePostfix = "ScriptDtObject"

    /**
     * Пакет, куда будут сгружаться все сгенерированные классы
     */
    val packageName: String = "ru.naumen.core.server.script.spi"

    /**
     * Имя для класса, который будет являться хранилищем метаинформации
     */
    val generatedMetaClassName = "GeneratedMeta"

    /**
     * Пакет, куда складывается метаинформация
     */
    val generatedMetaClassPackage = "ru.ekazantsev.nsd_generated_fake_classes"

    /**
     * Имя папки создаваемого проекта
     */
    val projectFolderName = "nsd_fake_classes"

    /**
     * Путь до попки, в которой будет находиться проект
     */
    val projectPath: String = System.getProperty("user.home") + "\\nsd_sdk\\data"

    /**
     * Путь до папки проекта
     */
    val projectFolder: String = "$projectPath\\$projectFolderName"

    /**
     * Папка исходников сгенерированного проекта
     */
    val generatedProjectSrcPath = "$projectFolder\\src\\main\\java"

    /**
     * Наименование jar файла
     */
    val targetJarName = "nsd_fake_classes"

    val targetJarVersion = "1.0.0"

    /**
     * Путь до собранного jar в проекте файла
     */
    val newJarPath = "$projectFolder\\build\\libs\\$targetJarName-$targetJarVersion.jar"

    /**
     * Путь до папки, куда будет помещен целевой jar файл
     */
    val targetJarFolder: String

    /**
     * Целевой путь до jar файла
     */
    val targetJarPath : String

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