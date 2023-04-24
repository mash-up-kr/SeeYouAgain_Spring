package main

import org.jsoup.Jsoup
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

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

    @DisplayName("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100 크롤링 테스트")
    @Test
    fun crawlingTest() {
        // 헤드라인 리스트
        val doc = Jsoup.connect(url).get()

        // 헤드라인 더보기 링크를 가져오기 위한 베이스 코드
        val allHeadLineMoreLinksDoc = doc
            .getElementsByClass("cluster_foot_more nclicks(cls_pol.clstitle)")
            .tagName("a")

        // 각 헤드라인 리스트에서 해당 기사들의 링크를 담기 위한 리스트
        val allHeadLineNewsLinks = ArrayList<String>()

        // HTML 태그 내에서 헤드라인 링크만 가져올 수 있도록 가공
        for (element in allHeadLineMoreLinksDoc) {
            val link = element.toString()
            val start = link.indexOf("/") // "/" 가 처음 등장하는 인덱스
            val end = link.indexOf("\" ") // "" " 가 처음 등장하는 인덱스
            allHeadLineNewsLinks.add(symbolicLinkBaseUrl + link.substring(start, end))
        }

        val eachHeadLineLinks = ArrayList<String>()
        val eachHeadLineTitles = ArrayList<String>()
        val eachHeadLineContents = ArrayList<String>()
        val eachHeadLineImages = ArrayList<String>()

        // 각 헤드라인에 해당하는 리스트의 링크를 순회하는 부분
        // 이 링크 내에서 각 뉴스를 순회해야함
        // 링크만 따오는 subString, indexOf 구문에서 매직넘버를 사용하게 됨 이거 고쳐야함.
        for (link in allHeadLineNewsLinks) {
            val moreDoc = Jsoup.connect(link).get()
            val title = moreDoc.getElementsByClass("nclicks(cls_pol.clsart1)").toString()
            val start = title.indexOf("\"") + 1
            val end = title.indexOf("\" ")
            eachHeadLineLinks.add(title.substring(start, end))
        }

        for (eachHeadLineLink in eachHeadLineLinks) {
            val eachHeadLineDoc = Jsoup.connect(eachHeadLineLink).get()
            val title = eachHeadLineDoc.getElementsByClass("media_end_head_headline")
            val content = eachHeadLineDoc.getElementsByClass("go_trans _article_content")
            val image = eachHeadLineDoc.getElementsByClass("_LAZY_LOADING")
            eachHeadLineTitles.add(title.text())
            eachHeadLineContents.add(content.text())
        }

        for (i: Int in 0 until eachHeadLineTitles.size) {
            println(eachHeadLineTitles.get(i))
            println(eachHeadLineContents.get(i))
            println("---")
        }
    }
}
