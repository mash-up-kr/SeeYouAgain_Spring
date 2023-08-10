package com.mashup.shorts.domain.membercompany

import com.mashup.shorts.domain.member.Company
import com.mashup.shorts.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberCompanyCreate(
    private val memberCompanyRepository: MemberCompanyRepository
) {

    fun createMemberCompany(member: Member, companies: List<Company>) {
        companies.map {
            memberCompanyRepository.save(
                MemberCompany(
                    member = member,
                    company = it
                )
            )
        }
    }
}