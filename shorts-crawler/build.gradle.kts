dependencies {
    implementation(project(":shorts-domain"))

    implementation ("com.theokanning.openai-gpt3-java:service:0.12.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jsoup:jsoup:1.15.4")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplication")
}
