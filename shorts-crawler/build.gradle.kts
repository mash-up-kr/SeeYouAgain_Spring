repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven(url = "https://repo1.maven.org/maven2/")
}

dependencies {
    implementation(project(":shorts-domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // OpenAI Dependency
    implementation("com.theokanning.openai-gpt3-java:service:0.12.0")

    // JSoup Dependency
    implementation("org.jsoup:jsoup:1.15.4")

    // KOMORAN Dependency
    implementation("com.github.shin285:KOMORAN:3.3.4")
    implementation("org.apache.opennlp:opennlp-tools:2.1.1")

    // Lucene Dependency
    implementation("org.apache.lucene:lucene-core:9.6.0")
    implementation("org.apache.lucene:lucene-analyzers-common:8.11.2")
    implementation("org.apache.lucene:lucene-queryparser:9.6.0")

    // WebFlux Dependency For Web Client
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Flyway Category Insert
    implementation("org.flywaydb:flyway-mysql")
}


application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplication")
}
