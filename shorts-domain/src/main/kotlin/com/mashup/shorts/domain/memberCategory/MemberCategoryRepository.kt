package com.mashup.shorts.domain.memberCategory

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.memberCategory.MemberCategory

@Repository
interface MemberCategoryRepository : JpaRepository<MemberCategory, Long>
