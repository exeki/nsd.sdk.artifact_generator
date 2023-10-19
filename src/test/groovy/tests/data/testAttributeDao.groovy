package tests.data

import com.j256.ormlite.stmt.PreparedQuery
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Attribute
import ru.ekazantsev.nsd_fake_class_generator.data.dto.AttributeType
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation

import static tests.TestUtils.*

Installation installation = new Installation()
installation.host = 'tetete'

PreparedQuery<Attribute> query = db.attributeDao.queryBuilder().where().eq("type", AttributeType.OBJECT).prepare()
Attribute attr =  db.attributeDao.queryForFirst(query)
logger.info(attr.relatedMetaClass)
