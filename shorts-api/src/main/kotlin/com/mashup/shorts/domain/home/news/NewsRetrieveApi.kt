package com.mashup.shorts.domain.home.news

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.home.news.dto.NewsRetrieveMapper
import com.mashup.shorts.domain.home.news.dto.NewsRetrieveResponse
import com.mashup.shorts.domain.news.NewsRetrieve

@RestController
@RequestMapping("/v1/news")
class NewsRetrieveApi(
    private val newsRetrieve: NewsRetrieve,
) {

    @Auth
    @GetMapping("/{newsId}")
    fun retrieveNews(@PathVariable newsId: Long): ApiResponse<NewsRetrieveResponse> {
        val member = AuthContext.getMember()
        val newsRetrieveInfo = newsRetrieve.retrieveNews(member, newsId)
        return ApiResponse.success(
            OK,
            NewsRetrieveMapper.newsRetrieveInfoToResponse(newsRetrieveInfo)
        )
    }
}
