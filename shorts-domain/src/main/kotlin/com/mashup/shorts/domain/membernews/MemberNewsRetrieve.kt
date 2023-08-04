package com.mashup.shorts.domain.membernews

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membercompany.MemberCompanyRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.news.NewsRetrieve
import com.mashup.shorts.domain.newscard.Pivots
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberNewsRetrieve(
    private val memberNewsRepository: MemberNewsRepository,
    private val newsRepository: NewsRepository,
    private val newsRetrieve: NewsRetrieve,
    private val memberCompanyRepository: MemberCompanyRepository,
) {

    fun retrieveMemberNewsCount(member: Member): Int {
        return memberNewsRepository.findAllByMember(member).count()
    }

    fun retrieveMemberNewsBySorting(
        member: Member,
        cursorWrittenDateTime: String,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        val memberNewsList = memberNewsRepository.findAllByMember(member)

        if (cursorWrittenDateTime.isEmpty()) {
            return when (pivot) {
                Pivots.ASC -> ascNewsListNoCursor(memberNewsList, size)
                Pivots.DESC -> descNewsListNoCursor(memberNewsList, size)
            }
        }

        return when (pivot) {
            Pivots.ASC -> ascNewsList(memberNewsList, cursorWrittenDateTime, size)
            Pivots.DESC -> descNewsList(memberNewsList, cursorWrittenDateTime, size)
        }
    }

    fun retrieveNewsByMemberCompany(
        member: Member,
        cursorId: Long,
        size: Int,
    ): List<News> {
        return newsRetrieve.retrieveByCompany(
            companies = memberCompanyRepository.findAllByMember(member).map { it.company },
            cursorId = cursorId,
            size = size
        )
    }

    private fun ascNewsListNoCursor(
        memberNewsList: List<MemberNews>,
        size: Int
    ): List<News> {
        val newsList = memberNewsList
            .map { it.news }
            .sortedBy { it.writtenDateTime }

        return newsRepository.findAllById(newsList.map { it.id })
            .sortedBy { it.writtenDateTime }
            .take(size)
    }

    private fun descNewsListNoCursor(
        memberNewsList: List<MemberNews>,
        size: Int
    ): List<News> {
        val newsList = memberNewsList
            .map { it.news }
            .sortedByDescending { it.writtenDateTime }

        return newsRepository.findAllById(newsList.map { it.id })
            .sortedByDescending { it.writtenDateTime }
            .take(size)
    }

    private fun ascNewsList(
        memberNewsList: List<MemberNews>,
        cursorWrittenDateTime: String,
        size: Int
    ): List<News> {
        val newsList = memberNewsList
            .map { it.news }
            .sortedBy { it.writtenDateTime }
            .filter { it.writtenDateTime > cursorWrittenDateTime }

        return newsRepository.findAllById(newsList.map { it.id })
            .sortedBy { it.writtenDateTime }
            .take(size)
    }

    private fun descNewsList(
        memberNewsList: List<MemberNews>,
        cursorWrittenDateTime: String,
        size: Int
    ): List<News> {
        val newsList = memberNewsList
            .map { it.news }
            .sortedByDescending { it.writtenDateTime }
            .filter { it.writtenDateTime < cursorWrittenDateTime }

        return newsRepository.findAllById(newsList.map { it.id })
            .sortedByDescending { it.writtenDateTime }
            .take(size)
    }
}
