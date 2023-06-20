package com.mashup.shorts.domain.member.membercategory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.member.MemberRepository

/**
 * MemberCategoryRetrieve
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 20.
 */
@Transactional(readOnly = true)
@Service
class MemberCategoryRetrieve(
    private val memberCategoryRepository: MemberCategoryRepository,
    private val memberRepository: MemberRepository
) {

    fun retrieveMemberCategory(memberUniqueId: String): List<CategoryName> {
        val member = memberRepository.findByUniqueId(memberUniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${memberUniqueId}에 해당하는 멤버가 존재하지 않습니다."
        )

        return memberCategoryRepository.findByMember(member).map {
            it.category.name
        }
    }
}
