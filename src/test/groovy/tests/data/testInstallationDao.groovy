package tests.data

import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import static tests.TestUtils.*

Installation installation = new Installation()
installation.host = 'tetete'

db.installationDao.create(installation)
logger.info(installation.id.toString())
logger.info(db.installationDao.queryForAll().collect{it.host}.join(', '))
