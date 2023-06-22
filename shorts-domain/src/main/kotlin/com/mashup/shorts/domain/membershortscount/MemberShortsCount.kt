package com.mashup.shorts.domain.membershortscount

import java.time.LocalDate
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Table(name = "member_shorts_count")
@Entity
class MemberShortsCount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @Column(name = "count", nullable = false)
    var count: Int = 0,

    @Column(name = "target_date", nullable = false)
    val targetDate: LocalDate,
) {

    fun increaseCount() {
        this.count += 1
    }
}
