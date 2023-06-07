package com.mashup.shorts.api

import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["spring.config.location=classpath:/"])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
abstract class ApiTestBase
