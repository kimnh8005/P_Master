package kr.co.pulmuone.mall.store.shop.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;
import kr.co.pulmuone.v1.store.shop.service.StoreShopBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StoreShopMallServiceImpl implements StoreShopMallService {

    @Autowired
    private StoreShopBiz storeShopBiz;

    @Override
    public ApiResult<?> getAreaTypeList() {
        return ApiResult.success(storeShopBiz.getAreaTypeList());
    }

    @Override
    public ApiResult<?> getShopList(ShopListRequestDto dto) {
        return ApiResult.success(storeShopBiz.getShopList(dto));
    }

    @Override
    public ApiResult<?> getShop(String urStoreId) {
        return ApiResult.success(storeShopBiz.getShop(urStoreId));
    }

    @Override
    public ApiResult<?> getPickUpShopList() {
        return ApiResult.success(storeShopBiz.getPickUpShopList());
    }

}