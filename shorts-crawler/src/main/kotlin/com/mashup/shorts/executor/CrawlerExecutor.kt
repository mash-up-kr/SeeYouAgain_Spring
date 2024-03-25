package com.mashup.shorts.executor

import java.time.LocalDateTime
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import com.mashup.shorts.core.modern.CrawlerCore
import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode
import com.mashup.shorts.logger.LogType
import com.mashup.shorts.logger.ThirdPartyLogger

@Component
class CrawlerExecutor(
    private val crawlerCore: CrawlerCore,
    private val thirdPartyLogger: ThirdPartyLogger,
) {

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0)
    )
    fun execute() {
        crawlerCore.executeCrawling()
        thirdPartyLogger.log(
            msg = "${LocalDateTime.now()} - 크롤링이 정상적으로 수행되었습니다.",
            logType = LogType.SUCCESS
        )
    }

    @Recover
    fun recover(exception: Exception) {
        thirdPartyLogger.log(
            msg = "${LocalDateTime.now()} 크롤링 중 예외가 발생하여 총 3회를 시도했으나 작업이 실패했습니다. - " +
                "${exception.localizedMessage}}",
            logType = LogType.FAIL
        )
        throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E500_INTERNAL_SERVER_ERROR,
            resultErrorMessage = "크롤링 중 예외가 발생하여 총 3회를 시도했으나 작업이 실패했습니다."
        )
    }
}
