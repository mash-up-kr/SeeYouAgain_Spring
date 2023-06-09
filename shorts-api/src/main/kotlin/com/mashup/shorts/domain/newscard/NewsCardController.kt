package com.mashup.shorts.domain.newscard

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.newscard.dto.NewsCardResponseForm.LoadAllDetailNewsInNewsCard

@RestController
@RequestMapping("/v1/news-card")
class NewsCardController(
    private val newsCardService: NewsCardService,
) {

    /**
    뉴스 카드 내의 모든 뉴스를 조회한다.
    Param : 뉴스 카드 ID, 커서 ID
    Return : MutableList<LoadAllDetailNewsInNewsCard>
     */
    @ResponseStatus(OK)
    @GetMapping("/{newsCardId}")
    fun loadDetailNewsInNewsCard(
        @PathVariable newsCardId: Long,
        @RequestParam cursorId: Long,
        @RequestParam size: Int,
    ): ApiResponse<MutableList<LoadAllDetailNewsInNewsCard>> {
        return success(
            OK,
            LoadAllDetailNewsInNewsCard.persistenceToResponseForm(
                newsCardService.loadDetailNewsInNewsCard(newsCardId, cursorId, size)
            )
        )
    }


}
