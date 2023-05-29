package com.mashup.shorts.core

import java.time.LocalDateTime
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository


@Component
class CrawlerCore(
    private val newsRepository: NewsRepository,
) {

    @Transactional
    fun executeCrawling() {
        val politicsNews = mutableListOf<NewsInformation>()
        val economyNews = mutableListOf<NewsInformation>()
        val societyNews = mutableListOf<NewsInformation>()
        val lifeCultureNews = mutableListOf<NewsInformation>()
        val worldNews = mutableListOf<NewsInformation>()
        val iTScienceNews = mutableListOf<NewsInformation>()

        val allNews = mutableListOf<News>()

        for (i: Int in urls.indices) {
            log.info(LocalDateTime.now().toString() + urls[i] + " - crawling start")
            val doc = setup(urls[i], i)
            val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
            val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, i)
            val numberOfNews = extractedAllNews[0].size

            for (index: Int in 0 until numberOfNews) {
                when (i) {
                    0 -> politicsNews.add(NewsInformation(extractedAllNews, index))
                    1 -> economyNews.add(NewsInformation(extractedAllNews, index))
                    2 -> societyNews.add(NewsInformation(extractedAllNews, index))
                    3 -> lifeCultureNews.add(NewsInformation(extractedAllNews, index))
                    4 -> worldNews.add(NewsInformation(extractedAllNews, index))
                    5 -> iTScienceNews.add(NewsInformation(extractedAllNews, index))
                }
            }
            log.info("Take a break for 10 seconds to prevent request load")
            Thread.sleep(10000)
        }

        politicsNews.asSequence().forEach {
            allNews.add(
                News(
                    it.title, it.content, it.thumbnail, it.link,
                    it.press, it.writtenDateTime, it.isHeadLine.name,
                    Category("정치")
                )
            )
        }

        economyNews.asSequence().forEach {
            allNews.add(
                News(
                    it.title, it.content, it.thumbnail, it.link,
                    it.press, it.writtenDateTime, it.isHeadLine.name,
                    Category("경제")
                )
            )
        }

        societyNews.asSequence().forEach {
            allNews.add(
                News(
                    it.title, it.content, it.thumbnail, it.link,
                    it.press, it.writtenDateTime, it.isHeadLine.name,
                    Category("사회")
                )
            )
        }

        lifeCultureNews.asSequence().forEach {
            allNews.add(
                News(
                    it.title, it.content, it.thumbnail, it.link,
                    it.press, it.writtenDateTime, it.isHeadLine.name,
                    Category("생활/문화")
                )
            )
        }

        worldNews.asSequence().forEach {
            allNews.add(
                News(
                    it.title, it.content, it.thumbnail, it.link,
                    it.press, it.writtenDateTime, it.isHeadLine.name,
                    Category("세계")
                )
            )
        }

        lifeCultureNews.asSequence().forEach {
            allNews.add(
                News(
                    it.title, it.content, it.thumbnail, it.link,
                    it.press, it.writtenDateTime, it.isHeadLine.name,
                    Category("IT/과학")
                )
            )
        }

        for (allNew in allNews) {
            newsRepository.save(allNew)
        }
    }

    /**
    https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100
    https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101
    https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102
    https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103
    https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104
    https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105
    setup()메서드는 위 링크들에서
    "N개의 기사 더보기"를 누르면 이동할 링크들을 뽑아온다.
    각 헤드라인
     */
    private fun setup(url: String, i: Int): Elements {
        return Jsoup.connect(url).get()
            .getElementsByClass(moreHeadLineLinksElements[i])
            .tagName("a")
    }

    /**
    https://news.naver.com/main/clusterArticles.naver?id=c_202305150820_00000001&mode=LSD&mid=shm&sid1=100&oid=421&aid=0006805653
    setup()메서드에서 뽑은 "N개의 기사 더보기"가 가진 링크를 하나씩 탐색하는 동작을 수행한다.
    즉, 각각의 헤드라인에 대한 N개의 기사를 볼 수 있는 링크를 뽑아낸다.
     */
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

    // 특정 헤드라인에 해당하는 모든 뉴스를 순회하며
    // 제목, 내용, 썸네일, 링크, 언론사, 작성날짜를 순서대로 담은 리스트를 반환한다.
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

        for (link in allHeadLineNewsLinks) {
            var headLineFlag = true
            val moreDoc = Jsoup.connect(link).get()
            val crawledHtmlLinks = moreDoc.getElementsByClass(detailDocClassNames[categorySeparator])
                .toString()
                .split("</a>")

            var numberOfNews = 0
            for (htmlLink in crawledHtmlLinks) {
                val detailLink = Jsoup.parse(htmlLink)
                    .select("a[href]")
                    .attr("href")

                if (!detailHeadLineNewsLinks.contains(detailLink) && detailLink.isNotEmpty()) {
                    detailHeadLineNewsLinks.add(detailLink)
                    val detailDoc = Jsoup.connect(detailLink).get()
                    val image = detailDoc.getElementById(imageIdName) ?: ""
                    val title = detailDoc.getElementsByClass(titleClassName).text()
                    val content = detailDoc.getElementsByClass(contentClassName).text()
                    val press = detailDoc.getElementsByClass(pressClassName).text()
                    val writtenDateTime = detailDoc.getElementsByClass(writtenDateTimeClassName).text()
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
                result.add(detailHeadLineNewsTitles)
                result.add(detailHeadLineContents)
                result.add(detailHeadLineThumbnails)
                result.add(detailHeadLineNewsLinks)
                result.add(detailHeadLineNewsPresses)
                result.add(detailHeadLineNewsWrittenDateTime)
                result.add(detailHeadLineNewsIsHeadLine)
            }
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

    }
}
