package com.mashup.shorts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortsApiApplication

fun main(args: Array<String>) {
	runApplication<ShortsApiApplication>(*args)
}
