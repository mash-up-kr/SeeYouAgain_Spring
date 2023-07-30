package com.mashup.shorts.domain.memberbadge

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "member_badge")
@Entity
class MemberBadge(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    // 작심삼일 뱃지
    @Column
    var threeDaysContinuousAttendance: Boolean,

    @Column
    var threeDaysContinuousAttendanceCreatedAt: LocalDateTime,

    // 단골손님 뱃지
    @Column
    var tenDaysContinuousAttendance: Boolean,

    @Column
    var tenDaysContinuousAttendanceCreatedAt: LocalDateTime,

    // 세상 탐험가 뱃지 - 독서왕
    @Column
    var kingOfRead: Boolean,

    @Column
    var kingOfReadCreatedAt: LocalDateTime,

    // 뿌듯한 첫 공유 뱃지
    @Column
    var kingOfSharing: Boolean,

    @Column
    var kingOfSharingCreatedAt: LocalDateTime,

    // 설레는 첫 저장 뱃지
    @Column
    var firstTodayShortsSaving: Boolean,

    @Column
    var firstTodayShortsSavingCreatedAt: LocalDateTime,

    // 시작이 반 뱃지
    @Column
    var firstAllReadShorts: Boolean,

    @Column
    var firstAllReadShortsCreatedAt: LocalDateTime,

    // 오래 간질될 지식 뱃지
    @Column
    var firstOldShortsSaving: Boolean,

    @Column
    var firstOldShortsSavingCreatedAt: LocalDateTime,

    // 취향 존중 뱃지
    @Column
    var changeMode: Boolean,

    @Column
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

    fun achieveKingOfRead() {
        this.kingOfRead = true
        this.kingOfReadCreatedAt = LocalDateTime.now()
    }

    fun achieveKingOfSharing() {
        this.kingOfSharing = true
        this.kingOfSharingCreatedAt = LocalDateTime.now()
    }

    fun achieveFirstTodayShortsSaving() {
        this.firstTodayShortsSaving = true
        this.firstTodayShortsSavingCreatedAt = LocalDateTime.now()
    }

    fun achieveFirstAllShortsSaving() {
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
