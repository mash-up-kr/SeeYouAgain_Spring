package com.mashup.shorts.domain.facade.memberlogbadge

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.ShowMode
import com.mashup.shorts.domain.memberbadge.MemberBadge
import com.mashup.shorts.domain.memberbadge.MemberBadgeRepository
import com.mashup.shorts.domain.memberlog.MemberLog
import com.mashup.shorts.domain.memberlog.MemberLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberLogBadgeFacadeService(
    private val memberLogRepository: MemberLogRepository,
    private val memberBadgeRepository: MemberBadgeRepository,
) {

    fun createLogAndBadgeByMember(member: Member) {
        memberLogRepository.save(
            MemberLog(
                member = member,
            )
        )

        memberBadgeRepository.save(
            MemberBadge(
                member = member
            )
        )
    }

    fun todayShortsLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSavedTodayShortsCount()

        if (memberLog.savedTodayShortsCount == 1L) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveFirstTodayShortsSaving()
        }
    }

    fun oldShortsLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSavedOldShortsCount()

        if (memberLog.savedOldShortsCount == 1L) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveFirstOldShortsSaving()
        }
    }

    fun readCompleteLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseReadCompleteCount()

        if (memberLog.readCompleteCount == 1) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveFirstReadCompleteShortsSaving()
        }
    }

    fun memberSharingLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSharedCount()

        if (memberLog.sharedCount == 1) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveKingOfSharing()
        }
    }

    fun updateMemberShowModeLog(member: Member, showMode: ShowMode) {
        member.changeShowMode(showMode)
        val memberBadge = memberBadgeRepository.findByMember(member)
        if (!memberBadge.changeMode) {
            memberBadge.achieveChangeMode()
        }
    }

    fun memberAttendanceLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.isContinuousAttendance()
        if (memberLog.continuousAttendanceCount == CONTINUOUS_LOGIN_THREE_TIMES) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveThreeDaysContinuousAttendance()
        } else if (memberLog.continuousAttendanceCount == CONTINUOUS_LOGIN_TEN_TIME) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveTenDaysContinuousAttendance()
        }
    }

    fun memberNumberOfReadsPerWeekLog(member: Member, count: Int) {
        val memberBadge = memberBadgeRepository.findByMember(member)

        if (count == CONTINUOUS_READ_SHORTS_TWENTY) {
            memberBadge.achieveExplorer()
        }
    }

    companion object {
        const val CONTINUOUS_LOGIN_THREE_TIMES = 3
        const val CONTINUOUS_LOGIN_TEN_TIME = 10
        const val CONTINUOUS_READ_SHORTS_TWENTY = 20
    }
}