package com.mashup.shorts.domain.newscard

import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.newscard.dto.NewsCardFormResponse
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/v1/news-card")
class NewsCardApi(
    private val newsCardRetrieve: NewsCardRetrieve,
) {

    /**
    뉴스 카드 내의 모든 뉴스를 조회한다.
    Param : 뉴스 카드 ID, 커서 ID, 사이즈
    Return : MutableList<LoadAllDetailNewsInNewsCard>
     */
    @GetMapping("/{newsCardId}")
    fun loadDetailNewsInNewsCard(
        @PathVariable newsCardId: Long,
        @RequestParam @Min(0) @Max(Long.MAX_VALUE) cursorId: Long,
        @RequestParam @Min(1) @Max(20) size: Int,
    ): ApiResponse<List<NewsCardFormResponse>> {
        return success(
            OK,
            NewsCardFormResponse.persistenceToResponseForm(
                newsCardRetrieve.loadDetailNewsInNewsCard(newsCardId, cursorId, size)
            )
        )
    }
}
