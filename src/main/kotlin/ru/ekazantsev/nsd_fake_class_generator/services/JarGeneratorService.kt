package ru.ekazantsev.nsd_fake_class_generator.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.ekazantsev.nsd_basic_api_connector.ConnectorParams
import ru.ekazantsev.nsd_fake_class_generator.ArtifactConstants
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import ru.ekazantsev.nsd_fake_class_generator.data.writers.InstallationWriter
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class JarGeneratorService(private val artifactConstants: ArtifactConstants) {

    private val logger: Logger = LoggerFactory.getLogger(JarGeneratorService::class.java)
    private val projectGenerator: ProjectGeneratorService = ProjectGeneratorService(artifactConstants)

    /**
     * Запустить сборку проекта при помощи gradle
     * ВАЖНО: gradle уже должен быть установлен на ПК
     */
    private fun runGradleBuild() {
        val processBuilder = ProcessBuilder("${artifactConstants.projectFolder}\\gradlew.bat", "jar")

        processBuilder.directory(File(artifactConstants.projectFolder))

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

    fun generate(installationId: String) {
        logger.info("Start jar generation")
        val params: ConnectorParams = ConnectorParams.byConfigFile(installationId)
        val installation: Installation = InstallationWriter().createOrUpdate(params)
        projectGenerator.generateProject(installation)
        logger.info("Starting building jar...")
        runGradleBuild()
        logger.info("Jar build - done")
        logger.info("Jar build - done")
        logger.info("Copying jar to target folder...")
        Files.copy(File(artifactConstants.newJarPath).toPath(), File(artifactConstants.targetJarPath).toPath(), StandardCopyOption.REPLACE_EXISTING)
        logger.info("Copying jar - done")
        logger.info("Jar generation - done")
    }
}