package com.mashup.shorts.domain.newscard

import java.time.LocalDateTime
import kotlin.Long.Companion.MAX_VALUE
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.newscard.dto.DetailNewsCardResponse
import com.mashup.shorts.domain.newscard.dto.DetailNewsCardResponse.Companion.persistenceToResponseForm
import com.mashup.shorts.domain.newscard.dto.RetrieveAllNewsCardResponse
import com.mashup.shorts.domain.newscard.dto.RetrieveAllNewsCardResponse.Companion.persistenceToResponseForm
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/v1/news-card")
class NewsCardApi(
    private val newsCardRetrieve: NewsCardRetrieve,
) {

    /**
    모든 뉴스 카드를 조회한다
    Param : 조회 대상 시각, 커서 ID, 사이즈
    Return : MutableList<LoadAllDetailNewsInNewsCard>
     */
    @Auth
    @GetMapping
    fun retrieveNewsCard(
        @RequestParam targetDateTime: LocalDateTime,
        @RequestParam @Min(0) @Max(MAX_VALUE) cursorId: Long,
        @RequestParam @Min(1) @Max(20) size: Int,
    ): ApiResponse<List<RetrieveAllNewsCardResponse>> {
        return success(
            OK,
            persistenceToResponseForm(
                newsCardRetrieve.retrieveNewsCardByMember(
                    memberUniqueId = AuthContext.getMemberId(),
                    targetDateTime = targetDateTime,
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }


    /**
    뉴스 카드 내의 모든 뉴스를 조회한다.
    Param : 뉴스 카드 ID, 커서 ID, 사이즈
    Return : MutableList<LoadAllDetailNewsInNewsCard>
     */
    @GetMapping("/{newsCardId}")
    fun retrieveDetailNewsInNewsCard(
        @PathVariable newsCardId: Long,
        @RequestParam @Min(0) @Max(MAX_VALUE) cursorId: Long,
        @RequestParam @Min(1) @Max(20) size: Int,
    ): ApiResponse<List<DetailNewsCardResponse>> {
        return success(
            OK,
            persistenceToResponseForm(
                newsCardRetrieve.retrieveDetailNewsInNewsCard(
                    newsCardId = newsCardId,
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }
}
