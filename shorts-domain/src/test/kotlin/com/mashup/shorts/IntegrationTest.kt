package com.mashup.shorts

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal abstract class IntegrationTest : DomainTestBase() {

    @Autowired
    lateinit var databaseCleanup: DatabaseCleanup

    @BeforeEach
    abstract fun cleanUp()
}