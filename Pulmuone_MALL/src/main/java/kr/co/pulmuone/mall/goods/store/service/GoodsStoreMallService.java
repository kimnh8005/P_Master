package kr.co.pulmuone.mall.goods.store.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsRequestDto;

public interface GoodsStoreMallService {

    ApiResult<?> getOrgaStorePageInfo() throws Exception;

    ApiResult<?> getOrgaStoreGoods(OrgaStoreGoodsRequestDto dto) throws Exception;

    ApiResult<?> getOrgaFlyerGoods(String discountType) throws Exception;
}
