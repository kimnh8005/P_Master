package kr.co.pulmuone.mall.store.shop.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;

public interface StoreShopMallService {

    ApiResult<?> getAreaTypeList();

    ApiResult<?> getShopList(ShopListRequestDto dto);

    ApiResult<?> getShop(String urStoreId);

    ApiResult<?> getPickUpShopList();

}
