package kr.co.pulmuone.v1.goods.store.service;

import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStorePageInfoResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.StoreSearchGoodsRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;

public interface GoodsStoreBiz {

    OrgaStorePageInfoResponseDto getOrgaStorePageInfo() throws Exception;

    OrgaStoreGoodsResponseDto getOrgaStoreGoods(OrgaStoreGoodsRequestDto dto) throws Exception;

    SearchResultDto getSearchGoodsList(StoreSearchGoodsRequestDto dto) throws Exception;

    OrgaStoreGoodsResponseDto getOrgaFlyerGoods(String displayType) throws Exception;
}
