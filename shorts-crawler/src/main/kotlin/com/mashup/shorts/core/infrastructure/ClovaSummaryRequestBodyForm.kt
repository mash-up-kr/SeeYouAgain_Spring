package com.mashup.shorts.core.infrastructure

data class ClovaSummaryRequestBodyForm(
    val document: Document,
    val option: Option,
)

data class Document(
    val title: String,
    val content: String,
)

data class Option(
    val language: String,
    val model: String?,
    val tone: Int?,
    val summaryCount: Int?,
)
