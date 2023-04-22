package com.mashup.shorts.api

import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource

/**
 * ApiTestBase
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 22.
 */
@TestPropertySource(properties = ["spring.config.location=classpath:/"])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
abstract class ApiTestBase