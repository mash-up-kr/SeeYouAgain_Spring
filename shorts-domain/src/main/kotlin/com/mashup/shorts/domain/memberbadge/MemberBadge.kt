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

    // 작심삼일 - 3일 연속 접속
    @Column(nullable = false)
    var threeDaysContinuousAttendance: Boolean = false,

    @Column(nullable = true)
    var threeDaysContinuousAttendanceCreatedAt: LocalDateTime? = null,

    // 단골손님 - 10일 연속 접속
    @Column(nullable = false)
    var tenDaysContinuousAttendance: Boolean = false,

    @Column(nullable = true)
    var tenDaysContinuousAttendanceCreatedAt: LocalDateTime? = null,

    // 세상 탐험가 뱃지 - 1주일 간 뉴스를 20개 저장
    @Column(nullable = false)
    var explorer: Boolean = false,

    @Column(nullable = true)
    var explorerCreatedAt: LocalDateTime? = null,

    // 뿌듯한 첫 공유 - 첫 공유 시도
    @Column(nullable = false)
    var kingOfSharing: Boolean = false,

    @Column(nullable = true)
    var kingOfSharingCreatedAt: LocalDateTime? = null,

    // 설레는 첫 저장 뱃지 - 첫 뉴스 저장
    @Column(nullable = false)
    var firstNewsSaving: Boolean = false,

    @Column(nullable = true)
    var firstNewsSavingCreatedAt: LocalDateTime? = null,

    // 시작이 반 - 처음으로 뉴스를 다 읽음
    @Column(nullable = false)
    var firstClearNews: Boolean = false,

    @Column(nullable = true)
    var firstClearNewsCreatedAt: LocalDateTime? = null,

    // 취향 존중 - 조회 모드 변경
    @Column(nullable = false)
    var changeMode: Boolean = false,

    @Column(nullable = true)
    var changeModeCreatedAt: LocalDateTime? = null
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

    fun achieveFirstNewsSaving() {
        this.firstNewsSaving = true
        this.firstNewsSavingCreatedAt = LocalDateTime.now()
    }

    fun achieveFirstClearNews() {
        this.firstClearNews = true
        this.firstClearNewsCreatedAt = LocalDateTime.now()
    }

    fun achieveChangeMode() {
        this.changeMode = true
        this.changeModeCreatedAt = LocalDateTime.now()
    }
}
