package com.mashup.shorts.domain.home.memberCategory

import java.util.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.home.memberCategory.dto.CategoryCreateBulkRequest
import com.mashup.shorts.domain.membercategory.MemberCategoryCreate

@RequestMapping("/v1/member/category")
@RestController
class MemberCategoryCreateApi(
    private val memberCategoryCreate: MemberCategoryCreate,
) {

    /**
    유저 카테고리 등록 API
    @Body : {List<CategoryName>}
     */
    @PostMapping
    fun createCategory(
        @RequestBody categoryCreateBulkRequest: CategoryCreateBulkRequest,
    ): ApiResponse<Map<String, String>> {
        val memberUniqueId = UUID.randomUUID().toString()
        memberCategoryCreate.createCategory(categoryCreateBulkRequest.categoryNames, memberUniqueId)
        return ApiResponse.success(HttpStatus.CREATED, mapOf("uniqueId" to memberUniqueId))
    }

    /**
    유저 카테고리 수정 API
    @Body : {List<CategoryName>}
     */
    @Auth
    @PutMapping
    fun modifyMemberCategory(
        @RequestBody categoryCreateBulkRequest: CategoryCreateBulkRequest,
    ): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberCategoryCreate.modifyMemberCategory(categoryCreateBulkRequest.categoryNames, member)
        return ApiResponse.success(HttpStatus.OK)
    }
}
