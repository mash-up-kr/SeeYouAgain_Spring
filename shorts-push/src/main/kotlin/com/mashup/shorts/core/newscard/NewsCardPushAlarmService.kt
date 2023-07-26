package com.mashup.shorts.core.newscard

import com.mashup.shorts.core.FCMPushAlarmForm
import com.mashup.shorts.core.FCMPushAlarmService
import com.mashup.shorts.domain.member.MemberRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class NewsCardPushAlarmService(
    private val fcmPushAlarmService: FCMPushAlarmService,
    private val memberRepository: MemberRepository,
) {

    /**
    매일 아침 7시마다 푸쉬 알림 전송
     */
    @Scheduled(cron = "0 7 * * * *")
    fun sendGoodMorningNewsCard() {
        fcmPushAlarmService.send(
            FCMPushAlarmForm(
                members = memberRepository.findAll(),
                title = title,
                content = content,
                imageURL = imageURL,
                dataMap = mutableMapOf()
            )
        )
    }

    companion object {
        private const val title = "Shorts"
        private const val content = "오늘 아침 숏스로 하루를 시작해봐요!"
        private const val imageURL = "https://github.com/mash-up-kr/SeeYouAgain_Spring/assets/60564431/19c98251-83e8-40b5-af8e-97bbbc1556c6"
    }
}
