dependencies {
    api(project(":shorts-domain"))
    api(project(":shorts-common"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
