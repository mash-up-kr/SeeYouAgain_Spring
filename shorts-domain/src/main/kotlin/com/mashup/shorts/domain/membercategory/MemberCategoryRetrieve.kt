package com.mashup.shorts.domain.membercategory

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.ShowMode
import com.mashup.shorts.domain.membercompany.MemberCompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class MemberCategoryRetrieve(
    private val memberCategoryRepository: MemberCategoryRepository,
    private val memberCompanyRepository: MemberCompanyRepository,
) {

    fun retrieveMemberCategory(member: Member): List<Any> {
        if (member.showMode == ShowMode.NORMAL) {
            return memberCategoryRepository.findByMember(member).map { it.category.name }
        }
        return memberCompanyRepository.findAllByMember(member).map { it.company.name }
    }
}
