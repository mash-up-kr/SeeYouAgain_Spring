package com.mashup.shorts.domain.member.membercategory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.category.CategoryRetrieve
import com.mashup.shorts.domain.member.MemberCreate
import com.mashup.shorts.domain.member.MemberRepository

/**
 * MemberCategoryCreate
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
@Transactional(readOnly = true)
@Service
class MemberCategoryCreate(
    private val memberCategoryRepository: MemberCategoryRepository,
    private val categoryRetrieve: CategoryRetrieve,
    private val memberCreate: MemberCreate,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun createCategory(categoryNames: List<CategoryName>, uniqueId: String) {
        val categories = categoryRetrieve.getCategories(categoryNames)
        val member = memberCreate.createMember(uniqueId)
        val memberCategories = categories.map { MemberCategory(category = it, member = member) }
        memberCategoryRepository.saveAll(memberCategories)
    }

    @Transactional
    fun modifyMemberCategory(categoryNames: List<CategoryName>, memberUniqueId: String) {
        // TODO: ThreadLocal 에 Member 담아서 사용하도록 로직 리팩터링 하기
        val member = memberRepository.findByUniqueId(memberUniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${memberUniqueId}에 해당하는 멤버가 존재하지 않습니다."
        )

        val categories = categoryRetrieve.getCategories(categoryNames)
        val memberCategories = categories.map { MemberCategory(category = it, member = member) }
        memberCategoryRepository.deleteAllByMember(member)
        memberCategoryRepository.saveAll(memberCategories)
    }
}

