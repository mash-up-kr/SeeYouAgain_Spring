package com.mashup.shorts.domain.membernews

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.Pivots

@Service
@Transactional(readOnly = true)
class MemberNewsRetrieve(
    private val memberNewsRepository: MemberNewsRepository,
    private val newsRepository: NewsRepository,
) {

    fun retrieveMemberNews(
        targetDate: LocalDate,
        member: Member,
        cursorWrittenDateTime: String,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        val memberNewsBundle = memberNewsRepository.findAllByMember(member)

        val firstDayOfMonth = LocalDateTime.of(
            targetDate.withDayOfMonth(1),
            LocalTime.of(0, 0)
        )
        val lastDayOfMonth = LocalDateTime.of(
            targetDate.withDayOfMonth(targetDate.lengthOfMonth()),
            LocalTime.of(23, 59)
        )

        return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
            firstDayOfMonth,
            lastDayOfMonth,
            cursorWrittenDateTime,
            memberNewsBundle.map { it.news.id },
            size,
            pivot
        )
    }

    fun retrieveMemberNewsCount(member: Member): Int {
        return memberNewsRepository.findAllByMember(member).size
    }
}
