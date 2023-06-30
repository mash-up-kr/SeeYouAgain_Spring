package com.mashup.shorts.domain.hot.keyword

import java.time.LocalDateTime
import org.springframework.http.HttpStatus.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.home.newscard.dto.RetrieveNewsBundleInNewsCardResponse
import com.mashup.shorts.domain.hot.keyword.dto.HotKeywordsResponse
import com.mashup.shorts.domain.hot.keyword.dto.KeywordRankingMapper
import com.mashup.shorts.domain.keyword.HotKeywordRetrieve
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RequestMapping("/v1/hot-keywords")
@RestController
class HotKeywordsRetrieveApi(
    private val hotKeywordRetrieve: HotKeywordRetrieve,
) {

    /**
    키워드 랭킹 조회 API
     */
    @GetMapping
    fun retrieveHotKeywords(): ApiResponse<HotKeywordsResponse> {
        val targetTime = LocalDateTime.now().withSecond(0).withNano(0)
        val keywordRanking = hotKeywordRetrieve.retrieveHotKeywords(targetTime)
        return success(OK, KeywordRankingMapper.keywordRankingToResponse(keywordRanking))
    }

    /**
    키워드로 관련 뉴스 조회 API
    @PathVariable : keyword
    @Param : targetDateTime, cursorId, size
     */
    @GetMapping("/{keyword}")
    fun retrieveDetailHotKeyword(
        @PathVariable keyword: String,
        @RequestParam targetDateTime: LocalDateTime,
        @RequestParam(defaultValue = "0", required = false) @Min(0) @Max(Long.MAX_VALUE)
        cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
    ): ApiResponse<List<RetrieveNewsBundleInNewsCardResponse>> {
        return success(
            OK,
            RetrieveNewsBundleInNewsCardResponse.persistenceToResponseForm(
                hotKeywordRetrieve.retrieveDetailHotKeyword(
                    targetDateTime = targetDateTime,
                    keyword = keyword,
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }
}
