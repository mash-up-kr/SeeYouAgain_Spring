package com.mashup.shorts.core

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import com.mashup.shorts.ShortsCrawlerApplication
import com.mashup.shorts.logger.LogType
import com.mashup.shorts.logger.ThirdPartyLogger

@Disabled
@ActiveProfiles("dev")
@SpringBootTest(classes = [ShortsCrawlerApplication::class])
class ThirdPartyLoggerTest {

    @Autowired
    private lateinit var thirdPartyLogger: ThirdPartyLogger

    @Test
    fun execute() {
        thirdPartyLogger.log("성공 메세지 발송", LogType.SUCCESS)
        thirdPartyLogger.log("실패 메세지 발송", LogType.FAIL)
    }
}
