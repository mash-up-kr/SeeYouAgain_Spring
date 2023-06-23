package com.mashup.shorts.domain.keyword

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.keyword.dtomapper.RetrieveDetailHotKeyWordResponseMapper
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional(readOnly = true)
class HotKeywordRetrieve(
    private val newsCardRepository: NewsCardRepository,
    private val hotKeywordRepository: HotKeywordRepository
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

    fun retrieveHotKeywords(now: LocalDateTime): KeywordRanking {
        val targetTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, 0)
        val hotKeyword = hotKeywordRepository.findByCreatedAtAfter(targetTime)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "해당 시간대의 핫 키워드가 존재하지 않습니다."
            )
        val ranking: List<String> = emptyList()
        for (keyword in hotKeyword.keywordRanking.split(",")) {
            ranking.plus(keyword)
        }
        return KeywordRanking(hotKeyword.createdAt.toString(), ranking)
    }
}
