package com.mashup.shorts.domain.member.membershortscount

import java.time.LocalDate
import org.springframework.data.annotation.Id
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.Column
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

class MemberShortsCount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @Column(name = "count", nullable = false)
    var count: Int = 0,

    @Column(name = "targetTime", nullable = false)
    val targetTime: LocalDate,
) {

    fun increaseCount() {
        this.count += 1
    }
}
