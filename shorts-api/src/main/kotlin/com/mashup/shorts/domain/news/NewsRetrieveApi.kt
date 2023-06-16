package com.mashup.shorts.domain.news

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse

@RestController
@RequestMapping("/v1/news")
class NewsRetrieveApi(
    private val newsRetrieve: NewsRetrieve,
) {

    @GetMapping("{newsId}")
    fun retrieveNewsLinkForWebView(
        @PathVariable newsId: Long,
    ): ApiResponse<String> {
        return ApiResponse.success(
            OK,
            newsRetrieve.retrieveNewsLinkByNewsId(newsId)
        )
    }
}
