plugins {
    kotlin("jvm") version "1.9.10"
    id("maven-publish")
    id("groovy")
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "ru.kazantsev.nsd.sdk"
version = "1.0.0"

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    javadoc {
        dependsOn(dokkaJavadoc)
    }

    dokkaJavadoc {
        outputDirectory.set(buildDir.resolve("docs\\javadoc"))
    }

    register<Jar>("javadocJar") {
        from(getByName("javadoc").outputs.files)
        archiveClassifier.set("javadoc")
    }

    register<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])

            artifact(tasks.named("javadocJar"))
            artifact(tasks.named("sourcesJar"))

            pom {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
            }
        }
    }
    repositories {
        mavenLocal()
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("ru.kazantsev.nsd.sdk:data:1.0.0")
    implementation("ru.kazantsev.nsd.sdk:upper_level_classes:1.0.0")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("com.squareup:javapoet:1.13.0")

    testImplementation("org.codehaus.groovy:groovy-all:3.0.19")
    testImplementation(kotlin("test"))
}
