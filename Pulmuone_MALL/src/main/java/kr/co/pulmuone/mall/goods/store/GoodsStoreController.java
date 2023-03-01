package kr.co.pulmuone.mall.goods.store;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.mall.goods.store.service.GoodsStoreMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStorePageInfoResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.StoreSearchGoodsRequestDto;
import kr.co.pulmuone.v1.goods.store.service.GoodsStoreBiz;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsStoreController {

    @Autowired
    private GoodsStoreMallService goodsStoreMallService;

    @Autowired
    private GoodsStoreBiz goodsStoreBiz;

    @GetMapping(value = "/goods/store/getOrgaStorePageInfo")
    @ApiOperation(value = "올가 매장전용관 페이지 정보")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = OrgaStorePageInfoResponseDto.class)
    })
    public ApiResult<?> getOrgaStorePageInfo() throws Exception {
        return goodsStoreMallService.getOrgaStorePageInfo();
    }

    @PostMapping(value = "/goods/store/getOrgaStoreGoods")
    @ApiOperation(value = "올가 매장전용관 상품 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = OrgaStoreGoodsResponseDto.class)
    })
    public ApiResult<?> getOrgaStoreGoods(OrgaStoreGoodsRequestDto dto) throws Exception {
        return goodsStoreMallService.getOrgaStoreGoods(dto);
    }

    @PostMapping(value = "/goods/store/getSearchGoodsList")
    @ApiOperation(value = "매장배송 상품 검색")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SearchResultDto.class)
    })
    public ApiResult<?> getSearchGoodsList(StoreSearchGoodsRequestDto dto) throws Exception {
        return ApiResult.success(goodsStoreBiz.getSearchGoodsList(dto));
    }

    @PostMapping(value = "/goods/store/getOrgaFlyerGoods")
    @ApiOperation(value = "전단행사 대체 상품 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SearchResultDto.class)
    })
    public ApiResult<?> getOrgaFlyerGoods(String discountType) throws Exception {
        return goodsStoreMallService.getOrgaFlyerGoods(discountType);
    }
}