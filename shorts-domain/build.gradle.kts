dependencies {
    api(project(":shorts-common"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("com.mysql:mysql-connector-j:8.0.33")

    // jasypt
    api("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.3")

    // querydsl
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt ("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt ("jakarta.annotation:jakarta.annotation-api")
    kapt ("jakarta.persistence:jakarta.persistence-api")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
