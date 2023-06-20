package com.mashup.shorts.domain.member.membernewscard

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.member.membernews.MemberNewsRepository
import com.mashup.shorts.domain.member.membernewscard.dtomapper.RetrieveAllNewsCardResponseMapper
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCardQueryDSLRepository

@Service
@Transactional(readOnly = true)
class MemberNewsCardRetrieve(
    private val memberRepository: MemberRepository,
    private val memberCategoryRepository: MemberCategoryRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberNewsCardQueryDSLRepository: NewsCardQueryDSLRepository,
    private val newsRepository: NewsRepository,
) {

    fun retrieveNewsCardByMember(
        memberUniqueId: String,
        targetDateTime: LocalDateTime,
        cursorId: Long,
        size: Int,
    ): List<RetrieveAllNewsCardResponseMapper> {
        val member = memberRepository.findByUniqueId(memberUniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "${memberUniqueId}에 해당하는 사용자는 존재하지 않습니다."
        )

        val memberCategories = memberCategoryRepository.findByMember(member)
        val filteredNewsIds = filterAlreadySavedNews(member)

        println("filteredNewsIds = ${filteredNewsIds}")

        return memberNewsCardQueryDSLRepository.findNewsCardsByMemberCategoryAndCursorId(
            filteredNewsIds = filteredNewsIds,
            cursorId = cursorId,
            size = size,
            categories = memberCategories.map { it.category.id }
        )
    }

    private fun filterAlreadySavedNews(member: Member): List<String> {
        val memberNewsBundle = memberNewsRepository.findAllByMember(member)
        val result = mutableListOf<String>()
        memberNewsBundle.map { result.add(it.news.id.toString()) }
        return result
    }
}
