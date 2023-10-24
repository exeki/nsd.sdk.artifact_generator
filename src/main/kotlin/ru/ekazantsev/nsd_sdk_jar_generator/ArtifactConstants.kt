package ru.ekazantsev.nsd_sdk_jar_generator

import java.io.File

/**
 * Отвечает за константы артефакта
 */
class ArtifactConstants {

    /**
     * Задает путь до рабочей директории, где будет сгенерирован проект
     * @param workingDirectoryPath путь до рабочей директории, где будет сгенерирован проект
     */
    constructor(workingDirectoryPath: String) {
        this.workingDirectory = workingDirectoryPath
        this.projectPath = "$workingDirectory\\data"
        this.projectFolder = "$projectPath\\$projectFolderName"
        this.generatedProjectSrcPath = "$projectFolder\\src\\main\\java"
        this.newJarFolder = "$projectFolder\\build\\libs"
        listOf(newJarFolder, generatedProjectSrcPath).forEach { File(it).mkdirs() }
    }

    /**
     * Расставит путь до рабочей директории по умолчанию ${System.getProperty("user.home")}\nsd_sdk
     */
    constructor() : this("${System.getProperty("user.home")}\\nsd_sdk")

    /**
     * Наименование jar файла
     */
    val targetArtifactName = "nsd_fake_classes"

    /**
     * Версия jar файла
     */
    val targetArtifactVersion = "1.0.0"

    /**
     * Целевая группа артефакта
     */
    val targetArtifactGroup = "ru.ekazantsev"

    /**
     * Имя папки создаваемого проекта
     */
    val projectFolderName = "nsd_fake_classes"

    /**
     * Постфикс для всех сгенерированных классов
     */
    val classNamePostfix = "SDO"

    /**
     * Во всех классах или описаниях классов будет этот символ
     */
    val classDelimiter: Char = '_'

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
     * Работая директория для хранения файлов
     */
    val workingDirectory: String

    /**
     * Путь до папки, куда будет помещена папка с проектом
     */
    val projectFolder: String

    /**
     * Путь до проекта
     */
    val projectPath: String

    /**
     * Папка исходников сгенерированного проекта
     */
    val generatedProjectSrcPath: String

    /**
     * Путь до собранного jar в проекте файла
     */
    val newJarFolder: String

    /**
     * Получить наименование для класса на основании кода метакласса NSD
     */
    fun getClassNameFromMetacode(code: String): String {
        val strings: MutableList<String> = mutableListOf()
        code.split("$").forEach {
            strings.add(it[0].uppercase() + it.substring(1, it.length))
        }
        strings.add(classNamePostfix)
        return strings.joinToString(classDelimiter.toString())
    }
}