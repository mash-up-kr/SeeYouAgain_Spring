package com.mashup.shorts.domain.memberCategory

import java.util.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.member.membercategory.MemberCategoryCreate
import io.swagger.v3.oas.annotations.Operation

/**
 * MemberCategoryCreateApi
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
@RequestMapping("/v1/member/category")
@RestController
class MemberCategoryCreateApi(
    private val memberCategoryCreate: MemberCategoryCreate
) {

    @Operation(summary = "유저 카테고리 저장", description = "카테고리 저장")
    @PostMapping
    fun createCategory(@RequestBody categoryCreateBulkRequest: CategoryCreateBulkRequest): ApiResponse<Map<String, String>> {
        val uniqueId = UUID.randomUUID().toString()
        memberCategoryCreate.createCategory(categoryCreateBulkRequest.categoryNames, uniqueId)
        return ApiResponse.success(HttpStatus.CREATED, mapOf("uniqueId" to uniqueId))
    }
}
