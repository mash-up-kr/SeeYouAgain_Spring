package main


import java.time.LocalDateTime
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.mashup.shorts.ShortsCrawlerApplication
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.core.infrastructure.ClovaRequestInterface
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository


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

private const val relatedCountClassName = "cluster_banner_count_icon_num"

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

private const val titleIndex = 0
private const val contentIndex = 1
private const val thumbnailIndex = 2
private const val linkIndex = 3
private const val pressIndex = 4
private const val writtenDateIndex = 5
private const val isHeadLineIndex = 6


/*

교육 박홍근·복지 한정애·행안 정청래 선출 계획 본회의 표결 직전 보류 前원내대표·前장관·최고위원에 의총서 \"기득권 나눠 먹기\" 비판…원점 재검토 국기에 대한 경례하는 이재명 대표와 박광온 원내대표 (서울=연합뉴스) 하사헌 기자 = 더불어민주당 이재명 대표, 박광온 원내대표와 의원들이 30일 오후 서울 여의도 국회에서 열린 의원총회에서 국기에 대한 경례를 하고 있다. 2023.5.30 toadboy@yna.co.kr (서울=연합뉴스) 설승은 한주홍 정수연 기자 = 더불어민주당에서 자당 몫 상임위원장 교체 문제를 놓고 잡음이 일고 있다. 탈당한 김남국 의원의 가상자산 보유 논란과 '2021년 전당대회 돈봉투 의혹'으로 인한 위기를 타개하겠다며 쇄신을 결의해놓고는 정작 집안에서 자리싸움을 벌인다는 지적도 일각서 나온다. 위원장 교체 대상 상임위인 교육·행정안전·산업통상자원중소벤처기업·보건복지·환경노동·과학기술정보방송통신(과방위)·예산결산특별위원회 등 7곳 중 민주당 몫은 과방위를 뺀 6곳이다. 여야는 작년 7월 후반기 국회 원(院) 구성 협상시 행안위와 과방위원장에 대해선 1년씩 번갈아 맡기로 했다. 여야는 30일 본회의에서 이 가운데 교육·행정안전·보건복지 등 민주당 몫 3자리와 여당 몫인 과방위 위원장을 선출하기로 하고 본회의 안건까지 만들었으나, 민주당이 돌연 자당 몫 위원장 선출을 보류했다. 본회의 개최 30분 전 열린 의원총회에서 자당 몫 상임위원장 인선에 대한 문제 제기가 나온 데 따른 것이다. 교육위원장에 박홍근 의원, 행정안전위원장에 정청래 최고위원, 복지위원장에 한정애 의원이 각각 내정된 상황이었다. 김한규 원내대변인은 의총 후 \"여러 의원이, 국민들이 쇄신과 혁신을 기대하는 상황을 고려했을 때 조금 더 당내 논의를 하는 게 좋겠다는 의견을 줬다\"며 \"오늘은 민주당이 추천한 후보를 선출하는 과정은 진행하지 않고 당내에서 좀 더 논의하도록 했다\"고 밝혔다. 의총에서는 기동민·허영 의원을 비롯한 일부 의원들이 위원장 선출과 관련해 토론이 더 필요하다는 취지의 주장을 폈고, 참석자들이 박수로 동의의 뜻을 표한 것으로 알려졌다. 의총에서 기 의원은 \"'기득권 나눠먹기'의 전형으로, 이런 모습이 혁신과 쇄신을 하겠다는 당의 모습으로 보이겠나. 기준과 원칙이 필요하다\"고 말한 것으로 전해졌다. 상임위원장 후보 가운데 박 의원과 한 의원은 각각 원내대표와 장관직을 지냈고, 정 최고위원은 현재 당직을 맡고 있다는 점을 겨냥한 것으로 풀이된다. 당내에선 그간 선수(選數)와 나이를 고려하되, 장관이나 주요 당직을 지낸 경우 상임위원장을 하지 않는 것이 '관례'였지만 지켜지지 않고 있는 만큼 기준을 다시 세우자는 주장이기도 하다. 의총에서 의원들의 지적이 잇따르자 박 의원과 한 의원은 \"자리에 연연하지 않고 원내 결정에 따르겠다\"는 취지로 발언했다고 한다. 앞서 정 최고위원의 경우 작년 과방위원장 직무를 시작할 당시에도 '최고위원이 상임위원장을 맡지 않는 것은 관례'라는 당내 비판에 직면했었다. 하지만 정 최고위원이 과방위와 행안위를 1년 단위로 맞교대하기로 한 여야 합의에 따라 행안위로 옮겨 위원장을 계속하겠다는 입장을 견지해 원내지도부가 고심에 빠지기도 했다. 정 최고위원은 이날 본회의에서 자신과 행안위원장-과방위원장 직을 '맞교대'하기로 한 장제원 의원이 과방위원장에 선출된 직후 페이스북에 \"1년 전 맞교대하기로 했던 분명한 합의가 있건만 참 별일이 다 있다. 매우 유감스럽다. 그러나 꺾이지 않고 가겠다\"라고 적었다. 나머지 상임위도 순탄치 않은 모양새다. 돈 봉투 의혹으로 탈당한 산자위원장 윤관석 의원은 당의 사퇴 설득 끝에 위원장직을 내려놨다. 불법 토지 거래 혐의로 1심에서 징역 6월에 집행유예 2년을 선고받은 김경협 의원은 환노위원장으로 거론됐으나 역시 당 설득으로 위원장을 맡지 않기로 했다. 예결위원장에는 우상호 의원이 내정된 것으로 알려졌지만 그 역시 원내대표 이력이 있다. 주요 당직을 지낸 인사들을 제외하면 차기 상임위원장은 재선의 이상헌 김철민 의원 등이 차례다. ses@yna.co.kr

위 문장은 요약 돌리면 아래와 같은 에러가 터짐

HttpStatusCode	ErrorCode	ErrorMessage	Description
400	            E003	    Text            quota Exceeded	문장이 기준치보다 초과 했을 경우


 */

