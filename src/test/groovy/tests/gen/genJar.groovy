package tests.gen

import ru.kazantsev.nsd.sdk_data.DbAccess
import ru.kazantsev.nsd.sdk_jar_generator.ArtifactConstants
import ru.kazantsev.nsd.sdk_jar_generator.JarGeneratorService

String pathToDb = 'C:\\Users\\ekazantsev\\nsd_sdk\\data\\sdk_meta_store.mv.db'
JarGeneratorService get = new JarGeneratorService(new ArtifactConstants("DSO_TEST"), new DbAccess(pathToDb))
get.generate()