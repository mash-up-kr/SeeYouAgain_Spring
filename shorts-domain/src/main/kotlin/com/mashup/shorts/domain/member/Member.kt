package com.mashup.shorts.domain.member

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Table(name = "member")
@Entity
class Member : BaseEntity()
