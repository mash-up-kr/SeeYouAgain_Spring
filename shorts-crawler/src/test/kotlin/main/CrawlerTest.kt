package main

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.SocketTimeoutException

/* 정치 헤드라인

div class="list_body section_index"
div class="_persist"
div class="cluster"
div class="cluster_group _cluster_content"
div class="cluster_body"
ul class="cluster_list"
li class="cluster_item"

class="cluster_thumb" (썸네일)

class="cluster_text"

    class="cluster_text_headline nclicks(cls_pol.clsart)" (제목)


class="cluster_foot_more nclicks(cls_pol.clstitle)"


 */

class CrawlerTest {

    private val url = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100"

    private val symbolicLinkBaseUrl = "https://news.naver.com"
    private val allDetailHeadLineNewsTitleIndex = 0
    private val allDetailHeadLineNewsContentIndex = 1
    private val allDetailHeadLineNewsThumbnailIndex = 2
    private val allDetailHeadLineNewsLinkIndex = 3
    private val allDetailHeadLineNewsPressIndex = 4
    private val allDetailHeadLineNewsWrittenDateIndex = 5

    @DisplayName("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100 크롤링 테스트")
    @Test
    fun crawlingTest() {
        val doc = setup()
        val newsObjects = ArrayList<NewsInformation>()
        val allHeadLineNews = extractAllHeadLineLinks(doc)

        var extractAllDetailHeadLineNews: List<List<String>>

        try {
            extractAllDetailHeadLineNews = extractAllDetailHeadLineNews(allHeadLineNews)
        } catch (exception: SocketTimeoutException) {
            extractAllDetailHeadLineNews = extractAllDetailHeadLineNews(allHeadLineNews)
        }

        for (i: Int in 0..extractAllDetailHeadLineNews.size) {
            newsObjects.add(
                NewsInformation(
                    extractAllDetailHeadLineNews.get(allDetailHeadLineNewsTitleIndex).get(i),
                    extractAllDetailHeadLineNews.get(allDetailHeadLineNewsContentIndex).get(i),
                    extractAllDetailHeadLineNews.get(allDetailHeadLineNewsThumbnailIndex).get(i),
                    extractAllDetailHeadLineNews.get(allDetailHeadLineNewsLinkIndex).get(i),
                    extractAllDetailHeadLineNews.get(allDetailHeadLineNewsPressIndex).get(i),
                    extractAllDetailHeadLineNews.get(allDetailHeadLineNewsWrittenDateIndex).get(i),
                )
            )
        }
    }

    private fun setup(): Elements {
        return Jsoup.connect(url).get()
            .getElementsByClass("cluster_foot_more nclicks(cls_pol.clstitle)")
            .tagName("a")
    }

    // 특정 헤드라인에 해당하는 모든 뉴스를 리스트로 반환한다.
    private fun extractAllHeadLineLinks(allHeadLineMoreLinksDoc: Elements): List<String> {
        val allDetailHeadLineNewsLinks = ArrayList<String>()

        // HTML 태그 내에서 헤드라인 링크만 가져올 수 있도록 가공
        for (element in allHeadLineMoreLinksDoc) {
            val link = element.toString()
            val start = link.indexOf("/") // "/" 가 처음 등장하는 인덱스
            val end = link.indexOf("\" ") // "" " 가 처음 등장하는 인덱스
            allDetailHeadLineNewsLinks.add(symbolicLinkBaseUrl + link.substring(start, end))
        }

        return allDetailHeadLineNewsLinks
            .filter { !it.equals("") }
            .distinct()
    }

    // 특정 헤드라인에 해당하는 모든 뉴스를 순회하며 그 링크를 반환한다.
    // 가끔씩 빈 문자열이 들어가는 경우도 있어서 반환할 때 filter를 사용했다.
    private fun extractAllDetailHeadLineNews(allHeadLineNews: List<String>): List<List<String>> {
        val result = ArrayList<ArrayList<String>>()
        val detailHeadLineNewsTitles = ArrayList<String>()
        val detailHeadLineContents = ArrayList<String>()
        val detailHeadLineThumbnails = ArrayList<String>()
        val detailHeadLineNewsLinks = ArrayList<String>()
        val detailHeadLineNewsPresses = ArrayList<String>()
        val detailHeadLineNewsWrittenDateTime = ArrayList<String>()

        for (link in allHeadLineNews) {
            val moreDoc = Jsoup.connect(link).get()
            val crawledHtmls = moreDoc.getElementsByClass("nclicks(cls_pol.clsart1)")
                .toString()
                .split("</a>")
            for (crawledHtml in crawledHtmls) {
                val detailLink = Jsoup.parse(crawledHtml)
                    .select("a[href]")
                    .attr("href")
                if (!detailLink.equals("")) {
                    val detailDoc = Jsoup.connect(detailLink).get()
                    val title = detailDoc.getElementsByClass("media_end_head_headline").text()
                    val content = detailDoc.getElementsByClass("go_trans _article_content").text()
                    val image = detailDoc.getElementsByClass("go_trans _article_content")
                    val press = detailDoc.getElementsByClass("media_end_linked_more_point").text()
                    val writtenDateTime =
                        detailDoc.getElementsByClass("media_end_head_info_datestamp_time _ARTICLE_DATE_TIME")
                            .text()

                    detailHeadLineNewsTitles.add(title)
                    detailHeadLineContents.add(content)
                    detailHeadLineThumbnails.add(image.text())
                    detailHeadLineNewsLinks.add(detailLink)
                    detailHeadLineNewsPresses.add(press)
                    detailHeadLineNewsWrittenDateTime.add(writtenDateTime)
                }
            }
        }

        result.add(detailHeadLineNewsTitles)
        result.add(detailHeadLineContents)
        result.add(detailHeadLineThumbnails)
        result.add(detailHeadLineNewsLinks)
        result.add(detailHeadLineNewsPresses)
        result.add(detailHeadLineNewsWrittenDateTime)
        return result
    }
}