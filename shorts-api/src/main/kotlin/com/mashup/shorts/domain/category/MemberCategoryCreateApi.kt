package com.mashup.shorts.domain.category

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.memberCategory.MemberCategoryCreate

/**
 * MemberCategoryCreateApi
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 10.
 */
@RequestMapping("/api/v1/member/category")
@RestController
class MemberCategoryCreateApi(
    private val memberCategoryCreate: MemberCategoryCreate
) {

    @PostMapping
    fun createCategory(@RequestBody categoryCreateBulkRequest: CategoryCreateBulkRequest): ApiResponse<Void> {
        val uniqueId = UUID.randomUUID().toString()
        memberCategoryCreate.createCategory(categoryCreateBulkRequest.categoryNames, uniqueId)
        return ApiResponse.success(HttpStatus.CREATED)
    }
}
