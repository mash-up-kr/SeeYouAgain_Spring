val asciidoctorExtensions: Configuration by configurations.creating

tasks.test {
    outputs.dir("build/generated-snippets")
}

tasks.asciidoctor {
    inputs.dir("build/generated-snippets")
    dependsOn(tasks.test)
    configurations(asciidoctorExtensions.name)
    baseDirFollowsSourceFile()
}

tasks.register("restDocs") {
    dependsOn("asciidoctor")
    doLast {
        copy {
            from(file("$buildDir/asciidoc/html5"))
            into(file("docs"))
        }
    }
}


dependencies {
    implementation(project(":shorts-domain"))

    // Spring Rest Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
}

application {
    mainClass.set("com.mashup.shorts.ShortsApiApplicationKt")
}