package kr.co.pulmuone.mall.goods.goods.service;

import java.util.*;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.goods.category.service.GoodsCategoryBiz;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponByGoodsRequestDto;
import kr.co.pulmuone.v1.system.code.service.SystemCodeBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.mall.user.buyer.service.UserBuyerMallService;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleType;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackEachCountDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackImageListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackScorePercentDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackSetBestRequestDto;
import kr.co.pulmuone.v1.customer.feedback.service.CustomerFeedbackBiz;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaByGoodsResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaVo;
import kr.co.pulmuone.v1.customer.qna.service.CustomerQnaBiz;
import kr.co.pulmuone.v1.display.contents.service.DisplayContentsBiz;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsCertificationListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsNutritionListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsSpecListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsEtcBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeResponseDto;
import kr.co.pulmuone.v1.goods.notice.service.GoodsNoticeBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;
import kr.co.pulmuone.v1.policy.benefit.service.PolicyBenefitEmployeeBiz;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponByGoodsListRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftListVo;
import kr.co.pulmuone.v1.promotion.exhibit.service.PromotionExhibitBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateScheduleDto;
import kr.co.pulmuone.v1.shopping.favorites.service.ShoppingFavoritesBiz;
import kr.co.pulmuone.v1.shopping.recently.service.ShoppingRecentlyBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200810   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class GoodsGoodsMallServiceImpl implements GoodsGoodsMallService {

  @Autowired
  public GoodsGoodsBiz            goodsGoodsBiz;

  @Autowired
  public GoodsEtcBiz              goodsEtcBiz;

  @Autowired
  public OrderOrderBiz            orderOrderBiz;

  @Autowired
  public GoodsShippingTemplateBiz goodsShippingTemplateBiz;

  @Autowired
  public ShoppingFavoritesBiz     shoppingFavoritesBiz;

  @Autowired
  public ShoppingRecentlyBiz      shoppingRecentryBiz;

  @Autowired
  public PolicyConfigBiz          policyConfigBiz;

  @Autowired
  public PromotionCouponBiz       promotionCouponBiz;

  @Autowired
  public StoreDeliveryBiz         storeDeliveryBiz;

  @Autowired
  public UserBuyerBiz             userBuyerBiz;

  @Autowired
  public CustomerFeedbackBiz      customerFeedbackBiz;

  @Autowired
  public CustomerQnaBiz           customerQnaBiz;

  @Autowired
  private UserCertificationBiz userCertificationBiz;

  @Autowired
  private PromotionExhibitBiz promotionExhibitBiz;

  @Autowired
  private UserBuyerMallService userBuyerMallService;

  @Autowired
  private GoodsNoticeBiz goodsNoticeBiz;

  @Autowired
  private DisplayContentsBiz displayContentsBiz;

  @Autowired
  private PolicyBenefitEmployeeBiz policyBenefitEmployeeBiz;

  @Autowired
  private SystemCodeBiz systemCodeBiz;

    @Autowired
    private GoodsCategoryBiz goodsCategoryBiz;

  /**
   * 상품 상세 조회
   *
   * @param ilGoodsId
   * @return GoodsInfoResponseDto
   * @throws Exception
   */
  @Override
  public ApiResult<?> getGoodsPageInfo(Long ilGoodsId, String mallDiv, String ilCtgryId, String urPcidCd) throws Exception {

    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
    boolean isMember = StringUtil.isNotEmpty(urUserId);
    boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
    Long spFavoritesGoodsId = 0L;
    int limitMaxCnt = 0;
    int systemLimitMaxCnt = 0;
    int orderGoodsBuyQty = 0;
    List<GoodsSearchResultDto> recommendedGoodsList = new ArrayList<>();
    List<GoodsNutritionListResultVo> goodsNutritionList = new ArrayList<>();
    List<ShippingDataResultDto> shippingDataList = new ArrayList<>();
    List<PackageGoodsListDto> goodsPackageList = new ArrayList<>();
    List<GoodsReserveOptionListDto> reserveOptionList = new ArrayList<>();
    String goodsDailyAllergyYn = "";
    String goodsDailyBulkYn = "";
    List<HashMap<String, String>> goodsDailyBulkList = new ArrayList<>();
    RegularShippingConfigDto regularShippingConfigDto = new RegularShippingConfigDto();
    List<GoodsDailyCycleDto> goodsDailyCycleList = new ArrayList<>();
    boolean isShippingPossibility = true;
    EmployeeDiscountInfoDto employeeDiscountInfoDto = new EmployeeDiscountInfoDto();
    boolean isNewBuyerSpecials = false;
    int newBuyerSpecialSsalePrice = 0;
    int stock = 0;
    String storeDeliveryInterval = "";

    // 매장
    boolean isBuyStoreGoods = false;
    boolean isStoreShippingPossibility = false;
    int storeStock = 0;
    int storeSalePrice = 0;
    int storeDiscountPrice = 0;
    int storeCouponDiscountPrice = 0;
    int storePaymentExpectedPrice = 0;

    GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder()
                                                     .ilGoodsId(ilGoodsId)
                                                     .deviceInfo(DeviceUtil.getDirInfo())
                                                     .isApp(DeviceUtil.isApp())
                                                     .isMember(isMember)
                                                     .isEmployee(isEmployee)
                                                     .isDawnDelivery(false)
                                                     .build();

    // 상품 기본 정보
    BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

    if (goods != null) {

    	// 임직원 전용 상품일때 임직원 아닌경우 접근 불가
		if (!isEmployee && !"Y".equals(goods.getPurchaseNonmemberYn())
				&& !"Y".equals(goods.getPurchaseMemberYn()) && "Y".equals(goods.getPurchaseEmployeeYn())) {
			return ApiResult.result("임직원", GoodsEnums.Goods.LIMIT_PURCHASE);
		}

    	// 추가, 증정상품은 예외 처리
    	if(GoodsEnums.GoodsType.ADDITIONAL.getCode().equals(goods.getGoodsType()) || GoodsEnums.GoodsType.GIFT.getCode().equals(goods.getGoodsType()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(goods.getGoodsType())) {
    		return ApiResult.result(GoodsEnums.Goods.NOT_VIEW_GOODS_TYPE);
    	}


      // 할인율
      int discountRate = PriceUtil.getRate(goods.getRecommendedPrice(), goods.getSalePrice());
      // 할인금액
      int discountPrice = goods.getRecommendedPrice() - goods.getSalePrice();
      // 일반회원 상품 결제 예정금액
      int buyerPaymentExpectedPrice = goods.getRecommendedPrice() - discountPrice;

      stock = goods.getStockQty();

      // 상품 상세 정보
      DetailSelectGoodsVo goodsDetail = goodsGoodsBiz.getDetailSelectGoods(ilGoodsId);

      // 상품 이미지
      // 묶음 상품 여부, 상품id, 품목 id, 이미지 노출 타입
      List<GoodsImageDto> goodsImageList = goodsGoodsBiz.getGoodsImageList(goods.getGoodsType(),
                                                                           ilGoodsId,
                                                                           goods.getIlItemCode(),
                                                                           goodsDetail.getGoodsPackageImageType());
      List<HashMap> goodsImage = new ArrayList<>();
      for (GoodsImageDto goodsImageDto : goodsImageList) {
        HashMap<String, String> imageMap = new HashMap<>();
        imageMap.put("smallImage", goodsImageDto.getCImage());
        imageMap.put("bigImage", goodsImageDto.getBImage());
        goodsImage.add(imageMap);
      }

      // 상품 인증정보
      List<GoodsCertificationListResultVo> getGoodsCertificationList = goodsGoodsBiz.getGoodsCertificationList(ilGoodsId);

      // 상품정보제공고시
      List<GoodsSpecListResultVo> getGoodsSpecList = goodsGoodsBiz.getGoodsSpecList(ilGoodsId);

      // 추천상품 리스트
      recommendedGoodsList = goodsEtcBiz.getRecommendedGoodsList(goodsDetail.getCategoryIdDepth2());

      // 최대구매 가능수량
      // 최대구매수량 제한있을 때
      if (goods.getLimitMaximumCountYn().equals("Y")) {
        orderGoodsBuyQty = orderOrderBiz.getOrderGoodsBuyQty(ilGoodsId, urUserId);
        systemLimitMaxCnt = goods.getLimitMaximumCount();
        limitMaxCnt = systemLimitMaxCnt - orderGoodsBuyQty;
        if (limitMaxCnt < 0)
          limitMaxCnt = 0;
      }

      // 비회원구매가능 여부
      boolean isBuyNonMember = "Y".equals(goods.getPurchaseNonmemberYn());
      boolean isBuyMember = "Y".equals(goods.getPurchaseMemberYn());
      boolean isBuyEmployee = "Y".equals(goods.getPurchaseEmployeeYn());

      // 영양정보
      if (goodsDetail.getNutritionDisplayYn().equals("Y")) {
        goodsNutritionList = goodsGoodsBiz.getGoodsNutritionList(goods.getIlItemCode());
      }

      // 묶음상품일 경우
      if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
        goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(ilGoodsId, isMember, isEmployee, false, null, 1);

        RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);
        stock = recalculationPackage.getStockQty();
        goods.setSaleStatus(recalculationPackage.getSaleStatus());
        goods.setArrivalScheduledDateDto(recalculationPackage.getArrivalScheduledDateDto());

      }

      // 추가구성 상품
      List<AdditionalGoodsDto> additionalGoodsList = goodsGoodsBiz.getAdditionalGoodsInfoList(ilGoodsId,
                                                                                              isMember,
                                                                                              isEmployee,
                                                                                              false,
                                                                                              null);

      // 배송정보
      ShippingDataResultDto getShippingData = goodsShippingTemplateBiz.getShippingInfo(goods.getSaleType(),
                                                                                       ilGoodsId,
                                                                                       goods.getUrWareHouseId());

	  // 일일 배송의 경우 패턴을 넘겨수 처리해야함
      if (GoodsEnums.SaleType.DAILY.getCode().equals(goods.getSaleType())) {
    	  goods.setArrivalScheduledDateDtoList(goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(goods.getArrivalScheduledDateDtoList(), goods.getUrWareHouseId(), GoodsCycleType.DAYS5_PER_WEEK.getCode(), null));
    	  goods.setArrivalScheduledDateDto(goodsGoodsBiz.getLatestArrivalScheduledDateDto(goods.getArrivalScheduledDateDtoList()));
      }

      // 도착 예정시간 세팅
      if (goods.getArrivalScheduledDateDto() != null) {
        getShippingData.setArrivalScheduledDate(goods.getArrivalScheduledDateDto().getArrivalScheduledDate());
      }
      shippingDataList.add(getShippingData);

      // 새벽배송 가능 && 예약판매가 아닐 경우
      if (goods.canDawnDelivery() && !goods.getSaleType().equals(GoodsEnums.SaleType.RESERVATION.getCode())) {
        ShippingDataResultDto dawnShippingData = new ShippingDataResultDto();

        dawnShippingData.setDeliveryType(GoodsEnums.DeliveryType.DAWN.getCode());
        dawnShippingData.setDeliveryTypeName(GoodsEnums.DeliveryType.DAWN.getCodeName());
        dawnShippingData.setShippingPriceText(getShippingData.getShippingPriceText());

        // 새벽배송시 도착 예정 시간
        ArrivalScheduledDateDto dawnDeliveryArrivalScheduledDateDto = null;
        // 묶음 상품인 경우
        if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
        	List<PackageGoodsListDto> dawnDeliveryGoodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(ilGoodsId, isMember, isEmployee, true, null, 1);

            RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), dawnDeliveryGoodsPackageList);
            dawnDeliveryArrivalScheduledDateDto = recalculationPackage.getArrivalScheduledDateDto();
        } else {
        	dawnDeliveryArrivalScheduledDateDto = goodsGoodsBiz.getLatestArrivalScheduledDateDto(goods.getUrWareHouseId(), goods.getIlGoodsId(), true, null);
        }
        if (dawnDeliveryArrivalScheduledDateDto != null) {
          dawnShippingData.setArrivalScheduledDate(dawnDeliveryArrivalScheduledDateDto.getArrivalScheduledDate());
        }

        shippingDataList.add(dawnShippingData);
      }

      // 예약상품일 경우
      if (goods.getSaleType().equals(GoodsEnums.SaleType.RESERVATION.getCode())) {
        reserveOptionList = goodsGoodsBiz.getGoodsReserveOptionList(ilGoodsId);

        // 예약상품 모든 판매회차의 재고가 0이거나 예약주문시간이 지났을때, 상품 판매상태 일시품절로 수정
        int allGoodsStock = reserveOptionList.stream().map(m -> m.getStockQty()).mapToInt(Integer::intValue).sum();

//        LocalDateTime currentDate = LocalDateTime.now();
//        boolean allMatch = reserveOptionList.stream().allMatch(f -> currentDate.isAfter(f.getReserveEndDate()));

        stock = allGoodsStock;
//        if (stock == 0 || allMatch) {
//          goods.setSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
//        }
      }

      // 정기배송
      // 수정예정
      int regularShippingSalePrice = 0;
      if (goods.getSaleType().equals(GoodsEnums.SaleType.REGULAR.getCode())) {

        regularShippingConfigDto = policyConfigBiz.getRegularShippingConfig();
        DiscountCalculationResultDto discountCalculationResultDto =
                                                                  goodsEtcBiz.discountCalculation(goods.getSalePrice(),
                                                                                                  GoodsEnums.GoodsDiscountMethodType.FIXED_RATE.getCode(),
                                                                                                  regularShippingConfigDto.getRegularShippingBasicDiscountRate());
        regularShippingSalePrice = discountCalculationResultDto.getDiscountAppliedPrice();
      }

      // 일일상품
      if (goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())) {

        // 일일상품 정보 조회
        GetSessionShippingResponseDto shippingAddress = userCertificationBiz.getSessionShipping();
        goodsDailyCycleList = goodsGoodsBiz.getGoodsDailyCycleList(ilGoodsId, goods.getGoodsDailyType(), shippingAddress.getReceiverZipCode(), shippingAddress.getBuildingCode());

        // 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면
        // isShippingPossibility 가능
		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(goods.getIlGoodsId(),
				buyerVo.getReceiverZipCode(), buyerVo.getBuildingCode());

		if (shippingPossibilityStoreDeliveryAreaInfo != null) {
			storeDeliveryInterval = shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType();
			isShippingPossibility = true;
		} else {
			isShippingPossibility = false;
		}
      } else {
        isShippingPossibility = !goodsShippingTemplateBiz.isUnDeliverableArea(goods.getUndeliverableAreaType(), buyerVo.getReceiverZipCode());
      }

      // 판매유형이 일일/베이비밀일 때
      if (goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())
          && goods.getGoodsDailyType().equals(GoodsEnums.GoodsDailyType.BABYMEAL.getCode())) {
        goodsDailyAllergyYn = goods.getGoodsDailyAllergyYn();
        goodsDailyBulkYn = goods.getGoodsDailyBulkYn();

        // 일괄배송 배송 세트
        goodsDailyBulkList = goodsGoodsBiz.getGoodsDailyBulkList(ilGoodsId);

      }

      long urUserIdL = 0L;
      if(!urUserId.equals("")){
        urUserIdL = Long.parseLong(urUserId);
      }

      // 쿠폰 최대 할인 금액 계산
      List<GoodsApplyCouponDto> goodsApplyCouponList = promotionCouponBiz.getGoodsApplyCouponList(ilGoodsId, urUserIdL);

      // 쿠폰 할인금액
      int couponDiscountPrice = 0;
      if (goodsApplyCouponList != null) {
          // 쿠폰 예외처리
          // 전체 발급수량이면서 본인 발급 아닌건 제외
          goodsApplyCouponList = goodsApplyCouponList.stream()
                  .filter(vo -> vo.getIssueQty() == 0||vo.getIssueQty() > vo.getAllIssueCount()||vo.getUserIssueCount() > 0)
                  .collect(Collectors.toList());

        List<Integer> goodsApplyCouponDiscountPriceList = new ArrayList<>();
        for (GoodsApplyCouponDto goodsApplyCouponDto : goodsApplyCouponList) {
          DiscountCalculationResultDto couponDiscountCalculation =
                                                                 promotionCouponBiz.discountCalculation(goods.getSalePrice(),
                                                                                                        goodsApplyCouponDto.getDiscountType(),
                                                                                                        goodsApplyCouponDto.getDiscountValue(),
                                                                                                        StringUtil.nvlInt(goodsApplyCouponDto.getPercentageMaxDiscountAmount()));
          goodsApplyCouponDiscountPriceList.add(couponDiscountCalculation.getDiscountPrice());
        }

        // 쿠폰 할인금액 중 할인금액이 가장 큰 쿠폰 할인금액 추출
        couponDiscountPrice =
                            goodsApplyCouponDiscountPriceList.isEmpty() ? 0 : Collections.max(goodsApplyCouponDiscountPriceList);
        // 일반회원 상품 결제 예정금액에서 쿠폰 할인금액 -
        buyerPaymentExpectedPrice -= couponDiscountPrice;
      }

      // 임직원 할인 정보 (렌탈은 제외)
      if (isEmployee && !GoodsEnums.GoodsType.RENTAL.getCode().equals(goods.getGoodsType())) {
    	List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo = policyBenefitEmployeeBiz
  					.getEmployeeDiscountBrandByUser(buyerVo.getUrErpEmployeeCode());

        // 임직원 할인율
		if (policyBenefitEmployeeInfo != null) {
			if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
				employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculationPackage(goods.getIlGoodsId(), policyBenefitEmployeeInfo, goodsPackageList, 1);
			} else {
				PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz
						.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, goods.getUrBrandId());

		        PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz
						.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo, goods.getUrBrandId());

		        if(policyBenefitEmployeeBrandByUserVo != null) {
					int employeeDiscountRatio = policyBenefitEmployeeBrandByUserVo.getDiscountRatio();
					int goodsEmployeeDiscountRatio = goodsGoodsBiz.getGoodsEmployeeDiscountRatio(goods.getIlGoodsId());
					if (goodsEmployeeDiscountRatio > 0) {
						employeeDiscountRatio = goodsEmployeeDiscountRatio;
					}
					employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio, BuyerConstants.EMPLOYEE_MAX_POINT, goods.getRecommendedPrice(), 1);
		        }
			}

			if(employeeDiscountInfoDto != null && employeeDiscountInfoDto.getDiscountPrice() > 0) {
				goods.setSalePrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
				discountRate = PriceUtil.getRate(goods.getRecommendedPrice(), goods.getSalePrice());
			}

			goodsGoodsBiz.employeeDiscountCalculationAddGoods(policyBenefitEmployeeInfo, additionalGoodsList);
		}
      }

      // 매장 배송일때
      isBuyStoreGoods = "Y".equals(goods.getSaleShopYn());
      if(isBuyStoreGoods) {

		if(GoodsEnums.GoodsType.SHOP_ONLY.getCode().equals(goods.getGoodsType())) {
	      	// 매장 전용 상품은 일반 택배 정보 안보여줘야함
	       	shippingDataList.remove(0);
	    }

    	ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(buyerVo.getReceiverZipCode(), buyerVo.getBuildingCode());

  		if (shippingPossibilityStoreDeliveryAreaInfo != null) {
  			isStoreShippingPossibility = true;

  			// 매장 배송 정책정보 추가
  	        ShippingDataResultDto shopShippingData = new ShippingDataResultDto();

  	        shopShippingData.setDeliveryType(GoodsEnums.DeliveryType.SHOP.getCode());
  	        shopShippingData.setDeliveryTypeName(GoodsEnums.DeliveryType.SHOP.getCodeName());
  	        shopShippingData.setStoreName(shippingPossibilityStoreDeliveryAreaInfo.getStoreName());
  	        shopShippingData.setShippingPriceText(getShippingData.getShippingPriceText());

  	        Long storeUrWarehouseId = goodsGoodsBiz.getBasicStoreWarehouseStoreId();
			List<ArrivalScheduledDateDto> storeArrivalScheduledDateDtoList = goodsGoodsBiz.getStoreArrivalScheduledDateDtoList(storeUrWarehouseId, shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId(), 99, 1);
			for (ArrivalScheduledDateDto storeArrivalScheduledDateDto : storeArrivalScheduledDateDtoList) {
				CartStoreShippingDateDto storeShippingDateDto = storeDeliveryBiz.convertCartStoreShippingDateDto(storeArrivalScheduledDateDto, shippingPossibilityStoreDeliveryAreaInfo);
				if(storeDeliveryBiz.isPossibleSelectStoreSchedule(storeShippingDateDto.getSchedule())) {
					shopShippingData.setArrivalScheduledDate(storeShippingDateDto.getArrivalScheduledDate());
					CartStoreShippingDateScheduleDto scheduleDto = storeShippingDateDto.getSchedule().stream().filter(dto -> dto.isPossibleSelect()).findAny().orElse(null);
					shopShippingData.setArrivalScheduledStartTime(scheduleDto.getStartTime());
					shopShippingData.setArrivalScheduledEndTime(scheduleDto.getEndTime());
					break;
				}
			}

  	        shippingDataList.add(shopShippingData);

  	        // 매장 정보
  	      ItemStoreInfoVo itemStoreInfo = goodsGoodsBiz.getItemStoreInfo(shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId(), goods.getIlItemCode());
  	      if(itemStoreInfo != null) {
  	    	  // 매장 재고
  	    	  storeStock = itemStoreInfo.getStoreStock();

  	    	  // 임직원 할인이 있을경우 매장 정보금액도 임직원 할인 정보로 처리
	  	      if(employeeDiscountInfoDto != null && employeeDiscountInfoDto.getDiscountPrice() > 0) {
	  	    	storeSalePrice = goods.getSalePrice();
	  	    	storeDiscountPrice = goods.getRecommendedPrice() - goods.getSalePrice();
	  	    	storePaymentExpectedPrice = storeSalePrice;
	  	      } else {
	  	    	  // 매장 가격
	  	    	  StorePriceDto storePriceDto = goodsGoodsBiz.getStoreSalePrice(goods.getDiscountType(), goods.getRecommendedPrice(), goods.getSalePrice(), itemStoreInfo.getStoreSalePrice());
	  	    	  storeSalePrice = storePriceDto.getSalePrice();
				  storeDiscountPrice = goods.getRecommendedPrice() - storeSalePrice;

				  List<Integer> goodsApplyCouponDiscountPriceList = new ArrayList<>();
			      for (GoodsApplyCouponDto goodsApplyCouponDto : goodsApplyCouponList) {
			          DiscountCalculationResultDto couponDiscountCalculation =
			                                                                 promotionCouponBiz.discountCalculation(storeSalePrice,
			                                                                                                        goodsApplyCouponDto.getDiscountType(),
			                                                                                                        goodsApplyCouponDto.getDiscountValue(),
			                                                                                                        StringUtil.nvlInt(goodsApplyCouponDto.getPercentageMaxDiscountAmount()));
			          goodsApplyCouponDiscountPriceList.add(couponDiscountCalculation.getDiscountPrice());
			      }
			        // 쿠폰 할인금액 중 할인금액이 가장 큰 쿠폰 할인금액 추출
			      storeCouponDiscountPrice = goodsApplyCouponDiscountPriceList.isEmpty() ? 0 : Collections.max(goodsApplyCouponDiscountPriceList);
				  storePaymentExpectedPrice = storeSalePrice - storeCouponDiscountPrice;
	  	      }
  	      }
  		} else {
  			isStoreShippingPossibility = false;
  		}
      }

      // 회원인 경우
      if (isMember) {
        // 찜 상품 정보
        spFavoritesGoodsId = shoppingFavoritesBiz.getGoodsFavorites(urUserId, ilGoodsId);

        // 최근 본 상품 추가 - 회원
        shoppingRecentryBiz.viewsGoods(UserEnums.UserStatusType.MEMBER, urUserId, "", ilGoodsId);

        // 신규회원 특가
        int newBuyerSpecialsCouponByUserMap = userBuyerBiz.getNewBuyerSpecialsCouponByUser(ilGoodsId,
                                                                                               urUserId,
                                                                                               DeviceUtil.getDirInfo(),
                                                                                               DeviceUtil.isApp());
        if (newBuyerSpecialsCouponByUserMap != 0) {
          isNewBuyerSpecials = true;
          newBuyerSpecialSsalePrice = newBuyerSpecialsCouponByUserMap;
        }

        // 비회원인 경우
      } else {

        // 최근 본 상품 추가 - 비회원
        shoppingRecentryBiz.viewsGoods(UserEnums.UserStatusType.NONMEMBER, "", urPcidCd, ilGoodsId);

        // 신규회원 특가
        int newBuyerSpecialsCouponByNonMemberMap = promotionCouponBiz.getNewBuyerSpecialsCouponByNonMember(ilGoodsId,
                                                                                                             DeviceUtil.getDirInfo(),
                                                                                                             DeviceUtil.isApp());
        if (newBuyerSpecialsCouponByNonMemberMap != 0) {
          isNewBuyerSpecials = true;
          newBuyerSpecialSsalePrice = newBuyerSpecialsCouponByNonMemberMap;
        }
      }

      // 상품후기 가져오기
      List<Long> ilGoodsList = new ArrayList<>();
      ilGoodsList.add(ilGoodsId);
      if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
          ilGoodsList.addAll(goodsPackageList.stream()
                  .map(PackageGoodsListDto::getIlGoodsId)
                  .collect(Collectors.toList()));
      }
      List<FeedbackScorePercentDto> feedbackScorePercentList = customerFeedbackBiz.getFeedbackScorePercentList(ilGoodsList);
      // 상품후기 각각의 총갯수 가져오기
      FeedbackEachCountDto feedbackEachCount = customerFeedbackBiz.getFeedbackEachCount(ilGoodsList, ilGoodsId);

      // 증정행사 정보 가져오기
      List<GiftListVo> giftResponseDto = promotionExhibitBiz.getGiftList(
                                                      GiftListRequestDto.builder()
                                                              .ilGoodsId(ilGoodsId)
                                                              .urBrandId(goods.getUrBrandId())
                                                              .dpBrandId(goods.getDpBrandId())
                                                              .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                                                              .urGroupId(buyerVo.getUrGroupId())
                                                              .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                                                              .giftType(ExhibitEnums.GiftType.GOODS.getCode())
                                                              .build());

      // 상품 긴급공지 정보 가져오기
      List<GoodsNoticeResponseDto> noticeResponseDto = goodsNoticeBiz.getGoodsNoticeListByUser(goods.getWarehouseGroupCode(), goods.getUrWareHouseId());

      // 쿠폰PK 암호화
      List<GoodsApplyCouponResponseDto> encryptCouponList = new ArrayList<>(
              goodsApplyCouponList.stream()
              .map(GoodsApplyCouponResponseDto::new)
              .collect(Collectors.toList())
      );

      // 카테고리정보 조회
      if(mallDiv == null) {
          mallDiv = GoodsEnums.MallDiv.PULMUONE.getCode();
      } else {
          switch (mallDiv) {
              case "orga":
                  mallDiv = GoodsEnums.MallDiv.ORGA.getCode();
                  break;
              case "eatslim":
                  mallDiv = GoodsEnums.MallDiv.EATSLIM.getCode();
                  break;
              case "babymeal":
                  mallDiv = GoodsEnums.MallDiv.BABYMEAL.getCode();
                  break;
              default:
                  mallDiv = GoodsEnums.MallDiv.PULMUONE.getCode();
                  break;
          }
      }
      String goodsCtgrPro = goodsCategoryBiz.getGoodsCategory(ilGoodsId, mallDiv, ilCtgryId);

      // 보관방법
      String storageMethodTypeName = "";
      if (!GoodsEnums.GoodsType.INCORPOREITY.getCode().equals(goodsDetail.getGoodsTp())) {
          storageMethodTypeName = goodsDetail.getStorageMethodTypeName();
      }

      GoodsPageInfoResponseDto goodsInfoResultDto =
                                                  GoodsPageInfoResponseDto.builder()
                                                                          .ilGoodsId(ilGoodsId)
                                                                          .goodsType(goods.getGoodsType())
                                                                          .saleType(goods.getSaleType())
                                                                          .goodsImage(goodsImage)
                                                                          .urSupplierId(goods.getUrSupplierId())
                                                                          .brandName(goodsDetail.getBrandName())
                                                                          .urBrandName(goodsDetail.getUrBrandName())
                                                                          .urBrandId(goods.getUrBrandId())
                                                                          .dpBrandId(goods.getDpBrandId())
                                                                          .goodsName(goods.getGoodsName())
                                                                          .goodsDesc(goodsDetail.getGoodsDescription())
                                                                          .recommendedPrice(goods.getRecommendedPrice())
                                                                          .salePrice(goods.getSalePrice())
                                                                          .discountRate(discountRate)
                                                                          .isBuyNonMember(isBuyNonMember)
                                                                          .isBuyMember(isBuyMember)
                                                                          .isBuyEmployee(isBuyEmployee)
	                                                                      .isBuyStoreGoods(isBuyStoreGoods)
                                                                          .spFavoritesGoodsId(spFavoritesGoodsId)
                                                                          .discountTypeName(goods.getDiscountTypeName())
                                                                          .discountPrice(discountPrice)
                                                                          .couponDiscountPrice(couponDiscountPrice)
                                                                          .buyerPaymentExpectedPrice(buyerPaymentExpectedPrice)
                                                                          .employeeDiscountPrice(employeeDiscountInfoDto.getDiscountPrice())
                                                                          .employeePaymentExpectedPrice(employeeDiscountInfoDto.getDiscountAppliedPrice())
                                                                          .storeSalePrice(storeSalePrice)
                                                                          .storeDiscountPrice(storeDiscountPrice)
                                                                          .storeCouponDiscountPrice(storeCouponDiscountPrice)
                                                                          .storePaymentExpectedPrice(storePaymentExpectedPrice)
                                                                          .isRecommendedGoods(goodsDetail.getIsRecommendedGoods())
                                                                          .isNewGoods(goodsDetail.getIsNewGoods())
                                                                          .isBestGoods(goodsDetail.getIsBestGoods())
                                                                          .coupon(encryptCouponList)
                                                                          .certification(getGoodsCertificationList)
                                                                          .delivery(shippingDataList)
                                                                          .storageMethodTypeName(storageMethodTypeName)
                                                                          .isGift(giftResponseDto.size() > 0)
                                                                          .gift(giftResponseDto.stream().map(GiftListResponseDto::new).collect(Collectors.toList()))
                                                                          .notice(noticeResponseDto)
                                                                          .noticeBelow1ImgUrl(goodsDetail.getNoticeBelow1ImageUrl())
                                                                          .noticeBelow2ImgUrl(goodsDetail.getNoticeBelow2ImageUrl())
                                                                          .saleStatus(goods.getSaleStatus())
                                                                          .basicDescription(goodsDetail.getBasicDescription())
                                                                          .detailDescription(goodsDetail.getDetailDescription())
                                                                          .recommendedGoodsList(recommendedGoodsList)
                                                                          .videoAutoPlayYn(goodsDetail.getVideoAutoplayYn())
                                                                          .videoUrl(goodsDetail.getVideoUrl())
                                                                          .limitMinCnt(goods.getLimitMinimumCount())
                                                                          .limitMaxCntYn(goods.getLimitMaximumCountYn())
                                                                          .limitMaxType(goods.getLimitMaximumType())
                                                                          .limitMaxCnt(limitMaxCnt)
                                                                          .systemLimitMaxCnt(systemLimitMaxCnt)
                                                                          .stockQty(stock)
                                                                          .storeStockQty(storeStock)
                                                                          .spec(getGoodsSpecList)
                                                                          .nutritionDispYn(goodsDetail.getNutritionDisplayYn())
                                                                          .nutrition(goodsNutritionList)
                                                                          .nutritionQtyPerOnce(goodsDetail.getNutritionQtyPerOnce())
                                                                          .nutritionQtyTotal(goodsDetail.getNutritionQtyTotal())
                                                                          .nutritionEtc(goodsDetail.getNutritionEtc())
                                                                          .nutritionDispDefault(goodsDetail.getNutritionDisplayDefault())
                                                                          .claimDescription(goodsDetail.getClaimDescription())
                                                                          .additionalGoodsList(additionalGoodsList)
                                                                          .goodsPackageBasicDescriptionYn(goodsDetail.getGoodsPackageBasicDescYn())
                                                                          .goodsPackageBasicDescription(goodsDetail.getGoodsPackageBasicDesc())
                                                                          .goodsPackageList(goodsPackageList)
                                                                          .reserveOption(reserveOptionList)
                                                                          .regularShippingSalePrice(regularShippingSalePrice)
                                                                          .regularShippingBasicDiscountRate(regularShippingConfigDto.getRegularShippingBasicDiscountRate())
                                                                          .regularShippingAdditionalDiscountApplicationTimes(regularShippingConfigDto.getRegularShippingAdditionalDiscountApplicationTimes())
                                                                          .regularShippingAdditionalDiscountRate(regularShippingConfigDto.getRegularShippingAdditionalDiscountRate())
                                                                          .goodsDailyType(goods.getGoodsDailyType())
                                                                          .goodsDailyCycle(goodsDailyCycleList)
                                                                          .goodsDailyAllergyYn(goodsDailyAllergyYn)
                                                                          .goodsDailyBulkYn(goodsDailyBulkYn)
                                                                          .goodsDailyBulk(goodsDailyBulkList)
                                                                          .isShippingPossibility(isShippingPossibility)
                                                                          .isStoreShippingPossibility(isStoreShippingPossibility)
                                                                          .isNewBuyerSpecials(isNewBuyerSpecials)
                                                                          .newBuyerSpecialSsalePrice(newBuyerSpecialSsalePrice)
                                                                          .couponUseYn(goods.getCouponUseYn())
                                                                          .feedbackScorePercentList(feedbackScorePercentList)
                                                                          .feedbackTotalCount(feedbackEachCount.getFeedbackTotalCount())
                                                                          .satisfactionScore(feedbackEachCount.getSatisfactionScore())
                                                                          .satisfactionCount(feedbackEachCount.getSatisfactionCount())
                                                                          .qnaTotalCount(feedbackEachCount.getQnaTotalCount())
                                                                          .isDealGoods(displayContentsBiz.isDealGoods(ilGoodsId))
                                                                          .healthGoodsYn(goods.getHealthGoodsYn())
                                                                          .rentalFeePerMonth(goodsDetail.getRentalFeePerMonth())
                                                                          .rentalDueMonth(goodsDetail.getRentalDueMonth())
                                                                          .rentalDeposit(goodsDetail.getRentalDeposit())
                                                                          .rentalInstallFee(goodsDetail.getRentalInstallFee())
                                                                          .rentalRegistFee(goodsDetail.getRentalRegistFee())
                                                                          .storeDeliveryInterval(storeDeliveryInterval)
                                                                          .isGreenjuiceParcel(goods.isGreenjuiceParcel())
                                                                          .presentYn(goods.getPresentYn())
                                                                          .meal(goodsGoodsBiz.getGoodsPageInfoMeal(ilGoodsId, goods.getArrivalScheduledDateDtoList()))
                                                                          .goodsCtgrPro(goodsCtgrPro)
                                                                          .build();

      return ApiResult.success(goodsInfoResultDto);
    } else {

      HashMap<String, String> goodsDisplayInfo = goodsGoodsBiz.getGoodsDisplayInfo(goodsRequestDto);
      List<String> displayDeviceList = new ArrayList<>();

      if (goodsDisplayInfo == null) {

        return ApiResult.result(GoodsEnums.Goods.NO_GOODS);
      } else {
        // 구매 허용범위 제한 상품일 경우(일반, 임직원 전용, 비회원)
        if (!isMember) {
          if ("N".equals(goodsDisplayInfo.get("PURCHASE_NONMEMBER_YN"))) {
            if ("Y".equals(goodsDisplayInfo.get("PURCHASE_MEMBER_YN"))) {
              return ApiResult.result("회원", GoodsEnums.Goods.LIMIT_PURCHASE);
            }
            if ("Y".equals(goodsDisplayInfo.get("PURCHASE_EMPLOYEE_YN"))) {
              return ApiResult.result("임직원", GoodsEnums.Goods.LIMIT_PURCHASE);
            }
          }
        } else {
          if ("N".equals(goodsDisplayInfo.get("PURCHASE_MEMBER_YN"))) {
            if ("Y".equals(goodsDisplayInfo.get("PURCHASE_NONMEMBER_YN"))) {
              return ApiResult.result("비회원", GoodsEnums.Goods.LIMIT_PURCHASE);
            }
            if ("Y".equals(goodsDisplayInfo.get("PURCHASE_EMPLOYEE_YN"))) {
              return ApiResult.result("임직원", GoodsEnums.Goods.LIMIT_PURCHASE);
            }
          }
        }
        if (isEmployee) {
          if ("N".equals(goodsDisplayInfo.get("PURCHASE_EMPLOYEE_YN"))
              && "Y".equals(goodsDisplayInfo.get("PURCHASE_NONMEMBER_YN"))) {
            return ApiResult.result("비회원", GoodsEnums.Goods.LIMIT_PURCHASE);
          }
        }

        // 판매 허용범위 제한 상품일 경우(PC Web, Mobile Web, APP)
        if (DeviceUtil.isApp()) {
          if ("Y".equals(goodsDisplayInfo.get("DISP_WEB_PC_YN"))) {
            displayDeviceList.add(GoodsEnums.DeviceType.PC.getCode());
          }
          if ("Y".equals(goodsDisplayInfo.get("DISP_WEB_MOBILE_YN"))) {
            displayDeviceList.add(GoodsEnums.DeviceType.MOBILE.getCode());
          }
        } else {
          if (DeviceUtil.getDirInfo().equalsIgnoreCase(GoodsEnums.DeviceType.PC.getCode())) {
            if ("Y".equals(goodsDisplayInfo.get("DISP_WEB_MOBILE_YN"))) {
              displayDeviceList.add(GoodsEnums.DeviceType.MOBILE.getCode());
            }
            if ("Y".equals(goodsDisplayInfo.get("DISP_APP_YN"))) {
              displayDeviceList.add(GoodsEnums.DeviceType.APP.getCode());
            }
          } else if (DeviceUtil.getDirInfo().equalsIgnoreCase(GoodsEnums.DeviceType.MOBILE.getCode())) {
            if ("Y".equals(goodsDisplayInfo.get("DISP_WEB_PC_YN"))) {
              displayDeviceList.add(GoodsEnums.DeviceType.PC.getCode());
            }
            if ("Y".equals(goodsDisplayInfo.get("DISP_APP_YN"))) {
              displayDeviceList.add(GoodsEnums.DeviceType.APP.getCode());
            }
          }
        }
        return ApiResult.result(displayDeviceList, GoodsEnums.Goods.LIMIT_DISPLAY);
      }
    }
  }

  /**
   * 장바구니 옵션 항목 조회
   *
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  @Override
  public ApiResult<?> getOptionInfo(String cartType, Long ilGoodsId, String employeeYn) throws Exception {
    GetOptionInfoResponseDto getOptionInfoResponseDto = new GetOptionInfoResponseDto();
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
    boolean isMember = StringUtil.isNotEmpty(urUserId);
    boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
    int systemLimitMaxCnt = 0;
    int limitMaxCnt = 0;
    int orderGoodsBuyQty = 0;
    List<GoodsReserveOptionListDto> reserveOptionList = new ArrayList<>();
    String goodsDailyAllergyYn = "";
    String goodsDailyBulkYn = "";
    List<HashMap<String, String>> goodsDailyBulkList = new ArrayList<>();
    List<GoodsDailyCycleDto> goodsDailyCycleList = new ArrayList<>();
    int stock = 0;
    int storeStock = 0;
    String storeDeliveryInterval = "";

    GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder()
                                                     .ilGoodsId(ilGoodsId)
                                                     .deviceInfo(DeviceUtil.getDirInfo())
                                                     .isApp(DeviceUtil.isApp())
                                                     .isMember(isMember)
                                                     .isEmployee(isEmployee)
                                                     .isDawnDelivery(false)
                                                     .build();

    // 상품 기본 정보
    BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

    if (goods != null) {

      // 상품 재고 정보
      stock = goods.getStockQty();

      // 최대구매 가능수량
      // 최대구매수량 제한있을 때
      if (goods.getLimitMaximumCountYn().equals("Y")) {
    	systemLimitMaxCnt = goods.getLimitMaximumCount();
        orderGoodsBuyQty = orderOrderBiz.getOrderGoodsBuyQty(ilGoodsId, urUserId);
        limitMaxCnt = systemLimitMaxCnt - orderGoodsBuyQty;
        if (limitMaxCnt < 0)
          limitMaxCnt = 0;
      }

      // 추가구성 상품
      List<AdditionalGoodsDto> additionalGoodsList = goodsGoodsBiz.getAdditionalGoodsInfoList(ilGoodsId,
                                                                                              isMember,
                                                                                              isEmployee,
                                                                                              false,
                                                                                              null);

   // 묶음상품일 경우
      if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
    	  List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(ilGoodsId, isMember, isEmployee, false, null, 1);

        RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);

        stock = recalculationPackage.getStockQty();
        goods.setArrivalScheduledDateDto(recalculationPackage.getArrivalScheduledDateDto());
        goods.setSaleStatus(recalculationPackage.getSaleStatus());
      }

      // 예약상품일 경우
      if (goods.getSaleType().equals(GoodsEnums.SaleType.RESERVATION.getCode())) {
        reserveOptionList = goodsGoodsBiz.getGoodsReserveOptionList(ilGoodsId);

        // 예약상품 모든 판매회차의 재고가 0이거나 예약주문시간이 지났을때, 상품 판매상태 일시품절로 수정
        int allGoodsStock = reserveOptionList.stream().map(m -> m.getStockQty()).mapToInt(Integer::intValue).sum();

//        LocalDateTime currentDate = LocalDateTime.now();
//        boolean allMatch = reserveOptionList.stream().allMatch(f -> currentDate.isAfter(f.getReserveEndDate()));
        stock = allGoodsStock;
//        if (allGoodsStock == 0 || allMatch) {
//          goods.setSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
//        }
      }

      // 일일상품
      if (goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())) {

        // 일일상품 정보 조회
        GetSessionShippingResponseDto shippingAddress = userCertificationBiz.getSessionShipping();
        goodsDailyCycleList = goodsGoodsBiz.getGoodsDailyCycleList(ilGoodsId, goods.getGoodsDailyType(), shippingAddress.getReceiverZipCode(), shippingAddress.getBuildingCode());

        // 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면
        // isShippingPossibility 가능
        ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(goods.getIlGoodsId(),
        		buyerVo.getReceiverZipCode(), buyerVo.getBuildingCode());

        if (shippingPossibilityStoreDeliveryAreaInfo != null) {
        	storeDeliveryInterval = shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType();
        	getOptionInfoResponseDto.setIsShippingPossibility(true);
        }else {
        	getOptionInfoResponseDto.setIsShippingPossibility(false);
        }

      }

      // 판매유형이 일일/베이비밀일 때
      if (goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())
          && goods.getGoodsDailyType().equals(GoodsEnums.GoodsDailyType.BABYMEAL.getCode())) {
        goodsDailyAllergyYn = goods.getGoodsDailyAllergyYn();
        goodsDailyBulkYn = goods.getGoodsDailyBulkYn();

        // 일괄배송 배송 세트
        goodsDailyBulkList = goodsGoodsBiz.getGoodsDailyBulkList(ilGoodsId);

      }

      // 임직원 할인 정보
      if (employeeYn!= null && !"N".equals(employeeYn)) {
    	List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo = policyBenefitEmployeeBiz.getEmployeeDiscountBrandByUser(buyerVo.getUrErpEmployeeCode());

        // 임직원 할인율
		if (policyBenefitEmployeeInfo != null) {
			goodsGoodsBiz.employeeDiscountCalculationAddGoods(policyBenefitEmployeeInfo, additionalGoodsList);
		}
      }

      // 매장 배송일때
      if(ShoppingEnums.CartType.SHOP.getCode().equals(cartType)) {

    	ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(buyerVo.getReceiverZipCode(), buyerVo.getBuildingCode());

  		if (shippingPossibilityStoreDeliveryAreaInfo != null) {

  	        // 매장 정보
  	      ItemStoreInfoVo itemStoreInfo = goodsGoodsBiz.getItemStoreInfo(shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId(), goods.getIlItemCode());
  	      if(itemStoreInfo != null) {
  	    	  // 매장 재고
  	    	  storeStock = itemStoreInfo.getStoreStock();
  	      }
  		}
      }

      getOptionInfoResponseDto.setGoodsType(goods.getGoodsType());
      getOptionInfoResponseDto.setSaleType(goods.getSaleType());
      getOptionInfoResponseDto.setSaleStatus(goods.getSaleStatus());
      getOptionInfoResponseDto.setLimitMinCnt(goods.getLimitMinimumCount());
      getOptionInfoResponseDto.setLimitMaxCntYn(goods.getLimitMaximumCountYn());
      getOptionInfoResponseDto.setLimitMaxType(goods.getLimitMaximumType());
      getOptionInfoResponseDto.setLimitMaxCnt(limitMaxCnt);
      getOptionInfoResponseDto.setSystemLimitMaxCnt(systemLimitMaxCnt);
      getOptionInfoResponseDto.setStockQty(stock);
      getOptionInfoResponseDto.setStoreStockQty(storeStock);
      getOptionInfoResponseDto.setAdditionalGoodsList(additionalGoodsList);
      getOptionInfoResponseDto.setReserveOption(reserveOptionList);
      getOptionInfoResponseDto.setGoodsDailyType(goods.getGoodsDailyType());
      getOptionInfoResponseDto.setGoodsDailyCycle(goodsDailyCycleList);
      getOptionInfoResponseDto.setGoodsDailyAllergyYn(goodsDailyAllergyYn);
      getOptionInfoResponseDto.setGoodsDailyBulkYn(goodsDailyBulkYn);
      getOptionInfoResponseDto.setGoodsDailyBulk(goodsDailyBulkList);
      getOptionInfoResponseDto.setStoreDeliveryInterval(storeDeliveryInterval);
    }

    return ApiResult.success(getOptionInfoResponseDto);
  }

  /**
   * 상품 후기 이미지 목록 조회
   *
   * @param dto
   * @return ApiResult<?>
   * @throws Exception
   */
  @Override
  @UserMaskingRun(system = "MALL")
  public ApiResult<?> getFeedbackImageListByGoods(FeedbackImageListByGoodsRequestDto dto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo != null && !StringUtil.isEmpty(buyerVo.getUrUserId())) {
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
    }
    dto.setMember(buyerVo != null && StringUtil.isNotEmpty(buyerVo.getUrUserId()));
    dto.setEmployee(buyerVo != null && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode()));
    return ApiResult.success(customerFeedbackBiz.getFeedbackImageListByGoods(dto));
  }

  /**
   * 쿠폰다운로드 - 상품상세
   *
   * @param dto CouponByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  public ApiResult<?> addCouponByGoods(CouponByGoodsListRequestDto dto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    }

    List<String> encryptList = dto.getCoupon().stream()
                  .map(CouponByGoodsRequestDto::getPmCouponId)
                  .collect(Collectors.toList());

    List<Long> pmCouponIdList = systemCodeBiz.decryptStr(encryptList).stream()
                  .filter(Objects::nonNull)
                  .filter(StringUtil::isDigits)
                  .map(Long::parseLong)
                  .collect(Collectors.toList());

    return promotionCouponBiz.addCouponByList(pmCouponIdList, Long.valueOf(buyerVo.getUrUserId()));
  }

  /**
   * 상품 문의 목록 조회 - 상품상세
   *
   * @param dto ProductQnaListByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  @UserMaskingRun(system = "MALL")
  public ApiResult<?> getProductQnaListByGoods(ProductQnaListByGoodsRequestDto dto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo != null && !StringUtil.isEmpty(buyerVo.getUrUserId())) {
    	dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
    }

    return ApiResult.success(customerQnaBiz.getProductQnaListByGoods(dto));
  }

  /**
   * 상품 문의 유형 목록 - 상품상세
   *
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  public ApiResult<?> getProductType() throws Exception {
    return ApiResult.success(userBuyerBiz.getCommonCode("QNA_PRODUCT_TP"));
  }

  /**
   * 상품 문의 작성 - 상품상세
   *
   * @Param dto ProductQnaRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  @UserMaskingRun(system = "MALL")
  public ApiResult<?> addProductQna(ProductQnaRequestDto dto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    }
    dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

    // ECS 연동 위한 회원 정보 set
    dto.setUserName(buyerVo.getUserName());
    dto.setUserMobile(buyerVo.getUserMobile());
    dto.setUserEmail(buyerVo.getUserEmail());

    return customerQnaBiz.addProductQna(dto);
  }

  /**
   * 상품 문의 상세 조회 - 상품상세
   *
   * @Param csQnaId Long
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  public ApiResult<?> getProductQnaByGoods(Long csQnaId) throws Exception {
    ProductQnaByGoodsResponseDto result = new ProductQnaByGoodsResponseDto();
    result.setList(userBuyerBiz.getCommonCode("QNA_PRODUCT_TP"));
    result.setProductQna(customerQnaBiz.getProductQnaByGoods(csQnaId));
    return ApiResult.success(result);
  }

  /**
   * 상품 문의 수정 - 상품상세
   *
   * @Param vo ProductQnaVo
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  public ApiResult<?> putProductQna(ProductQnaVo vo) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    }
    vo.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
    return customerQnaBiz.putProductQna(vo);
  }

  /**
   * 상품 후기 목록 조회
   *
   * @param dto
   * @return ApiResult<?>
   * @throws Exception
   */
  @Override
  @UserMaskingRun(system = "MALL")
  public ApiResult<?> getFeedbackListByGoods(FeedbackListByGoodsRequestDto dto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo != null && !StringUtil.isEmpty(buyerVo.getUrUserId())) {
      dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
    }
    dto.setMember(buyerVo != null && StringUtil.isNotEmpty(buyerVo.getUrUserId()));
    dto.setEmployee(buyerVo != null && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode()));
    return ApiResult.success(customerFeedbackBiz.getFeedbackListByGoods(dto));
  }

  /**
   * 상품 상세 후기 추천
   *
   * @param dto FeedbackSetBestRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  public ApiResult<?> putFeedbackSetBestYes(FeedbackSetBestRequestDto dto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    }
    dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
    return customerFeedbackBiz.putFeedbackSetBestYes(dto);
  }

  /**
   * 상품 상세 후기 추천 취소
   *
   * @param dto FeedbackSetBestRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @Override
  public ApiResult<?> putFeedbackSetBestNo(FeedbackSetBestRequestDto dto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    }
    dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
    return customerFeedbackBiz.putFeedbackSetBestNo(dto);
  }

  /**
   * 식단 스케줄 조회
   */
  @Override
	public ApiResult<?> getGoodsDailyMealSchedule(GoodsDailyMealScheduleRequestDto reqDto) throws Exception {
		return ApiResult.success(goodsGoodsBiz.getGoodsDailyMealSchedule(reqDto));
	}

  /**
   * 식단 컨텐츠 상세 조회
   */
  @Override
	public ApiResult<?> getGoodsDailyMealContents(String ilGoodsDailyMealContsCd) throws Exception {
	  return ApiResult.success(goodsGoodsBiz.getGoodsDailyMealContents(ilGoodsDailyMealContsCd));
	}
}
