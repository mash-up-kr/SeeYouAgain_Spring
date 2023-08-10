package com.mashup.shorts.domain.membercompany

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Company
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.*

@Table(name = "member_company")
@Entity
class MemberCompany(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @Column(name = "company", nullable = true, length = 255)
    var company: Company,
) : BaseEntity()
