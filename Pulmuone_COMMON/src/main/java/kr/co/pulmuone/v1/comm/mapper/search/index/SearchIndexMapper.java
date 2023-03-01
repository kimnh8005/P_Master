package kr.co.pulmuone.v1.comm.mapper.search.index;

import kr.co.pulmuone.v1.search.indexer.dto.CategoryBoostDocumentDto;
import kr.co.pulmuone.v1.search.indexer.dto.GoodsDocumentDto;
import kr.co.pulmuone.v1.search.indexer.dto.PromotionSuggestionDto;
import kr.co.pulmuone.v1.search.indexer.dto.StoreGoodsDocumentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchIndexMapper {

    /**
     * 상품 색인 대상 조회
     * @return
     */
    List<GoodsDocumentDto> getIndexTargetGoodsList();

    /**
     * 카테고리 부스팅 대상 조회
     * @return
     */
    List<CategoryBoostDocumentDto> getCategoryBoostList();


    /**
     * 이벤트/기획전 검색어 추천 대상 조회
     * @return
     */
    List<PromotionSuggestionDto> getPromotionSuggestionList();

    List<GoodsDocumentDto> getIndexTargetGoodsSuggestionList();

    /**
     * 매장 상품 색인 대상 조회
     * @return
     */
    List<StoreGoodsDocumentDto> getIndexTargetStoreGoodsList();
}
