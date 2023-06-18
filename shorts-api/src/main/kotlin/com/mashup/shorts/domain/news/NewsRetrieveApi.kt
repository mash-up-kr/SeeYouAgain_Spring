package com.mashup.shorts.domain.news

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.news.dto.NewsRetrieveMapper
import com.mashup.shorts.domain.news.dto.NewsRetrieveResponse

@RestController
@RequestMapping("/v1/news")
class NewsRetrieveApi(
    private val newsRetrieve: NewsRetrieve,
) {

    @Auth
    @GetMapping("/{newsId}")
    fun retrieveNews(@PathVariable newsId: Long): ApiResponse<NewsRetrieveResponse> {
        val memberUniqueId = AuthContext.getMemberId()
        val newsRetrieveInfo = newsRetrieve.retrieveNews(memberUniqueId, newsId)
        return ApiResponse.success(OK, NewsRetrieveMapper.newsRetrieveInfoToResponse(newsRetrieveInfo))
    }
}
