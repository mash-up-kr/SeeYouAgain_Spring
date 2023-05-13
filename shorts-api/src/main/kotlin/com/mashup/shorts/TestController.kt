package com.mashup.shorts

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * TestController
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 14.
 */
@RestController
class TestController {

    @GetMapping("/test")
    fun test(): Test {
        return Test(
            a = "a",
            b = "b"
        )
    }
}

class Test(
    val a: String,
    val b: String
)
