package com.mashup.shorts.core

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
    private val crawlerCore: CrawlerCore
) {

    @GetMapping
    fun test() {
        crawlerCore.executeCrawling()
    }
}
