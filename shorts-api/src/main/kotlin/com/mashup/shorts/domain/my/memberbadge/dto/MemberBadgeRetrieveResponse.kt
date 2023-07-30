package com.mashup.shorts.domain.my.memberbadge.dto

data class MemberBadgeRetrieveResponse(
    val threeDaysContinuousAttendance: Boolean,
    val tenDaysContinuousAttendance: Boolean,
    val explorer: Boolean,
    val kingOfSharing: Boolean,
    val firstAllReadShorts: Boolean,
    val firstOldShortsSaving: Boolean,
    val changeMode: Boolean,
)
