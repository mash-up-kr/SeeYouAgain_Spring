package com.mashup.shorts.core

import java.time.LocalDateTime
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

    @Transactional(noRollbackFor = [Exception::class])
    fun executeCrawling() {
        var newsBundle: MutableList<News>
        var newsCards = mutableListOf<NewsCard>()

        for (i: Int in urls.indices) {
            log.info(LocalDateTime.now().toString() + " - " + urls[i] + " - crawling start")
            val doc = setup(urls[i], i)
            val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
            val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, i)

            for (index: Int in 0 until extractedAllNews[7].size) {
                newsBundle = mutableListOf()
                val relatedNewsCount = extractedAllNews[7][index].toString().toInt()
                for (columnIndex in 0 until relatedNewsCount) {
                    when (i) {
                        0 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews[titleIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[contentIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[thumbnailIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[linkIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[pressIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[writtenDateIndex][columnIndex] as? String ?: "",
                                    if (extractedAllNews[isHeadLineIndex][columnIndex] as? Boolean == true) "HEADLINE" else "NORMAL",
                                    categoryRepository.findByName("정치")
                                )
                            )
                        }

                        1 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews[titleIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[contentIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[thumbnailIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[linkIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[pressIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[writtenDateIndex][columnIndex] as? String ?: "",
                                    if (extractedAllNews[isHeadLineIndex][columnIndex] as? Boolean == true) "HEADLINE" else "NORMAL",
                                    categoryRepository.findByName("경제")
                                )
                            )
                        }

                        2 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews[titleIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[contentIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[thumbnailIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[linkIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[pressIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[writtenDateIndex][columnIndex] as? String ?: "",
                                    if (extractedAllNews[isHeadLineIndex][columnIndex] as? Boolean == true) "HEADLINE" else "NORMAL",
                                    categoryRepository.findByName("사회")
                                )
                            )
                        }

                        3 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews[titleIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[contentIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[thumbnailIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[linkIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[pressIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[writtenDateIndex][columnIndex] as? String ?: "",
                                    if (extractedAllNews[isHeadLineIndex][columnIndex] as? Boolean == true) "HEADLINE" else "NORMAL",
                                    categoryRepository.findByName("생활/문화")
                                )
                            )
                        }

                        4 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews[titleIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[contentIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[thumbnailIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[linkIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[pressIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[writtenDateIndex][columnIndex] as? String ?: "",
                                    if (extractedAllNews[isHeadLineIndex][columnIndex] as? Boolean == true) "HEADLINE" else "NORMAL",
                                    categoryRepository.findByName("세계")
                                )
                            )
                        }

                        5 -> {
                            newsBundle.add(
                                News(
                                    extractedAllNews[titleIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[contentIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[thumbnailIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[linkIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[pressIndex][columnIndex] as? String ?: "",
                                    extractedAllNews[writtenDateIndex][columnIndex] as? String ?: "",
                                    if (extractedAllNews[isHeadLineIndex][columnIndex] as? Boolean == true) "HEADLINE" else "NORMAL",
                                    categoryRepository.findByName("IT/과학")
                                )
                            )
                        }
                    }
                }
                for (news in newsBundle) {
                    newsRepository.save(news)
                }
                when (i) {
                    0 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName("정치"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    1 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName("경제"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    2 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName("사회"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    3 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName("생활/문화"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    4 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName("세계"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    5 -> {
                        newsCards.add(
                            NewsCard(
                                categoryRepository.findByName("IT/과학"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }
                }
            }

            for (newsCard in newsCards) {
                newsCardRepository.save(newsCard)
            }

            log.info("Take a break for 3 seconds to prevent request load")
            Thread.sleep(3000)

            newsCards = mutableListOf()
        }
        log.info(LocalDateTime.now().toString() + " - " + "crawling done")
    }

    private fun setup(url: String, i: Int): Elements {
        return Jsoup.connect(url).get()
            .getElementsByClass(moreHeadLineLinksElements[i])
            .tagName("a")
    }

    private fun extractAllHeadLineNewsLinks(allHeadLineMoreLinksDoc: Elements): List<String> {
        val allDetailHeadLineNewsLinks = ArrayList<String>()

        // HTML 태그 내에서 헤드라인 링크만 가져올 수 있도록 가공
        for (element in allHeadLineMoreLinksDoc) {
            val link = element.toString()
            val start = link.indexOf("/")
            val end = link.indexOf("\" ")
            allDetailHeadLineNewsLinks.add(symbolicLinkBaseUrl + link.substring(start, end))
        }

        return allDetailHeadLineNewsLinks
            .filter { !it.equals("") }
            .distinct()
    }

    private fun extractAllDetailNewsInHeadLine(
        allHeadLineNewsLinks: List<String>,
        categorySeparator: Int,
    ): List<List<Any>> {
        val result = mutableListOf<MutableList<Any>>()
        val detailHeadLineNewsTitles = mutableListOf<Any>()
        val detailHeadLineContents = mutableListOf<Any>()
        val detailHeadLineThumbnails = mutableListOf<Any>()
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
                .getElementsByClass(relatedCountClassName)
                .text()
                .toInt()

            var numberOfNews = 0
            for (htmlLink in crawledHtmlLinks) {
                val detailLink = Jsoup.parse(htmlLink)
                    .select("a[href]")
                    .attr("href")

                if (!detailHeadLineNewsLinks.contains(detailLink) && detailLink.isNotEmpty()) {
                    detailHeadLineNewsLinks.add(detailLink)
                    val detailDoc = Jsoup.connect(detailLink).get()
                    val image = detailDoc
                        .getElementById(imageIdName) ?: ""
                    val title = detailDoc
                        .getElementsByClass(titleClassName).text()
                    val content = detailDoc
                        .getElementsByClass(contentClassName).addClass("#text").text()
                    val press = detailDoc
                        .getElementsByClass(pressClassName).text()
                    val writtenDateTime = detailDoc
                        .getElementsByClass(writtenDateTimeClassName).text()
                    detailHeadLineNewsTitles.add(title)
                    detailHeadLineContents.add(content)
                    detailHeadLineThumbnails.add(image.toString())
                    detailHeadLineNewsPresses.add(press)
                    detailHeadLineNewsWrittenDateTime.add(writtenDateTime)
                    numberOfNews++

                    if (headLineFlag) {
                        detailHeadLineNewsIsHeadLine.add(true)
                        headLineFlag = false
                    } else {
                        detailHeadLineNewsIsHeadLine.add(false)
                    }
                }
            }
            detailHeadLineNewsRelatedCount.add(relatedNewsCount)
            result.add(detailHeadLineNewsTitles)
            result.add(detailHeadLineContents)
            result.add(detailHeadLineThumbnails)
            result.add(detailHeadLineNewsLinks)
            result.add(detailHeadLineNewsPresses)
            result.add(detailHeadLineNewsWrittenDateTime)
            result.add(detailHeadLineNewsIsHeadLine)
            result.add(detailHeadLineNewsRelatedCount)
        }
        return result
    }

    companion object {
        private const val politicsUrl = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100"
        private const val economyUrl = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101"
        private const val societyUrl = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102"
        private const val lifeCultureUrl = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103"
        private const val worldUrl = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104"
        private const val iTScienceUrl = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105"
        private val urls = listOf(politicsUrl, economyUrl, societyUrl, lifeCultureUrl, worldUrl, iTScienceUrl)

        private const val symbolicLinkBaseUrl = "https://news.naver.com"

        private const val politicsMoreHeadLineLinksElement = "sh_head_more nclicks(cls_pol.clstitle)"
        private const val economicMoreHeadLineLinksElement = "sh_head_more nclicks(cls_eco.clstitle)"
        private const val societyMoreHeadLineLinksElement = "sh_head_more nclicks(cls_nav.clstitle)"
        private const val lifeCultureMoreHeadLineLinksElement = "sh_head_more nclicks(cls_lif.clstitle)"
        private const val worldMoreHeadLineLinksElement = "sh_head_more nclicks(cls_wor.clstitle)"
        private const val itScienceMoreHeadLineLinksElement = "sh_head_more nclicks(cls_sci.clstitle)"

        private val moreHeadLineLinksElements = listOf(
            politicsMoreHeadLineLinksElement,
            economicMoreHeadLineLinksElement,
            societyMoreHeadLineLinksElement,
            lifeCultureMoreHeadLineLinksElement,
            worldMoreHeadLineLinksElement,
            itScienceMoreHeadLineLinksElement
        )

        private const val detailPoliticsDocClassName = "nclicks(cls_pol.clsart1)"
        private const val detailEconomicDocClassName = "nclicks(cls_eco.clsart1)"
        private const val detailSocietyDocClassName = "nclicks(cls_nav.clsart1)"
        private const val detailLifeCultureDocClassName = "nclicks(cls_lif.clsart1)"
        private const val detailWorldDocClassName = "nclicks(cls_wor.clsart1)"
        private const val detailItScienceDocClassName = "nclicks(cls_sci.clsart1)"

        private val detailDocClassNames = listOf(
            detailPoliticsDocClassName,
            detailEconomicDocClassName,
            detailSocietyDocClassName,
            detailLifeCultureDocClassName,
            detailWorldDocClassName,
            detailItScienceDocClassName
        )

        private const val titleClassName = "media_end_head_headline"
        private const val contentClassName = "go_trans _article_content"
        private const val imageIdName = "img1"
        private const val pressClassName = "media_end_linked_more_point"
        private const val writtenDateTimeClassName = "media_end_head_info_datestamp_time _ARTICLE_DATE_TIME"
        private const val relatedCountClassName = "cluster_banner_count_icon_num"

        private const val titleIndex = 0
        private const val contentIndex = 1
        private const val thumbnailIndex = 2
        private const val linkIndex = 3
        private const val pressIndex = 4
        private const val writtenDateIndex = 5
        private const val isHeadLineIndex = 6
    }
}
