package ru.ekazantsev.nsd_sdk_jar_generator

import java.io.File


/**
 * Отвечает за константы артефакта
 */
class ArtifactConstants {

    /**
     * Описывает константы
     */
    companion object {
        /**
         * Наименование артификата по умолчанию
         */
        val defaultArtifactName: String = "nsd_fake_classes"
        /**
         * Префикс для артефакта, если ему задается наименование
         */
        val defaultArtifactPrefix: String = "nsd_"
        /**
         * Постфикс для артефакта, если ему задается наименование
         */
        val defaultArtifactPostfix: String = "_fake_classes"
        /**
         * Версия для артефакта по умолчанию
         */
        val defaultArtifactVersion : String = "1.0.0"
        /**
         * Группа для артефакта по умолчанию
         */
        val defaultArtifactGroup : String = "ru.ekazantsev"
        /**
         * Наименование папки для проекта по умолчанию
         */
        val defaultProjectFolderName : String = "fake_classes_project"
        /**
         * Постфикс для генерируемых классов по умолчанию
         */
        val defaultClassNamePostfix : String = "SDO"
        /**
         * Разделитель меж частиями имени генерируемых классов (класс, тип метакласса и постфикс)  по умолчанию
         */
        val defaultClassDelimiter : Char = '_'
        /**
         * Пакет, куда будут складываться все генерируемые классы  по умолчанию
         */
        val defaultPackageName : String = "ru.naumen.core.server.script.spi"
        /**
         * Пакет, куда будет отправлен класс, содержащий метоинформацию артефакта  по умолчанию
         */
        val defaultGeneratedMetaClassPackage : String = "ru.ekazantsev.nsd_generated_fake_classes"
        /**
         * Наименование класса с метаинформацией по умолчанию
         */
        val defaultGeneratedMetaClassName : String = "GeneratedMeta"
    }

    /**
     * @param workingDirectoryPath путь до рабочей директории, где будет сгенерирован проект
     * @param artifactName префикс артефакта
     */
    constructor(artifactName: String?, workingDirectoryPath: String) {
        this.workingDirectory = workingDirectoryPath
        this.projectPath = "$workingDirectory\\data"
        if(artifactName == null) this.targetArtifactName = defaultArtifactName
        else this.targetArtifactName = defaultArtifactPrefix + artifactName.lowercase() + defaultArtifactPostfix
        this.projectFolder ="$projectPath\\$projectFolderName"
        this.generatedProjectSrcPath = "$projectFolder\\src\\main\\java"
        listOf(generatedProjectSrcPath).forEach { File(it).mkdirs() }
    }

    /**
     * Расставит путь до рабочей директории по умолчанию ${System.getProperty("user.home")}\nsd_sdk
     * @param artifactName префикс артефакта
     */
    constructor(artifactName: String?) : this(artifactName, "${System.getProperty("user.home")}\\nsd_sdk")

    /**
     * Расставит путь до рабочей директории по умолчанию ${System.getProperty("user.home")}\nsd_sdk
     * и укажет префикс артефакта в виде "noname"
     */
    constructor() : this(null)

    /**
     * Наименование jar файла
     */
    val targetArtifactName : String

    /**
     * Версия jar файла
     */
    val targetArtifactVersion = defaultArtifactVersion

    /**
     * Целевая группа артефакта
     */
    val targetArtifactGroup = defaultArtifactGroup

    /**
     * Имя папки создаваемого проекта
     */
    val projectFolderName = defaultProjectFolderName

    /**
     * Постфикс для всех сгенерированных классов
     */
    val classNamePostfix = defaultClassNamePostfix

    /**
     * Во всех классах или описаниях классов будет этот символ
     */
    val classDelimiter: Char = defaultClassDelimiter

    /**
     * Пакет, куда будут сгружаться все сгенерированные классы
     */
    val packageName: String = defaultPackageName

    /**
     * Имя для класса, который будет являться хранилищем метаинформации
     */
    val generatedMetaClassName = defaultGeneratedMetaClassName

    /**
     * Пакет, куда складывается метаинформация
     */
    val generatedMetaClassPackage = defaultGeneratedMetaClassPackage

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