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
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.core.NewsDOMClassNameConst.CONTENT_CLASS_NAME
import com.mashup.shorts.core.NewsDOMClassNameConst.HEADLINE
import com.mashup.shorts.core.NewsDOMClassNameConst.IMAGE_ID_NAME
import com.mashup.shorts.core.NewsDOMClassNameConst.NORMAL
import com.mashup.shorts.core.NewsDOMClassNameConst.PRESS_CLASS_NAME
import com.mashup.shorts.core.NewsDOMClassNameConst.TITLE_CLASS_NAME
import com.mashup.shorts.core.NewsDOMClassNameConst.WRITTEN_DATETIME_CLASS_NAME
import com.mashup.shorts.core.NewsDOMClassNameConst.detailDocClassNames
import com.mashup.shorts.core.NewsLinkElementConst.moreHeadLineLinksElements
import com.mashup.shorts.core.NewsUrlConst.SYMBOLIC_LINK_BASE_URL
import com.mashup.shorts.core.NewsUrlConst.categoryToUrl
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.category.CategoryName.CULTURE
import com.mashup.shorts.domain.category.CategoryName.ECONOMIC
import com.mashup.shorts.domain.category.CategoryName.POLITICS
import com.mashup.shorts.domain.category.CategoryName.SCIENCE
import com.mashup.shorts.domain.category.CategoryName.SOCIETY
import com.mashup.shorts.domain.category.CategoryName.WORLD
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
        for (categoryPair in categoryToUrl) {
            val category = when (categoryPair.key) {
                POLITICS -> categoryRepository.findByName(POLITICS)
                ECONOMIC -> categoryRepository.findByName(ECONOMIC)
                SOCIETY -> categoryRepository.findByName(SOCIETY)
                CULTURE -> categoryRepository.findByName(CULTURE)
                WORLD -> categoryRepository.findByName(WORLD)
                SCIENCE -> categoryRepository.findByName(SCIENCE)
                else -> throw ShortsBaseException.from(
                    shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                    resultErrorMessage = "${LocalDateTime.now()} - 크롤링 시도 중 ${categoryPair.key} 를 찾을 수 없습니다."
                )
            }

            val headLineLinks = getMoreHeadLineLinks(
                url = categoryPair.value,
                categoryName = categoryPair.key
            )
            val allNewsLinks = extractAllHeadLineNewsLinks(headLineLinks)
            val persistenceNewsBundle = newsRepository.findAllByCategory(category)
            val newsCardBundle = extractNewsCardBundle(
                allNewsLinks,
                categoryPair.key,
                category,
            )

            for (newsCard in newsCardBundle) {
                val persistenceTargetNewsList = mutableListOf<News>()
                for (news in newsCard) {
                    if (news.title !in persistenceNewsBundle.map { it.title }) {
                        persistenceTargetNewsList.add(news)
                        newsRepository.save(news)
                    } else {
                        val alreadyExistNews = newsRepository.findByTitle(news.title) ?: throw ShortsBaseException.from(
                            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                            resultErrorMessage = "뉴스를 저장하는 중 ${news.title} 에 해당하는 뉴스를 찾을 수 없습니다."
                        )
                        alreadyExistNews.increaseCrawledCount()
                        newsRepository.save(alreadyExistNews)
                        persistenceTargetNewsList.add(alreadyExistNews)
                    }
                }

                // 뉴스 내용 가져올 인덱스를 0으로 고정할지 랜덤값을 넣을지 고려해봐야함.
                val extractKeyword = extractKeyword(persistenceTargetNewsList[0].content)
                val persistenceNewsCard = NewsCard(
                    category = category,
                    multipleNews = filterSquareBracket(persistenceTargetNewsList.map { it.id }.toString()),
                    keywords = extractKeyword
                )
                newsCardRepository.save(persistenceNewsCard)
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

    private fun getMoreHeadLineLinks(url: String, categoryName: CategoryName): Elements {
        return Jsoup.connect(url).get()
            .getElementsByClass(moreHeadLineLinksElements[categoryName]!!)
            .tagName("a")
    }

    private fun extractAllHeadLineNewsLinks(allHeadLineMoreLinksDocs: Elements): List<String> {
        val allDetailHeadLineNewsLinks = ArrayList<String>()

        for (element in allHeadLineMoreLinksDocs) {
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
        categoryName: CategoryName,
        category: Category,
    ): List<MutableList<News>> {
        val cardNewsBundle = mutableListOf<MutableList<News>>()
        var cardNews = mutableListOf<News>()

        for (link in allHeadLineNewsLinks) {
            var headLineFlag = true
            val moreDoc = Jsoup.connect(link).get()

            val crawledHtmlLinks = moreDoc
                .getElementsByClass(detailDocClassNames[categoryName]!!)
                .toString()
                .split("</a>")

            val crawledTitles = mutableListOf<String>()

            loopInHeadLine@
            for (htmlLink in crawledHtmlLinks) {
                val detailLink = Jsoup.parse(htmlLink)
                    .select("a[href]")
                    .attr("href")

                if (detailLink.isEmpty()) {
                    continue
                }

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
                        title = title,
                        content = content,
                        thumbnailImageUrl = filterImageLinkForm(imageLink),
                        newsLink = detailLink,
                        press = press,
                        writtenDateTime = writtenDateTime,
                        type = convertHeadLine(headLineFlag),
                        crawledCount = 1,
                        category = category,
                    )
                )
                headLineFlag = false
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
}
