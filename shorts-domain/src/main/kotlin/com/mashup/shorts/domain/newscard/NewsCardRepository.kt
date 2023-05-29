package com.mashup.shorts.domain.newscard

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsCardRepository : JpaRepository<NewsCard, Long>
