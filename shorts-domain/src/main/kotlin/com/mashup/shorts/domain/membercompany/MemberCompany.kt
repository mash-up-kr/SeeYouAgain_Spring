package com.mashup.shorts.domain.membercompany

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.*

@Table(name = "member_company")
@Entity
class MemberCompany(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @Column(name = "company", nullable = false, length = 100)
    var company: String,
) : BaseEntity()
