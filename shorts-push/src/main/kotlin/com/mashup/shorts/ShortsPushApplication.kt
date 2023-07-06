package com.mashup.shorts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class ShortsPushApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.location", "classpath:/domain-config/,classpath:/")
    runApplication<ShortsPushApplication>(*args)
}
