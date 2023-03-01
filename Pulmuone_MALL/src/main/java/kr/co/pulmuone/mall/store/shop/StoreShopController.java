package kr.co.pulmuone.mall.store.shop;

import io.swagger.annotations.*;
import kr.co.pulmuone.mall.store.shop.service.StoreShopMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;
import kr.co.pulmuone.v1.store.shop.dto.ShopListResponseDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.PickUpShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreShopController {

    @Autowired
    private StoreShopMallService storeShopMallService;

    @ApiOperation(value = "시도 리스트 조회")
    @GetMapping(value = "/store/shop/getAreaTypeList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CodeInfoVo.class)
    })
    public ApiResult<?> getAreaTypeList() {
        return storeShopMallService.getAreaTypeList();
    }

    @ApiOperation(value = "매장 리스트")
    @PostMapping(value = "/store/shop/getShopList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ShopListResponseDto.class)
    })
    public ApiResult<?> getShopList(ShopListRequestDto dto) throws Exception {
        return storeShopMallService.getShopList(dto);
    }

    @ApiOperation(value = "매장 조회")
    @PostMapping(value = "/store/shop/getShop")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "urStoreId", value = "매장 ID", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ShopVo.class)
    })
    public ApiResult<?> getShop(String urStoreId) throws Exception {
        return storeShopMallService.getShop(urStoreId);
    }

    @ApiOperation(value = "픽업가능 매장 리스트")
    @GetMapping(value = "/store/shop/getPickUpShopList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PickUpShopListVo.class)
    })
    public ApiResult<?> getPickUpShopList() throws Exception {
        return storeShopMallService.getPickUpShopList();
    }

}
