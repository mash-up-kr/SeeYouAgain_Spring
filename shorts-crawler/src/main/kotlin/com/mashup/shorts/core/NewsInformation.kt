package com.mashup.shorts.core

data class NewsInformation(
    var title: String,
    var content: String,
    var thumbnail: String,
    var link: String,
    var press: String,
    var writtenDateTime: String,
    var isHeadLine: HeadLine,
) {

    constructor(extractedNews: List<List<Any>>, index: Int) : this(
        extractedNews[titleIndex][index] as? String ?: "",
        extractedNews[contentIndex][index] as? String ?: "",
        extractedNews[thumbnailIndex][index] as? String ?: "",
        extractedNews[linkIndex][index] as? String ?: "",
        extractedNews[pressIndex][index] as? String ?: "",
        extractedNews[writtenDateIndex][index] as? String ?: "",
        if (extractedNews[isHeadLineIndex][index] as? Boolean == true) HeadLine.HEAD_LINE else HeadLine.NORMAL
    )

    companion object {
        private const val titleIndex = 0
        private const val contentIndex = 1
        private const val thumbnailIndex = 2
        private const val linkIndex = 3
        private const val pressIndex = 4
        private const val isHeadLineIndex = 6
        private const val writtenDateIndex = 5
    }
}
