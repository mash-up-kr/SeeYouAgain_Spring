package com.mashup.shorts.domain.member

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Table(name = "member")
@Entity
class Member(
    @Column(name = "unique_id", nullable = false, length = 100)
    val uniqueId: String,

    @Column(name = "nickname", nullable = false, length = 40)
    val nickname: String,
) : BaseEntity()
