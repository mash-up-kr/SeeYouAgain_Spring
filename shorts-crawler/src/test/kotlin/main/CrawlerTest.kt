package main

import org.jsoup.Jsoup
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CrawlerTest {

    private val url = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100"
    private val keyword = "전세사기"

    @DisplayName("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100 크롤링 테스트")
    @Test
    fun crawlingTest() {
        val doc = Jsoup.connect(url + keyword).get()
        val elements = doc.getElementsByClass("list_tit")
        val pressElements = doc.getElementsByClass("cluster_text_info")

        println("pressElements = ${pressElements}")


        for (element in elements) {
            val title = element.text()
            val link = element.attr("href")

            println(title)
            println(link)
            println("---")
        }
    }
}