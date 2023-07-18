package com.mashup.shorts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableRetry
@SpringBootApplication
class ShortsCrawlerApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.location", "classpath:/domain-config/,classpath:/")
    runApplication<ShortsCrawlerApplication>(*args)
}
