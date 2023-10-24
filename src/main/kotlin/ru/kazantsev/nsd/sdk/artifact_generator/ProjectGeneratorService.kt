package ru.kazantsev.nsd.sdk.artifact_generator

import com.squareup.javapoet.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.kazantsev.nsd.sdk.data.DbAccess
import ru.kazantsev.nsd.sdk.data.dto.Installation
import ru.kazantsev.nsd.sdk.artifact_generator.src_generation.ClassGeneratorService
import ru.kazantsev.nsd.sdk.artifact_generator.src_generation.MetainfoClassGeneratorService
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Служба, генерирующая весь проект
 * является входной точкой в генерацию проекта
 * @param artifactConstants константы генерируемого артефакта (пути, наименования)
 * @param db экземпляр связи с базой данных
 */
class ProjectGeneratorService(private var artifactConstants: ArtifactConstants, private val db: DbAccess) {

    private val logger: Logger = LoggerFactory.getLogger(ProjectGeneratorService::class.java)
    private val classLoader: ClassLoader = Thread.currentThread().contextClassLoader

    /**
     * Копирует папку из ресурсов проекта в целевую директорию
     * @param resourcePath к папке в ресурсах, которую нужно скопировать
     * @param destinationPath целевой путь, куда копировать
     */
    private fun copyResourceDirectory(resourcePath: String, destinationPath: String) {
        val resourceUrl = classLoader.getResource(resourcePath)

        if (resourceUrl != null) {
            val resourceFile = File(resourceUrl.toURI())
            val destinationDirectory = File(destinationPath)

            if (resourceFile.isDirectory) {
                if (!destinationDirectory.exists()) destinationDirectory.mkdirs()

                resourceFile.walkTopDown().forEach { file ->
                    val relativePath = File(resourceFile.toURI()).toPath().relativize(file.toPath())
                    val destinationFile = File(destinationDirectory, relativePath.toString())

                    if (file.isDirectory) {
                        if (!destinationFile.exists()) destinationFile.mkdirs()
                    } else Files.copy(
                        Files.newInputStream(file.toPath()),
                        destinationFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }
        } else throw RuntimeException("Cant find resource $resourcePath")
    }

    /**
     * Скопировать файл с заменой текста
     * @param resourcePath ресурс, который нужно скопировать
     * @param targetFolder целевая папка
     * @param variables значения для змены
     */
    private fun copyResourceAsTemplate(resourcePath: String, targetFolder: String, variables: Map<String, String>) {
        var template = classLoader.getResource(resourcePath)?.readText()
            ?: throw RuntimeException("Cant find $resourcePath file in resources")
        variables.forEach { (key, value) ->
            template = template.replace("\${$key}", value)
        }
        File("$targetFolder\\${File(resourcePath).name}").writeText(template)
    }

    /**
     * Скопировать файл
     * @param resourcePath ресурс, который нужно скопировать
     * @param targetFolder целевая папка
     */
    private fun copyFile(resourcePath: String, targetFolder: String) {
        val inputStream = classLoader.getResourceAsStream(resourcePath)
            ?: throw RuntimeException("Cant find $resourcePath file in resources")
        File("$targetFolder\\${File(resourcePath).name}").writeBytes(inputStream.readAllBytes())
    }

    /**
     * Сгенерировать проект
     * хранилище метаинформации должно быть заранее наполнено
     * @param inst инсталляция, по которой нужно сгенерировать проект
     */
    fun generateProject(inst: Installation) {
        logger.info("Project generation started...")
        val existedProject = File(artifactConstants.projectFolder)
        if (existedProject.exists()) {
            logger.info("Found existed project folder, deleting...")
            existedProject.delete()
            logger.info("Existed project deleting - done")
        }
        val classGenerator = ClassGeneratorService(artifactConstants, db)
        logger.info("Class generation started...")
        inst.metaClasses.forEach {
            val classProto: TypeSpec = classGenerator.generateClassProto(it).build()
            val file = File(artifactConstants.generatedProjectSrcPath);
            val fileContent = JavaFile.builder(artifactConstants.packageName, classProto).build();
            fileContent.writeTo(file);
        }
        logger.info("Class generation - done")
        logger.info("Metainfo class generation started...")
        val metaClassGenService = MetainfoClassGeneratorService(classGenerator, artifactConstants)
        val meteClazz: TypeSpec = metaClassGenService.generateMetaInfoClass().build()
        val fileContent = JavaFile.builder(artifactConstants.generatedMetaClassPackage, meteClazz)
        fileContent.build().writeTo(File(artifactConstants.generatedProjectSrcPath))
        logger.info("Metainfo class generation - done")
        logger.info("Copy files...")

        copyFile("projectFiles\\gradlew", artifactConstants.projectFolder)
        copyFile("projectFiles\\gradlew.bat", artifactConstants.projectFolder)
        copyResourceDirectory("projectFiles\\gradle", "${artifactConstants.projectFolder}\\gradle")
        copyResourceDirectory("projectFiles\\src", "${artifactConstants.projectFolder}\\src")

        copyResourceAsTemplate(
            "projectFiles/build.gradle.kts",
            artifactConstants.projectFolder,
            mapOf(
                "targetArtifactVersion" to artifactConstants.targetArtifactVersion,
                "targetArtifactGroup" to artifactConstants.targetArtifactGroup
            )
        )

        copyResourceAsTemplate(
            "projectFiles/settings.gradle.kts",
            artifactConstants.projectFolder,
            mapOf("targetArtifactName" to artifactConstants.targetArtifactName)
        )

        logger.info("Copy files - done")
        logger.info("Project generation - done")
    }
}