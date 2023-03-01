package kr.co.pulmuone.v1.store.shop.service;

import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;
import kr.co.pulmuone.v1.store.shop.dto.ShopListResponseDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.PickUpShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreShopBizImpl implements StoreShopBiz {

    @Autowired
    private StoreShopService storeShopService;

    @Override
    public List<CodeInfoVo> getAreaTypeList() {
        return storeShopService.getAreaTypeList();
    }

    @Override
    public ShopListResponseDto getShopList(ShopListRequestDto dto) {
        return storeShopService.getShopList(dto);
    }

    @Override
    public ShopVo getShop(String urStoreId) {
        return storeShopService.getShop(urStoreId);
    }

    @Override
    public List<PickUpShopListVo> getPickUpShopList() {
        return storeShopService.getPickUpShopList();
    }

}