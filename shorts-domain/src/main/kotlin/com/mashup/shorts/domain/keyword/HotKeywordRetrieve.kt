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

    fun retrieveHotKeywords(targetTime: LocalDateTime): KeywordRanking {
        val hotKeyword = hotKeywordRepository.findTopByOrderByIdDesc()
            ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "해당 시간대의 핫 키워드가 존재하지 않습니다. (time: ${targetTime}}"
            )

        // TODO: 임시 코드 => 리팩터링 해야 함
        val regex = "\\((.*?), (\\d+)\\)".toRegex()
        val matches = regex.findAll(hotKeyword.keywordRanking)
        val result = mutableListOf<String>()
        for (match in matches) {
            result.add(match.groupValues[1])
        }

        return KeywordRanking(hotKeyword.createdAt.toString(), result)
    }
}
