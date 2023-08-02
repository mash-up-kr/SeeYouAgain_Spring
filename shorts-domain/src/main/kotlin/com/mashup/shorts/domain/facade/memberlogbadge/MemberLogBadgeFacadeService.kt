package com.mashup.shorts.domain.facade.memberlogbadge

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.ShowMode
import com.mashup.shorts.domain.memberbadge.MemberBadge
import com.mashup.shorts.domain.memberbadge.MemberBadgeRepository
import com.mashup.shorts.domain.memberlog.MemberLog
import com.mashup.shorts.domain.memberlog.MemberLogRepository
import com.mashup.shorts.domain.news.News
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

    fun saveNewsLogByNewsCard(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSavedNewsCountByNewsCard()

        if (memberLog.savedNewsCountByNewsCard == 1) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveFirstNewsSaving()
        }
    }

    fun saveNewsLogByKeyword(member: Member) {
        val memberLog = memberLogRepository.findByMember(member)
        memberLog.increaseSavedNewsCountByKeyword()

        if (memberLog.savedNewsCountByKeyword == 1) {
            val memberBadge = memberBadgeRepository.findByMember(member)
            memberBadge.achieveFirstNewsSaving()
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

    fun memberClearingNewsLog(member: Member, news: News) {
        val memberLog = memberLogRepository.findByMember(member)
        val memberBadge = memberBadgeRepository.findByMember(member)

        memberLog.increaseClearingNewsCount()
        memberLog.weeklyReadCount++

        if (memberLog.clearingNewsCount == 1) {
            memberBadge.achieveFirstClearNews()
        }

        if (memberLog.weeklyReadCount == CONTINUOUS_READ_SHORTS_TWENTY && !memberBadge.explorer) {
            memberBadge.achieveExplorer()
        }
    }

    companion object {
        const val CONTINUOUS_LOGIN_THREE_TIMES = 3
        const val CONTINUOUS_LOGIN_TEN_TIME = 10
        const val CONTINUOUS_READ_SHORTS_TWENTY = 20
    }
}