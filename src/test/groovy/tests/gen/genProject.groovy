package tests.gen

import ru.ekazantsev.nsd_fake_class_generator.ArtifactConstants
import ru.ekazantsev.nsd_fake_class_generator.services.JarGeneratorService
import ru.ekazantsev.nsd_fake_class_generator.services.ProjectGeneratorService
import static tests.TestUtils.*

JarGeneratorService get = new JarGeneratorService(new ArtifactConstants())
get.generate("PUBLIC_TEST")
//String path = System.getProperty('user.home') + '\\nsd_sdk\\out'
//ProjectGeneratorService gen = new ru.ekazantsev.nsd_fake_class_generator.services.ProjectGeneratorService.ProjectGeneratorService(path)
//logger.info("Приступаю к генерации проекта")
//gen.generateProject(db.installationDao.queryForFirst())