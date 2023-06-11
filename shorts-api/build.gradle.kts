tasks {
    val snippetsDir by extra { file("build/generated-snippets") }

    test {
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        dependsOn(test)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        dependsOn(test)
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }

    build {
        dependsOn(asciidoctor)
    }
}

dependencies {
    api(project(":shorts-domain"))

    // Validation Annotation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Spring Rest Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
}

application {
    mainClass.set("com.mashup.shorts.ShortsApiApplicationKt")
}
