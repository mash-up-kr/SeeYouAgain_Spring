package main

import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository

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
private const val imageClassName = "_LAZY_LOADING"
private const val pressClassName = "media_end_linked_more_point"
private const val writtenDateTimeClassName = "media_end_head_info_datestamp_time _ARTICLE_DATE_TIME"

private const val titleIndex = 0
private const val contentIndex = 1
private const val thumbnailIndex = 2
private const val linkIndex = 3
private const val pressIndex = 4
private const val writtenDateIndex = 5
private const val isHeadLineIndex = 6

@DataJpaTest
@ActiveProfiles("test")
internal class CrawlerCoreTest {

    // @Autowired
    // lateinit var newsRepository: NewsRepository

    @Test
    @DisplayName("카테고리 : 정치")
    fun test0() {
        val poli = ArrayList<NewsInformation>()
        val doc = setup(urls[0], 0)
        val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
        val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, 0)
        val numberOfNews = extractedAllNews[0].size

        for (index: Int in 0 until numberOfNews) {
            poli.add(
                NewsInformation(
                    extractedAllNews[titleIndex][index] as? String ?: "",
                    extractedAllNews[contentIndex][index] as? String ?: "",
                    extractedAllNews[thumbnailIndex][index] as? String ?: "",
                    extractedAllNews[linkIndex][index] as? String ?: "",
                    extractedAllNews[pressIndex][index] as? String ?: "",
                    extractedAllNews[writtenDateIndex][index] as? String ?: "",
                    if (extractedAllNews[6][index] as? Boolean == true) {
                        HeadLine.HEAD_LINE
                    } else {
                        HeadLine.NORMAL
                    }
                )
            )
        }
        println("poliNews = ${poli.size}")
    }

    @Test
    @DisplayName("카테고리 : 경제")
    fun test1() {
        val eco = ArrayList<NewsInformation>()
        val doc = setup(urls[1], 1)
        val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
        val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, 1)
        val numberOfNews = extractedAllNews[0].size

        for (index: Int in 0 until numberOfNews) {
            eco.add(
                NewsInformation(
                    extractedAllNews[titleIndex][index] as? String ?: "",
                    extractedAllNews[contentIndex][index] as? String ?: "",
                    extractedAllNews[thumbnailIndex][index] as? String ?: "",
                    extractedAllNews[linkIndex][index] as? String ?: "",
                    extractedAllNews[pressIndex][index] as? String ?: "",
                    extractedAllNews[writtenDateIndex][index] as? String ?: "",
                    if (extractedAllNews[6][index] as? Boolean == true) {
                        HeadLine.HEAD_LINE
                    } else {
                        HeadLine.NORMAL
                    }
                )
            )
        }
        println("ecoNews = ${eco.size}")
    }

    @Test
    @DisplayName("카테고리 : 사회")
    fun test2() {
        val social = ArrayList<NewsInformation>()
        val doc = setup(urls[2], 2)
        val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
        val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, 2)
        val numberOfNews = extractedAllNews[0].size

        for (index: Int in 0 until numberOfNews) {
            social.add(
                NewsInformation(
                    extractedAllNews[titleIndex][index] as? String ?: "",
                    extractedAllNews[contentIndex][index] as? String ?: "",
                    extractedAllNews[thumbnailIndex][index] as? String ?: "",
                    extractedAllNews[linkIndex][index] as? String ?: "",
                    extractedAllNews[pressIndex][index] as? String ?: "",
                    extractedAllNews[writtenDateIndex][index] as? String ?: "",
                    if (extractedAllNews[6][index] as? Boolean == true) {
                        HeadLine.HEAD_LINE
                    } else {
                        HeadLine.NORMAL
                    }
                )
            )
        }
        println("socialNews = ${social.size}")
    }

    @Test
    @DisplayName("카테고리 : 생활/문화")
    fun test3() {
        val lifeCul = ArrayList<NewsInformation>()
        val doc = setup(urls[3], 3)
        val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
        val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, 3)
        val numberOfNews = extractedAllNews[0].size

        for (index: Int in 0 until numberOfNews) {
            lifeCul.add(
                NewsInformation(
                    extractedAllNews[titleIndex][index] as? String ?: "",
                    extractedAllNews[contentIndex][index] as? String ?: "",
                    extractedAllNews[thumbnailIndex][index] as? String ?: "",
                    extractedAllNews[linkIndex][index] as? String ?: "",
                    extractedAllNews[pressIndex][index] as? String ?: "",
                    extractedAllNews[writtenDateIndex][index] as? String ?: "",
                    if (extractedAllNews[6][index] as? Boolean == true) {
                        HeadLine.HEAD_LINE
                    } else {
                        HeadLine.NORMAL
                    }
                )
            )
        }
        println("lifeCulNews = ${lifeCul.size}")
    }

    @Test
    @DisplayName("카테고리 : 세계")
    fun test4() {
        val worldNews = ArrayList<NewsInformation>()
        val doc = setup(urls[4], 4)
        val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
        val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, 4)
        val numberOfNews = extractedAllNews[0].size

        for (index: Int in 0 until numberOfNews) {
            worldNews.add(
                NewsInformation(
                    extractedAllNews[titleIndex][index] as? String ?: "",
                    extractedAllNews[contentIndex][index] as? String ?: "",
                    extractedAllNews[thumbnailIndex][index] as? String ?: "",
                    extractedAllNews[linkIndex][index] as? String ?: "",
                    extractedAllNews[pressIndex][index] as? String ?: "",
                    extractedAllNews[writtenDateIndex][index] as? String ?: "",
                    if (extractedAllNews[6][index] as? Boolean == true) {
                        HeadLine.HEAD_LINE
                    } else {
                        HeadLine.NORMAL
                    }
                )
            )
        }
        println("worldNews = ${worldNews.size}")
    }

    @Test
    @DisplayName("카테고리 : IT/과학")
    fun test5() {
        val itScience = ArrayList<NewsInformation>()
        val doc = setup(urls[5], 5)
        val allHeadLineNewsLinks = extractAllHeadLineNewsLinks(doc)
        val extractedAllNews = extractAllDetailNewsInHeadLine(allHeadLineNewsLinks, 5)
        val numberOfNews = extractedAllNews[0].size

        for (index: Int in 0 until numberOfNews) {
            itScience.add(
                NewsInformation(
                    extractedAllNews[titleIndex][index] as? String ?: "",
                    extractedAllNews[contentIndex][index] as? String ?: "",
                    extractedAllNews[thumbnailIndex][index] as? String ?: "",
                    extractedAllNews[linkIndex][index] as? String ?: "",
                    extractedAllNews[pressIndex][index] as? String ?: "",
                    extractedAllNews[writtenDateIndex][index] as? String ?: "",
                    if (extractedAllNews[6][index] as? Boolean == true) {
                        HeadLine.HEAD_LINE
                    } else {
                        HeadLine.NORMAL
                    }
                )
            )
        }
        println("itNews = ${itScience.size}")
    }


    @Test
    @DisplayName("모든 카테고리 한 번에 크롤링 해오기")
    fun executeCrawling() {
        val politicsNews = mutableListOf<NewsInformation>()
        val societyNews = mutableListOf<NewsInformation>()
        val economyNews = mutableListOf<NewsInformation>()
        val lifeCultureNews = mutableListOf<NewsInformation>()
        val worldNews = mutableListOf<NewsInformation>()
        val iTScienceNews = mutableListOf<NewsInformation>()

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
        log.info("Number Of politicsNews = ${politicsNews.size}")
        log.info("Number Of economyNews = ${economyNews.size}")
        log.info("Number Of societyNews = ${societyNews.size}")
        log.info("Number Of lifeCultureNews = ${lifeCultureNews.size}")
        log.info("Number Of worldNews = ${worldNews.size}")
        log.info("Number Of iTScienceNews = ${iTScienceNews.size}")
    }


    @Test
    @DisplayName("크롤링 된 뉴스들 저장 테스트")
    private fun saveTest() {
        val politicsNews = mutableListOf<NewsInformation>()
        val societyNews = mutableListOf<NewsInformation>()
        val economyNews = mutableListOf<NewsInformation>()
        val lifeCultureNews = mutableListOf<NewsInformation>()
        val worldNews = mutableListOf<NewsInformation>()
        val iTScienceNews = mutableListOf<NewsInformation>()

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

        val allNews = mutableListOf<News>()

        for (news in politicsNews) {
            allNews.add(
                News(
                    news.title,
                    news.content,
                    news.thumbnail,
                    news.link,
                    news.press,
                    news.writtenDateTime,
                    news.isHeadLine.name,
                    Category("정치"),
                    null
                )
            )
        }

        for (news in economyNews) {
            allNews.add(
                News(
                    news.title,
                    news.content,
                    news.thumbnail,
                    news.link,
                    news.press,
                    news.writtenDateTime,
                    news.isHeadLine.name,
                    Category("경제"),
                    null
                )
            )
        }

        for (news in societyNews) {
            allNews.add(
                News(
                    news.title,
                    news.content,
                    news.thumbnail,
                    news.link,
                    news.press,
                    news.writtenDateTime,
                    news.isHeadLine.name,
                    Category("사회"),
                    null
                )
            )
        }

        for (news in lifeCultureNews) {
            allNews.add(
                News(
                    news.title,
                    news.content,
                    news.thumbnail,
                    news.link,
                    news.press,
                    news.writtenDateTime,
                    news.isHeadLine.name,
                    Category("생활/문화"),
                    null
                )
            )
        }

        for (news in worldNews) {
            allNews.add(
                News(
                    news.title,
                    news.content,
                    news.thumbnail,
                    news.link,
                    news.press,
                    news.writtenDateTime,
                    news.isHeadLine.name,
                    Category("세계"),
                    null
                )
            )
        }

        for (news in iTScienceNews) {
            allNews.add(
                News(
                    news.title,
                    news.content,
                    news.thumbnail,
                    news.link,
                    news.press,
                    news.writtenDateTime,
                    news.isHeadLine.name,
                    Category("IT/과학"),
                    null
                )
            )
        }

//        for (allNew in allNews) {
//            newsRepository.save(allNew)
//        }

        // assertThat()
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
                    val image = detailDoc.getElementById("img1") ?: ""
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

}

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
}

enum class HeadLine {
    HEAD_LINE, NORMAL
}
