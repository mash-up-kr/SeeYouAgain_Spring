package com.mashup.shorts.domain.my.memberstatistics.dto

import java.time.LocalDateTime

data class MemberStatisticsResponse(
    val targetDateTime: LocalDateTime,
    val statistics: Map<String, Long>
) {
}
