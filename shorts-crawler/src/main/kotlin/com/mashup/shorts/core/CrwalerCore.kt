package com.mashup.shorts.core

import java.io.StringReader
import java.time.LocalDateTime
import org.apache.lucene.analysis.ko.KoreanAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.category.CategoryRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional
class CrawlerCore(
    private val newsRepository: NewsRepository,
    private val newsCardRepository: NewsCardRepository,
    private val categoryRepository: CategoryRepository,
) {

    @Scheduled(cron = "0 * * * * *")
    fun executeCrawling() {
        for (categoryIndex: Int in urls.indices) {

            val category = when (categoryIndex) {
                0 -> categoryRepository.findByName(CategoryName.POLITICS)
                1 -> categoryRepository.findByName(CategoryName.ECONOMIC)
                2 -> categoryRepository.findByName(CategoryName.SOCIETY)
                3 -> categoryRepository.findByName(CategoryName.CULTURE)
                4 -> categoryRepository.findByName(CategoryName.WORLD)
                5 -> categoryRepository.findByName(CategoryName.SCIENCE)
                else -> null
            }

            log.info(LocalDateTime.now().toString() + " - " + urls[categoryIndex] + " - crawling start")

            val doc = setup(urls[categoryIndex], categoryIndex)
            val allNewsLinks = extractAllHeadLineNewsLinks(doc)
            val persistenceNewsBundle = newsRepository.findAllByCategory(
                categoryRepository.findByName(category!!.name)
            )

            val newsCardBundle = extractNewsCardBundle(
                allNewsLinks,
                categoryIndex,
                category,
            )

            val createdNewsCardIds = mutableListOf<Long>()

            for (newsCard in newsCardBundle) {
                val persistenceTargetNewsList = mutableListOf<News>()
                for (news in newsCard) {
                    if (news.title !in persistenceNewsBundle.map { it.title }) {
                        persistenceTargetNewsList.add(news)
                        newsRepository.save(news)
                    } else {
                        val findByTitle = newsRepository.findByTitle(news.title)
                        findByTitle.increaseCrawledCount()
                        newsRepository.save(findByTitle)
                        persistenceTargetNewsList.add(findByTitle)
                    }
                }

                val persistenceNewsCard = NewsCard(
                    category,
                    filterSquareBracket(persistenceTargetNewsList.map { it.id }.toString()),
                    ""
                )

                createdNewsCardIds.add(
                    newsCardRepository.save(persistenceNewsCard).id
                )
            }

            for (createdNewsCardId in createdNewsCardIds) {
                val newsCard = newsCardRepository.findById(createdNewsCardId).get()
                val newsIdInNewsCard = newsCard.multipleNews?.split(", ")?.first()?.toLong()
                val headLineNewsContent = newsIdInNewsCard?.let { newsRepository.findById(it).get().content }
                newsCard.insertKeyword(headLineNewsContent?.let { extractKeyword(it) })
                newsCardRepository.save(newsCard)
            }

            log.info("Take a break for 1 seconds to prevent request overload")
            Thread.sleep(1000)
        }

        log.info(LocalDateTime.now().toString() + " - " + "crawling done")

    }

    private fun extractKeyword(content: String): String {
        val keywordCount = 4
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

    private fun extractNewsCardBundle(
        allHeadLineNewsLinks: List<String>,
        categorySeparator: Int,
        category: Category,
    ): List<MutableList<News>> {
        val cardNewsBundle = mutableListOf<MutableList<News>>()
        var cardNews = mutableListOf<News>()

        for (link in allHeadLineNewsLinks) {
            var headLineFlag = true
            val moreDoc = Jsoup.connect(link).get()
            val crawledHtmlLinks = moreDoc
                .getElementsByClass(detailDocClassNames[categorySeparator])
                .toString()
                .split("</a>")

            val crawledTitles = mutableListOf<String>()

            loopInHeadLine@
            for (htmlLink in crawledHtmlLinks) {
                val detailLink = Jsoup.parse(htmlLink)
                    .select("a[href]")
                    .attr("href")

                if (detailLink.isNotEmpty()) {
                    Thread.sleep(100)
                    val detailDoc = Jsoup.connect(detailLink).get()
                    val title = detailDoc.getElementsByClass(TITLE_CLASS_NAME).text()

                    if (crawledTitles.contains(title)) {
                        continue@loopInHeadLine
                    }

                    crawledTitles.add(title)

                    val content = detailDoc
                        .getElementsByClass(CONTENT_CLASS_NAME).addClass("#text")
                        .text()
                    val imageLink = detailDoc.getElementById(IMAGE_ID_NAME).toString()
                    val press = detailDoc.getElementsByClass(PRESS_CLASS_NAME).text()
                    val writtenDateTime = detailDoc.getElementsByClass(WRITTEN_DATETIME_CLASS_NAME).text()

                    cardNews.add(
                        News(
                            title, content,
                            filterImageLinkForm(imageLink),
                            detailLink, press, writtenDateTime,
                            convertHeadLine(headLineFlag),
                            1,
                            category,
                        )
                    )
                    headLineFlag = false
                }
            }
            cardNewsBundle.add(cardNews)
            cardNews = mutableListOf()
        }
        return cardNewsBundle
    }

    private fun filterSquareBracket(target: String): String {
        return target
            .replace("[", "")
            .replace("]", "")
    }

    private fun filterImageLinkForm(rawImageLink: String): String {
        return rawImageLink
            .substringAfter("data-src=\"")
            .substringBefore("\"")
    }

    private fun convertHeadLine(headLineFlag: Boolean): String {
        return if (headLineFlag) {
            HEADLINE
        } else {
            NORMAL
        }
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

        private const val HEADLINE = "HEADLINE"
        private const val NORMAL = "NORMAL"
    }
}
