package com.mashup.shorts.executor

import java.time.LocalDateTime
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode
import com.mashup.shorts.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.core.modern.CrawlerCore
import com.mashup.shorts.logger.LogType
import com.mashup.shorts.logger.ThirdPartyLogger

@Component
class CrawlerExecutor(
    private val crawlerCore: CrawlerCore,
    private val thirdPartyLogger: ThirdPartyLogger,
) {

    @Retryable(value = [Exception::class], maxAttempts = 3)
    @Transactional(rollbackFor = [Exception::class])
    @Scheduled(cron = "0 0 * * * *")
    fun execute() {
        try {
            thirdPartyLogger.log(
                msg = "${crawlerCore.executeCrawling()} - 크롤링이 정상적으로 수행되었습니다.",
                logType = LogType.SUCCESS
            )
        } catch (e: Exception) {
            thirdPartyLogger.log(
                msg = "${LocalDateTime.now()} - 다음과 같은 이유로 크롤링이 실패하였습니다. ${e.localizedMessage}",
                logType = LogType.FAIL
            )
        }
    }

    @Recover
    fun recover(exception: Exception) {
        log.error { "크롤링 중 예외가 발생하여 총 3회를 시도했으나 작업이 실패했습니다." }
        log.error { "ExceptionStackTrace : ${exception.localizedMessage}" }
        log.error { "ExceptionCause : ${exception.cause}" }
        throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E500_INTERNAL_SERVER_ERROR,
            resultErrorMessage = "크롤링 중 예외가 발생하여 총 3회를 시도했으나 작업이 실패했습니다."
        )
    }
}
