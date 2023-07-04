package com.mashup.shorts.domain.keyword

import java.time.LocalDateTime
import java.time.LocalTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional(readOnly = true)
class HotKeywordRetrieve(
    private val newsRepository: NewsRepository,
    private val newsCardRepository: NewsCardRepository,
    private val hotKeywordRepository: HotKeywordRepository,
) {

    fun retrieveDetailHotKeyword(
        targetDateTime: LocalDateTime,
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<News> {
        val newsCards = newsCardRepository.findByKeywordsLikeAndCursorId(
            keyword = keyword,
            cursorId = cursorId,
            size = size
        )

        val newsIds = newsCards.map { index ->
            index.multipleNews.split(", ").map { it.toLong() }
        }

        val firstDayOfMonth = LocalDateTime.of(
            targetDateTime.toLocalDate(),
            LocalTime.of(0, 0)
        )
        val lastDayOfMonth = LocalDateTime.of(
            targetDateTime.toLocalDate(),
            LocalTime.of(23, 59)
        )

        return newsRepository.loadNewsBundleByCursorIdAndTargetTime(
            firstDayOfMonth,
            lastDayOfMonth,
            cursorId,
            newsIds.flatten(),
            size,
        )
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