@SpringBootTest(classes = [ShortsCrawlerApplication::class])
class CrawlerCoreTest @Autowired constructor(
    val clovaRequestInterface: ClovaRequestInterface,
    private val newsRepository: NewsRepository,
    private val newsCardRepository: NewsCardRepository,
) {

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
                    if (extractedAllNews[isHeadLineIndex][index] as? Boolean == true) {
                        HeadLine.HEAD_LINE
                    } else {
                        HeadLine.NORMAL
                    }
                )
            )
            println("extractedAllNews = ${extractedAllNews[titleIndex][index]}")
        }
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
                    if (extractedAllNews[isHeadLineIndex][index] as? Boolean == true) {
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
                    if (extractedAllNews[isHeadLineIndex][index] as? Boolean == true) {
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
                    if (extractedAllNews[isHeadLineIndex][index] as? Boolean == true) {
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
                    if (extractedAllNews[isHeadLineIndex][index] as? Boolean == true) {
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
                    if (extractedAllNews[isHeadLineIndex][index] as? Boolean == true) {
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
                                    Category("정치")
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
                                    Category("경제")
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
                                    Category("사회")
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
                                    Category("생활/문화")
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
                                    Category("세계")
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
                                    Category("IT/과학")
                                )
                            )
                        }
                    }
                }
                when (i) {
                    0 -> {
                        newsCards.add(
                            NewsCard(
                                Category("정치"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    1 -> {
                        newsCards.add(
                            NewsCard(
                                Category("경제"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    2 -> {
                        newsCards.add(
                            NewsCard(
                                Category("사회"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    3 -> {
                        newsCards.add(
                            NewsCard(
                                Category("생활/문화"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    4 -> {
                        newsCards.add(
                            NewsCard(
                                Category("세계"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }

                    5 -> {
                        newsCards.add(
                            NewsCard(
                                Category("IT/과학"),
                                newsBundle.map { it.id }.toString(),
                                ""
                            )
                        )
                    }
                }
                for (news in newsBundle) {
                    newsRepository.save(news)
                }
            }

            for (newsCard in newsCards) {
                newsCardRepository.save(newsCard)
            }

            log.info("Take a break for 3 seconds to prevent request load")
            Thread.sleep(3000)

            newsCards = mutableListOf()
        }
    }

    private fun setup(url: String, i: Int): Elements {
        return Jsoup.connect(url).get()
            .getElementsByClass(moreHeadLineLinksElements[i])
            .tagName("a")
    }

    private fun extractAllHeadLineNewsLinks(allHeadLineMoreLinksDoc: Elements): List<String> {
        val allDetailHeadLineNewsLinks = ArrayList<String>()

        for (element in allHeadLineMoreLinksDoc) {
            val link = element.toString()
            val start = link.indexOf("/")
            val end = link.indexOf("\" ")
            allDetailHeadLineNewsLinks.add(symbolicLinkBaseUrl + link.substring(start, end))
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
            if (extractedNews[isHeadLineIndex][index] as? Boolean == true) HeadLine.HEAD_LINE else HeadLine.NORMAL,
        )
    }

    enum class HeadLine {
        HEAD_LINE, NORMAL
    }
}
