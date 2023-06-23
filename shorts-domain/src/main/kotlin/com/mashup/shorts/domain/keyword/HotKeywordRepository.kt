package com.mashup.shorts.domain.keyword

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HotKeywordRepository : JpaRepository<HotKeyword, Long>
