package com.mashup.shorts.domain.keyword

import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HotKeywordRepository : JpaRepository<HotKeyword, Long> {

    fun findTopByOrderByIdDesc(): HotKeyword?
}
