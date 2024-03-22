repositories {
    mavenCentral()
}

dependencies {
    api(project(":shorts-common"))

    // Lucene Dependency
    implementation("org.apache.lucene:lucene-core:8.11.3")
    implementation("org.apache.lucene:lucene-analyzers-nori:8.11.3")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
