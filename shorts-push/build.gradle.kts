plugins {
    id("java")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":shorts-domain"))

    implementation("com.google.firebase:firebase-admin:9.2.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.mashup.shorts.ShortsPushApplicationKt")
}
