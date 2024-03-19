package com.mashup.shorts.domain.hot.keyword

import java.time.LocalDateTime
import org.springframework.http.HttpStatus.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.response.ApiResponse.Companion.success
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
}
