package kr.co.pulmuone.mall.promotion.exhibit;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyCycleDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.*;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.mall.promotion.exhibit.service.PromotionExhibitMallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromotionExhibitController {
    @Autowired
    public PromotionExhibitMallService promotionExhibitMallService;

    @PostMapping(value = "/promotion/exhibit/getExhibitListByUser")
    @ApiOperation(value = "기획전 목록 조회", httpMethod = "POST", notes = "기획전 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ExhibitListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getExhibitListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        return promotionExhibitMallService.getExhibitListByUser(dto);
    }

    @PostMapping(value = "/promotion/exhibit/getNormalByUser")
    @ApiOperation(value = "일반 기획전 조회", httpMethod = "POST", notes = "일반 기획전 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "evExhibitId", value = "기획전 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = NormalByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NO_EXHIBIT] NO_EXHIBIT - 기획전 없음 \n" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getNormalByUser(Long evExhibitId) throws Exception {
        return promotionExhibitMallService.getNormalByUser(evExhibitId);
    }

    @PostMapping(value = "/promotion/exhibit/getSelectListByUser")
    @ApiOperation(value = "골라담기 목록 조회", httpMethod = "POST", notes = "골라담기 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SelectListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getSelectListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        return promotionExhibitMallService.getSelectListByUser(dto);
    }

    @PostMapping(value = "/promotion/exhibit/getSelectByUser")
    @ApiOperation(value = "골라담기 조회", httpMethod = "POST", notes = "골라담기 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "evExhibitId", value = "기획전 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SelectByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NO_EXHIBIT] NO_EXHIBIT - 기획전 없음 \n" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getSelectByUser(Long evExhibitId) throws Exception {
        return promotionExhibitMallService.getSelectByUser(evExhibitId);
    }

    @PostMapping(value = "/promotion/exhibit/getGiftByUser")
    @ApiOperation(value = "증정 기획전 조회", httpMethod = "POST", notes = "증정 기획전 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "evExhibitId", value = "기획전 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = NormalByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NO_EXHIBIT] NO_EXHIBIT - 기획전 없음 \n" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getGiftByUser(Long evExhibitId) throws Exception {
        return promotionExhibitMallService.getGiftByUser(evExhibitId);
    }

    @PostMapping(value = "/promotion/exhibit/getGreenJuicePageInfo")
    @ApiOperation(value = "녹즙내맘대로 주문하기 페이지 정보", httpMethod = "POST", notes = "녹즙내맘대로 주문하기 페이지 정보")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsDailyCycleDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getGreenJuicePageInfo(GreenJuicePageRequestDto dto) throws Exception {
        return promotionExhibitMallService.getGreenJuicePageInfo(dto);
    }

    @GetMapping(value = "/promotion/exhibit/getGreenJuiceGoods")
    @ApiOperation(value = "녹즙내맘대로 주문하기 상품 조회", httpMethod = "GET", notes = "녹즙내맘대로 주문하기 상품 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsSearchResultDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getGreenJuiceGoods() throws Exception {
        return promotionExhibitMallService.getGreenJuiceGoods();
    }

    @PostMapping(value = "/promotion/exhibit/addSelectOrder")
    @ApiOperation(value = "골라담기 장바구니 저장 및 바로 구매", httpMethod = "POST", notes = "골라담기 장바구니 저장 및 바로 구매")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SelectOrderResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NOT_QTY] NOT_QTY - 수량 오류 \n" +
                    "[NOT_GOODS] NOT_GOODS - 골라담기 상품 아님 \n" +
                    "[NO_STOCK] NO_STOCK - 재고 부족 \n" +
                    "[NO_EXHIBIT] NO_EXHIBIT - 기획전 없음 \n" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> addSelectOrder(SelectOrderRequestDto dto) throws Exception {
        return promotionExhibitMallService.addSelectOrder(dto);
    }

    @PostMapping(value = "/promotion/exhibit/addGreenJuiceOrder")
    @ApiOperation(value = "녹즙내맘대로주문하기 바로구매", httpMethod = "POST", notes = "녹즙내맘대로주문하기 바로구매")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SelectOrderResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 "
            )
    })
    public ApiResult<?> addGreenJuiceOrder(GreenJuiceOrderRequestDto dto) throws Exception {
        return promotionExhibitMallService.addGreenJuiceOrder(dto);
    }

}
