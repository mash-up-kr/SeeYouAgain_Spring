//package com.mashup.shorts.domain.member.membernewscard.dtomapper
//
//import java.time.LocalDateTime
//import com.mashup.shorts.domain.newscard.NewsCard
//
//data class RetrieveAllNewsCardResponse(
//    var id: Long,
//    var keywords: String,
//    var category: String,
//    var crawledDateTime: LocalDateTime,
//) {
//    companion object {
//        fun persistenceToResponseForm(newsCards: List<NewsCard>): List<RetrieveAllNewsCardResponse> {
//            return newsCards.map {
//                RetrieveAllNewsCardResponse(
//                    id = it.id,
//                    keywords = it.keywords,
//                    category = it.category.name.name,
//                    crawledDateTime = it.createdAt,
//                )
//            }
//        }
//    }
//}
