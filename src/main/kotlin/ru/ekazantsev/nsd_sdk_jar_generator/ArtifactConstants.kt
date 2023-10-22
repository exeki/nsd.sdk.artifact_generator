package ru.ekazantsev.nsd_sdk_jar_generator

/**
 * Путь до папки, куда будет помещен целевой jar файл
 */

/**
 * Служба, хранящая основные константы генерируемого артефакта
 * @param targetJarFolder путь до папки, куда будет помещен целевой jar файл
 * @param projectPath путь до папки, где будет создана папка с проектом
 */
class ArtifactConstants (
    private val targetJarFolder: String,
    private val projectPath : String
) {

    /**
     * Расставит все параметры по умолчанию
     */
    constructor() : this("${System.getProperty("user.home")}\\nsd_sdk\\out")

    /**
     * Установит projectPath по умолчанию
     * @param targetJarFolder путь до папки, куда будет помещен целевой jar файл
     */
    constructor(targetJarFolder: String) : this(targetJarFolder,  "${System.getProperty("user.home")}\\nsd_sdk\\data")

    /**
     * Постфикс для всех сгенерированных классов
     */
    private val classNamePostfix = "ScriptDtObject"

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
    private val projectFolderName = "nsd_fake_classes"

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

    /**
     * Версия jar файла
     */
    val targetJarVersion = "1.0.0"

    /**
     * Путь до собранного jar в проекте файла
     */
    val newJarPath = "$projectFolder\\build\\libs\\$targetJarName-$targetJarVersion.jar"

    /**
     * Целевой путь до jar файла
     */
    val targetJarPath : String = "$targetJarFolder\\$targetJarName-$targetJarVersion.jar"

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