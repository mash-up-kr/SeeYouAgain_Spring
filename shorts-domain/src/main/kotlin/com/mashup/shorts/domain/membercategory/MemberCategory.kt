package com.mashup.shorts.domain.membercategory

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "member_category")
@Entity
class MemberCategory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,
) : BaseEntity()
