package com.mashup.shorts.domain.news

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.membernews.MemberNewsRepository

@Service
@Transactional(readOnly = true)
class NewsRetrieve(
    private val memberRepository: MemberRepository,
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository
) {

    fun retrieveNews(member: Member, newsId: Long): NewsRetrieveInfo {
        val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "뉴스 링크를 가져오는 중 요청한 NewsId : ${newsId}를 찾을 수 없습니다."
        )
        if (memberNewsRepository.existsByMemberAndNews(member, news)) {
            return NewsRetrieveInfo(newsLink = news.newsLink, isSaved = true)
        }
        return NewsRetrieveInfo(newsLink = news.newsLink, isSaved = false)
    }

}
