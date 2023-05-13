package com.mashup.shorts.domain.member

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Member
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
@Table(name = "member")
@Entity
class Member(
    @Column(name = "uuid")
    val uuid: String
): BaseEntity()
