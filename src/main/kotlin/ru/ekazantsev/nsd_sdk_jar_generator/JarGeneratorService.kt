package ru.ekazantsev.nsd_sdk_jar_generator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.ekazantsev.nsd_sdk_data.DbAccess
import ru.ekazantsev.nsd_sdk_data.dto.Installation
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class JarGeneratorService(private val artifactConstants: ArtifactConstants, private val db: DbAccess) {

    private val logger: Logger = LoggerFactory.getLogger(JarGeneratorService::class.java)
    private val projectGenerator: ProjectGeneratorService = ProjectGeneratorService(artifactConstants, db)

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
                logger.info("GRADLE: $line")
            }
        }

        val errorThread = Thread {
            errorReader.forEachLine { line ->
                logger.error("GRADLE: $line")
            }
        }

        inputThread.start()
        errorThread.start()

        process.waitFor()
        inputThread.join()
        errorThread.join()
    }

    /**
     * Генерирует jar по единственной инсталляции в datasource
     */
    fun generate() {
        val installations = db.installationDao.queryForAll()
        if (installations.size > 1) throw RuntimeException("Found any installations in datasource")
        else if (installations.size == 0) throw RuntimeException("Cant find any installation in datasource")
        val installation: Installation = installations.firstOrNull()!!
        generate(installation)
    }

    /**
     * Генерирует jar по исталляции с указанным ID
     * @param installationId id искомой инсталляции, по которой нужно сгенерировать jar
     */
    fun generate(installationId: String) {
        val installation: Installation = db.installationDao.queryForEq("userId", installationId).firstOrNull()
            ?: throw RuntimeException("Installation $installationId not found in datasource")
        generate(installation)
    }

    /**
     * Генерирует jar по переданной инсталляции
     * @param installation инсталляция, по которой нужно сгенерировать jar
     */
    fun generate(installation: Installation) {
        logger.info("Start jar generation")
        projectGenerator.generateProject(installation)
        logger.info("Starting building jar...")
        runGradleBuild()
        logger.info("Jar build - done")
        logger.info("Jar build - done")
        logger.info("Copying jar to target folder...")
        Files.copy(
            File(artifactConstants.newJarPath).toPath(),
            File(artifactConstants.targetJarPath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
        logger.info("Copying jar - done")
        logger.info("Jar generation - done")
    }

}