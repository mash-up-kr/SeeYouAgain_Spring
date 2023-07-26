package com.mashup.shorts.api

import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.domain.member.Member
import jakarta.annotation.PostConstruct
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["spring.config.location=classpath:/"])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
abstract class ApiTestBase {

    @PostConstruct
    fun initMember() {
        AuthContext.USER_CONTEXT.set(
            Member(
                uniqueId = "Unique",
                nickname = "nickname",
                fcmTokenPayload = "payload",
                isAllowedAlarm = true
            )
        )
    }
}
