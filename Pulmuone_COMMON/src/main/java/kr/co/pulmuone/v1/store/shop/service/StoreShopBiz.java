package kr.co.pulmuone.v1.store.shop.service;

import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;
import kr.co.pulmuone.v1.store.shop.dto.ShopListResponseDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.PickUpShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;

import java.util.List;

public interface StoreShopBiz {

    List<CodeInfoVo> getAreaTypeList();

    ShopListResponseDto getShopList(ShopListRequestDto dto);

    ShopVo getShop(String urStoreId);

    List<PickUpShopListVo> getPickUpShopList();

}