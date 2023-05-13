dependencies {
    implementation(project(":shorts-domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jsoup:jsoup:1.15.4")
}

application {
    mainClass.set("com.mashup.shorts.ShortsCrawlerApplication")
}