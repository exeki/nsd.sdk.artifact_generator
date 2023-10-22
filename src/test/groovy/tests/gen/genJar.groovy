package tests.gen

import ru.ekazantsev.nsd_sdk_data.DbAccess
import ru.ekazantsev.nsd_sdk_jar_generator.ArtifactConstants
import ru.ekazantsev.nsd_sdk_jar_generator.JarGeneratorService

String pathToDb = 'C:\\Users\\ekazantsev\\nsd_sdk\\data\\sdk_meta_store.mv.db'
JarGeneratorService get = new JarGeneratorService(new ArtifactConstants(), new DbAccess(pathToDb))
get.generate("DSO_TEST")