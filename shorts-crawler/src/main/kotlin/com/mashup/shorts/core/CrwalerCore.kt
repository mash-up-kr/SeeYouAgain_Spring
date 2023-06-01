package com.mashup.shorts.core

import java.io.StringReader
import java.time.LocalDateTime
import org.apache.lucene.analysis.ko.KoreanAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.domain.category.CategoryRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository


@Component
class CrawlerCore(
    private val newsRepository: NewsRepository,
    private val newsCardRepository: NewsCardRepository,
    private val categoryRepository: CategoryRepository,
) {

    @Transactional
    fun executeCrawling() {
        var newsBundle = mutableListOf<News>()
        var newsCards = mutableListOf<NewsCard>()

        // 특정 카테고리 순회
        //urls.indices
        for (categoryIndex: Int in 5..5) {
            log.info(LocalDateTime.now().toString() + " - " + urls[categoryIndex] + " - crawling start")
            val doc = setup(urls[categoryIndex], categoryIndex)
            val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
            val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, categoryIndex)

            // 특정 카테고리의 헤드라인 갯수 만큼 순회
            for (index: Int in 0 until extractedAllNews[RELATED_COUNT_INDEX].size) {
                val relatedNewsCount = extractedAllNews[RELATED_COUNT_INDEX][index].toString().toInt()

                // 특정 헤드라인의 N개의 관련된 기사 순회
                for (iterator in 0 until relatedNewsCount) {
                    when (categoryIndex) {
                        0 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews,
                                    TITLE_INDEX,
                                    CONTENT_INDEX,
                                    IMAGE_LINK_INDEX,
                                    LINK_INDEX,
                                    PRESS_INDEX,
                                    WRITTEN_DATETIME_INDEX,
                                    IS_HEADLINE_INDEX,
                                    iterator,
                                    categoryRepository.findByName(POLITICS)
                                )
                            )
                        }

                        1 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews,
                                    TITLE_INDEX,
                                    CONTENT_INDEX,
                                    IMAGE_LINK_INDEX,
                                    LINK_INDEX,
                                    PRESS_INDEX,
                                    WRITTEN_DATETIME_INDEX,
                                    IS_HEADLINE_INDEX,
                                    iterator,
                                    categoryRepository.findByName(ECONOMIC)
                                )
                            )
                        }

                        2 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews,
                                    TITLE_INDEX,
                                    CONTENT_INDEX,
                                    IMAGE_LINK_INDEX,
                                    LINK_INDEX,
                                    PRESS_INDEX,
                                    WRITTEN_DATETIME_INDEX,
                                    IS_HEADLINE_INDEX,
                                    iterator,
                                    categoryRepository.findByName(SOCIETY)
                                )
                            )
                        }

                        3 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews,
                                    TITLE_INDEX,
                                    CONTENT_INDEX,
                                    IMAGE_LINK_INDEX,
                                    LINK_INDEX,
                                    PRESS_INDEX,
                                    WRITTEN_DATETIME_INDEX,
                                    IS_HEADLINE_INDEX,
                                    iterator,
                                    categoryRepository.findByName(LIFE_CULTURE)
                                )
                            )
                        }

                        4 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews,
                                    TITLE_INDEX,
                                    CONTENT_INDEX,
                                    IMAGE_LINK_INDEX,
                                    LINK_INDEX,
                                    PRESS_INDEX,
                                    WRITTEN_DATETIME_INDEX,
                                    IS_HEADLINE_INDEX,
                                    iterator,
                                    categoryRepository.findByName(WORLD)
                                )
                            )
                        }

                        5 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews,
                                    TITLE_INDEX,
                                    CONTENT_INDEX,
                                    IMAGE_LINK_INDEX,
                                    LINK_INDEX,
                                    PRESS_INDEX,
                                    WRITTEN_DATETIME_INDEX,
                                    IS_HEADLINE_INDEX,
                                    iterator,
                                    categoryRepository.findByName(IT_SCIENCE)
                                )
                            )
                        }
                    }
                }

                val uniqueTitles = linkedSetOf<String>()
                val filteredNews = mutableListOf<News>()

                for (news in newsBundle) {
                    if (uniqueTitles.add(news.title)) {
                        filteredNews.add(news)
                    }
                }

                for (news in filteredNews) newsRepository.save(news)

                println("newsRepository.findAll().size = ${newsRepository.findAll().size}")

                when (categoryIndex) {
                    0 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName(POLITICS),
                                newsBundle.map { it.id }.toString()
                                    .replace("[", "")
                                    .replace("]", ""),
                                ""
                            )
                        )
                    }

                    1 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName(ECONOMIC),
                                newsBundle.map { it.id }.toString()
                                    .replace("[", "")
                                    .replace("]", ""),
                                ""
                            )
                        )
                    }

                    2 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName(SOCIETY),
                                newsBundle.map { it.id }.toString()
                                    .replace("[", "")
                                    .replace("]", ""),
                                ""
                            )
                        )
                    }

                    3 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName(LIFE_CULTURE),
                                newsBundle.map { it.id }.toString()
                                    .replace("[", "")
                                    .replace("]", ""),
                                ""
                            )
                        )
                    }

                    4 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName(WORLD),
                                newsBundle.map { it.id }.toString()
                                    .replace("[", "")
                                    .replace("]", ""),
                                ""
                            )
                        )
                    }

                    5 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName(IT_SCIENCE),
                                newsBundle.map { it.id }.toString()
                                    .replace("[", "")
                                    .replace("]", ""),
                                ""
                            )
                        )
                    }
                }
            }
            for (newsCard in newsCards) {
                val newsId = newsCard.multipleNews?.get(0)?.code?.toLong()
                val headLineNewsContent = newsId?.let { newsRepository.findById(it).get().content }
                newsCard.insertKeyword(headLineNewsContent?.let { extractKeyword(it) })
                newsCardRepository.save(newsCard)
            }
            newsBundle = mutableListOf()
            newsCards = mutableListOf()
            log.info("Take a break for 3 seconds to prevent request overload")
            Thread.sleep(3000)
            log.info(LocalDateTime.now().toString() + " - " + "crawling done")
        }
    }


    private fun extractKeyword(content: String): String {
        val keywordCount = 5
        val analyzer = KoreanAnalyzer()
        val stopWords = setOf(
            "은", "는", "이", "가", "을", "를", "과", "와", "에서", "으로", "에게", "으로부터", "에", "의"
        )
        val wordFrequencies = mutableMapOf<String, Int>()
        val reader = StringReader(content)
        val tokenStream = analyzer.tokenStream("text", reader)
        val charTermAttribute: CharTermAttribute = tokenStream.addAttribute(CharTermAttribute::class.java)

        tokenStream.reset()
        while (tokenStream.incrementToken()) {
            val term = charTermAttribute.toString()
            if (term !in stopWords && term.length > 1) {
                wordFrequencies[term] = wordFrequencies.getOrDefault(term, 0) + 1
            }
        }
        tokenStream.end()
        tokenStream.close()

        val sortedKeywords = wordFrequencies.entries.sortedByDescending { it.value }
        val topKeywords = sortedKeywords.take(keywordCount).map { it.key }

        return topKeywords.joinToString(", ")
            .replace("[", "")
            .replace("]", "")
    }

    private fun setup(url: String, categoryIndex: Int): Elements {
        return Jsoup.connect(url).get()
            .getElementsByClass(moreHeadLineLinksElements[categoryIndex])
            .tagName("a")
    }

    private fun extractAllHeadLineNewsLinks(allHeadLineMoreLinksDoc: Elements): List<String> {
        val allDetailHeadLineNewsLinks = ArrayList<String>()

        for (element in allHeadLineMoreLinksDoc) {
            val link = element.toString()
            val start = link.indexOf("/")
            val end = link.indexOf("\" ")
            allDetailHeadLineNewsLinks.add(SYMBOLIC_LINK_BASE_URL + link.substring(start, end))
        }

        return allDetailHeadLineNewsLinks
            .filter { it != "" }
            .distinct()
    }

    private fun extractAllDetailNewsInHeadLine(
        allHeadLineNewsLinks: List<String>,
        categorySeparator: Int,
    ): List<List<Any>> {
        val result = mutableListOf<MutableList<Any>>()
        val detailHeadLineNewsTitles = mutableListOf<Any>()
        val detailHeadLineContents = mutableListOf<Any>()
        val detailHeadLineImageLinks = mutableListOf<Any>()
        val detailHeadLineNewsLinks = mutableListOf<Any>()
        val detailHeadLineNewsPresses = mutableListOf<Any>()
        val detailHeadLineNewsWrittenDateTime = mutableListOf<Any>()
        val detailHeadLineNewsIsHeadLine = mutableListOf<Any>()
        val detailHeadLineNewsRelatedCount = mutableListOf<Any>()

        for (link in allHeadLineNewsLinks) {
            var headLineFlag = true
            val moreDoc = Jsoup.connect(link).get()
            val crawledHtmlLinks = moreDoc
                .getElementsByClass(detailDocClassNames[categorySeparator])
                .toString()
                .split("</a>")

            val relatedNewsCount = moreDoc
                .getElementsByClass(RELATED_COUNT_CLASS_NAME)
                .text()
                .toInt()

            for (htmlLink in crawledHtmlLinks) {
                val detailLink = Jsoup.parse(htmlLink)
                    .select("a[href]")
                    .attr("href")

                if (!detailHeadLineNewsLinks.contains(detailLink) && detailLink.isNotEmpty()) {
                    detailHeadLineNewsLinks.add(detailLink)
                    val detailDoc = Jsoup.connect(detailLink).get()
                    val imageLink = detailDoc
                        .getElementById(IMAGE_ID_NAME) ?: ""
                    val title = detailDoc
                        .getElementsByClass(TITLE_CLASS_NAME).text()
                    val content = detailDoc
                        .getElementsByClass(CONTENT_CLASS_NAME).addClass("#text").text()
                    val press = detailDoc
                        .getElementsByClass(PRESS_CLASS_NAME).text()
                    val writtenDateTime = detailDoc
                        .getElementsByClass(WRITTEN_DATETIME_CLASS_NAME).text()

                    if (!detailHeadLineNewsTitles.contains(title)) {
                        detailHeadLineNewsTitles.add(title)
                        detailHeadLineContents.add(content)
                        detailHeadLineImageLinks.add(imageLink)
                        detailHeadLineNewsPresses.add(press)
                        detailHeadLineNewsWrittenDateTime.add(writtenDateTime)

                        if (headLineFlag) {
                            detailHeadLineNewsIsHeadLine.add(true)
                            headLineFlag = false
                        } else {
                            detailHeadLineNewsIsHeadLine.add(false)
                        }
                    }
                }
            }
            detailHeadLineNewsRelatedCount.add(relatedNewsCount)
            result.add(detailHeadLineNewsTitles)
            result.add(detailHeadLineContents)
            result.add(detailHeadLineImageLinks)
            result.add(detailHeadLineNewsLinks)
            result.add(detailHeadLineNewsPresses)
            result.add(detailHeadLineNewsWrittenDateTime)
            result.add(detailHeadLineNewsIsHeadLine)
            result.add(detailHeadLineNewsRelatedCount)
        }
        return result
    }

    companion object {
        private const val POLITICS_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100"
        private const val ECONOMY_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101"
        private const val SOCIETY_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102"
        private const val LIFE_CULTURE_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103"
        private const val WORLD_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104"
        private const val IT_SCIENCE_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105"
        private val urls = listOf(
            POLITICS_URL,
            ECONOMY_URL,
            SOCIETY_URL,
            LIFE_CULTURE_URL,
            WORLD_URL,
            IT_SCIENCE_URL
        )

        private const val SYMBOLIC_LINK_BASE_URL = "https://news.naver.com"

        private const val POLITICS_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_pol.clstitle)"
        private const val ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_eco.clstitle)"
        private const val SOCIETY_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_nav.clstitle)"
        private const val LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_lif.clstitle)"
        private const val WORLD_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_wor.clstitle)"
        private const val IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_sci.clstitle)"

        private val moreHeadLineLinksElements = listOf(
            POLITICS_MORE_HEADLINE_LINKS_ELEMENT,
            ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT,
            SOCIETY_MORE_HEADLINE_LINKS_ELEMENT,
            LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT,
            WORLD_MORE_HEADLINE_LINKS_ELEMENT,
            IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT
        )

        private const val DETAIL_POLITICS_DOC_CLASS_NAME = "nclicks(cls_pol.clsart1)"
        private const val DETAIL_ECONOMIC_DOC_CLASS_NAME = "nclicks(cls_eco.clsart1)"
        private const val DETAIL_SOCIETY_DOC_CLASS_NAME = "nclicks(cls_nav.clsart1)"
        private const val DETAIL_LIFE_CULTURE_DOC_CLASS_NAME = "nclicks(cls_lif.clsart1)"
        private const val DETAIL_WORLD_DOC_CLASS_NAME = "nclicks(cls_wor.clsart1)"
        private const val DETAIL_IT_SCIENCE_DOC_CLASS_NAME = "nclicks(cls_sci.clsart1)"

        private val detailDocClassNames = listOf(
            DETAIL_POLITICS_DOC_CLASS_NAME,
            DETAIL_ECONOMIC_DOC_CLASS_NAME,
            DETAIL_SOCIETY_DOC_CLASS_NAME,
            DETAIL_LIFE_CULTURE_DOC_CLASS_NAME,
            DETAIL_WORLD_DOC_CLASS_NAME,
            DETAIL_IT_SCIENCE_DOC_CLASS_NAME
        )

        private const val TITLE_CLASS_NAME = "media_end_head_headline"
        private const val CONTENT_CLASS_NAME = "go_trans _article_content"
        private const val IMAGE_ID_NAME = "img1"
        private const val PRESS_CLASS_NAME = "media_end_linked_more_point"
        private const val WRITTEN_DATETIME_CLASS_NAME = "media_end_head_info_datestamp_time _ARTICLE_DATE_TIME"
        private const val RELATED_COUNT_CLASS_NAME = "cluster_banner_count_icon_num"

        private const val TITLE_INDEX = 0
        private const val CONTENT_INDEX = 1
        private const val IMAGE_LINK_INDEX = 2
        private const val LINK_INDEX = 3
        private const val PRESS_INDEX = 4
        private const val WRITTEN_DATETIME_INDEX = 5
        private const val IS_HEADLINE_INDEX = 6
        private const val RELATED_COUNT_INDEX = 7

        private const val POLITICS = "정치"
        private const val ECONOMIC = "경제"
        private const val SOCIETY = "사회"
        private const val LIFE_CULTURE = "생활/문화"
        private const val WORLD = "세계"
        private const val IT_SCIENCE = "IT/과학"
    }
}
