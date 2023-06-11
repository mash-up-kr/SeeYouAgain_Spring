package com.mashup.shorts.domain.member.membercategory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.category.CategoryRetrieve
import com.mashup.shorts.domain.member.MemberCreate

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
    private val memberCreate: MemberCreate
) {

    @Transactional
    fun createCategory(categoryNames: List<CategoryName>, uniqueId: String) {
        val categories = categoryRetrieve.getCategories(categoryNames)
        val member = memberCreate.createMember(uniqueId)
        val memberCategories = categories.map { MemberCategory(category = it, member = member) }
        memberCategoryRepository.saveAll(memberCategories)
    }
}

