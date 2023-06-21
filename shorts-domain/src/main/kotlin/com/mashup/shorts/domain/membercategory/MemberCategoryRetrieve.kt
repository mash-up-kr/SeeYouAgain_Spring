package com.mashup.shorts.domain.membercategory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.member.Member

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
    private val memberCategoryRepository: MemberCategoryRepository
) {

    fun retrieveMemberCategory(member: Member): List<CategoryName> {
        return memberCategoryRepository.findByMember(member).map { it.category.name }
    }
}
