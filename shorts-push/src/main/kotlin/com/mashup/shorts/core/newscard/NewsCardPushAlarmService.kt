package com.mashup.shorts.core.newscard

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import com.mashup.shorts.core.FCMPushAlarmForm
import com.mashup.shorts.core.FCMPushAlarmService
import com.mashup.shorts.domain.member.MemberRepository

@Service
class NewsCardPushAlarmService(
    private val fcmPushAlarmService: FCMPushAlarmService,
    private val memberRepository: MemberRepository,
) {

    private val title = "Shorts"
    private val content = "오늘 아침 숏스로 하루를 시작해봐요!"
    private val imageURL = "https://github.com/mash-up-kr/SeeYouAgain_Spring/assets/60564431/19c98251-83e8-40b5-af8e-97bbbc1556c6"

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
}
