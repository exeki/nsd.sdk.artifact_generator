package ru.ekazantsev.nsd_fake_class_generator.src_generation

import com.squareup.javapoet.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import javax.lang.model.element.Modifier

/**
 * Служба, генерирующая прототип класса, являющийся хранилищем метаинформации
 */
class MetainfoClassGeneratorService(private val classGeneratorService: ClassGeneratorService) {

    constructor(classGeneratorService: ClassGeneratorService, packageService: PackageService) : this(
        classGeneratorService
    ) {
        this.packageService = packageService
    }

    private val logger: Logger = LoggerFactory.getLogger(MetainfoClassGeneratorService::class.java)

    private var packageService: PackageService = PackageService()

    /**
     * Генерирует прототип класса, являющийся хранилищем метаинформации
     */
    fun generateMetaInfoClass(): TypeSpec.Builder {
        val generatedMetaClassProto = TypeSpec.classBuilder(packageService.generatedMetaClassName)
        generatedMetaClassProto.addField(
            FieldSpec
                .builder(
                    ParameterizedTypeName.get(
                        ClassName.get(List::class.java),
                        ClassName.get(String::class.java)
                    ),
                    "generatedClassNames"
                )
                .initializer(
                    "java.util.Arrays.asList(\$L)",
                    classGeneratorService.generatedClasses.stream().map { "\"$it\"" }.collect(Collectors.joining(", "))
                )
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build()
        )
        generatedMetaClassProto.addField(
            FieldSpec.builder(String::class.java, "generatedClassesPackage")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("\"\$L\"", packageService.packageName)
                .addJavadoc(CodeBlock.of("Получить перечень наименований сгенерированных классов"))
                .build()
        )
        generatedMetaClassProto.addMethod(
            MethodSpec.methodBuilder("getGeneratedClassNames")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(List::class.java, String::class.java))
                .addStatement("return \$N", "generatedClassNames")
                .addJavadoc(CodeBlock.of("Package, куда были сформированы классы"))
                .build()
        )
        generatedMetaClassProto.addMethod(
            MethodSpec.methodBuilder("getGeneratedClassesPackage")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(String::class.java))
                .addStatement("return \$N", "generatedClassesPackage")
                .build()
        )
        generatedMetaClassProto.addJavadoc(CodeBlock.of("Класс, содержащий информацию по сгенерированным класам"))
        generatedMetaClassProto.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        return generatedMetaClassProto
    }
}