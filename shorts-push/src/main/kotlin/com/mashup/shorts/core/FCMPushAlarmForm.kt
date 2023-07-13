package com.mashup.shorts.core

import com.mashup.shorts.domain.member.Member

open class FCMPushAlarmForm(
    val members: List<Member>,
    val title: String,
    val content: String,
    val imageURL: String,
    val dataMap: Map<String, String>,
)
