package com.mashup.shorts.domain.memberlogbadge

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
class MemberLogBadgeService(
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

    fun saveNewsCardLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSavedNewsCardCount()

        if (memberLog.savedNewsCardCount == FIRST_PROCEED) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveFirstNewsSaving()
        }
    }

    fun saveNewsLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSavedNewsCount()

        if (memberLog.savedNewsCount == FIRST_PROCEED) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveFirstNewsSaving()
        }
    }

    fun memberSharingLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSharedCount()

        if (memberLog.sharedCount == FIRST_PROCEED) {
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

    fun memberReadNewsLog(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        val memberBadge = memberBadgeRepository.findByMember(member)

        memberLog.increaseReadNewsCount()
        memberLog.weeklyReadCount++

        if (memberLog.readNewsCount == FIRST_PROCEED) {
            memberBadge.achieveFirstClearNews()
        }

        if (memberLog.weeklyReadCount == CONTINUOUS_READ_SHORTS_TWENTY && !memberBadge.explorer) {
            memberBadge.achieveExplorer()
        }
    }

    companion object {
        const val FIRST_PROCEED = 1
        const val CONTINUOUS_LOGIN_THREE_TIMES = 3
        const val CONTINUOUS_LOGIN_TEN_TIME = 10
        const val CONTINUOUS_READ_SHORTS_TWENTY = 20
    }
}
