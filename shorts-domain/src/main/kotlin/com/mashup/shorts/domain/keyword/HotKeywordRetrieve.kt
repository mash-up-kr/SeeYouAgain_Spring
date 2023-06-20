package com.mashup.shorts.domain.keyword

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.keyword.dtomapper.RetrieveDetailHotKeyWordResponseMapper
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional(readOnly = true)
class HotKeywordRetrieve(
    private val newsCardRepository: NewsCardRepository,
) {

    fun retrieveDetailHotKeyword(
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<RetrieveDetailHotKeyWordResponseMapper> {
        val newsCards = newsCardRepository.findByKeywordsLikeAndCursorId(
            keyword = keyword,
            cursorId = cursorId,
            size = size
        )
        return RetrieveDetailHotKeyWordResponseMapper.persistenceToResponseForm(newsCards)
    }
}
