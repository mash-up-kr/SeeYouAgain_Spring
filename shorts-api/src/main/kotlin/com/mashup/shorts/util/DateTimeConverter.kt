package com.mashup.shorts.util

import java.time.LocalDateTime

fun convert(localDateTime: LocalDateTime): LocalDateTime {
    return localDateTime.withSecond(0).withNano(0)
}
