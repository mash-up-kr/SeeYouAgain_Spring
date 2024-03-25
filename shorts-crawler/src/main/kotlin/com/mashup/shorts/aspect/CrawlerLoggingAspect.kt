package com.mashup.shorts.aspect

import java.time.LocalDateTime
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import com.mashup.shorts.logger.LogType
import com.mashup.shorts.logger.ThirdPartyLogger

@Aspect
@Component
class CrawlerLoggingAspect(
    private val thirdPartyLogger: ThirdPartyLogger,
) {

    @AfterReturning(pointcut = "execution($TARGET_METHOD_PATH)")
    fun logAfterReturning(joinPoint: JoinPoint) {
        thirdPartyLogger.log(
            msg = "${LocalDateTime.now()} - 크롤링이 정상적으로 수행되었습니다.",
            logType = LogType.SUCCESS
        )
    }

    @AfterThrowing(
        pointcut = "execution($TARGET_METHOD_PATH)",
        throwing = "exception"
    )
    fun logAfterThrowing(joinPoint: JoinPoint, exception: Exception) {
        thirdPartyLogger.log(
            msg = "${LocalDateTime.now()} 크롤링 중 예외가 발생하여 총 3회를 시도했으나 작업이 실패했습니다. - " +
                "${exception.localizedMessage}}",
            logType = LogType.FAIL
        )
    }

    companion object {
        private const val TARGET_METHOD_PATH = "* com.mashup.shorts.executor.CrawlerExecutor.execute()"
    }
}
