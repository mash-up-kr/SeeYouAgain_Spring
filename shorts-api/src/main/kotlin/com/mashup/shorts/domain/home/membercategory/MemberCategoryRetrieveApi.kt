package com.mashup.shorts.domain.home.membercategory

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membercategory.MemberCategoryRetrieve
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/member/category")
@RestController
class MemberCategoryRetrieveApi(
    private val memberCategoryRetrieve: MemberCategoryRetrieve,
) {

    /**
    유저 카테고리 조회 API
    2023.08.18 변경 내용 - 조회 모드를 변경하면 리턴 값이 구독한 회사로 변경됩니다.
     */
    @Auth
    @GetMapping
    fun retrieveMemberCategory(): ApiResponse<Map<String, List<Any>>> {
        return ApiResponse.success(
            OK,
            mapOf(
                "categories" to memberCategoryRetrieve.retrieveMemberCategory(
                    AuthContext.getMember()
                )
            )
        )
    }
}
