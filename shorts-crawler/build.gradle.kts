dependencies {
    api(project(":shorts-domain"))
    api(project(":shorts-common"))
    api(project(":shorts-keyword-extractor"))
    api(project(":shorts-rank-processor"))

    // JSoup Dependency
    implementation("org.jsoup:jsoup:1.15.4")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.9")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplicationKt")
}
