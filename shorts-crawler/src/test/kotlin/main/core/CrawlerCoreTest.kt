package main.core

import java.io.StringReader
import java.time.LocalDateTime
import org.apache.lucene.analysis.ko.KoreanAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.mashup.shorts.ShortsCrawlerApplication
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.core.NewsDOMClassNameConst
import com.mashup.shorts.core.NewsLinkElementConst
import com.mashup.shorts.core.NewsUrlConst
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.category.CategoryRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository

@SpringBootTest(classes = [ShortsCrawlerApplication::class])
class CrawlerCoreTest @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val newsRepository: NewsRepository,
    private val newsCardRepository: NewsCardRepository,
) {

    @Test
    @DisplayName("모든 카테고리 한 번에 크롤링 해오기")
    fun executeCrawling() {

        for (categoryPair in NewsUrlConst.categoryToUrl) {
            val category = when (categoryPair.key) {
                CategoryName.POLITICS -> categoryRepository.findByName(CategoryName.POLITICS)
                CategoryName.ECONOMIC -> categoryRepository.findByName(CategoryName.ECONOMIC)
                CategoryName.SOCIETY -> categoryRepository.findByName(CategoryName.SOCIETY)
                CategoryName.CULTURE -> categoryRepository.findByName(CategoryName.CULTURE)
                CategoryName.WORLD -> categoryRepository.findByName(CategoryName.WORLD)
                CategoryName.SCIENCE -> categoryRepository.findByName(CategoryName.SCIENCE)
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
                        val alreadyNews = newsRepository.findByTitle(news.title) ?: throw ShortsBaseException.from(
                            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                            resultErrorMessage = "뉴스를 저장하는 중 ${news.title} 에 해당하는 뉴스를 찾을 수 없습니다."
                        )
                        alreadyNews.increaseCrawledCount()
                        newsRepository.save(alreadyNews)
                        persistenceTargetNewsList.add(alreadyNews)
                    }
                }

                // 뉴스 내용 가져올 인덱스를 0으로 고정할지 랜덤값을 넣을지 고려해봐야함.
                val extractKeyword = extractKeyword(persistenceTargetNewsList[0].content)

                val persistenceNewsCard = NewsCard(
                    category,
                    filterSquareBracket(persistenceTargetNewsList.map { it.id }.toString()),
                    extractKeyword
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
            .getElementsByClass(NewsLinkElementConst.moreHeadLineLinksElements[categoryName]!!)
            .tagName("a")
    }

    private fun extractAllHeadLineNewsLinks(allHeadLineMoreLinksDocs: Elements): List<String> {
        val allDetailHeadLineNewsLinks = ArrayList<String>()

        for (element in allHeadLineMoreLinksDocs) {
            val link = element.toString()
            val start = link.indexOf("/")
            val end = link.indexOf("\" ")
            allDetailHeadLineNewsLinks.add(NewsUrlConst.SYMBOLIC_LINK_BASE_URL + link.substring(start, end))
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
                .getElementsByClass(NewsDOMClassNameConst.detailDocClassNames[categoryName]!!)
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
                val title = detailDoc.getElementsByClass(NewsDOMClassNameConst.TITLE_CLASS_NAME).text()

                if (crawledTitles.contains(title)) {
                    continue@loopInHeadLine
                }

                crawledTitles.add(title)

                val content = detailDoc
                    .getElementsByClass(NewsDOMClassNameConst.CONTENT_CLASS_NAME).addClass("#text")
                    .text()
                val imageLink = detailDoc.getElementById(NewsDOMClassNameConst.IMAGE_ID_NAME).toString()
                val press = detailDoc.getElementsByClass(NewsDOMClassNameConst.PRESS_CLASS_NAME).text()
                val writtenDateTime =
                    detailDoc.getElementsByClass(NewsDOMClassNameConst.WRITTEN_DATETIME_CLASS_NAME).text()

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
            NewsDOMClassNameConst.HEADLINE
        } else {
            NewsDOMClassNameConst.NORMAL
        }
    }
}
