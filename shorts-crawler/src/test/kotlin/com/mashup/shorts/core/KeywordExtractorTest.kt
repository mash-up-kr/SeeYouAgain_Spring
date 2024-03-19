package com.mashup.shorts.core

import com.mashup.shorts.core.modern.LuceneAnalyzerKeywordExtractor
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Disabled
@SpringBootTest
class KeywordExtractorTest(
    @Autowired private val keywordExtractor: LuceneAnalyzerKeywordExtractor,
) {

    @Test
    fun execute() {
        val title = "대구 수성알파시티에 2031년까지 1천개 기업 유치 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에 수성알파시티에"
        val content =
            "수원역 수원역 수원역 수원역 수원역 수원역 수원역 수원역 최저임금 최저임금 최저임금 최저임금 최저임금 최저임금 최저임금 최저임금 최저임금 최저임금 (서울=연합뉴스) 강건택 기자 = 정부가 대구 수성알파시티를 2만 명의 디지털 인재가 상주하는 국가 디지털 혁신지구로 본격 조성한다.\n" +
                "\n" +
                "과학기술정보통신부는 4일 대구 경북대에서 윤석열 대통령 주재로 열린 '국민과 함께하는 민생 토론회 : 첨단 신산업으로 우뚝 솟는 대구'에서 이와 같은 계획을 보고했다.\n" +
                "\n" +
                "과기정통부는 \"수성알파시티를 국가 디지털 혁신지구로 본격 조성해 2031년까지 디지털 기업 1천 개와 2만 명의 상주인력이 집적되도록 하는 것이 목표\"라고 밝혔다.\n" +
                "\n" +
                "10년 전까지만 해도 포도밭에 불과했던 수성알파시티 부지는 2014년 시작된 SW 융합기술 고도화사업 추진을 통해 현재까지 모두 243개 기업이 입주를 확정했다.\n" +
                "\n" +
                "지난해에는 국가 디지털 혁신지구 시범지역으로 선정돼 3년간 1단계 시범사업을 진행 중이다. 시범사업에 따라 대구경북과학기술원(DGIST), 경북대, 포항공대, 계명대 등 4개 대학, 7개 인공지능·소프트웨어 연구센터를 유치했다.\n" +
                "\n" +
                "과기정통부는 우수 연구시설 구축, 대형 연구개발 과제 등으로 국내외 우수 연구팀을 수성알파시티로 끌어들여 '디지털 연구개발 허브'를 조성하기 위한 2단계 본사업 계획을 대구시와 함께 수립 중이라고 전했다.\n" +
                "\n" +
                "이에 따라 대구시는 본사업 추진계획과 연계해 IBM, 하버드 의대, 스탠퍼드대 등 14개 기관에 소속된 해외 우수 연구자들의 사업 참여 의향서를 확보했고, DGIST와 대학원 중심 제2캠퍼스(가칭 'ABB 글로벌 캠퍼스') 설립 협의를 진행하고 있다.\n" +
                "\n" +
                "과기정통부는 \"수성알파시티는 높은 수준의 산학협력 체계를 갖췄고, 영남권 주요 국가산단과의 접근성이 좋아 디지털 생태계 거점으로서의 입지 조건이 우수하다\"며 \"향후 지방시대의 랜드마크로 성장할 수 있을 것이라고 말했다."
        println(keywordExtractor.extractKeyword(title, content))
    }
}
