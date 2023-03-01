package kr.co.pulmuone.mall.display.contents;

import io.swagger.annotations.*;
import kr.co.pulmuone.mall.display.contents.service.DisplayContentsMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.display.contents.dto.*;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200928   	 	        이원호         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class DisplayContentsController {
    @Autowired
    private DisplayContentsMallService displayContentsMallService;

    @GetMapping(value = "/display/contents/getInventoryContentsInfo/{pageCd}/{deviceType}/{userType}")
    @ApiOperation(value = "인벤토리별 컨텐츠 정보 조회 - 페이지코드", httpMethod = "GET", notes = "전시 인벤토리별 컨텐츠 정보 조회 - 페이지코드")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageCd", value = "페이지 코드", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deviceType", value = "디바이스 유형", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userType", value = "유저 유형", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = InventoryContentsInfoResponseDto.class)
    })
    public ApiResult<?> getInventoryContentsInfoList(@PathVariable(value = "pageCd") String pageCd, @PathVariable(value = "deviceType") String deviceType, @PathVariable(value = "userType") String userType) throws Exception {
        return displayContentsMallService.getInventoryContentsInfoList(pageCd, deviceType, userType);
    }

    @GetMapping(value = "/display/contents/getCategoryInfo/{inventoryCd}/{deviceType}/{userType}")
    @ApiOperation(value = "전시 카테고리 정보 조회", httpMethod = "GET", notes = "전시 카테고리 정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inventoryCd", value = "인벤토리 코드", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deviceType", value = "디바이스 유형", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userType", value = "유저 유형", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = InventoryCategoryResponseDto.class)
    })
    public ApiResult<?> getCategoryInfo(@PathVariable(value = "inventoryCd") String inventoryCd, @PathVariable(value = "deviceType") String deviceType, @PathVariable(value = "userType") String userType) throws Exception {
        return displayContentsMallService.getCategoryInfo(inventoryCd, deviceType, userType);
    }

    @GetMapping(value = "/display/contents/getContentsLevel1ByInventoryCd/{inventoryCd}/{deviceType}")
    @ApiOperation(value = "인벤토리 조회", httpMethod = "GET", notes = "인벤토리 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inventoryCd", value = "인벤토리 코드", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deviceType", value = "디바이스 유형", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ContentsDetailVo.class)
    })
    public ApiResult<?> getContentsLevel1ByInventoryCd(@PathVariable(value = "inventoryCd") String inventoryCd, @PathVariable(value = "deviceType") String deviceType) throws Exception {
        return displayContentsMallService.getContentsLevel1ByInventoryCd(inventoryCd, deviceType);
    }

    @GetMapping(value = {"/display/contents/getContentsInfo/{dpContsId}/{deviceType}/{userType}/{goodsSortCode}", "/display/contents/getContentsInfo/{dpContsId}/{deviceType}/{userType}"})
    @ApiOperation(value = "전시 컨텐츠 정보 조회", httpMethod = "GET", notes = "전시 컨텐츠 정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dpContsId", value = "전시 컨텐츠 PK", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "deviceType", value = "디바이스 유형", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userType", value = "유저 유형", required = true, dataType = "String"),
            @ApiImplicitParam(name = "goodsSortCode", value = "상품 정렬 코드", dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ContentsInfoResponseDto.class)
    })
    public ApiResult<?> getContentsInfo(@PathVariable(value = "dpContsId") Long dpContsId, @PathVariable(value = "deviceType") String deviceType, @PathVariable(value = "userType") String userType, @PathVariable(value = "goodsSortCode", required = false) String goodsSortCode) throws Exception {
        goodsSortCode = StringUtils.isEmpty(goodsSortCode) ? DisplayEnums.GoodsSortCode.DEFAULT.getCode() : goodsSortCode;
        return displayContentsMallService.getContentsInfo(dpContsId, deviceType, userType, goodsSortCode);
    }

    /**
     * 배송지 주소 조회
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @GetMapping(value = "/display/contents/getShippingAddress")
    @ApiOperation(value = "배송지 주소 조회", httpMethod = "GET", notes = "배송지 주소 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetSessionShippingResponseDto.class)
    })
    public ApiResult<?> getShippingAddress() throws Exception {
        return displayContentsMallService.getShippingAddress();
    }

    /**
     * 신규회원 쿠폰 조회
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @GetMapping(value = "/display/contents/getUserJoinCoupon")
    @ApiOperation(value = "신규회원 쿠폰 조회", httpMethod = "GET", notes = "신규회원 쿠폰 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CouponResponseDto.class)
    })
    public ApiResult<?> getCoupon() throws Exception {
        return displayContentsMallService.getCoupon();
    }

    /**
     * 신규회원 쿠폰 상품 정보 조회
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @GetMapping(value = "/display/contents/getUserJoinGoodsList")
    @ApiOperation(value = "신규회원 쿠폰 상품 정보 조회", httpMethod = "GET", notes = "신규회원 쿠폰 상품 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetSessionShippingResponseDto.class)
    })
    public ApiResult<?> getUserJoinGoodsList() throws Exception {
        return displayContentsMallService.getUserJoinGoodsList();
    }


    @PostMapping(value = "/display/contents/getBestGoods")
    @ApiOperation(value = "잘나가요이상품 상품조회", httpMethod = "POST", notes = "잘나가요이상품 상품조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchType", value = "조회 구분(MAIN, BEST)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mallDiv", value = "몰 구분", required = true, dataType = "String"),
            @ApiImplicitParam(name = "ilCtgryId", value = "카테고리 PK", dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsSearchResultDto.class)
    })
    public ApiResult<?> getBestGoods(String searchType, String mallDiv, Long ilCtgryId) throws Exception {
        return displayContentsMallService.getBestGoods(searchType, mallDiv, ilCtgryId);
    }

    @PostMapping(value = "/display/contents/getDailyShippingGoods")
    @ApiOperation(value = "일일배송 상품조회", httpMethod = "POST", notes = "일일배송 상품조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsResponseDto.class)
    })
    public ApiResult<?> getDailyShippingGoods(DailyShippingGoodsRequestDto dto) throws Exception {
        return displayContentsMallService.getDailyShippingGoods(dto);
    }

    @PostMapping(value = "/display/contents/getSaleGoodsPageInfo")
    @ApiOperation(value = "지금세일 페이지 정보", httpMethod = "POST", notes = "지금세일 페이지 정보")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mallDiv", value = "몰 구분", required = true, dataType = "String"),
            @ApiImplicitParam(name = "discountRateStart", value = "할인율 시작", dataType = "int"),
            @ApiImplicitParam(name = "discountRateEnd", value = "할인율 종료", dataType = "int")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsResponseDto.class)
    })
    public ApiResult<?> getSaleGoodsPageInfo(String mallDiv, @RequestParam(defaultValue = "100") int discountRateStart, @RequestParam(defaultValue = "0") int discountRateEnd) throws Exception {
        return displayContentsMallService.getSaleGoodsPageInfo(mallDiv, discountRateStart, discountRateEnd);
    }

    @PostMapping(value = "/display/contents/getSaleGoods")
    @ApiOperation(value = "지금세일 상품조회", httpMethod = "POST", notes = "지금세일 상품조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsResponseDto.class)
    })
    public ApiResult<?> getSaleGoods(SaleGoodsRequestDto dto) throws Exception {
        return displayContentsMallService.getSaleGoods(dto);
    }

    @PostMapping(value = "/display/contents/getNewGoods")
    @ApiOperation(value = "신제품 상품조회", httpMethod = "POST", notes = "신제품 상품조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsResponseDto.class)
    })
    public ApiResult<?> getNewGoods(NewGoodsRequestDto dto) throws Exception {
        return displayContentsMallService.getNewGoods(dto);
    }

    @PostMapping(value = "/display/contents/getBrandGoods")
    @ApiOperation(value = "브랜드 상품조회", httpMethod = "POST", notes = "브랜드 상품조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsResponseDto.class)
    })
    public ApiResult<?> getBrandGoods(BrandGoodsRequestDto dto) throws Exception {
        return displayContentsMallService.getBrandGoods(dto);
    }

    @PostMapping(value = "/display/contents/getLohasBanner")
    @ApiOperation(value = "로하스 배너조회", httpMethod = "POST", notes = "로하스 배너조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = LohasBannerResponseDto.class)
    })
    public ApiResult<?> getLohasBanner(LohasBannerRequestDto dto) throws Exception {
        return displayContentsMallService.getLohasBanner(dto);
    }

    @GetMapping(value = "/display/contents/getBestGoodsList/{mallDiv}/{deviceType}/{userType}")
    @ApiOperation(value = "잘나가요이상품 카테고리 전체 List", httpMethod = "GET", notes = "잘나가요이상품 카테고리 전체 List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mallDiv", value = "몰 구분", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deviceType", value = "디바이스 유형", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userType", value = "유저 유형", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = BestGoodsListResponseDto.class)
    })
    public ApiResult<?> getBestGoodsList(@PathVariable(value = "mallDiv") String mallDiv, @PathVariable(value = "deviceType") String deviceType, @PathVariable(value = "userType") String userType) throws Exception {
        return displayContentsMallService.getBestGoodsList(mallDiv, deviceType, userType);
    }

}
