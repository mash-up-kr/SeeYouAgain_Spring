package com.mashup.shorts.domain.membernews

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
        member: Member,
        cursorWrittenDateTime: String,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        val memberNewsBundle = memberNewsRepository.findAllByMember(member)
        return newsRepository.loadNewsBundleByCursorAndNewsCardMultipleNews(
            cursorWrittenDateTime,
            memberNewsBundle.map { it.news.id },
            size,
            pivot
        )
    }
}
