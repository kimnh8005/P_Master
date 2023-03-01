package kr.co.pulmuone.v1.store.shop.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;
import kr.co.pulmuone.v1.store.shop.dto.ShopListResponseDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.PickUpShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoreShopServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private StoreShopService storeShopService;

    @Test
    void getAreaTypeList_조회_성공() {
        //given, when
        List<CodeInfoVo> result = storeShopService.getAreaTypeList();

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getShopList_조회_성공() {
        //given
        ShopListRequestDto dto = new ShopListRequestDto();
        dto.setSearchText("대구");
        dto.setPage(1);
        dto.setLimit(20);

        //when
        ShopListResponseDto result = storeShopService.getShopList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getShop_조회_성공() {
        //given
        String urStoreId = "O01011";

        //when
        ShopVo result = storeShopService.getShop(urStoreId);

        //then
        assertNotNull(result);
    }

    @Test
    void getPickUpShopList_조회_성공() {
        //given, when
        List<PickUpShopListVo> result = storeShopService.getPickUpShopList();

        //then
        assertTrue(result.size() > 0);
    }

}