package com.mashup.shorts.domain.newscard

import kotlin.Long.Companion.MAX_VALUE
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.newscard.dto.DetailNewsCardResponse
import com.mashup.shorts.domain.newscard.dto.DetailNewsCardResponse.Companion.persistenceToResponseForm
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/v1/news-card")
class NewsCardApi(
    private val newsCardRetrieve: NewsCardRetrieve,
) {

    @GetMapping("/{newsCardId}")
    fun retrieveDetailNewsInNewsCard(
        @PathVariable newsCardId: Long,
        @RequestParam @Min(0) @Max(MAX_VALUE) cursorId: Long,
        @RequestParam @Min(1) @Max(10) size: Int,
        @RequestParam pivot: String,
    ): ApiResponse<List<DetailNewsCardResponse>> {
        return success(
            OK,
            persistenceToResponseForm(
                newsCardRetrieve.retrieveDetailNewsInNewsCard(
                    newsCardId = newsCardId,
                    cursorId = cursorId,
                    size = size,
                    pivot = pivot
                )
            )
        )
    }
}
