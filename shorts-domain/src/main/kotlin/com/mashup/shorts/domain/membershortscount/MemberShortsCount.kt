package com.mashup.shorts.domain.membershortscount

import com.mashup.shorts.domain.member.Member
import jakarta.persistence.*
import java.time.LocalDate

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
