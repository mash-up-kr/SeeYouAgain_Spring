package com.mashup.shorts.domain.home.news

import java.time.LocalDateTime
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.domain.home.news.dto.NewsRetrieveMapper
import com.mashup.shorts.domain.home.news.dto.NewsRetrieveResponse
import com.mashup.shorts.domain.home.newscard.dto.RetrieveNewsBundleInNewsCardResponse
import com.mashup.shorts.domain.news.NewsRetrieve
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@RestController
@RequestMapping("/v1/news")
class NewsRetrieveApi(
    private val newsRetrieve: NewsRetrieve,
) {

    /**
    뉴스 자세히 보기 API (웹 뷰)
    @PathVariable : newsId
     */
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

    /**
    키워드로 관련 뉴스 조회 API
    @PathVariable : keyword
    @Param : targetDateTime, cursorId, size
     */
    @GetMapping
    fun retrieveDetailHotKeyword(
        @RequestParam keyword: String,
        @RequestParam targetDateTime: LocalDateTime,
        @RequestParam(defaultValue = "0", required = false) @Min(0) @Max(Long.MAX_VALUE)
        cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
    ): ApiResponse<List<RetrieveNewsBundleInNewsCardResponse>> {
        return ApiResponse.success(
            OK,
            RetrieveNewsBundleInNewsCardResponse.persistenceToResponseForm(
                newsRetrieve.retrieveByKeyword(
                    targetDateTime = targetDateTime,
                    keyword = keyword,
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }
}
