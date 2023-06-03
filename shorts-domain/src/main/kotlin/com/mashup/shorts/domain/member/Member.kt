package com.mashup.shorts.domain.member

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Table(name = "member")
@Entity
class Member(
    @Column(name = "unique_id")
    val uniqueId: String,
) : BaseEntity()
