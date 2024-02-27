repositories {
    maven("https://jitpack.io")
}

dependencies {
    api(project(":shorts-domain"))

    // JSoup Dependency
    implementation("org.jsoup:jsoup:1.15.4")

    // Komoran Dependency
    implementation("com.github.shin285:KOMORAN:3.3.9")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplicationKt")
}
