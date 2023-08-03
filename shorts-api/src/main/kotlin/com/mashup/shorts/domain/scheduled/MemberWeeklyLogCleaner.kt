package com.mashup.shorts.domain.scheduled

import com.mashup.shorts.domain.memberlog.MemberLogRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberWeeklyLogCleaner(
    private val memberLogRepository: MemberLogRepository
) {

    // 매주 월요일 00시에 동작한다.
    @Scheduled(cron = "0 0 0 * * 1")
    fun eraseMemberWeeklyReadCount() {
        memberLogRepository.findAll().map { it.weeklyReadCount = 0 }
    }
}