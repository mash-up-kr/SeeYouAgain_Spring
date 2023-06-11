package com.mashup.shorts.core

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.stereotype.Component
import com.mashup.shorts.core.const.NewsDOMClassNameConst.CONTENT_CLASS_NAME
import com.mashup.shorts.core.const.NewsDOMClassNameConst.IMAGE_ID_NAME
import com.mashup.shorts.core.const.NewsDOMClassNameConst.PRESS_CLASS_NAME
import com.mashup.shorts.core.const.NewsDOMClassNameConst.TITLE_CLASS_NAME
import com.mashup.shorts.core.const.NewsDOMClassNameConst.WRITTEN_DATETIME_CLASS_NAME
import com.mashup.shorts.core.const.NewsDOMClassNameConst.detailDocClassNames
import com.mashup.shorts.core.const.NewsLinkElementConst.moreHeadLineLinksElements
import com.mashup.shorts.core.const.NewsUrlConst.SYMBOLIC_LINK_BASE_URL
import com.mashup.shorts.core.util.CrawlerContentConverter
import com.mashup.shorts.core.util.CrawlerContentFilter
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.news.News

@Component
class CrawlerBase {

    internal fun getMoreHeadLineLinks(url: String, categoryName: CategoryName): Elements {
        return Jsoup.connect(url).get()
            .getElementsByClass(moreHeadLineLinksElements[categoryName]!!)
            .tagName("a")
    }

    internal fun extractAllHeadLineNewsLinks(allHeadLineMoreLinksDocs: Elements): List<String> {
        val allDetailHeadLineNewsLinks = mutableListOf<String>()

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

    internal fun extractNewsCardBundle(
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

                // 너무 빠른 요청으로 인해 크롤링 차단을 방지하고자 0.1초의 간격 부여
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
                val writtenDateTime =
                    detailDoc.getElementsByClass(WRITTEN_DATETIME_CLASS_NAME).text()

                cardNews.add(
                    News(
                        title = title,
                        content = content,
                        thumbnailImageUrl = CrawlerContentFilter.filterImageLinkForm(imageLink),
                        newsLink = detailLink,
                        press = press,
                        writtenDateTime = writtenDateTime,
                        type = CrawlerContentConverter.convertHeadLine(headLineFlag),
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
}