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
        val targetDateTimePeriod = parseTargetDateTimePeriod(targetDate)
        val firstDayOfMonth = targetDateTimePeriod.first
        val lastDayOfMonth = targetDateTimePeriod.second

        if (cursorWrittenDateTime.isEmpty()) {
            return notContainedCursor(
                firstDayOfMonth,
                lastDayOfMonth,
                member,
                size,
                pivot
            )
        }

        return containedCursor(
            firstDayOfMonth,
            lastDayOfMonth,
            member,
            size,
            pivot,
            cursorWrittenDateTime
        )

    }

    fun retrieveMemberNewsCountByTargetDateTime(
        member: Member,
        targetDate: LocalDate,
    ): Int {
        val targetDateTimePeriod = parseTargetDateTimePeriod(targetDate)
        val firstDayOfMonth = targetDateTimePeriod.first
        val lastDayOfMonth = targetDateTimePeriod.second

        return memberNewsRepository.countByMemberAndCreatedAtBetween(
            member,
            firstDayOfMonth,
            lastDayOfMonth
        )
    }

    private fun notContainedCursor(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        member: Member,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        if (pivot == Pivots.ASC) {
            val memberNewsBundle = memberNewsRepository.findAllByMember(member)
            val newsBundle = memberNewsBundle
                .map { it.news }
                .sortedBy { it.writtenDateTime }

            return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
                firstDayOfMonth,
                lastDayOfMonth,
                newsBundle.map { it.id },
                size
            ).sortedBy { it.writtenDateTime }
        }

        val memberNewsBundle = memberNewsRepository.findAllByMember(member)
        val newsBundle = memberNewsBundle
            .map { it.news }
            .sortedByDescending { it.writtenDateTime }

        return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
            firstDayOfMonth,
            lastDayOfMonth,
            newsBundle.map { it.id },
            size
        ).sortedByDescending { it.writtenDateTime }
    }

    private fun containedCursor(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        member: Member,
        size: Int,
        pivot: Pivots,
        cursorWrittenDateTime: String,
    ): List<News> {
        if (pivot == Pivots.ASC) {
            val memberNewsBundle = memberNewsRepository.findAllByMember(member)
            val newsBundle = memberNewsBundle
                .map { it.news }
                .sortedBy { it.writtenDateTime }
                .filter { it.writtenDateTime > cursorWrittenDateTime }

            return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
                firstDayOfMonth,
                lastDayOfMonth,
                newsBundle.map { it.id },
                size
            ).sortedBy { it.writtenDateTime }
        }

        val memberNewsBundle = memberNewsRepository.findAllByMember(member)
        val newsBundle = memberNewsBundle
            .map { it.news }
            .sortedByDescending { it.writtenDateTime }
            .filter { it.writtenDateTime < cursorWrittenDateTime }

        return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
            firstDayOfMonth,
            lastDayOfMonth,
            newsBundle.map { it.id },
            size
        ).sortedByDescending { it.writtenDateTime }
    }

    private fun parseTargetDateTimePeriod(targetDate: LocalDate): Pair<LocalDateTime, LocalDateTime> {
        val firstDayOfMonth = LocalDateTime.of(
            targetDate.withDayOfMonth(1),
            LocalTime.of(0, 0)
        )
        val lastDayOfMonth = LocalDateTime.of(
            targetDate.withDayOfMonth(targetDate.lengthOfMonth()),
            LocalTime.of(23, 59)
        )

        return Pair(firstDayOfMonth, lastDayOfMonth)
    }
}
