import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("application")
    id("com.google.cloud.tools.jib") version "3.2.1"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
	kotlin("kapt") version "1.6.21"
}

java.sourceCompatibility = JavaVersion.VERSION_17

application {
	mainClass.set("com.mashup.shorts.ShortsApiApplicationKt")
}

allprojects {
	group = "com.mashup"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "kotlin")
	apply(plugin = "java-library")
	apply(plugin = "kotlin-jpa")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.asciidoctor.jvm.convert")
    apply(plugin = "com.google.cloud.tools.jib")
	apply(plugin = "kotlin-kapt")
	apply(plugin = "application")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("io.github.microutils:kotlin-logging:2.0.8")

		implementation("org.jetbrains.kotlin:kotlin-reflect")
		testImplementation("org.springframework.boot:spring-boot-starter-test")

        // SpringMockk
        testImplementation("com.ninja-squad:springmockk:3.1.1")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
