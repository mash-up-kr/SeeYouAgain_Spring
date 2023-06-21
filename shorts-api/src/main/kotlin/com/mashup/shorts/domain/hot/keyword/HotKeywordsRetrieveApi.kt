package com.mashup.shorts.domain.hot.keyword

import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.keyword.HotKeywordRetrieve
import com.mashup.shorts.domain.hot.keyword.dto.HotKeywordsResponse
import com.mashup.shorts.domain.hot.keyword.dto.RetrieveDetailHotKeywordResponse
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@RequestMapping("/v1/hot-keywords")
@RestController
class HotKeywordsRetrieveApi(
    private val hotKeywordRetrieve: HotKeywordRetrieve,
) {

    @GetMapping
    fun retrieveHotKeywords(): ApiResponse<HotKeywordsResponse> {
        val response = HotKeywordsResponse.of(
            "2023년 6월 17일 17:01 ~ 18:00",
            listOf("숏스", "또보겠지", "매쉬업", "아오스", "디자인", "스프링", "AI", "짧은 뉴스", "챗 GPT", "해커톤")
        )
        return success(OK, response)
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
