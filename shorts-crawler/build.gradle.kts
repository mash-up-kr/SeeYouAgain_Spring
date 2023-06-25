dependencies {
    api(project(":shorts-domain"))

    // JSoup Dependency
    implementation("org.jsoup:jsoup:1.15.4")

    // Lucene Dependency
    implementation("org.apache.lucene:lucene-core:8.4.1")
    implementation("org.apache.lucene:lucene-analyzers-nori:8.4.1")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplicationKt")
}
