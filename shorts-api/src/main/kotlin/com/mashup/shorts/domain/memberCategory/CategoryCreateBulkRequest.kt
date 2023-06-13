package com.mashup.shorts.domain.memberCategory

import com.mashup.shorts.domain.category.CategoryName
import io.swagger.v3.oas.annotations.media.Schema

/**
 * CategoryCreateBulkRequest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
data class CategoryCreateBulkRequest(
    @Schema(description = "추가할 카테고리 이름", example = "POLITICS, ECONOMY ...")
    val categoryNames: List<CategoryName>
)
