package com.mashup.shorts.domain.hot.keyword

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.hot.keyword.dto.HotKeywordsResponse
import com.mashup.shorts.domain.hot.keyword.dto.KeywordRankingMapper
import com.mashup.shorts.domain.hot.keyword.dto.RetrieveDetailHotKeywordResponse
import com.mashup.shorts.domain.keyword.HotKeywordRetrieve
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@RequestMapping("/v1/hot-keywords")
@RestController
class HotKeywordsRetrieveApi(
    private val hotKeywordRetrieve: HotKeywordRetrieve,
) {

    @GetMapping
    fun retrieveHotKeywords(@RequestParam targetTime: String): ApiResponse<HotKeywordsResponse> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        // 핫키워드 조회 시간
        val retrieveTime = LocalDateTime.parse(targetTime, formatter)
        val keywordRanking = hotKeywordRetrieve.retrieveHotKeywords(retrieveTime)
        return success(OK, KeywordRankingMapper.keywordRankingToResponse(keywordRanking))
    }

    @GetMapping("/{keyword}")
    fun retrieveDetailHotKeyword(
        @PathVariable keyword: String,
        @RequestParam(defaultValue = "0", required = false)
        @Min(0) @Max(Long.MAX_VALUE) cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(10) size: Int,
    ): ApiResponse<List<RetrieveDetailHotKeywordResponse>> {
        return success(
            OK,
            RetrieveDetailHotKeywordResponse.mapperToResponseForm(
                hotKeywordRetrieve.retrieveDetailHotKeyword(keyword, cursorId, size)
            )
        )
    }
}
