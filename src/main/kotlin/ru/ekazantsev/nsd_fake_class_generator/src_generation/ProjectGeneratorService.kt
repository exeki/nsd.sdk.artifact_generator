package ru.ekazantsev.nsd_fake_class_generator.src_generation

import com.squareup.javapoet.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.ekazantsev.nsd_fake_class_generator.data.DbAccess
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Служба, генерирующая весь проект
 * является входной точкой в генерацию проекта
 */
class ProjectGeneratorService(targetFolderPath: String) {
    private var packageService: PackageService = PackageService()
    private val logger: Logger = LoggerFactory.getLogger(ProjectGeneratorService::class.java)
    private val projectPath: String = "$targetFolderPath\\${packageService.projectFolder}"
    private val generatedProjectSrcPath = "$projectPath\\src\\main\\java"
    val db = DbAccess.getInstance()
    private val filesToCopy = setOf(
        "build.gradle",
        "gradlew",
        "gradlew.bat",
        "nsd_classes-1.0.0.jar",
        "settings.gradle"
    )


    /**
     * Запустить сборку проекта при помощи gradle
     * ВАЖНО: gradle уже должен быть установлен на ПК
     */
    private fun runGradleBuild() {
        val processBuilder = ProcessBuilder("${projectPath}\\gradlew.bat", "jar")

        processBuilder.directory(File(projectPath))

        val process = processBuilder.start()
        val inputStream = process.inputStream
        val errorStream = process.errorStream

        val inputReader = inputStream.reader()
        val errorReader = errorStream.reader()

        val inputThread = Thread {
            inputReader.forEachLine { line ->
                println("GRADLE INFO: $line")
            }
        }

        val errorThread = Thread {
            errorReader.forEachLine { line ->
                println("GRADLE ERROR: $line")
            }
        }

        inputThread.start()
        errorThread.start()

        process.waitFor()
        inputThread.join()
        errorThread.join()
    }

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
                    } else Files.copy(Files.newInputStream(file.toPath()), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
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
        val classGenerator = ClassGeneratorService()
        logger.info("Class generation started...")
        inst.metaClasses.forEach {
            val classProto: TypeSpec = classGenerator.generateClassProto(it).build()
            val file = File(generatedProjectSrcPath);
            val fileContent = JavaFile.builder(packageService.packageName, classProto).build();
            fileContent.writeTo(file);
        }
        logger.info("Class generation - done")
        logger.info("Metainfo class generation started...")
        val metaClassGenService = MetainfoClassGeneratorService(classGenerator)
        val meteClazz: TypeSpec = metaClassGenService.generateMetaInfoClass().build()
        val fileContent = JavaFile.builder(packageService.generatedMetaClassPackage, meteClazz)
        fileContent.build().writeTo(File(generatedProjectSrcPath))
        logger.info("Metainfo class generation - done")
        logger.info("Copy files...")
        val classLoader: ClassLoader = Thread.currentThread().contextClassLoader
        filesToCopy.forEach {
            val inputStream: InputStream? = classLoader.getResourceAsStream("projectFiles\\$it")
            if (inputStream != null) Files.copy(
                inputStream,
                File("$projectPath\\$it").toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
            else throw RuntimeException("Cant find resource $it")
        }
        copyResourceDirectoryToLocation("projectFiles\\gradle", "$projectPath\\gradle")
        logger.info("Copy files - done")
        logger.info("Starting building jar...")
        runGradleBuild()
        logger.info("Jar build - done")
        logger.info("Project generation - done")
    }
}