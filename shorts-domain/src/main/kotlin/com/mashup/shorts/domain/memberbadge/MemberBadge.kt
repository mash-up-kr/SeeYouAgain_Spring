package com.mashup.shorts.domain.memberbadge

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "member_badge")
@Entity
class MemberBadge(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    // 작심삼일 뱃지
    @Column(nullable = false)
    var threeDaysContinuousAttendance: Boolean = false,

    @Column(nullable = true)
    var threeDaysContinuousAttendanceCreatedAt: LocalDateTime,

    // 단골손님 뱃지
    @Column(nullable = false)
    var tenDaysContinuousAttendance: Boolean = false,

    @Column(nullable = true)
    var tenDaysContinuousAttendanceCreatedAt: LocalDateTime,

    // 세상 탐험가 뱃지
    @Column(nullable = false)
    var explorer: Boolean = false,

    @Column(nullable = true)
    var explorerCreatedAt: LocalDateTime,

    // 뿌듯한 첫 공유 뱃지
    @Column(nullable = false)
    var kingOfSharing: Boolean = false,

    @Column(nullable = true)
    var kingOfSharingCreatedAt: LocalDateTime,

    // 설레는 첫 저장 뱃지
    @Column(nullable = false)
    var firstTodayShortsSaving: Boolean = false,

    @Column(nullable = true)
    var firstTodayShortsSavingCreatedAt: LocalDateTime,

    // 시작이 반 뱃지
    @Column(nullable = false)
    var firstAllReadShorts: Boolean = false,

    @Column(nullable = true)
    var firstAllReadShortsCreatedAt: LocalDateTime,

    // 오래 간직될 지식 뱃지
    @Column(nullable = false)
    var firstOldShortsSaving: Boolean = false,

    @Column(nullable = true)
    var firstOldShortsSavingCreatedAt: LocalDateTime,

    // 취향 존중 뱃지
    @Column(nullable = false)
    var changeMode: Boolean = false,

    @Column(nullable = true)
    var changeModeCreatedAt: LocalDateTime
) : BaseEntity() {

    fun achieveThreeDaysContinuousAttendance() {
        this.threeDaysContinuousAttendance = true
        this.threeDaysContinuousAttendanceCreatedAt = LocalDateTime.now()
    }

    fun achieveTenDaysContinuousAttendance() {
        this.tenDaysContinuousAttendance = true
        this.tenDaysContinuousAttendanceCreatedAt = LocalDateTime.now()
    }

    fun achieveExplorer() {
        this.explorer = true
        this.explorerCreatedAt = LocalDateTime.now()
    }

    fun achieveKingOfSharing() {
        this.kingOfSharing = true
        this.kingOfSharingCreatedAt = LocalDateTime.now()
    }

    fun achieveFirstTodayShortsSaving() {
        this.firstTodayShortsSaving = true
        this.firstTodayShortsSavingCreatedAt = LocalDateTime.now()
    }

    fun achieveFirstReadCompleteShortsSaving() {
        this.firstAllReadShorts = true
        this.firstAllReadShortsCreatedAt = LocalDateTime.now()
    }

    fun achieveFirstOldShortsSaving() {
        this.firstOldShortsSaving = true
        this.firstOldShortsSavingCreatedAt = LocalDateTime.now()
    }

    fun achieveChangeMode() {
        this.changeMode = true
        this.changeModeCreatedAt = LocalDateTime.now()
    }
}
