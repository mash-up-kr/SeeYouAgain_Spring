package com.mashup.shorts.domain.my.statistics

import java.time.LocalDate

interface ShortsCntByDateVo {
    fun getDate(): LocalDate
    fun getShortsCnt(): Int
}
