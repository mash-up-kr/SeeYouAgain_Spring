package com.mashup.shorts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortsCrawlerApplication

fun main(args: Array<String>) {
	runApplication<ShortsCrawlerApplication>(*args)
}
