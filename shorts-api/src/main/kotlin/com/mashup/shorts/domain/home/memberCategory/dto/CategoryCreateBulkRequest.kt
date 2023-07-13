package com.mashup.shorts.domain.home.memberCategory.dto

import com.mashup.shorts.domain.category.CategoryName

data class CategoryCreateBulkRequest(
    val categoryNames: List<CategoryName>,
    val fcmTokenPayload: String,
)

data class CategoryUpdateBulkRequest(
    val categoryNames: List<CategoryName>,
)
