package com.mashup.shorts.domain.memberlog

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Table(name = "member_log")
@Entity
class MemberLog(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    // 최근 접속 일자
    @Column(nullable = true)
    var lastAttendanceDateTime: LocalDateTime = LocalDateTime.now(),

    // 연속 접속일 수
    @Column(nullable = false)
    var continuousAttendanceCount: Int = 1,

    // 공유 횟수
    @Column(nullable = false)
    var sharedCount: Int = 0,

    // 키워드를 통해 조회한 뉴스를 저장한 갯수
    @Column(nullable = false)
    var savedNewsCountByKeyword: Int = 0,

    // 뉴스 카드에 담긴 뉴스를 저장한 갯수
    @Column(nullable = false)
    var savedNewsCountByNewsCard: Int = 0,

    // 저장한 뉴스를 읽은 횟수
    @Column(nullable = false)
    var clearingNewsCount: Int = 0,
) : BaseEntity() {

    fun isContinuousAttendance() {
        val lastAttendanceDateTime = this.lastAttendanceDateTime.toLocalDate()
        val now = LocalDate.now()

        if (lastAttendanceDateTime == now.minusDays(1)) {
            continuousAttendanceCount++
        } else {
            continuousAttendanceCount = 1
        }
    }

    fun increaseSharedCount() {
        this.sharedCount += 1
    }

    fun increaseSavedNewsCountByKeyword() {
        this.savedNewsCountByKeyword += 1
    }

    fun increaseSavedNewsCountByNewsCard() {
        this.savedNewsCountByNewsCard += 1
    }

    fun increaseClearingNewsCount() {
        this.clearingNewsCount += 1
    }

}
