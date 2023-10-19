package tests.fetchAndWrite

import ru.ekazantsev.nsd_fake_class_generator.data.dto.Attribute
import ru.ekazantsev.nsd_fake_class_generator.data.dto.AttributeGroup
import ru.ekazantsev.nsd_fake_class_generator.data.dto.Installation
import ru.ekazantsev.nsd_fake_class_generator.data.dto.MetaClass
import ru.ekazantsev.nsd_fake_class_generator.nsd_connector.dto.MetaClassWrapperDto
import ru.ekazantsev.nsd_fake_class_generator.data.writers.AttributeGroupWriter
import ru.ekazantsev.nsd_fake_class_generator.data.writers.AttributeWriter
import ru.ekazantsev.nsd_fake_class_generator.data.writers.InstallationWriter
import ru.ekazantsev.nsd_fake_class_generator.data.writers.MetaClassWriter

import static tests.TestUtils.*

InstallationWriter instWriter = new InstallationWriter()
MetaClassWriter metaWriter = new MetaClassWriter()
AttributeWriter attrWriter = new AttributeWriter()
AttributeGroupWriter groupWriter = new AttributeGroupWriter()

MetaClassWrapperDto metaDto = nsdFakeApi.getMetaClassInfo("serviceCall")
Installation inst = instWriter.createOrUpdate(connectorParams)
logger.info("Записал инсталляцию ${inst.host}")
MetaClass meta = metaWriter.createOrUpdate(inst, metaDto)
logger.info("Записал метакласс ${meta.fullCode}")
metaDto.attributes.forEach {
    Attribute attr = attrWriter.createOrUpdate(meta, it)
    logger.info("Записал атрибут ${attr.code}")
}
metaDto.attributeGroups.forEach {
    AttributeGroup group = groupWriter.createOrUpdate(meta, it)
    logger.info("Записал группу ${group.code}")
}


