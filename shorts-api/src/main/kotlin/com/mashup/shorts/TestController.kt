package com.mashup.shorts

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * TestController
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 22.
 */
@RestController
class TestController(
    private val testService: TestService
) {

    @GetMapping("/test")
    fun test(): Gyun {
        return testService.test()
    }
}

data class Gyun(
    val a: String,
    val b: String
)