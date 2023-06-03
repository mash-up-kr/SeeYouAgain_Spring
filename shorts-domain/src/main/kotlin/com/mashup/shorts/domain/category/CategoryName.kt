package com.mashup.shorts.domain.category

enum class CategoryName(
    private val korDesc: String
) {

    POLITICS("정치"),
    ECONOMIC("경제"),
    SOCIETY("사회"),
    CULTURE("생활/문화"),
    WORLD("세계"),
    SCIENCE("IT/과학");

    companion object {
        val categories = CategoryName.values()

        fun getKorDesc(name: String): String {
            return CategoryName.valueOf(name).korDesc
        }
    }
}
