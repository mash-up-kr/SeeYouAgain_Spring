package com.mashup.shorts.domain.home.newscard

import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.home.newscard.dto.DetailNewsCardResponse
import com.mashup.shorts.domain.home.newscard.dto.DetailNewsCardResponse.Companion.persistenceToResponseForm
import com.mashup.shorts.domain.newscard.NewsCardRetrieve
import com.mashup.shorts.domain.newscard.Pivots
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/v1/news-card")
class NewsCardRetrieveApi(
    private val newsCardRetrieve: NewsCardRetrieve,
) {

    @GetMapping("/{newsCardId}")
    fun retrieveDetailNewsInNewsCard(
        @PathVariable newsCardId: Long,
        @RequestParam cursorWrittenDateTime: String,
        @RequestParam(value = "size", required = true) @Min(1) @Max(10) size: Int,
        @RequestParam pivot: Pivots,
    ): ApiResponse<List<DetailNewsCardResponse>> {
        return success(
            OK,
            persistenceToResponseForm(
                newsCardRetrieve.retrieveDetailNewsInNewsCard(
                    newsCardId = newsCardId,
                    cursorWrittenDateTime = cursorWrittenDateTime,
                    size = size,
                    pivot = pivot
                )
            )
        )
    }
}
