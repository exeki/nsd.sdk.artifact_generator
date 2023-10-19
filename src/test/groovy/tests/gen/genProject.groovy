package tests.gen

import ru.ekazantsev.nsd_fake_class_generator.src_generation.ProjectGeneratorService
import static tests.TestUtils.*

String path = System.getProperty('user.home') + '\\nsd_sdk\\out'
ProjectGeneratorService gen = new ProjectGeneratorService(path)
logger.info("Приступаю к генерации проекта")
gen.generateProject(db.installationDao.queryForFirst())