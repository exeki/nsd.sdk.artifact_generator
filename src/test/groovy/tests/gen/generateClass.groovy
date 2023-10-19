package tests.gen

import com.squareup.javapoet.JavaFile
import ru.ekazantsev.nsd_fake_class_generator.data.dto.MetaClass
import ru.ekazantsev.nsd_fake_class_generator.src_generation.ClassGeneratorService


import static tests.TestUtils.*


static String getClassName(MetaClass metaClass){
    return metaClass.fullCode.tokenize('$').collect{it[0].toUpperCase() + it.substring(1, -1)}.join('$')
}

'IScriptDtObjectServiceCall$ServiceCall'
MetaClass meta = db.metaClassDao.queryForFirst()
JavaFile.Builder proto = new ClassGeneratorService().generateClassProto(meta)
JavaFile javaFile = JavaFile.builder(new ClassGeneratorService().packageName, proto.build()).build()
File file = new File("C:\\projects\\nds\\projects\\sdk\\nsd_fake_class_creator\\src\\test\\resources");
javaFile.writeTo(file);
