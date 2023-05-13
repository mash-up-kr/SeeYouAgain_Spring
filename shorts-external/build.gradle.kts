dependencies {
    implementation(project(":shorts-domain"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}