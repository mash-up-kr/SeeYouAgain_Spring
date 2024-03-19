repositories {
    maven("https://jitpack.io")
}

dependencies {
    api(project(":shorts-common"))

    // Lucene Dependency
    implementation("org.apache.lucene:lucene-core:8.4.1")
    implementation("org.apache.lucene:lucene-analyzers-nori:8.4.1")

    // Komoran Dependency
    implementation("com.github.shin285:KOMORAN:3.3.9")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
