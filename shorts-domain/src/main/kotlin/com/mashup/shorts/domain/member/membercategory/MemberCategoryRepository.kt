package com.mashup.shorts.domain.member.membercategory

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberCategoryRepository : JpaRepository<MemberCategory, Long>
