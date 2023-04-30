package com.mashup.shorts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortsCrawlerApiApplication

fun main(args: Array<String>) {
	runApplication<ShortsCrawlerApiApplication>(*args)
}
