tasks.asciidoctor {
    val snippetsDir by extra { file("build/generated-snippets") }

    inputs.dir(snippetsDir)
    //dependsOn(test)
    dependsOn(tasks.test) // 변경

    doFirst { // 2
        delete {
            file("src/main/resources/static/docs")
        }
    }
}

tasks.register("copyHTML", Copy::class) { // 3
    dependsOn(tasks.asciidoctor)
    from(file("build/docs/asciidoc"))
    into(file("src/main/resources/static/docs"))
}

tasks.build { // 4
    dependsOn(tasks.getByName("copyHTML"))
}

tasks.bootJar { // 5
    dependsOn(tasks.asciidoctor)
    dependsOn(tasks.getByName("copyHTML"))
}

dependencies {
    api(project(":shorts-domain"))

    // Validation Annotation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Spring Rest Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
}

application {
    mainClass.set("com.mashup.shorts.ShortsApiApplicationKt")
}

jib {
    from {
        image = "adoptopenjdk/openjdk17:alpine-jre"
    }
    to {
        image = System.getProperty("image", "shorts/shorts-api")
        tags = setOf(System.getProperty("tag", "latest"))
    }
    container {
        jvmFlags = listOf(
            "-Duser.timezone=Asia/Seoul"
        )
        ports = listOf("8080")
    }
}
