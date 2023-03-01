package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.search.indexer.dto.CategoryBoostDocumentDto;
import kr.co.pulmuone.v1.search.indexer.dto.PromotionSuggestionDocumentDto;
import kr.co.pulmuone.v1.search.indexer.dto.GoodsDocumentDto;
import kr.co.pulmuone.v1.search.indexer.dto.GoodsSuggestionDocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
class IndexServiceTemplateTest extends CommonServiceTestBaseForJunit5 {


    @Autowired
    GoodsIndexService goodsIndexService;

    @Autowired
    GoodsSuggestionIndexService goodsSuggestionIndexService;

    @Autowired
    CategoryBoostIndexService categoryBoostIndexService;

    @Autowired
    PromotionSuggestionIndexService promotionSuggestionIndexService;



    @Test
    void test_상품색인_데이터_없음() {

        GoodsIndexService goodsIndexService = new GoodsIndexService() {
            @Override
            protected List<GoodsDocumentDto> getIndexTargetList() {
                return null;
            }
        };

        Assertions.assertThrows(Exception.class, () -> {
            goodsIndexService.bulk();
        });
    }

    @Test
    void test_추천검색어_색인_데이터_없음() {

        GoodsSuggestionIndexService goodsSuggestionIndexService = new GoodsSuggestionIndexService() {
            @Override
            protected List<GoodsSuggestionDocumentDto> getIndexTargetList() {
                return null;
            }
        };

        Assertions.assertThrows(Exception.class, () -> {
            goodsSuggestionIndexService.bulk();
        });

    }

    @Test
    void test_카테고리부스팅_색인_데이터_없음() {

        CategoryBoostIndexService categoryBoostIndexService = new CategoryBoostIndexService() {
            @Override
            protected List<CategoryBoostDocumentDto> getIndexTargetList() {
                return null;
            }
        };

        Assertions.assertThrows(Exception.class, () -> {
            categoryBoostIndexService.bulk();
        });

    }

    @Test
    void test_이벤트_기획전_추천검색어_색인_데이터_없음() {

        PromotionSuggestionIndexService promotionSuggestionIndexService = new PromotionSuggestionIndexService() {
            @Override
            protected List<PromotionSuggestionDocumentDto> getIndexTargetList() {
                return null;
            }
        };

        Assertions.assertThrows(Exception.class, () -> {
            promotionSuggestionIndexService.bulk();
        });

    }



    @Test
    void test_상품색인() throws Exception {
        goodsIndexService.bulk();
    }


    @Test
    void test_자동완성색인() throws Exception {
        goodsSuggestionIndexService.bulk();
    }

    @Test
    void test_카테고리부스팅색인() throws Exception {
        categoryBoostIndexService.bulk();
    }

    @Test
    void test_이벤트기획전_추천검색어_색인() throws Exception {
        promotionSuggestionIndexService.bulk();
    }

}