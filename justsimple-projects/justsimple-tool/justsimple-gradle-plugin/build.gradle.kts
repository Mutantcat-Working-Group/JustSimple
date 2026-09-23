buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.8.0"
    id("com.vanniktech.maven.publish") version "0.24.0"
}

group = "org.mutantcat.justsimple"
version = "0.0.2"
description = "JustSimple Gradle Plugin"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("gradle-plugin"))
    implementation("org.ow2.asm:asm:9.4")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

gradlePlugin {
    plugins {
        create("JustSimplePlugin") {
            id = "org.mutantcat.justsimple"
            displayName = "JustSimple"
            description = project.description
            implementationClass = "org.mutantcat.justsimple.gradle.plugin.JustSimplePlugin"
        }
    }
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.DEFAULT)
    // or when publishing to https://s01.oss.sonatype.org
    // publishToMavenCentral(SonatypeHost.S01)

    signAllPublications()

    coordinates(project.group.toString(), "justsimple-gradle-plugin", project.version.toString())

    pom {
        name.set("JustSimple Gradle Plugin")
        description.set(project.description)
        inceptionYear.set("2023")
        url.set("https://github.com/Mutantcat-Working-Group/JustSimple")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("mutantcat")
                name.set("Mutantcat Working Group")
                url.set("https://github.com/Mutantcat-Working-Group")
            }
        }
        scm {
            url.set("https://github.com/Mutantcat-Working-Group/JustSimple")
            connection.set("scm:git:git@github.com:Mutantcat-Working-Group/JustSimple.git")
            developerConnection.set("scm:git:ssh://git@github.com:Mutantcat-Working-Group/JustSimple.git")
        }
    }
}
