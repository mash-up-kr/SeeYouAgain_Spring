package com.mashup.shorts

import org.springframework.stereotype.Service

/**
 * Test
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 22.
 */
@Service
class TestService {

    fun test(): Gyun {
        return Gyun(
                a = "a",
                b = "b"
        )
    }
}