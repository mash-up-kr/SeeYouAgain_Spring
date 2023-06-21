package com.mashup.shorts.domain.home.news.dto

import com.mashup.shorts.domain.news.NewsRetrieveInfo

class NewsRetrieveMapper {

    companion object {

        fun newsRetrieveInfoToResponse(newsRetrieveInfo: NewsRetrieveInfo): NewsRetrieveResponse {
            return NewsRetrieveResponse(newsLink = newsRetrieveInfo.newsLink, isSaved = newsRetrieveInfo.isSaved)
        }
    }
}
