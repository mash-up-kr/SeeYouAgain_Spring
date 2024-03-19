package com.mashup.shorts.domain.newscard

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository

@Service
@Transactional(readOnly = true)
class NewsCardRetrieve(
    private val newsCardRepository: NewsCardRepository,
    private val newsRepository: NewsRepository,
) {

    fun retrieveDetailNewsInNewsCard(
        newsCardId: Long,
        cursorWrittenDateTime: String,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = E404_NOT_FOUND,
                resultErrorMessage = "${newsCardId}에 해당 뉴스 카드는 존재하지 않습니다."
            )
        val newsIds = newsCard.multipleNews.split(", ").map { it.toLong() }

        if (cursorWrittenDateTime.isEmpty()) {
            return notContainedCursor(
                newsIds,
                size,
                pivot,
            )
        }

        return containedCursor(
            newsIds,
            cursorWrittenDateTime,
            size,
            pivot,
        )
    }

    private fun notContainedCursor(
        newsIds: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        if (pivot == Pivots.ASC) {
            val newsBundle = newsRepository.findAllById(newsIds)
                .sortedBy { it.writtenDateTime }

            return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNews(
                newsIds = newsBundle.map { it.id },
                size = size
            ).sortedBy { it.writtenDateTime }
        }

        val newsBundle = newsRepository.findAllById(newsIds)
            .sortedByDescending { it.writtenDateTime }

        return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNews(
            newsIds = newsBundle.map { it.id },
            size = size
        ).sortedByDescending { it.writtenDateTime }
    }

    private fun containedCursor(
        newsIds: List<Long>,
        cursorWrittenDateTime: String,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        if (pivot == Pivots.ASC) {
            val newsBundle = newsRepository.findAllById(newsIds)
                .sortedBy { it.writtenDateTime }
                .filter { it.writtenDateTime > cursorWrittenDateTime }

            return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNews(
                newsIds = newsBundle.map { it.id },
                size = size
            ).sortedBy { it.writtenDateTime }
        }

        val newsBundle = newsRepository.findAllById(newsIds)
            .sortedByDescending { it.writtenDateTime }
            .filter { it.writtenDateTime < cursorWrittenDateTime }

        return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNews(
            newsIds = newsBundle.map { it.id },
            size = size
        ).sortedByDescending { it.writtenDateTime }
    }
}
