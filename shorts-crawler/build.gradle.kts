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
    implementation("com.github.shin285:KOMORAN:3.3.9")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.9")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplicationKt")
}
