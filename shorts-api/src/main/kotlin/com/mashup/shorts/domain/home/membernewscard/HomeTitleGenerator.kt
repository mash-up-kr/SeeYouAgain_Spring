package com.mashup.shorts.domain.home.membernewscard

import com.mashup.shorts.domain.member.ShowMode

object HomeTitleGenerator {

    val title = mutableMapOf(
        "이직 전투력 상승 현장" to ShowMode.RECRUIT_SWITCH_JOB.name,
        "오늘도 이직을 노린다" to ShowMode.RECRUIT_SWITCH_JOB.name,
        "Dreams Come True" to ShowMode.RECRUIT_SWITCH_JOB.name,
        "취준력 Level UP" to ShowMode.RECRUIT_SWITCH_JOB.name,
        "슬기로운 취준생활" to ShowMode.RECRUIT_SWITCH_JOB.name,
        "적을 알고 나를 알면 투자 백승" to ShowMode.INVESTMENT.name,
        "존버는 반드시 승리합니다" to ShowMode.INVESTMENT.name,
        "개미는 뚠뚠 오늘도 뚠뚠" to ShowMode.INVESTMENT.name,
        "슈퍼개미 가즈아~!" to ShowMode.INVESTMENT.name,
    )

    fun generateRandomHomeTitle(showMode: ShowMode): String {
        return when (showMode) {
            ShowMode.RECRUIT_SWITCH_JOB ->
                title.entries.filter { it.value == ShowMode.RECRUIT_SWITCH_JOB.name }.map { it.key }.random()

            ShowMode.INVESTMENT ->
                title.entries.filter { it.value == ShowMode.INVESTMENT.name }.map { it.key }.random()

            ShowMode.MY_COMPANY -> "오늘의 Shorts News"
            ShowMode.NORMAL -> "오늘의 Shorts News"
        }
    }
}