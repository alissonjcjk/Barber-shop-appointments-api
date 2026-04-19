import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.barberdev"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val mapStructVersion = "1.6.3"

dependencies {
    // Spring Boot Core
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Database Migration
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Mapping
    implementation("org.mapstruct:mapstruct:$mapStructVersion")

    // Utilities
    compileOnly("org.projectlombok:lombok")

    // Dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Runtime
    runtimeOnly("org.postgresql:postgresql")

    // Annotation processors
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.projectlombok:lombok")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("build") {
    doLast {
        val trigger = file("src/main/resources/trigger.txt")
        if (!trigger.exists()) {
            trigger.createNewFile()
        }
        trigger.writeText(Date().time.toString())
    }
}

/**
 * Utility task to generate Flyway migration files automatically with timestamp.
 * Usage: ./gradlew generateFlywayMigrationFile -PmigrationName=<name>
 */
tasks.register("generateFlywayMigrationFile") {
    description = "Gera arquivo de migration do Flyway com timestamp automático"
    group = "Flyway"

    doLast {
        val migrationsDir = file("src/main/resources/db/migration")
        if (!migrationsDir.exists()) {
            migrationsDir.mkdirs()
        }

        val migrationNameFromConsole = project.findProperty("migrationName") as String?
            ?: throw IllegalArgumentException("Forneça o nome da migration: -PmigrationName=<nome>")

        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val migrationName = "V${timestamp}__${migrationNameFromConsole}.sql"

        val migrationFile = file("${migrationsDir.path}/$migrationName")
        migrationFile.writeText("-- $migrationName\n-- Generated at ${migrationsDir.path}\n")

        logger.lifecycle("Migration criada: ${migrationFile.path}")
    }
}
