package kr.co.pulmuone.v1.goods.store.service;

import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStorePageInfoResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.StoreSearchGoodsRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import kr.co.pulmuone.v1.search.searcher.dto.StoreGoodsSearchRequestDto;
import kr.co.pulmuone.v1.search.searcher.service.SearchBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsStoreBizImpl implements GoodsStoreBiz {
    @Autowired
    private GoodsStoreService goodsStoreService;

    @Autowired
    private SearchBiz searchBiz;

    @Override
    public OrgaStorePageInfoResponseDto getOrgaStorePageInfo() throws Exception {
        return goodsStoreService.getOrgaStorePageInfo();
    }

    @Override
    public OrgaStoreGoodsResponseDto getOrgaStoreGoods(OrgaStoreGoodsRequestDto dto) throws Exception {
        return goodsStoreService.getOrgaStoreGoods(dto);
    }

    @Override
    public OrgaStoreGoodsResponseDto getOrgaFlyerGoods(String discountType) throws Exception {
        return goodsStoreService.getOrgaFlyerGoods(discountType);
    }

    @Override
    public SearchResultDto getSearchGoodsList(StoreSearchGoodsRequestDto dto) throws Exception {
        StoreGoodsSearchRequestDto requestDto = StoreGoodsSearchRequestDto.builder()
                .keyword(dto.getKeyword())
                .storeId(dto.getUrStoreId())
                .excludeSoldOutGoods(dto.getExcludeSoldOutGoods())
                .page(dto.getPage())
                .limit(dto.getLimit())
                .sortCode(dto.getSortCode())
                .build();
        requestDto.decodeKeyword();
        return searchBiz.searchStoreGoods(requestDto);
    }

}
