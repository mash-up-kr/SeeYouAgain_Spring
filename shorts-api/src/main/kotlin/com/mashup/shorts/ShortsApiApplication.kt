package com.mashup.shorts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortsApiApplication

fun main(args: Array<String>) {
	System.setProperty("spring.config.location", "classpath:/domain-config/,classpath:/")
	runApplication<ShortsApiApplication>(*args)
}
