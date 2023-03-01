package kr.co.pulmuone.mall.display.contents.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.DisplayConstants;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.display.contents.dto.*;
import kr.co.pulmuone.v1.display.contents.service.DisplayContentsBiz;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import kr.co.pulmuone.v1.goods.category.service.GoodsCategoryBiz;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsEtcBiz;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchNewGoodsRequestDto;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponGoodsByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import kr.co.pulmuone.v1.search.searcher.dto.AggregationDocumentDto;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import kr.co.pulmuone.v1.search.searcher.service.SearchBiz;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
 * 1.0    20200826   	 	이원호            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class DisplayContentsMallServiceImpl implements DisplayContentsMallService {

    @Autowired
    private DisplayContentsBiz displayContentsBiz;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    @Autowired
    private PromotionCouponBiz promotionCouponBiz;

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;

    @Autowired
    private SearchBiz searchBiz;

    @Autowired
    private GoodsEtcBiz goodsEtcBiz;

    @Autowired
    private GoodsCategoryBiz goodsCategoryBiz;

    @Autowired
    private PolicyConfigBiz policyConfigBiz;

    @Override
    public ApiResult<?> getInventoryContentsInfoList(String pageCd, String deviceType, String userType) throws Exception {
        return ApiResult.success(displayContentsBiz.getInventoryContentsInfoList(pageCd, deviceType, userType));
    }

    @Override
    public ApiResult<?> getCategoryInfo(String inventoryCd, String deviceType, String userType) throws Exception {
        return ApiResult.success(displayContentsBiz.getCategoryInfo(inventoryCd, deviceType, userType));
    }

    @Override
    public ApiResult<?> getContentsLevel1ByInventoryCd(String inventoryCd, String deviceType) throws Exception {
        return ApiResult.success(displayContentsBiz.getContentsInfoByInventoryCd(inventoryCd, deviceType));
    }

    @Override
    public ApiResult<?> getContentsInfo(Long dpContsId, String deviceType, String userType, String goodsSortCode) throws Exception {
        return ApiResult.success(displayContentsBiz.getContentsInfo(dpContsId, deviceType, userType, goodsSortCode));
    }

    @Override
    public ApiResult<?> getShippingAddress() throws Exception {
        return ApiResult.success(userCertificationBiz.getSessionShipping());
    }

    @Override
    public ApiResult<?> getCoupon() throws Exception {
        List<CouponInfoByUserJoinVo> targetCouponList = promotionCouponBiz.getCouponInfoByUserJoin();
        List<CouponInfoByUserJoinVo> result = targetCouponList.stream()
                .filter(vo -> vo.getIssueDetailType().equals(CouponEnums.IssueDetailType.USER_JOIN.getCode()))  // 회원가입 쿠폰 정보
                .collect(Collectors.toList());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> getUserJoinGoodsList() throws Exception {
        List<CouponGoodsResponseDto> resultList = new ArrayList<>();

        List<CouponGoodsByUserJoinVo> targetList = promotionCouponBiz.getUserJoinGoods();
        for (CouponGoodsByUserJoinVo vo : targetList) {

            // 상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .goodsIdList(Collections.singletonList(vo.getCoverageId()))
                    .build();
            List<GoodsSearchResultDto> searchGoods = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);
            if (searchGoods.size() > 0) {
                resultList.add(new CouponGoodsResponseDto(vo.getDiscountValue(), searchGoods.get(0)));
            }
        }

        return ApiResult.success(resultList);
    }

    @Override
    public ApiResult<?> getBestGoods(String searchType, String mallDiv, Long ilCtgryId) throws Exception {
        int total = 100;
        if (searchType.equals("MAIN")) {
            total = 20;
        } else if (ilCtgryId > 0L) {
            total = 40;
        }

        BestGoodsRequestDto dto = new BestGoodsRequestDto();
        dto.setTotal(total);
        dto.setMallDiv(mallDiv);
        dto.setIlCtgryId(ilCtgryId);
        return ApiResult.success(displayContentsBiz.getBestGoods(dto));
    }

    @Override
    public ApiResult<?> getBestGoodsList(String mallDiv, String deviceType, String userType) throws Exception {
        // 카테고리 정보 조회
        List<GetCategoryListResultVo> categoryList = goodsCategoryBiz.getCategoryList(mallDiv);

        // set result
        List<BestGoodsListResponseDto> result = new ArrayList<>();
        result.add(new BestGoodsListResponseDto(0L, "전체"));
        result.addAll(categoryList.stream()
                .map(BestGoodsListResponseDto::new)
                .collect(Collectors.toList()));

        // 카테고리별 베스트 상품 조회
        for (BestGoodsListResponseDto vo : result) {
            int total = 40;
            if (vo.getIlCtgryId() == 0L) {
                total = 110;
            }

            BestGoodsRequestDto dto = new BestGoodsRequestDto();
            dto.setTotal(total);
            dto.setMallDiv(mallDiv);
            dto.setIlCtgryId(vo.getIlCtgryId());
            dto.setDeviceType(deviceType);
            dto.setUserType(userType);

            List<GoodsSearchResultDto> goods = displayContentsBiz.getBestGoods(dto);
            vo.setTotal(goods.size());
            vo.setGoods(goods);
        }

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> getDailyShippingGoods(DailyShippingGoodsRequestDto dto) throws Exception {
        GoodsResponseDto responseDto = new GoodsResponseDto();
        List<Long> goodsIdList = goodsSearchBiz.getDailyGoods(dto.getGoodsDailyType());

        if (goodsIdList.size() > 0) {
            // 상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .goodsIdList(goodsIdList)
                    .goodsSortCode(dto.getGoodsSortCode())
                    .build();

            List<GoodsSearchResultDto> searchGoods = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);
            if (searchGoods.size() > 0) {
                responseDto.setTotal(searchGoods.size());

                // page, limit 처리
                responseDto.setGoods(searchGoods.stream()
                        .skip(dto.getSkipPage())
                        .limit(dto.getLimit())
                        .collect(Collectors.toList()));
            }
        }

        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getSaleGoodsPageInfo(String mallDiv, int discountRateStart, int discountRateEnd) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        boolean isEmployee = (buyerVo != null) && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

        SaleGoodsPageInfoResponseDto responseDto = new SaleGoodsPageInfoResponseDto();

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .isEmployee(isEmployee)
                .minimumDiscountRate(discountRateEnd == 0 ? 1 : discountRateEnd)
                .maximumDiscountRate(discountRateStart)
                .isDiscountGoods(true)
                .page(1)
                .limit(1)
                .mallId(mallDiv)
                .isFirstSearch(true)
                .excludeSoldOutGoods(true)
                .build();

        SearchResultDto searchGoodsResponse = searchBiz.searchGoods(goodsSearchRequestDto);

        if (searchGoodsResponse != null && searchGoodsResponse.getFilter() != null) {
            responseDto.setCategory((List<AggregationDocumentDto>) searchGoodsResponse.getFilter().get("category"));
        }

        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getSaleGoods(SaleGoodsRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        boolean isEmployee = (buyerVo != null) && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

        GoodsResponseDto responseDto = new GoodsResponseDto();

        SortCode sortCode = SortCode.HIGH_DISCOUNT_RATE;
        if (dto.getGoodsSortCode() != null) {
            switch (dto.getGoodsSortCode()) {
                case "HIGH_RATE":
                    sortCode = SortCode.HIGH_DISCOUNT_RATE;
                    break;
                case "EMPLOYEE_HIGH_RATE":
                    sortCode = SortCode.HIGH_EMPLOYEE_DISCOUNT_RATE;
                    break;
                case "NEW":
                    sortCode = SortCode.NEW;
                    break;
                case "LOW_PRICE":
                    sortCode = SortCode.LOW_PRICE;
                    break;
                case "HIGH_PRICE":
                    sortCode = SortCode.HIGH_PRICE;
                    break;
                case "POPULARITY":
                    sortCode = SortCode.POPULARITY;
                    break;
                case "EMPLOYEE_LOW_PRICE":
                    sortCode = SortCode.EMPLOYEE_LOW_PRICE;
                    break;
                case "EMPLOYEE_HIGH_PRICE":
                    sortCode = SortCode.EMPLOYEE_HIGH_PRICE;
                    break;
            }
        }
        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .isEmployee(isEmployee)
                .minimumDiscountRate(dto.getDiscountRateEnd())
                .maximumDiscountRate(dto.getDiscountRateStart())
                .isDiscountGoods(true)
                .page(dto.getPage())
                .limit(dto.getLimit())
                .mallId(dto.getMallDiv())
                .lev1CategoryId(dto.getIlCtgryId())
                .sortCode(sortCode)
                .excludeSoldOutGoods(true)
                .build();

        SearchResultDto searchGoodsResponse = searchBiz.searchGoods(goodsSearchRequestDto);
        if (searchGoodsResponse != null) {
            responseDto.setTotal(searchGoodsResponse.getCount());
            responseDto.setGoods(searchGoodsResponse.getDocument());
        }

        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getNewGoods(NewGoodsRequestDto dto) throws Exception {
        GoodsResponseDto responseDto = new GoodsResponseDto();
        GoodsSearchNewGoodsRequestDto newGoodsRequestDto = GoodsSearchNewGoodsRequestDto.builder()
                .mallDiv(dto.getMallDiv())
                .monthSub(GoodsConstants.NEW_GOODS_MONTH_INTERVAL_3)
                .build();

        // ORGA 예외처리
        List<String> orgaDpBrandIdList = Arrays.stream(policyConfigBiz.getConfigValue(Constants.ORGA_DP_BRAND_KEY).split(",")).collect(Collectors.toList());
        newGoodsRequestDto.setDpBrandIdList(orgaDpBrandIdList);

        List<Long> goodsIdList = goodsSearchBiz.getNewGoods(newGoodsRequestDto);

        SortCode sortCode = SortCode.POPULARITY;
        if (dto.getGoodsSortCode() != null) {
            switch (dto.getGoodsSortCode()) {
                case "HIGH_RATE":
                    sortCode = SortCode.HIGH_DISCOUNT_RATE;
                    break;
                case "EMPLOYEE_HIGH_RATE":
                    sortCode = SortCode.HIGH_EMPLOYEE_DISCOUNT_RATE;
                    break;
                case "NEW":
                    sortCode = SortCode.NEW;
                    break;
                case "LOW_PRICE":
                    sortCode = SortCode.LOW_PRICE;
                    break;
                case "HIGH_PRICE":
                    sortCode = SortCode.HIGH_PRICE;
                    break;
                case "POPULARITY":
                    sortCode = SortCode.POPULARITY;
                    break;
                case "EMPLOYEE_LOW_PRICE":
                    sortCode = SortCode.EMPLOYEE_LOW_PRICE;
                    break;
                case "EMPLOYEE_HIGH_PRICE":
                    sortCode = SortCode.EMPLOYEE_HIGH_PRICE;
                    break;
            }
        }

        if (goodsIdList.size() > 0) {
            GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                    .mallId(dto.getMallDiv())
                    .goodsIdList(goodsIdList)
                    .sortCode(sortCode)
                    .page(dto.getPage())
                    .limit(dto.getLimit())
                    .excludeSoldOutGoods(true)
                    .build();

            SearchResultDto searchGoodsResponse = searchBiz.searchGoods(goodsSearchRequestDto);
            if (searchGoodsResponse != null) {
                responseDto.setTotal(searchGoodsResponse.getCount());
                responseDto.setGoods(searchGoodsResponse.getDocument());
            }
        }

        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getBrandGoods(BrandGoodsRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        boolean isEmployee = (buyerVo != null) && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

        GoodsResponseDto responseDto = new GoodsResponseDto();

        SortCode sortCode = SortCode.HIGH_DISCOUNT_RATE;
        if (dto.getGoodsSortCode() != null) {
            switch (dto.getGoodsSortCode()) {
                case "HIGH_RATE":
                    sortCode = SortCode.HIGH_DISCOUNT_RATE;
                    break;
                case "EMPLOYEE_HIGH_RATE":
                    sortCode = SortCode.HIGH_EMPLOYEE_DISCOUNT_RATE;
                    break;
                case "NEW":
                    sortCode = SortCode.NEW;
                    break;
                case "LOW_PRICE":
                    sortCode = SortCode.LOW_PRICE;
                    break;
                case "HIGH_PRICE":
                    sortCode = SortCode.HIGH_PRICE;
                    break;
                case "POPULARITY":
                    sortCode = SortCode.POPULARITY;
                    break;
                case "EMPLOYEE_LOW_PRICE":
                    sortCode = SortCode.EMPLOYEE_LOW_PRICE;
                    break;
                case "EMPLOYEE_HIGH_PRICE":
                    sortCode = SortCode.EMPLOYEE_HIGH_PRICE;
                    break;
            }
        }

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .isEmployee(isEmployee)
                .mallId(dto.getMallDiv())
                .page(dto.getPage())
                .limit(dto.getLimit())
                .brandIdList(Collections.singletonList(dto.getDpBrandId()))
                .sortCode(sortCode)
                .excludeSoldOutGoods(true)
                .build();

        SearchResultDto searchGoodsResponse = searchBiz.searchGoods(goodsSearchRequestDto);
        if (searchGoodsResponse != null) {
            responseDto.setTotal(searchGoodsResponse.getCount());
            responseDto.setGoods(searchGoodsResponse.getDocument());
        }

        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getLohasBanner(LohasBannerRequestDto dto) throws Exception {
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setInventoryCd(DisplayConstants.LOHAS_BANNER_CODE);
        return ApiResult.success(displayContentsBiz.getLohasBanner(dto));
    }

}
