package ru.ekazantsev.nsd_fake_class_generator.services

import com.squareup.javapoet.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.ekazantsev.nsd_fake_class_generator.ArtifactConstants
import ru.ekazantsev.nsd_fake_class_generator.data.DbAccess
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import ru.ekazantsev.nsd_fake_class_generator.services.src_generation.ClassGeneratorService
import ru.ekazantsev.nsd_fake_class_generator.services.src_generation.MetainfoClassGeneratorService
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Служба, генерирующая весь проект
 * является входной точкой в генерацию проекта
 */
class ProjectGeneratorService(private var artifactConstants: ArtifactConstants) {

    private val logger: Logger = LoggerFactory.getLogger(ProjectGeneratorService::class.java)
    val db = DbAccess.getInstance()

    private val filesToCopy = setOf(
        "gradlew",
        "gradlew.bat",
        "nsd_upper_level_classes-1.0.0.jar",
    )

    /**
     * Копирует папку из ресурсов проекта в целевую директорию
     */
    private fun copyResourceDirectoryToLocation(resourcePath: String, destinationPath: String) {
        val classLoader = Thread.currentThread().contextClassLoader
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
     * Сгенерировать проект
     * хранилище метаинформации должно быть заранее наполнено
     */
    fun generateProject(inst: Installation) {
        logger.info("Project generation started...")
        val existedProject = File(artifactConstants.projectFolder)
        if(existedProject.exists()) {
            logger.info("Found existed project folder, deleting...")
            existedProject.delete()
            logger.info("Existed project deleting - done")
        }
        val classGenerator = ClassGeneratorService(artifactConstants)
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
        val classLoader: ClassLoader = Thread.currentThread().contextClassLoader
        filesToCopy.forEach {
            val inputStream: InputStream? = classLoader.getResourceAsStream("projectFiles\\$it")
            if (inputStream != null) Files.copy(
                inputStream,
                File("${artifactConstants.projectFolder}\\$it").toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
            else throw RuntimeException("Cant find resource $it")
        }

        val template = classLoader.getResource("projectFiles/build.gradle")?.readText()
            ?: throw RuntimeException("Cant find build.gradle file in resources")

        val filledTemplate = template
            .replace("\${targetJarVersion}", artifactConstants.targetJarVersion)
            .replace("\${targetJarName}", artifactConstants.targetJarName)
        File("${artifactConstants.projectFolder}\\build.gradle").writeText(filledTemplate)

        copyResourceDirectoryToLocation("projectFiles\\gradle", "${artifactConstants.projectFolder}\\gradle")
        logger.info("Copy files - done")
        logger.info("Project generation - done")
    }
}