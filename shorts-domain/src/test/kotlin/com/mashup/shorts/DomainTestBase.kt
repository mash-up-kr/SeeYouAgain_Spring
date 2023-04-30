package com.mashup.shorts

import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource


@TestPropertySource(properties = ["spring.config.location=classpath:/core-config/, classpath:/"])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal abstract class DomainTestBase
