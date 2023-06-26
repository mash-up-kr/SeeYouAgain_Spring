repositories {
    maven("https://jitpack.io")
}

dependencies {
    api(project(":shorts-domain"))

    // JSoup Dependency
    implementation("org.jsoup:jsoup:1.15.4")

    // Lucene Dependency
    implementation("org.apache.lucene:lucene-core:8.4.1")
    implementation("org.apache.lucene:lucene-analyzers-nori:8.4.1")

    // Komoran Dependency
    implementation ("com.github.shin285:KOMORAN:3.3.9")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplicationKt")
}
