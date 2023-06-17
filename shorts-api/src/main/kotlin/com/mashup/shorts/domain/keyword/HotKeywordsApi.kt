package com.mashup.shorts.domain.keyword

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.keyword.dto.HotKeywordsResponse

@RequestMapping("/v1/hot-keywords")
@RestController
class HotKeywordsApi {

    @GetMapping
    fun retrieveHotKeywords(): ApiResponse<HotKeywordsResponse> {
        val response = HotKeywordsResponse.of("2023년 6월 17일 17:01 ~ 18:00",
            listOf("숏스", "또보겠지", "매쉬업", "아오스", "디자인", "스프링", "AI", "짧은 뉴스", "챗 GPT", "해커톤"))
        return ApiResponse.success(HttpStatus.OK, response)
    }
}
