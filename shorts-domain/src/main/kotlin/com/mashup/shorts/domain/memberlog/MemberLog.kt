package com.mashup.shorts.domain.memberlog

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.*

@Table(name = "member_log")
@Entity
class MemberLog(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    // 연속 접속일 수
    @Column
    var continuousCount: Int,

    // 1주일 동안 숏스를 읽은 갯수
    // 이 컬럼은 매주 월요일 00시마다 0으로 초기화 되어야함
    @Column
    var continuousWeekReadCount: Int,

    // 공유 횟수
    @Column
    var sharedCount: Int,

    // 저장한 오늘의 숏스 모든 갯수
    @Column
    var savedTodayShortsCount: Long,

    // 저장한 오래 간직할 숏스의 모든 갯수
    @Column
    var savedOldShortsCount: Long,
) : BaseEntity() {

    fun increaseContinuousCount() {
        this.continuousCount += 1
    }

    fun clearContinuousCount() {
        this.continuousCount = 0
    }

    fun increaseContinuousWeekReadCount() {
        this.continuousWeekReadCount += 1
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
}
