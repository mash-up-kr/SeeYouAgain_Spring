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

    // 1주일 동안 숏스를 읽은 갯수
    // 이 컬럼은 매주 월요일 00시마다 0으로 초기화 되어야함
    @Column(nullable = false)
    var continuousWeekReadCount: Int = 0,

    // 숏스 다 읽었어요 누적 갯수
    @Column(nullable = false)
    var readCompleteCount: Int = 0,

    // 공유 횟수
    @Column(nullable = false)
    var sharedCount: Int = 0,

    // 저장한 오늘의 숏스 모든 갯수
    @Column(nullable = false)
    var savedTodayShortsCount: Long = 0L,

    // 저장한 오래 간직할 숏스의 모든 갯수
    @Column(nullable = false)
    var savedOldShortsCount: Long = 0L,
) : BaseEntity() {

    fun increaseContinuousCount() {
        this.continuousAttendanceCount += 1
    }

    fun clearContinuousCount() {
        this.continuousAttendanceCount = 0
    }

    fun increaseContinuousWeekReadCount() {
        this.continuousWeekReadCount += 1
    }

    fun increaseReadCompleteCount() {
        this.readCompleteCount += 1
    }

    fun clearContinuousWeekReadCount() {
        this.continuousWeekReadCount = 0
    }

    fun increaseSharedCount() {
        this.sharedCount += 1
    }

    fun increaseSavedTodayShortsCount() {
        this.savedTodayShortsCount += 1
    }

    fun increaseSavedOldShortsCount() {
        this.savedOldShortsCount += 1
    }

    fun isContinuousAttendance() {
        val lastAttendanceDateTime = this.lastAttendanceDateTime.toLocalDate()
        val now = LocalDate.now()

        if (lastAttendanceDateTime == now.minusDays(1)) {
            continuousAttendanceCount++
        } else {
            continuousAttendanceCount = 1
        }
    }
}
