repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shorts-domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // JSoup Dependency
    implementation("org.jsoup:jsoup:1.15.4")

    // Lucene Dependency
    implementation("org.apache.lucene:lucene-core:8.4.1")
    implementation("org.apache.lucene:lucene-analyzers-nori:8.4.1")

    // WebFlux Dependency For Web Client
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Flyway Category Insert
    implementation("org.flywaydb:flyway-mysql")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplication")
}
