package kr.co.pulmuone.v1.shopping.cart.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.comm.constants.OrderShippingConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.NonDeliveryAreaInfoVo;
import kr.co.pulmuone.v1.policy.shiparea.service.PolicyShipareaBiz;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDailyType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.PresentYn;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.StoreDeliveryType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.ApplyPayment;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.BuyPurchaseType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.CartEmployeeYn;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.CartPromotionType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.CartType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.DeliveryType;
import kr.co.pulmuone.v1.comm.enums.StoreEnums.StoreApiDeliveryIntervalCode;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.EmployeeDiscountInfoDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsImageDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageEmployeeDiscountDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.goods.goods.dto.RecalculationPackageDto;
import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularBiz;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;
import kr.co.pulmuone.v1.policy.benefit.service.PolicyBenefitEmployeeBiz;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.SelectExhibitResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftGoodsVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftListVo;
import kr.co.pulmuone.v1.promotion.exhibit.service.PromotionExhibitBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoAdditionalGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartPickGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.ArrivalGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartAdditionalGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartCouponApplicationGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartDeliveryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDiscountDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsPackageDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartPickGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateScheduleDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingFastestScheduleDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartTypeSummaryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.ChangeArrivalScheduledDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckCartGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckCartResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartAddGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataBuyerChoiceShippingDataResposeDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataGoodsDataListResposeDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataGoodsSummeryResposeDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataShippingCouponResposeDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetGetCartDataShippingPriceResposeDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetSaveShippingCostGoodsListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.SpCartPickGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.UseGoodsCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.UseShippingCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.CartDeliveryTypeGroupByVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.ShippingTemplateGroupByVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartAddGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartPickGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.shopping.favorites.service.ShoppingFavoritesBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.dto.StoreDeliveryScheduleDto;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShoppingCartBizImpl implements ShoppingCartBiz {

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private ShoppingFavoritesBiz shoppingFavoritesBiz;

	@Autowired
	private UserBuyerBiz userBuyerBiz;

	@Autowired
	private PromotionCouponBiz promotionCouponBiz;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
	private StoreDeliveryBiz storeDeliveryBiz;

	@Autowired
	private PolicyConfigBiz policyConfigBiz;

	@Autowired
	private OrderRegularBiz orderRegularBiz;

	@Autowired
	private GoodsSearchBiz goodsSearchBiz;

	@Autowired
	private PromotionExhibitBiz promotionExhibitBiz;

	@Autowired
	private PolicyBenefitEmployeeBiz policyBenefitEmployeeBiz;

	@Autowired
	private OrderFrontBiz orderFrontBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;

	@Autowired
	private PolicyShipareaBiz policyShipareaBiz;

	public static final int CART_MAX_COUNT = 99;	// 장바구니 담기 최대 허용 개수

	/**
	 * 배송비 절약 상품 목록
	 *
	 * @param GetSaveShippingCostGoodsListRequestDto
	 * @return List<GoodsSearchResultDto>
	 * @throws Exception
	 */
	@Override
	public List<GoodsSearchResultDto> getSaveShippingCostGoodsList(
			GetSaveShippingCostGoodsListRequestDto getSaveShippingCostGoodsListRequestDto) throws Exception {
		List<GoodsSearchResultDto> result = new ArrayList<>();

		// 1. 배송비 절약 상품ID 리스트 조회
		List<Long> saveShippingCostGoodsList = shoppingCartService
				.getSaveShippingCostGoodsList(getSaveShippingCostGoodsListRequestDto);

		// 2. 조회된 상품ID로 상품검색
		if (!saveShippingCostGoodsList.isEmpty()) {
			String goodsSortCode = getSaveShippingCostGoodsListRequestDto.getSortCode() != null
					? getSaveShippingCostGoodsListRequestDto.getSortCode().name()
					: "";

			String urStoreId = null;
			if (getSaveShippingCostGoodsListRequestDto.getStoreDeliveryInfo() != null) {
				urStoreId = getSaveShippingCostGoodsListRequestDto.getStoreDeliveryInfo().getUrStoreId();
			}

			GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
					.goodsIdList(saveShippingCostGoodsList).urStoreId(urStoreId).goodsSortCode(goodsSortCode).build();

			result = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);
		}

		return result;
	}

	/**
	 * 장바구니 수정
	 *
	 * @param PutCartRequestDto
	 * @throws Exception
	 */
	@Override
	public int putCart(PutCartRequestDto putCartRequestDto) throws Exception {

		Long spCartId = null;
		boolean merge = false;

		// 예약 배송일때
		if (ShoppingEnums.DeliveryType.RESERVATION.getCode().equals(putCartRequestDto.getDeliveryType())) {

			// 기존 장바구니에 같은 회차 상품 담겨 있는지 조회
			spCartId = shoppingCartService.getCartIdByIlGoodsId(putCartRequestDto.getUrPcidCd(),
					putCartRequestDto.getUrUserId(), putCartRequestDto.getIlGoodsId(),
					putCartRequestDto.getIlGoodsReserveOptionId(), putCartRequestDto.getDeliveryType());

			//조회 했을때 내가 수정하려는 pk 와 다르면 merge
			if (spCartId != null && !putCartRequestDto.getSpCartId().equals(spCartId)) {
				merge = true;
			}
		}

		if (merge) {

			// 장바구니 수량 +
			shoppingCartService.putCartPlusQty(spCartId, putCartRequestDto.getQty());

			// 추가 구성 상품 등록
			List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList = putCartRequestDto.getAdditionalGoodsList();
			if (additionalGoodsList != null && additionalGoodsList.size() > 0) {
				for (AddCartInfoAdditionalGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {
					// 기존 추가 구성상품 담겨져 있는지 조회
					Long spCartAddGoodsId = shoppingCartService.getCartAddGoodsIdByIlGoodsId(spCartId,
							additionalGoodsRequestDto.getIlGoodsId());
					if (spCartAddGoodsId == null) {
						// 없으면 새로 등록
						SpCartAddGoodsVo spCartAddGoodsVo = new SpCartAddGoodsVo();
						spCartAddGoodsVo.setSpCartId(spCartId);
						spCartAddGoodsVo.setIlGoodsId(additionalGoodsRequestDto.getIlGoodsId());
						spCartAddGoodsVo.setQty(additionalGoodsRequestDto.getQty());
						shoppingCartService.addCartAddGoods(spCartAddGoodsVo);
					} else {
						// 있으면 장바구니 추가 구성 수량 +
						shoppingCartService.putCartAddGoodsPlusQty(spCartAddGoodsId,
								additionalGoodsRequestDto.getQty());
					}
				}
			}

			// 수정 전 장바구니 추가 구성 상품 삭제
			shoppingCartService.delCartAddGoodsBySpCartId(putCartRequestDto.getSpCartId());
			// 수정 전 장바구니 삭제
			shoppingCartService.delCart(putCartRequestDto.getSpCartId());

		} else {

			// 장바구니 수정
			SpCartVo spCartVo = new SpCartVo();
			spCartVo.setSpCartId(putCartRequestDto.getSpCartId());
			spCartVo.setQty(putCartRequestDto.getQty());
			spCartVo.setIlGoodsReserveOptionId(putCartRequestDto.getIlGoodsReserveOptionId());
			spCartVo.setGoodsDailyCycleType(putCartRequestDto.getGoodsDailyCycleType());
			spCartVo.setGoodsDailyCycleTermType(putCartRequestDto.getGoodsDailyCycleTermType());
			if (putCartRequestDto.getGoodsDailyCycleGreenJuiceWeekType() != null) {
				spCartVo.setGoodsDailyCycleGreenJuiceWeekType(
						String.join(",", putCartRequestDto.getGoodsDailyCycleGreenJuiceWeekType()));
			}
			spCartVo.setGoodsDailyAllergyYn(putCartRequestDto.getGoodsDailyAllergyYn());
			spCartVo.setGoodsDailyBulkYn(putCartRequestDto.getGoodsDailyBulkYn());
			spCartVo.setGoodsBulkType(putCartRequestDto.getGoodsBulkType());

			// 일일배송상품이 장바구니에서 배송유형을 변경한 경우 -> 이전 프로세스에서 이미 배송권역에 따른 배송타입 처리하고있어서 주석처리
//			if (putCartRequestDto.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())) {
//				if ("Y".equals(putCartRequestDto.getGoodsDailyBulkYn())) {
//					spCartVo.setDeliveryType(ShoppingEnums.DeliveryType.NORMAL.getCode());
//				} else {
//					spCartVo.setDeliveryType(ShoppingEnums.DeliveryType.DAILY.getCode());
//				}
//			}

			shoppingCartService.putCart(spCartVo);

			// 기존 장바구니PK의 추가 구성 상품 삭제
			shoppingCartService.delCartAddGoodsBySpCartId(putCartRequestDto.getSpCartId());

			// 추가 구성 상품
			List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList = putCartRequestDto.getAdditionalGoodsList();
			if (additionalGoodsList != null && additionalGoodsList.size() > 0) {

				// 추가 구성 상품 새로 추가
				for (AddCartInfoAdditionalGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {
					SpCartAddGoodsVo spCartAddGoodsVo = new SpCartAddGoodsVo();
					spCartAddGoodsVo.setSpCartId(putCartRequestDto.getSpCartId());
					spCartAddGoodsVo.setIlGoodsId(additionalGoodsRequestDto.getIlGoodsId());
					spCartAddGoodsVo.setQty(additionalGoodsRequestDto.getQty());
					shoppingCartService.addCartAddGoods(spCartAddGoodsVo);
				}
			}
		}

		return 1;
	}

	/**
	 * 장바구니 삭제
	 *
	 * @param DelCartRequestDto
	 * @throws Exception
	 */
	@Override
	public int delCartAndAddGoods(DelCartRequestDto delCartRequestDto) throws Exception {
		for (Long spCartId : delCartRequestDto.getSpCartId()) {
			log.info("spCartId=========>{}", spCartId);
			shoppingCartService.delCartAddGoodsBySpCartId(spCartId);
			shoppingCartService.delCartPickGoodsBySpCartId(spCartId);
			shoppingCartService.delCart(spCartId);
		}

		return 1;
	}

	/**
	 * 장바구니 추가 구성상품 삭제
	 *
	 * @param DelCartAddGoodsRequestDto
	 * @throws Exception
	 */
	@Override
	public int delCartAddGoods(DelCartAddGoodsRequestDto delCartAddGoodsRequestDto) throws Exception {
		for (Long spCartAddGoodsId : delCartAddGoodsRequestDto.getSpCartAddGoodsId()) {
			log.info("spCartAddGoodsId=========>{}", spCartAddGoodsId);

			shoppingCartService.delCartAddGoods(spCartAddGoodsId);
		}

		return 1;
	}

	/**
	 * 장바구니 타입별 정보
	 *
	 * @param GetCartDataRequestDto
	 * @return List<CartTypeSummaryDto>
	 * @throws Exception
	 */
	@Override
	public List<CartTypeSummaryDto> getCartTypeSummary(GetCartDataRequestDto getCartDataRequestDto) throws Exception {
		List<CartTypeSummaryDto> cartTypeSummaryList = new ArrayList<>();

		// 내 장바구니 spCartId 목록 조회
		List<Long> spCartIds = shoppingCartService.getCartIdList(getCartDataRequestDto);

		for (ShoppingEnums.CartType cartType : ShoppingEnums.CartType.values()) {
			if (!cartType.getCode().equals(CartType.RENTAL.getCode()) && !cartType.getCode().equals(CartType.INCORPOREITY.getCode())) {
				CartTypeSummaryDto cartTypeSummaryDto = new CartTypeSummaryDto();
				cartTypeSummaryDto.setCartType(cartType.getCode());
				cartTypeSummaryDto.setCartTypeName(cartType.getCodeName());
				if (spCartIds.size() > 0) {
					cartTypeSummaryDto
							.setTotal(shoppingCartService.getCartTypeSummary(cartType.getDeliveryTypeList(), spCartIds));
				} else {
					cartTypeSummaryDto.setTotal(0);
				}
				cartTypeSummaryList.add(cartTypeSummaryDto);
			}
		}

		return cartTypeSummaryList;
	}

	/**
	 * 장바구니 수량 정보
	 */
	@Override
	public int getCartCount(GetCartDataRequestDto getCartDataRequestDto) throws Exception {
		//장바구니 정보 중 일반배송으로 담긴 장바구니 품목만 카운트
		List<CartTypeSummaryDto> cartTypeSummary = getCartTypeSummary(getCartDataRequestDto);
		int cartCount = 0;
		if (cartTypeSummary != null) {
			for (CartTypeSummaryDto cartTypeDto : cartTypeSummary) {
				if (cartTypeDto.getCartType().equals(ShoppingEnums.CartType.NORMAL.getCode())) {
					cartCount = cartTypeDto.getTotal();
				}
			}
		}
		return cartCount;
	}

	/**
	 * 로그인 장바구니 머지
	 */
	@Override
	public void loginCartMerge(String urPcidCd, Long urUserId) throws Exception {
		List<SpCartVo> voList = shoppingCartService.getMergeCartListByUrUserId(urUserId);
		shoppingCartService.putCartUrUserId(urPcidCd, urUserId);
		if (voList != null && !voList.isEmpty()) {
			HashMap<Long, List<SpCartAddGoodsVo>> addVoListMap = new HashMap<Long, List<SpCartAddGoodsVo>>();

			// 기존 담긴 장바구니 삭제
			DelCartRequestDto delDto = new DelCartRequestDto();
			// 추가 구성상품 삭제 안되도록 처리 해야함
			for (SpCartVo vo : voList) {
				List<SpCartAddGoodsVo> addVoList = shoppingCartService.getCartAddGoodsIdList(vo.getSpCartId());
				addVoListMap.put(vo.getSpCartId(), addVoList);
			}

			delDto.setSpCartId(voList.stream().map(SpCartVo::getSpCartId).collect(Collectors.toList()));
			delCartAndAddGoods(delDto);
			//새로 장바구니 추가
			for (SpCartVo vo : voList) {
				AddCartInfoRequestDto dto = new AddCartInfoRequestDto();
				dto.setUrPcidCd(urPcidCd);
				dto.setPmAdExternalCd(vo.getPmAdExternalCd());
				dto.setPmAdInternalPageCd(vo.getPmAdInternalPageCd());
				dto.setPmAdInternalContentCd(vo.getPmAdInternalContentCd());
				dto.setUrUserId(urUserId);
				dto.setDeliveryType(vo.getDeliveryType());
				dto.setIlGoodsId(vo.getIlGoodsId());
				dto.setQty(vo.getQty());
				dto.setBuyNowYn(vo.getBuyNowYn());
				dto.setIlGoodsReserveOptionId(vo.getIlGoodsReserveOptionId());

				List<AddCartInfoAdditionalGoodsRequestDto> addDtoList = new ArrayList<AddCartInfoAdditionalGoodsRequestDto>();
				List<SpCartAddGoodsVo> addVoList = addVoListMap.get(vo.getSpCartId());
				if (addVoList != null && !addVoList.isEmpty()) {
					for (SpCartAddGoodsVo addVo : addVoList) {
						AddCartInfoAdditionalGoodsRequestDto addDto = new AddCartInfoAdditionalGoodsRequestDto();
						addDto.setIlGoodsId(addVo.getIlGoodsId());
						addDto.setQty(addVo.getQty());
						addDtoList.add(addDto);
					}
				}
				dto.setAdditionalGoodsList(addDtoList);
				addCartInfo(dto);
			}
		}
	}

	/**
	 * 장바구니 담기
	 *
	 * @param reqDto
	 * @return Long spCartId
	 * @throws Exception
	 */
	@Override
	public Long addCartInfo(AddCartInfoRequestDto reqDto) throws Exception {

		Long spCartId;

		// 바로결제가 아닌 장바구니 머지 가능여부 체크
		if (!"Y".equals(reqDto.getBuyNowYn()) && shoppingCartService.ifCheckAddCartMerge(reqDto.getIlGoodsId())) {
			// 기존 장바구니 같은 상품 담겨 있는지 조회
			spCartId = shoppingCartService.getCartIdByIlGoodsId(reqDto.getUrPcidCd(), reqDto.getUrUserId(),
					reqDto.getIlGoodsId(), reqDto.getIlGoodsReserveOptionId(), reqDto.getDeliveryType());
			if (spCartId == null) {
				// 없으면 새로 등록
				spCartId = addNewCart(reqDto);
			} else {
				// 있으면 장바구니 수량 +
				shoppingCartService.putCartPlusQty(spCartId, reqDto.getQty());
				// 추가 구성 상품 등록
				List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList = reqDto.getAdditionalGoodsList();
				if (additionalGoodsList != null && additionalGoodsList.size() > 0) {
					for (AddCartInfoAdditionalGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {
						// 기존 추가 구성상품 담겨져 있는지 조회
						Long spCartAddGoodsId = shoppingCartService.getCartAddGoodsIdByIlGoodsId(spCartId,
								additionalGoodsRequestDto.getIlGoodsId());
						if (spCartAddGoodsId == null) {
							// 없으면 새로 등록
							SpCartAddGoodsVo spCartAddGoodsVo = new SpCartAddGoodsVo();
							spCartAddGoodsVo.setSpCartId(spCartId);
							spCartAddGoodsVo.setIlGoodsId(additionalGoodsRequestDto.getIlGoodsId());
							spCartAddGoodsVo.setQty(additionalGoodsRequestDto.getQty());
							shoppingCartService.addCartAddGoods(spCartAddGoodsVo);
						} else {
							// 있으면 장바구니 추가 구성 수량 +
							shoppingCartService.putCartAddGoodsPlusQty(spCartAddGoodsId,
									additionalGoodsRequestDto.getQty());
						}
					}
				}
			}
		} else {
			// 머지 상품이 아니면 새로 등록
			spCartId = addNewCart(reqDto);
		}

		return spCartId;
	}

	/**
	 * 기획전 전용 장바구니 담기
	 *
	 * @param AddCartPickGoodsRequestDto
	 * @return Long spCartId
	 * @throws Exception
	 */
	@Override
	public Long addCartPickGoods(AddCartPickGoodsRequestDto reqDto) throws Exception {
		SpCartVo spCartVo = new SpCartVo();
		spCartVo.setUrPcidCd(reqDto.getUrPcidCd());
		spCartVo.setUrUserId(reqDto.getUrUserId());
		spCartVo.setDeliveryType(reqDto.getDeliveryType());
		spCartVo.setIlGoodsId(reqDto.getIlGoodsId());
		spCartVo.setQty(reqDto.getQty());
		spCartVo.setBuyNowYn(reqDto.getBuyNowYn());
		spCartVo.setGoodsDailyCycleType(reqDto.getGoodsDailyCycleType());
		spCartVo.setGoodsDailyCycleTermType(reqDto.getGoodsDailyCycleTermType());
		if (reqDto.getGoodsDailyCycleGreenJuiceWeekType() != null) {
			spCartVo.setGoodsDailyCycleGreenJuiceWeekType(
					String.join(",", reqDto.getGoodsDailyCycleGreenJuiceWeekType()));
		}
		spCartVo.setCartPromotionType(reqDto.getCartPromotionType());
		spCartVo.setEvExhibitId(reqDto.getEvExhibitId());
		spCartVo.setPmAdExternalCd(reqDto.getPmAdExternalCd());
		spCartVo.setPmAdInternalPageCd(reqDto.getPmAdInternalPageCd());
		spCartVo.setPmAdInternalContentCd(reqDto.getPmAdInternalContentCd());

		shoppingCartService.addCart(spCartVo);

		Long spCartId = spCartVo.getSpCartId();

		List<SpCartPickGoodsRequestDto> pickGoodsList = reqDto.getPickGoodsList();
		if (pickGoodsList != null && !pickGoodsList.isEmpty()) {
			for (SpCartPickGoodsRequestDto spCartPickGoodsRequestDto : pickGoodsList) {
				SpCartPickGoodsVo spCartPickGoodsVo = new SpCartPickGoodsVo();
				spCartPickGoodsVo.setSpCartId(spCartId);
				spCartPickGoodsVo.setIlGoodsId(spCartPickGoodsRequestDto.getIlGoodsId());
				spCartPickGoodsVo.setQty(spCartPickGoodsRequestDto.getQty());
				spCartPickGoodsVo.setGoodsDailyCycleWeekType(spCartPickGoodsRequestDto.getGoodsDailyCycleWeekType());
				shoppingCartService.addCartPickGoods(spCartPickGoodsVo);
			}
		}

		// 추가 구성 상품 등록
		List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList = reqDto.getAdditionalGoodsList();
		if (additionalGoodsList != null && additionalGoodsList.size() > 0) {
			for (AddCartInfoAdditionalGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {
				// 기존 추가 구성상품 담겨져 있는지 조회
				Long spCartAddGoodsId = shoppingCartService.getCartAddGoodsIdByIlGoodsId(spCartId,
						additionalGoodsRequestDto.getIlGoodsId());
				if (spCartAddGoodsId == null) {
					// 없으면 새로 등록
					SpCartAddGoodsVo spCartAddGoodsVo = new SpCartAddGoodsVo();
					spCartAddGoodsVo.setSpCartId(spCartId);
					spCartAddGoodsVo.setIlGoodsId(additionalGoodsRequestDto.getIlGoodsId());
					spCartAddGoodsVo.setQty(additionalGoodsRequestDto.getQty());
					shoppingCartService.addCartAddGoods(spCartAddGoodsVo);
				} else {
					// 있으면 장바구니 추가 구성 수량 +
					shoppingCartService.putCartAddGoodsPlusQty(spCartAddGoodsId,
							additionalGoodsRequestDto.getQty());
				}
			}
		}

		return spCartId;
	}

	/**
	 * 상품 saleType 으로 DeliveryType 가지고 오기
	 *
	 * @param saleType
	 * @return DeliveryType
	 * @throws Exception
	 */
	@Override
	public DeliveryType getDeliveryTypeBySaleType(String saleType) throws Exception {
		return shoppingCartService.getDeliveryTypeBySaleType(saleType);
	}

	/**
	 * get 장바구니
	 *
	 * @param spCartId
	 * @return SpCartVo
	 * @throws Exception
	 */
	@Override
	public SpCartVo getCart(Long spCartId) throws Exception {
		return shoppingCartService.getCart(spCartId);
	}

	/**
	 * 장바구니 담기 validation 체크
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isValidationAddCartInfo(AddCartInfoRequestDto reqDto) throws Exception {
		return shoppingCartService.isValidationAddCartInfo(reqDto);
	}

	/**
	 * 장바구니 , 장바구니 추가 구성상품 신규 등록
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	private Long addNewCart(AddCartInfoRequestDto reqDto) throws Exception {

		SpCartVo spCartVo = new SpCartVo();
		spCartVo.setUrPcidCd(reqDto.getUrPcidCd());
		spCartVo.setUrUserId(reqDto.getUrUserId());
		spCartVo.setDeliveryType(reqDto.getDeliveryType());
		spCartVo.setIlGoodsId(reqDto.getIlGoodsId());
		spCartVo.setQty(reqDto.getQty());
		spCartVo.setIlGoodsReserveOptionId(reqDto.getIlGoodsReserveOptionId());
		spCartVo.setBuyNowYn(reqDto.getBuyNowYn());
		spCartVo.setGoodsDailyCycleType(reqDto.getGoodsDailyCycleType());
		spCartVo.setGoodsDailyCycleTermType(reqDto.getGoodsDailyCycleTermType());
		if (reqDto.getGoodsDailyCycleGreenJuiceWeekType() != null) {
			spCartVo.setGoodsDailyCycleGreenJuiceWeekType(
					String.join(",", reqDto.getGoodsDailyCycleGreenJuiceWeekType()));
		}
		spCartVo.setGoodsDailyAllergyYn(reqDto.getGoodsDailyAllergyYn());
		spCartVo.setGoodsDailyBulkYn(reqDto.getGoodsDailyBulkYn());
		spCartVo.setGoodsBulkType(reqDto.getGoodsBulkType());
		spCartVo.setPmAdExternalCd(reqDto.getPmAdExternalCd());
		spCartVo.setPmAdInternalPageCd(reqDto.getPmAdInternalPageCd());
		spCartVo.setPmAdInternalContentCd(reqDto.getPmAdInternalContentCd());

		shoppingCartService.addCart(spCartVo);

		Long spCartId = spCartVo.getSpCartId();

		List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList = reqDto.getAdditionalGoodsList();
		if (additionalGoodsList != null && additionalGoodsList.size() > 0) {
			for (AddCartInfoAdditionalGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {
				SpCartAddGoodsVo spCartAddGoodsVo = new SpCartAddGoodsVo();
				spCartAddGoodsVo.setSpCartId(spCartId);
				spCartAddGoodsVo.setIlGoodsId(additionalGoodsRequestDto.getIlGoodsId());
				spCartAddGoodsVo.setQty(additionalGoodsRequestDto.getQty());
				shoppingCartService.addCartAddGoods(spCartAddGoodsVo);
			}
		}

		// 장바구니 담기 최대 허용 개수 체크
		checkCartMaxCountExceeded(reqDto);

		return spCartId;
	}

	/**
	 * 장바구니 정보
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CartDeliveryDto> getCartData(GetCartDataRequestDto reqDto) throws Exception {
		List<CartDeliveryDto> resDto = new ArrayList<CartDeliveryDto>();

		// 내 장바구니 spCartId 목록 조회 했을때 데이터 없으면 리턴
		List<Long> spCartIds = shoppingCartService.getCartIdList(reqDto);
		if (spCartIds.size() == 0) {
			return resDto;
		}

		// 증정 행사 증정품 (결제시 선택한 증정품 배송 정책에 포함(도착완료일자셋팅)하여 주문 생성)
		List<Long> cartGiftGoodsIds = null;
		if (reqDto.getGift() != null && !reqDto.getGift().isEmpty()) {
			cartGiftGoodsIds = reqDto.getGift().stream().map(m -> m.getIlGoodsId()).collect(Collectors.toList());
		}

		// - start 장바구니에서 배송 타입별로 조회
		CartDeliveryTypeGroupByVo[] deliveryTypeDataList = shoppingCartService.getCartDeliveryTypeGroupByList(
				ShoppingEnums.CartType.findByCode(reqDto.getCartType()).getDeliveryTypeList(), spCartIds,
				cartGiftGoodsIds);
		int shippingTemplateDataIndex = 1;
		int choiceShippingTemplateDataIndex = 0;
		if (deliveryTypeDataList != null && deliveryTypeDataList.length > 0) {

			int regularShippingBasicDiscountRate = 0;
			// 정기 배송시 정기배송 정책 가지고 와서 할인 하기 위한 정보 가지고 오기
			if (ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
				RegularShippingConfigDto regularShippingConfigDto = policyConfigBiz.getRegularShippingConfig();
				regularShippingBasicDiscountRate = regularShippingConfigDto.getRegularShippingBasicDiscountRate();
			}

			// 임직원 정보 조회
			HashMap<Long, Integer> employeePointInfo = new HashMap<Long, Integer>();
			List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo = null;
			if (CartEmployeeYn.IGNORE_EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())
					|| CartEmployeeYn.EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())) {
				policyBenefitEmployeeInfo = policyBenefitEmployeeBiz
						.getEmployeeDiscountBrandByUser(reqDto.getUrErpEmployeeCode());
			}
			// 접근 디바이스 타입 정보
			DeviceType deviceType = GoodsEnums.DeviceType.PC;
			if (DeviceUtil.isApp()) {
				deviceType = GoodsEnums.DeviceType.APP;
			} else if (DeviceUtil.getDirInfo().equalsIgnoreCase("mobile")) {
				deviceType = GoodsEnums.DeviceType.MOBILE;
			}

			// 도서산관 및 제주 배송 정보 조회
			ShippingAreaVo shippingAreaVo = goodsShippingTemplateBiz.getShippingArea(reqDto.getReceiverZipCode());

			// 새벽배송 권역 여부
			boolean isDawnDeliveryArea = storeDeliveryBiz.isDawnDeliveryArea(reqDto.getReceiverZipCode(), reqDto.getBuildingCode());

			// 매장 권역 정보
			ShippingPossibilityStoreDeliveryAreaDto storeDeliveryInfo = null;
			if(CartType.SHOP.getCode().equals(reqDto.getCartType())) {
				storeDeliveryInfo = storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(reqDto.getReceiverZipCode(), reqDto.getBuildingCode());
			}

			// 중복 품목 재고 정보
			HashMap<String, Integer> overlapBuyItem = new HashMap<String, Integer>();

			for (CartDeliveryTypeGroupByVo deliveryTypeData : deliveryTypeDataList) {

				boolean isEmployeeDiscountPossible = reqDto.isEmployee()
						&& (CartEmployeeYn.EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())
						|| CartEmployeeYn.IGNORE_EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn()))
						&& !DeliveryType.REGULAR.getCode().equals(deliveryTypeData.getDeliveryType());

				// 장바구니 배송 타입 set
				CartDeliveryDto deliveryDto = new CartDeliveryDto(deliveryTypeData);
				if(CartType.SHOP.getCode().equals(reqDto.getCartType())) {
					if (StoreDeliveryType.PICKUP.getCode().equals(reqDto.getStoreDeliveryType())) {
						deliveryDto.setDeliveryType(DeliveryType.SHOP_PICKUP.getCode());
						deliveryDto.setDeliveryTypeName(DeliveryType.SHOP_PICKUP.getCodeName());
					}
				}

				// 배송 정책 별 변수 생성
				List<CartShippingDto> shippingListDto = new ArrayList<>();

				// -- start 배송 타입 별 배송 정책별로 조회
				ShippingTemplateGroupByVo[] shippingTemplateDataList = shoppingCartService
						.getShippingTemplateGroupByListByDeliveryType(deliveryTypeData.getDeliveryType(), spCartIds,
								reqDto.getBridgeYn(), reqDto.getUrUserId(), cartGiftGoodsIds);
				for (ShippingTemplateGroupByVo ShippingTemplateData : shippingTemplateDataList) {
					//주문생성/주문복사시 배송비 무료 조건인 경우 배송비 0 처리
					if("Y".equals(reqDto.getFreeShippingPriceYn())) {
						ShippingTemplateData.setIlShippingTmplId(OrderShippingConstants.FREE_SHIPPING_TEMPLATE_ID);  //주문생성_무료배송비 배송정책탬플릿 세팅
					}
					CartShippingDto shippingDto = new CartShippingDto(shippingTemplateDataIndex, ShippingTemplateData, isDawnDeliveryArea);

					// 배송 정책별 sum 금액 변수 생성
					int goodsRecommendedPrice = 0;
					int goodsSalePrice = 0;
					int goodsDiscountPrice = 0;
					int goodsPriceByShipping = 0; // 배송비 기준 금액은 쿠폰 적용 하면 안되기 때문에 변수 별도로 처리
					int goodsQtyByShipping = 0;
					int goodsPaymentPrice = 0;
					int goodsTaxPaymentPrice = 0;
					int goodsTaxFreePaymentPrice = 0;
					int shippingBaiscPrice = 0;
					int shippingRegionPrice = 0;
					int shippingRecommendedPrice = 0;
					int shippingDiscountPrice = 0;
					int shippingPaymentPrice = 0;
					int freeShippingForNeedGoodsPrice = 0;
					int originShippingPrice = 0;

					// 배송 정책별 상품 리스트 변수 생성
					List<CartGoodsDto> goodsListDto = new ArrayList<CartGoodsDto>();

					// 배송 정책별 상품 정보 조회
					GetCartDataGoodsDataListResposeDto goodsDataListRes = getCartDataGoodsDataList(reqDto, spCartIds, deliveryTypeData, ShippingTemplateData);
					List<SpCartVo> goodsDataList = goodsDataListRes.getGoodsDataList();
					boolean isOnlyGiftShippingTemplate = goodsDataListRes.isOnlyGiftShippingTemplate();

					// 주문 결제시 고객이 선택한 배송일 및 새벽배송 여부로 재고 및 여부
					boolean isDawnDelivery = false;
					LocalDate arrivalScheduledDate = null;

					// 고객 선택한 배송 정보 조회
					GetCartDataBuyerChoiceShippingDataResposeDto buyerChoiceShippingDataRes = getBuyerChoiceShippingData(reqDto, isOnlyGiftShippingTemplate, choiceShippingTemplateDataIndex);
					choiceShippingTemplateDataIndex = buyerChoiceShippingDataRes.getShippingTemplateDataListIndex();
					// 고객 선택한 배송 정보 기준으로 새벽배송여부 및 도착예정일자 값 변경
					ChangeArrivalScheduledDto changeArrivalScheduledDto = buyerChoiceShippingDataRes.getChangeArrivalScheduledDto();

					// 선물 하기 아닐때만 고객 선택한 일자로 처리 (선물하기는 맨 마지막 일자로 convertGetCartDataShippingScheduled에서 자동 처리)
					if (!"Y".equals(reqDto.getPresentYn())) {
						//도착예정일
						if (changeArrivalScheduledDto != null) {
							isDawnDelivery = isDawnDeliveryArea && "Y".equals(changeArrivalScheduledDto.getDawnDeliveryYn()) ? true : false;
							arrivalScheduledDate = changeArrivalScheduledDto.getArrivalScheduledDate();
							shippingDto.setDawnDeliveryYn(changeArrivalScheduledDto.getDawnDeliveryYn());
						}

						// 정기배송 추가주문시 다음 배송일, 매장배송/픽업시 선택 날짜 기준으로 처리
						if (reqDto.getNextArrivalScheduledDate() != null) {
							arrivalScheduledDate = reqDto.getNextArrivalScheduledDate();
						}

						// 선물하기는 고객이 선택한 값과 상관없이 처리
						if (arrivalScheduledDate != null) {
							shippingDto.setArrivalScheduledDate(arrivalScheduledDate);
						}
					}

					int cartGoodsIndex = 0;
					for (SpCartVo goodsData : goodsDataList) {
						//주문생성/주문복사시 배송비 무료 조건인 경우 배송비 0 처리
						if("Y".equals(reqDto.getFreeShippingPriceYn())) {
							goodsData.setIlShippingTmplId(OrderShippingConstants.FREE_SHIPPING_TEMPLATE_ID);  //주문생성_무료배송비 배송정책탬플릿 세팅
						}
						CartGoodsDto goodsDto = new CartGoodsDto(goodsData);

						// 상품 정보 조회
						GoodsRequestDto goodsRequestDto = new GoodsRequestDto();
						goodsRequestDto.setIlGoodsId(goodsDto.getIlGoodsId());
						goodsRequestDto.setDeviceInfo(reqDto.getDeviceInfo());
						goodsRequestDto.setApp(reqDto.isApp());
						goodsRequestDto.setMember(reqDto.isMember());
						goodsRequestDto.setEmployee(reqDto.isEmployee());
						goodsRequestDto.setDawnDelivery(isDawnDelivery);
						goodsRequestDto.setArrivalScheduledDate(arrivalScheduledDate);
						goodsRequestDto.setGoodsDailyCycleType(goodsData.getGoodsDailyCycleType());
						goodsRequestDto.setBuyQty(goodsData.getQty());
						goodsRequestDto.setStoreDelivery(CartType.SHOP.getCode().equals(reqDto.getCartType()));
						goodsRequestDto.setStoreDeliveryInfo(storeDeliveryInfo);
						// 매장 상품일때 재고를 실시간으로 조회하여 가지고 오기
						goodsRequestDto.setRealTimeStoreStock(CartType.SHOP.getCode().equals(reqDto.getCartType()));
						// 베이비밀 일괄배송이고 일반 배송 (택배배송)일때 true
						goodsRequestDto.setDailyDelivery("Y".equals(goodsDto.getGoodsDailyBulkYn()) && DeliveryType.NORMAL.getCode().equals(deliveryTypeData.getDeliveryType()));
						// 중복 품목 재고 정보
						goodsRequestDto.setOverlapBuyItem(overlapBuyItem);
						goodsRequestDto.setGoodsDailyBulk("Y".equals(goodsDto.getGoodsDailyBulkYn()) && DeliveryType.DAILY.getCode().equals(deliveryTypeData.getDeliveryType()));

						// 상품
						if (reqDto.isBosCreateOrder() == true){
							goodsRequestDto.setBosCreateOrder(true);
						}

						BasicSelectGoodsVo goodsResultVo = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
						// 정기배송일때 첫 결제 날짜 선택 일자 변경 (배치 주문 생성일자보다 빠른 도착예정일 제거)
						if (CartType.REGULAR.getCode().equals(reqDto.getCartType()) && arrivalScheduledDate == null) {
							goodsGoodsBiz.convertRegularArrivalScheduledDate(goodsResultVo);
						}
						goodsDto.setByGoodsResultVo(goodsResultVo);
						// 상품 판매 유형 변경 체크하여 판매 중지로 상태 변경
						goodsDto.checkChangeSaleType(deliveryTypeData.getDeliveryType());

						// 상품 회원 권한 타입
						goodsDto.setBuyPurchaseType(reqDto, goodsResultVo);

						// 일일배송 합배송시 예외 처리
						goodsDto.setBuyPurchaseType(deliveryDto, cartGoodsIndex);

						// 브린지 페이지 이면서 새벽배송 가능할 경우 새벽배송 도착 예정일 처리해야함
						if ("Y".equals(reqDto.getBridgeYn()) && "Y".equals(shippingDto.getDawnDeliveryYn())) {
							goodsRequestDto.setDawnDelivery(true);
							BasicSelectGoodsVo dawnGoodsResultVo = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
							goodsDto.setDawnArrivalScheduledDateDto(dawnGoodsResultVo.getArrivalScheduledDateDto());
							goodsDto.setDawnArrivalScheduledDateDtoList(dawnGoodsResultVo.getArrivalScheduledDateDtoList());
						}

						// 상품 금액 계산
						convertGetCartDataGoodsDtoPrice(goodsDto);

						// 상품 이미지
						GoodsImageDto goodsImage = goodsGoodsBiz.getGoodsBasicImage(goodsDto.getGoodsType(), goodsDto.getIlGoodsId(), goodsDto.getIlItemCd(), goodsResultVo.getGoodsPackageImageType());
						if (goodsImage != null) {
							goodsDto.setGoodsImage(goodsImage.getSImage());
						}

						// 골라담기 상품정보 구성 (골라담기 할인은 여기서 정리함)
						convertGetCartDataCartPromotionGoods(reqDto, shippingDto, goodsDto, isDawnDelivery, arrivalScheduledDate, overlapBuyItem);

						// 묶음상품 구성상품 정보 구성
						convertGetCartDataPackageGoodsList(reqDto, shippingDto, goodsDto, isDawnDelivery, arrivalScheduledDate, overlapBuyItem);

						// 상품 할인 정보 구성
						convertGetCartDataCartDiscount(reqDto, isEmployeeDiscountPossible, goodsDto, goodsResultVo, policyBenefitEmployeeInfo, employeePointInfo, regularShippingBasicDiscountRate, deviceType);

						// 추가 구성상품 정보 구성
						convertGetCartDataAddGoodsList(reqDto, shippingDto, goodsDto, isDawnDelivery, arrivalScheduledDate);

						if (isEmployeeDiscountPossible) {

							// 묶음 상품일때 임직원할인시 각각 금액 계산
							convertGetCartDataGoodsPackageListEmployeeDeiscount(reqDto, goodsDto, policyBenefitEmployeeInfo, employeePointInfo);

							// 골라담기일때 일때 픽업상품 금액 계산 (녹즙 골라담기일때만)
							convertGetCartDataPickGoodsListEmployeeDeiscount(reqDto, goodsDto, policyBenefitEmployeeInfo, employeePointInfo);

							// 추가 구성상품 임직원 할인 적용
							convertGetCartDataAddGoodsListEmployeeDeiscount(reqDto, goodsDto, policyBenefitEmployeeInfo, employeePointInfo);
						} else {
							// 묶음상품 구성상품 할인 및 가격 계산
							convertGetCartDataGoodsPackageListPrice(goodsDto);

							// 골라담기 구성상품 정보 할인 및 가격 계산 (쿠폰)
							convertGetCartDataPickGoodsListPrice(goodsDto);
						}

						// 예약상품 정보 구성
						convertGetCartDataReserveGoods(deliveryTypeData, goodsDto);

						// 추가 구성상품 존재시 기존 상품 스케줄 및 재고 변경
						convertGetCartDataExistAddGoods(reqDto, shippingDto, goodsDto);

						// 기타정보 구성 (찜, 신규특가)
						convertGetCartDataEtcGoods(reqDto, goodsDto);

						// 주소기반으로 배송 가능 여부 체크
						ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = convertGetCartDataShippingPossibility(reqDto, deliveryDto, goodsDto, goodsResultVo, shippingAreaVo, storeDeliveryInfo, isDawnDelivery);

						// 일일배송 기본 선택일 데이터 수정
						convertGetCartDataDailyGoodsScheduled(goodsDto, shippingPossibilityStoreDeliveryAreaInfo);

						// 배송정책별 상품 summery 금액 처리
						GetCartDataGoodsSummeryResposeDto goodsSummeryRes = returnGetCartDataGoodsSummery(goodsDto);
						if (isEmployeeDiscountPossible) {
							goodsPriceByShipping += goodsSummeryRes.getGoodsRecommendedPrice();
						} else {
							goodsPriceByShipping += goodsSummeryRes.getGoodsSalePrice();
						}
						goodsQtyByShipping += goodsSummeryRes.getGoodsQtyByShipping();
						goodsRecommendedPrice += goodsSummeryRes.getGoodsRecommendedPrice();
						goodsSalePrice += goodsSummeryRes.getGoodsSalePrice();
						goodsPaymentPrice += goodsSummeryRes.getGoodsPaymentPrice();
						goodsDiscountPrice += goodsSummeryRes.getGoodsDiscountPrice();
						goodsTaxFreePaymentPrice += goodsSummeryRes.getGoodsTaxFreePaymentPrice();
						goodsTaxPaymentPrice += goodsSummeryRes.getGoodsTaxPaymentPrice();

						goodsListDto.add(goodsDto);
						cartGoodsIndex++;
					}

					// 배송 정책별 스케줄 정보 구성
					convertGetCartDataShippingScheduled(reqDto, shippingDto, goodsListDto);

					// 배송비 정보 조회
					GetGetCartDataShippingPriceResposeDto sippingPriceRes = getGetCartDataShippingPrice(reqDto, shippingDto, goodsListDto, goodsQtyByShipping, goodsPriceByShipping, isOnlyGiftShippingTemplate);
					shippingBaiscPrice = sippingPriceRes.getBaiscShippingPrice();
					shippingRegionPrice = sippingPriceRes.getRegionShippingPrice();
					shippingRecommendedPrice = sippingPriceRes.getShippingRecommendedPrice();
					originShippingPrice = sippingPriceRes.getOriginShippingPrice();
					freeShippingForNeedGoodsPrice = sippingPriceRes.getFreeShippingForNeedGoodsPrice();

					// 배송비 금액 계산하기 위해 상품 데이터 set 먼저 진행
					shippingDto.setGoodsRecommendedPrice(goodsRecommendedPrice);
					shippingDto.setGoodsSalePrice(goodsSalePrice);
					shippingDto.setGoodsDiscountPrice(goodsDiscountPrice);
					shippingDto.setGoodsPaymentPrice(goodsPaymentPrice);
					shippingDto.setGoodsTaxPaymentPrice(goodsTaxPaymentPrice);
					shippingDto.setGoodsTaxFreePaymentPrice(goodsTaxFreePaymentPrice);

					// 배송비 쿠폰 적용
					GetCartDataShippingCouponResposeDto shippingCouponRes = convertGetCartDataShippingCoupon(reqDto, shippingTemplateDataIndex, shippingDto, shippingRecommendedPrice, deviceType);
					shippingDiscountPrice = shippingCouponRes.getShippingDiscountPrice();
					shippingPaymentPrice = shippingCouponRes.getShippingPaymentPrice();

					shippingDto.setShippingBaiscPrice(shippingBaiscPrice);
					shippingDto.setShippingRegionPrice(shippingRegionPrice);
					shippingDto.setShippingRecommendedPrice(shippingRecommendedPrice);
					shippingDto.setShippingDiscountPrice(shippingDiscountPrice);
					shippingDto.setShippingPaymentPrice(shippingPaymentPrice);
					shippingDto.setOriginShippingPrice(originShippingPrice);
					shippingDto.setPaymentPrice(goodsPaymentPrice + shippingPaymentPrice);
					shippingDto.setFreeShippingForNeedGoodsPrice(freeShippingForNeedGoodsPrice);

					// 상품 도착 예정일 기준으로 상품 정보 일괄 수정
					convertGetCartDataShippingScheduledGoodsData(deliveryTypeData, shippingDto, goodsListDto);

					shippingDto.setGoods(goodsListDto);
					shippingListDto.add(shippingDto);
					shippingTemplateDataIndex++;
					// --- end 배송 정책별 상품 정보 조회
				}
				deliveryDto.setShipping(shippingListDto);
				resDto.add(deliveryDto);
				// -- end 배송 타입 별 배송 정책별로 조회
			}
		}
		// - end 장바구니에서 배송 타입별로 조회

		return resDto;
	}

	/**
	 * 배송 정책별 상품 정보 조회
	 *
	 * @param reqDto
	 * @param spCartIds
	 * @param deliveryTypeData
	 * @param ShippingTemplateData
	 * @return
	 * @throws Exception
	 */
	private GetCartDataGoodsDataListResposeDto getCartDataGoodsDataList(GetCartDataRequestDto reqDto, List<Long> spCartIds,
																		CartDeliveryTypeGroupByVo deliveryTypeData, ShippingTemplateGroupByVo ShippingTemplateData) throws Exception {
		GetCartDataGoodsDataListResposeDto res = new GetCartDataGoodsDataListResposeDto();
		// 현재 배송타입의 배송정책에 증정품만 포함되어 있는지 여부
		boolean isOnlyGiftShippingTemplate = false;

		// 장바구니 summery 에 배송지별 금액도 리턴 해줘야 해서 분기 처리
		List<Long> goodsListSpCartIds = spCartIds;
		if (reqDto.getSummerySpCartId() != null && reqDto.getSummerySpCartId().size() > 0) {
			goodsListSpCartIds = reqDto.getSummerySpCartId();
		}

		List<SpCartVo> goodsDataList = shoppingCartService.getGoodsListByShippingPolicy(
				deliveryTypeData.getDeliveryType(), ShippingTemplateData, goodsListSpCartIds);

		// 정기배송 이고 브릿지 페이지면 기존 상품 상품 리스트를 가지고 오고 머지 위한 로직
		if (ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())
				&& "Y".equals(reqDto.getBridgeYn())) {
			List<SpCartVo> regularGoodsDataList = orderRegularBiz
					.getGoodsListByShippingPolicy(ShippingTemplateData, reqDto.getUrUserId());
			if (regularGoodsDataList != null && regularGoodsDataList.size() > 0) {
				goodsDataList = Stream.concat(regularGoodsDataList.stream(), goodsDataList.stream())
						.collect(Collectors.toList());
			}
		}

		// 증정품있을 때, 기존 상품리스트에 머지
		if (reqDto.getGift() != null && reqDto.getGift().size() > 0) {
			List<SpCartVo> giftGoodsDataList = shoppingCartService
					.getGiftGoodsListByShippingPolicy(ShippingTemplateData, reqDto.getGift());

			if (giftGoodsDataList != null && giftGoodsDataList.size() > 0) {
				// 현재 배송타입의 배송정책에 증정품만 포함되어 있을 경우
				if (goodsDataList != null && goodsDataList.isEmpty()) {
					isOnlyGiftShippingTemplate = true;
					goodsDataList = giftGoodsDataList;
				} else {
					goodsDataList = Stream.concat(goodsDataList.stream(), giftGoodsDataList.stream())
							.collect(Collectors.toList());
				}
			}
		}
		res.setGoodsDataList(goodsDataList);
		res.setOnlyGiftShippingTemplate(isOnlyGiftShippingTemplate);
		return res;
	}

	/**
	 * 고객이 선택한 배송정보 조회
	 *
	 * @param reqDto
	 * @param isOnlyGiftShippingTemplate
	 * @param shippingTemplateDataListIndex
	 * @return
	 * @throws Exception
	 */
	private GetCartDataBuyerChoiceShippingDataResposeDto getBuyerChoiceShippingData(GetCartDataRequestDto reqDto,
																					boolean isOnlyGiftShippingTemplate, int shippingTemplateDataListIndex) throws Exception {
		GetCartDataBuyerChoiceShippingDataResposeDto res = new GetCartDataBuyerChoiceShippingDataResposeDto();



		// 증정 상품만 있는 배송 정책은 제외하기
		if (reqDto.getArrivalScheduled() != null && reqDto.getArrivalScheduled().size() > 0
				&& !isOnlyGiftShippingTemplate) {
			ChangeArrivalScheduledDto changeArrivalScheduledDto = reqDto.getArrivalScheduled()
					.get(shippingTemplateDataListIndex++);
			if (changeArrivalScheduledDto != null) {
				res.setChangeArrivalScheduledDto(changeArrivalScheduledDto);
			}
		}
		res.setShippingTemplateDataListIndex(shippingTemplateDataListIndex);
		return res;
	}

	/**
	 * 상품 금액 계산
	 *
	 * @param goodsDto
	 * @throws Exception
	 */
	private void convertGetCartDataGoodsDtoPrice(CartGoodsDto goodsDto) throws Exception {
		// 조회 상품 정보 장바구니 상품정보 set
		int qty = goodsDto.getQty();
		int deliveryQty = qty;
		int paymentPrice = goodsDto.getSalePrice() * qty;
		int recommendedPriceMltplQty = goodsDto.getRecommendedPrice() * qty;
		int salePriceMltplQty = goodsDto.getSalePrice() * qty;

		// 일일상품인 경우 식단 주기 요일,기간에 맞춰 금액 재계산
		if (GoodsEnums.SaleType.DAILY.getCode().equals(goodsDto.getSaleType())) {
			int goodsDailyCycleTypeDayQty = 0;
			int goodsDailyCycleTermTypeQty = 0;

			// 녹즙
			if (goodsDto.getGoodsDailyType().equals(GoodsEnums.GoodsDailyType.GREENJUICE.getCode())) {

				// 배송주기 일 개수
				goodsDailyCycleTypeDayQty = Integer.parseInt(GoodsEnums.GoodsCycleTypeByGreenJuice
						.findByCode(goodsDto.getGoodsDailyCycleType()).getDayQty());
				// 배송기간 개수
				goodsDailyCycleTermTypeQty = Integer.parseInt(GoodsEnums.GoodsCycleTermTypeQtyByGreenJuice
						.findByCode(goodsDto.getGoodsDailyCycleTermType()).getCodeName());

				// 베이비밀,잇슬림
			} else {

				// 베이비밀 일괄배송
				if ("Y".equals(goodsDto.getGoodsDailyBulkYn())
						&& goodsDto.getGoodsDailyType().equals(GoodsEnums.GoodsDailyType.BABYMEAL.getCode())) {
					// 일괄배송 세트
					int goodsDailyBulkQty = Integer.parseInt(
							GoodsEnums.GoodsDailyBulkQtyType.findByCode(goodsDto.getGoodsBulkType()).getCodeName());
					deliveryQty = deliveryQty * goodsDailyBulkQty;
					paymentPrice = paymentPrice * goodsDailyBulkQty;
					recommendedPriceMltplQty = recommendedPriceMltplQty * goodsDailyBulkQty;
					salePriceMltplQty = salePriceMltplQty * goodsDailyBulkQty;

					// 베이비밀 일일배송 or 잇슬림
				} else {
					// 배송주기 일 개수
					goodsDailyCycleTypeDayQty = Integer.parseInt(
							GoodsEnums.GoodsCycleType.findByCode(goodsDto.getGoodsDailyCycleType()).getTypeQty());
					// 배송기간 개수
					goodsDailyCycleTermTypeQty = Integer.parseInt(GoodsEnums.GoodsCycleTermType
							.findByCode(goodsDto.getGoodsDailyCycleTermType()).getTypeQty());
				}
			}

			if (goodsDailyCycleTypeDayQty != 0 && goodsDailyCycleTermTypeQty != 0) {
				deliveryQty = deliveryQty * goodsDailyCycleTypeDayQty * goodsDailyCycleTermTypeQty;
				paymentPrice = paymentPrice * goodsDailyCycleTypeDayQty * goodsDailyCycleTermTypeQty;
				recommendedPriceMltplQty = recommendedPriceMltplQty * goodsDailyCycleTypeDayQty
						* goodsDailyCycleTermTypeQty;
				salePriceMltplQty = salePriceMltplQty * goodsDailyCycleTypeDayQty * goodsDailyCycleTermTypeQty;
			}
		}

		goodsDto.setDeliveryQty(deliveryQty);
		goodsDto.setRecommendedPriceMltplQty(recommendedPriceMltplQty);
		goodsDto.setSalePriceMltplQty(salePriceMltplQty);
		goodsDto.setPaymentPrice(paymentPrice);
	}

	/**
	 * 골라담기 상품정보 변경
	 *
	 * @param reqDto
	 * @param shippingDto
	 * @param goodsDto
	 * @param isDawnDelivery
	 * @param arrivalScheduledDate
	 * @throws Exception
	 */
	private void convertGetCartDataCartPromotionGoods(GetCartDataRequestDto reqDto, CartShippingDto shippingDto,
			CartGoodsDto goodsDto, boolean isDawnDelivery, LocalDate arrivalScheduledDate, HashMap<String, Integer> overlapBuyItem) throws Exception {
		// 녹즙 내맘대로 주문 과 기획전 균일가 골라담기 일때만 로직을탐
		if (ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())
				|| ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {

			// 고객이 선택한 상품 정보 조회 재고는 getCartPickGoodsList 서비스에서 계산되어 나옴 (골라담기상품 구성상품의 실 재고수량 = 구성상품 재고 / 골라담기상품 구성수량)
			List<CartPickGoodsDto> pickGoodsDtoList = getCartPickGoodsList(goodsDto.getSpCartId(), reqDto.isMember(),
					reqDto.isEmployee(), isDawnDelivery, arrivalScheduledDate, goodsDto.getQty(), goodsDto.getGoodsDailyCycleType(), overlapBuyItem);

			SelectExhibitResponseDto selectExhibitResponseDto = null;
			if (ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {

				// 균일가 골라담기시 상품명은 기회전, 가격은 기획전에서 설정한 가격으로 처리
				selectExhibitResponseDto = promotionExhibitBiz.getSelectExhibit(goodsDto.getEvExhibitId());
				if (selectExhibitResponseDto != null) {
					goodsDto.setGoodsName(selectExhibitResponseDto.getTitle());
					// 정가는 아래 pickGoodsDtoList 합친 쳐야해서 아래로직에 있음
					goodsDto.setSalePrice(selectExhibitResponseDto.getSelectPrice());
					goodsDto.setSalePriceMltplQty(selectExhibitResponseDto.getSelectPrice() * goodsDto.getQty());
					goodsDto.setPaymentPrice(goodsDto.getSalePriceMltplQty());
					if (selectExhibitResponseDto.getIsActive()) {
						// 배송정책이 모두 같을경우만 판매 처리
						if(pickGoodsDtoList.stream().allMatch(dto -> dto.getIlShippingTmplId().equals(goodsDto.getIlShippingTmplId()))) {
							goodsDto.setSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
							goodsDto.setSystemSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
						} else {
							goodsDto.setSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
							goodsDto.setSystemSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
						}
					} else {
						goodsDto.setSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
						goodsDto.setSystemSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
					}
				} else {
					goodsDto.setSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
					goodsDto.setSystemSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
				}
			} else if (ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()
					// 녹즙 골라담기는 타이틀 고정
					.equals(goodsDto.getCartPromotionType())) {
				goodsDto.setGoodsName(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCodeName());
			}

			int pickSumRecommendedPrice = pickGoodsDtoList.stream()
					.mapToInt(m -> m.getRecommendedPrice() * m.getGoodsQty()).sum();
			int pickSumPaymentPrice = pickGoodsDtoList.stream().mapToInt(m -> m.getSaleTotalPrice()).sum();
			int pickSumGoodsQty = pickGoodsDtoList.stream().mapToInt(m -> m.getGoodsQty()).sum();

			// 녹즙 골라담기일때 수량 = 기간 * 골라담은 상품 모든 수량, 금액 = 기간 * 상품 판매가
			if (ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {

				// 식단주기 기간 개수
				int goodsDailyCycleTermTypeQty = Integer.parseInt(GoodsEnums.GoodsCycleTermTypeQtyByGreenJuice
						.findByCode(goodsDto.getGoodsDailyCycleTermType()).getCodeName());

				int cartPromotionGoodsRecommendedPrice = pickSumRecommendedPrice * goodsDailyCycleTermTypeQty;
				int cartPromotionGoodsPaymentPrice = pickSumPaymentPrice * goodsDailyCycleTermTypeQty;
				int cartPromotionGoodsQty = pickSumGoodsQty * goodsDailyCycleTermTypeQty;

				// 녹즙 골라담기는 goodsDto 가격은 구성상품의 가격들의 합
				goodsDto.setRecommendedPrice(pickSumRecommendedPrice);
				goodsDto.setRecommendedPriceMltplQty(cartPromotionGoodsRecommendedPrice);
				goodsDto.setSalePrice(pickSumPaymentPrice);
				goodsDto.setSalePriceMltplQty(cartPromotionGoodsPaymentPrice);
				goodsDto.setPaymentPrice(cartPromotionGoodsPaymentPrice);
				goodsDto.setDeliveryQty(goodsDailyCycleTermTypeQty);
				goodsDto.setQty(cartPromotionGoodsQty);
				for (CartPickGoodsDto dto : pickGoodsDtoList) {
					// 배송 수량은 주기 포함하여 계산
					dto.setDeliveryQty(dto.getDeliveryQty() * goodsDailyCycleTermTypeQty);
					dto.setPaymentPrice(dto.getPaymentPrice() * goodsDailyCycleTermTypeQty);
					dto.setSaleTotalPrice(dto.getSaleTotalPrice() * goodsDailyCycleTermTypeQty);
					List<CartGoodsDiscountDto> pickDiscountListDto = new ArrayList<CartGoodsDiscountDto>();
					// 각 상품의 일반 우선 할인 정보 세팅
					if (!GoodsEnums.GoodsDiscountType.NONE.getCode().equals(dto.getDiscountType())
							&& !GoodsEnums.GoodsDiscountType.NOT_APPLICABLE.getCode().equals(dto.getDiscountType())) {
						if (dto.getRecommendedPrice() - dto.getSalePrice() > 0) {
							CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
							goodsDiscountDto.setDiscountType(dto.getDiscountType());
							goodsDiscountDto.setDiscountTypeName(dto.getDiscountTypeName());
							goodsDiscountDto.setDiscountPrice((dto.getRecommendedPrice() * dto.getDeliveryQty() - dto.getPaymentPrice()));
							goodsDiscountDto.setExceedingLimitPrice(0);
							pickDiscountListDto.add(goodsDiscountDto);
						}
					}
					dto.setDiscount(pickDiscountListDto);
				}
			} else if (ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {

				int index = 0;
				int pickGoodsTotalPrice = 0;
				// 균일가 골라담기는 기획전에 설정된 값을 goodsDto 에 세팅 후 구성상품에 판매 비율별이 아닌 단순 수량으로 나눈 값으로 구성상품 판매가 구성됨

				if (selectExhibitResponseDto != null) {
					int goodsRecommendedPrice = 0;
					for (CartPickGoodsDto dto : pickGoodsDtoList) {
						List<CartGoodsDiscountDto> pickDiscountListDto = new ArrayList<CartGoodsDiscountDto>();

						int pickGoodsSalePrice = 0;
						int pickGoodsSaleTotalPrice = 0;
						// 할인금액 계산
						if (index == pickGoodsDtoList.size() - 1) {
							// 마지막 상품은 원단위 오차 포함하기 위해 총 판매가를 계산 후 단가를 계산한다.
							// 주문의 OD_ORDER_DETL.TOT_SALE_PRICE 은 정상적으로 들어가지만 OD_ORDER_DETL.SALE_PRICE 단가는 원단위 차이가 있을수 있다.
							pickGoodsSaleTotalPrice = goodsDto.getSalePriceMltplQty() - pickGoodsTotalPrice;
							pickGoodsSalePrice = pickGoodsSaleTotalPrice / dto.getDeliveryQty();
						} else {
							// 구성상품 판매 단가는 균일가 / 총 구성상품 수량
							pickGoodsSalePrice = Math.round(goodsDto.getSalePrice() / pickSumGoodsQty);
							pickGoodsSaleTotalPrice = pickGoodsSalePrice * dto.getDeliveryQty();
							// pickGoodsTotalPrice 는 마지막 상품에 원단위 금액 맞추기 위해 별도 합산
							pickGoodsTotalPrice += pickGoodsSaleTotalPrice;
							index++;
						}
						goodsRecommendedPrice += dto.getRecommendedPrice() * dto.getGoodsQty();

						// 위 계산된 정가 - 판매가로 균일가 구성상품 할인정보 세팅 (일반 즉시 할인 정보 는 무시)
						if (dto.getRecommendedPrice() > pickGoodsSalePrice) {
							CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
							goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.EXHIBIT_SELECT.getCode());
							goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.EXHIBIT_SELECT.getCodeName());
							goodsDiscountDto.setDiscountPrice((dto.getRecommendedPrice() * dto.getDeliveryQty()) - pickGoodsSaleTotalPrice);
							goodsDiscountDto.setExceedingLimitPrice(0);
							pickDiscountListDto.add(goodsDiscountDto);
						}

						dto.setSalePrice(pickGoodsSalePrice);
						dto.setSaleTotalPrice(pickGoodsSaleTotalPrice);
						dto.setPaymentPrice(pickGoodsSaleTotalPrice);
						dto.setDiscount(pickDiscountListDto);
					}

					// 균일가 골라담기 정가 및 할인내역 추가
					goodsDto.setRecommendedPrice(goodsRecommendedPrice);
					goodsDto.setRecommendedPriceMltplQty(goodsRecommendedPrice * goodsDto.getQty());
				}
			}

			// 픽업 상품 할인정보를 상품정보에 넣기
			List<CartGoodsDiscountDto> pickDiscountDtoList = pickGoodsDtoList.stream().map(CartPickGoodsDto::getDiscount)
					.flatMap(Collection::stream).collect(Collectors.toList());

			List<CartGoodsDiscountDto> cartGoodsDiscountGroupbyList = new ArrayList<CartGoodsDiscountDto>();
			pickDiscountDtoList.stream().collect(Collectors.groupingBy(CartGoodsDiscountDto::getDiscountType))
					.forEach((key, list) -> {

						int sumDiscountPrice = list.stream().mapToInt(CartGoodsDiscountDto::getDiscountPrice).sum();
						int sumExceedingLimitPrice = list.stream().mapToInt(CartGoodsDiscountDto::getExceedingLimitPrice)
								.sum();

						CartGoodsDiscountDto returnDto = new CartGoodsDiscountDto();
						returnDto.setDiscountType(key);
						returnDto.setDiscountTypeName(GoodsDiscountType.findByCode(key).getCodeName());
						returnDto.setDiscountPrice(sumDiscountPrice);
						returnDto.setExceedingLimitPrice(sumExceedingLimitPrice);
						cartGoodsDiscountGroupbyList.add(returnDto);
					});

			goodsDto.setDiscount(cartGoodsDiscountGroupbyList);

			RecalculationPackageDto recalculatonPackageDto = getRecalculationCartPickGoodsList(goodsDto.getSaleStatus(), pickGoodsDtoList);

			goodsDto.setSaleStatus(recalculatonPackageDto.getSaleStatus());
			goodsDto.setArrivalScheduledDateDtoList(recalculatonPackageDto.getArrivalScheduledDateDtoList());
			goodsDto.setArrivalScheduledDateDto(recalculatonPackageDto.getArrivalScheduledDateDto());

			int stock = 0;
			if (goodsDto.getArrivalScheduledDateDto() != null) {
				stock = goodsDto.getArrivalScheduledDateDto().getStock();
			}
			goodsDto.setStockQty(stock);
			goodsDto.setPickGoods(pickGoodsDtoList);

			// 브린지 페이지 이면서 새벽배송 가능할 경우 새벽배송 도착 예정일 처리해야함
			if ("Y".equals(reqDto.getBridgeYn()) && "Y".equals(shippingDto.getDawnDeliveryYn())) {
				List<CartPickGoodsDto> dawnPickGoodsDtoList = getCartPickGoodsList(goodsDto.getSpCartId(),
						reqDto.isMember(), reqDto.isEmployee(), true, arrivalScheduledDate, goodsDto.getQty(), goodsDto.getGoodsDailyCycleType(), overlapBuyItem);
				RecalculationPackageDto dawnRecalculatonPackageDto = getRecalculationCartPickGoodsList(goodsDto.getSaleStatus(), dawnPickGoodsDtoList);
				goodsDto.setDawnArrivalScheduledDateDtoList(dawnRecalculatonPackageDto.getArrivalScheduledDateDtoList());
				goodsDto.setDawnArrivalScheduledDateDto(dawnRecalculatonPackageDto.getArrivalScheduledDateDto());
			}
		}
	}

	/**
	 * 장바구니 상품 할인 정보 구성
	 *
	 * @param reqDto
	 * @param isEmployeeDiscountPossible
	 * @param goodsDto
	 * @param goodsResultVo
	 * @param policyBenefitEmployeeInfo
	 * @param employeePointInfo
	 * @param regularShippingBasicDiscountRate
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	private void convertGetCartDataCartDiscount(GetCartDataRequestDto reqDto, boolean isEmployeeDiscountPossible,
												CartGoodsDto goodsDto, BasicSelectGoodsVo goodsResultVo, List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo,
												HashMap<Long, Integer> employeePointInfo, int regularShippingBasicDiscountRate, DeviceType deviceType) throws Exception {

		// 균일가 골라담기는 convertGetCartDataCartPromotionGoods 에서 처리함
		if(!ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {
			List<CartGoodsDiscountDto> goodsDiscountListDto = new ArrayList<CartGoodsDiscountDto>();
			// 임직원 할인 적용시 (렌탈은 제외)
			if (isEmployeeDiscountPossible && !GoodsEnums.GoodsType.RENTAL.getCode().equals(goodsDto.getGoodsType())) {

				EmployeeDiscountInfoDto employeeDiscountInfoDto = null;
				Long psEmplDiscGrpId = null;

				// 사은품 임직원 혜택에서 제외
				// 묶음 상품, 녹즙 골라담기는 각 상품 상세별로 할인 계산한다음 sum 야 해서 아래 로직 안탐
				if (!ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getCartPromotionType())
						&& !GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsDto.getGoodsType())
						&& !GoodsEnums.GoodsType.GIFT.getCode().equals(goodsDto.getGoodsType())
						&& !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(goodsDto.getGoodsType())) {

					// 임직원 할인율
					PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz
							.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, goodsDto.getUrBrandId());

					PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz
							.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo, goodsDto.getUrBrandId());

					int employeeBuyQty = goodsDto.getDeliveryQty();
					if (findPolicyBenefitEmployeeVo != null && policyBenefitEmployeeBrandByUserVo != null) {
						int employeeDiscountRatio = policyBenefitEmployeeBrandByUserVo.getDiscountRatio();
						// 상품별 개별 임직원 할인정보 조회
						int goodsEmployeeDiscountRatio = goodsGoodsBiz.getGoodsEmployeeDiscountRatio(goodsDto.getIlGoodsId());
						if (goodsEmployeeDiscountRatio > 0) {
							employeeDiscountRatio = goodsEmployeeDiscountRatio;
						}
						if (CartEmployeeYn.EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())) {
							// 임직원 가지고 있는 포인트로 할인 적용 금액 계산
							psEmplDiscGrpId = findPolicyBenefitEmployeeVo.getPsEmplDiscGrpId();

							if (!employeePointInfo.containsKey(psEmplDiscGrpId)) {
								employeePointInfo.put(psEmplDiscGrpId, findPolicyBenefitEmployeeVo.getRemainAmount());
							}

							// 임직원 잔여포인트
							int residualEmployeePoint = employeePointInfo.get(psEmplDiscGrpId);

							// 임직원 할인 계산
							employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio, residualEmployeePoint, goodsDto.getRecommendedPrice(), employeeBuyQty);

							employeePointInfo.put(psEmplDiscGrpId,
									residualEmployeePoint - employeeDiscountInfoDto.getDiscountPrice());

						} else {
							// 포인트와 상관없이 할인 금액 계산
							employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio,BuyerConstants.EMPLOYEE_MAX_POINT, goodsDto.getRecommendedPrice(), employeeBuyQty);
						}

						if (employeeDiscountInfoDto != null) {
							goodsDto.setSalePrice(employeeDiscountInfoDto.getSalePrice());
							goodsDto.setSalePriceMltplQty(employeeDiscountInfoDto.getDiscountAppliedPrice());
							goodsDto.setPaymentPrice(employeeDiscountInfoDto.getDiscountAppliedPrice());

							CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
							goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCode());
							goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCodeName());
							goodsDiscountDto.setDiscountPrice(employeeDiscountInfoDto.getDiscountPrice());
							goodsDiscountDto.setExceedingLimitPrice(employeeDiscountInfoDto.getExcessPrice());
							goodsDiscountDto.setPsEmplDiscGrpId(psEmplDiscGrpId);

							goodsDiscountListDto.add(goodsDiscountDto);
						} else {
							goodsDto.setSalePrice(goodsDto.getRecommendedPrice());
							goodsDto.setSalePriceMltplQty(goodsDto.getRecommendedPriceMltplQty());
							goodsDto.setPaymentPrice(goodsDto.getRecommendedPriceMltplQty());
						}
					} else {
						goodsDto.setSalePrice(goodsDto.getRecommendedPrice());
						goodsDto.setSalePriceMltplQty(goodsDto.getRecommendedPriceMltplQty());
						goodsDto.setPaymentPrice(goodsDto.getRecommendedPriceMltplQty());
					}
				}
			} else {

				if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {
					goodsDiscountListDto = goodsDto.getDiscount();
				} else {
					// 일반 우선 할인
					if (!GoodsEnums.GoodsDiscountType.NONE.getCode().equals(goodsResultVo.getDiscountType())
							&& !GoodsEnums.GoodsDiscountType.NOT_APPLICABLE.getCode().equals(goodsResultVo.getDiscountType())) {
						if (goodsDto.getRecommendedPriceMltplQty() - goodsDto.getPaymentPrice() > 0) {
							CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
							goodsDiscountDto.setDiscountType(goodsResultVo.getDiscountType());
							goodsDiscountDto.setDiscountTypeName(goodsResultVo.getDiscountTypeName());
							goodsDiscountDto
									.setDiscountPrice(goodsDto.getRecommendedPriceMltplQty() - goodsDto.getPaymentPrice());
							goodsDiscountDto.setExceedingLimitPrice(0);

							goodsDiscountListDto.add(goodsDiscountDto);
						}
					}
				}

				// 정기 배송 할인
				if (GoodsEnums.SaleType.REGULAR.getCode().equals(goodsDto.getSaleType())
						&& regularShippingBasicDiscountRate > 0) {

					int beforePaymentPrice = goodsDto.getPaymentPrice();
					int afterPaymentPrice = PriceUtil.getPriceByRate(goodsDto.getPaymentPrice(),
							regularShippingBasicDiscountRate);

					goodsDto.setPaymentPrice(afterPaymentPrice);

					CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
					goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.REGULAR_DEFAULT.getCode());
					goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.REGULAR_DEFAULT.getCodeName());
					goodsDiscountDto.setDiscountPrice(beforePaymentPrice - afterPaymentPrice);
					goodsDiscountDto.setExceedingLimitPrice(0);

					goodsDiscountListDto.add(goodsDiscountDto);
				}

				// 상품 쿠폰 적용
				UseGoodsCouponDto useGoodsCouponDto = reqDto.findUseGoodsCoupon(goodsDto.getSpCartId());
				if (useGoodsCouponDto != null) {
					CouponDto useGoodsCouponInfo = promotionCouponBiz.getGoodsCouponApplicationListByUser(
							reqDto.getUrUserId(), goodsDto, deviceType, useGoodsCouponDto.getPmCouponIssueId());

					if (useGoodsCouponInfo != null && useGoodsCouponInfo.isActive() == true) {
						CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();

						goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode());
						goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCodeName());
						goodsDiscountDto.setDiscountPrice(useGoodsCouponInfo.getDiscountPrice());
						goodsDiscountDto.setExceedingLimitPrice(0);
						goodsDiscountDto.setPmCouponIssueId(useGoodsCouponInfo.getPmCouponIssueId());

						goodsDiscountListDto.add(goodsDiscountDto);

						goodsDto.setPaymentPrice(useGoodsCouponInfo.getPaymentPrice());
					}
				}

				// 장바구니 쿠폰 사용 정보
				CouponDto useCartCouponDto = reqDto.getUseCartCoupon();

				// 장바구니 쿠폰 적용
				if (useCartCouponDto != null && useCartCouponDto.isActive() == true) {
					CartCouponApplicationGoodsDto cartCouponApplicationGoodsDto = useCartCouponDto
							.findUseCartCouponGoodsCoupon(goodsDto.getSpCartId());
					if (cartCouponApplicationGoodsDto != null) {
						CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();

						goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.CART_COUPON.getCode());
						goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.CART_COUPON.getCodeName());
						goodsDiscountDto.setDiscountPrice(cartCouponApplicationGoodsDto.getDiscountPrice());
						goodsDiscountDto.setExceedingLimitPrice(0);
						goodsDiscountDto.setPmCouponIssueId(useCartCouponDto.getPmCouponIssueId());

						goodsDiscountListDto.add(goodsDiscountDto);

						goodsDto.setPaymentPrice(cartCouponApplicationGoodsDto.getPaymentPrice());
					}
				}
			}
			goodsDto.setDiscount(goodsDiscountListDto);
		}
	}

	/**
	 * 묶음상품 구성상품 정보 구성
	 *
	 * @param reqDto
	 * @param shippingDto
	 * @param goodsDto
	 * @param isDawnDelivery
	 * @param arrivalScheduledDate
	 * @throws Exception
	 */
	private void convertGetCartDataPackageGoodsList(GetCartDataRequestDto reqDto, CartShippingDto shippingDto,
			CartGoodsDto goodsDto, boolean isDawnDelivery, LocalDate arrivalScheduledDate, HashMap<String, Integer> overlapBuyItem) throws Exception {
		List<CartGoodsPackageDto> packageDtolist = new ArrayList<>();
		if (goodsDto.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
			List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(goodsDto.getIlGoodsId(),
					reqDto.isMember(), reqDto.isEmployee(), isDawnDelivery, arrivalScheduledDate, goodsDto.getQty(), overlapBuyItem);

			RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goodsDto.getSaleStatus(), goodsPackageList);

			if (StringUtil.isNotEmpty(recalculationPackage.getSaleStatus())) {
				goodsDto.setSaleStatus(recalculationPackage.getSaleStatus());
			}

			for (PackageGoodsListDto packageList : goodsPackageList) {
				CartGoodsPackageDto goodsPackageDto = new CartGoodsPackageDto(packageList, goodsDto.getQty());
				packageDtolist.add(goodsPackageDto);
			}

			goodsDto.setArrivalScheduledDateDtoList(recalculationPackage.getArrivalScheduledDateDtoList());
			goodsDto.setArrivalScheduledDateDto(recalculationPackage.getArrivalScheduledDateDto());

			int stock = 0;
			if (goodsDto.getArrivalScheduledDateDto() != null) {
				stock = goodsDto.getArrivalScheduledDateDto().getStock();
			}
			goodsDto.setStockQty(stock);
			// 상품의 판매상태 다시 체크
			goodsDto.setSaleStatus(recalculationPackage.getSaleStatus());


			// 브린지 페이지 이면서 새벽배송 가능할 경우 새벽배송 도착 예정일 처리해야함
			if ("Y".equals(reqDto.getBridgeYn()) && "Y".equals(shippingDto.getDawnDeliveryYn())) {
				List<PackageGoodsListDto> dawnGoodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(
						goodsDto.getIlGoodsId(), reqDto.isMember(), reqDto.isEmployee(), true, arrivalScheduledDate,
						goodsDto.getQty(), overlapBuyItem);
				RecalculationPackageDto dawnRecalculationPackage = goodsGoodsBiz
						.getRecalculationPackage(goodsDto.getSaleStatus(), dawnGoodsPackageList);

				goodsDto.setDawnArrivalScheduledDateDtoList(dawnRecalculationPackage.getArrivalScheduledDateDtoList());
				goodsDto.setDawnArrivalScheduledDateDto(dawnRecalculationPackage.getArrivalScheduledDateDto());

			}
		}
		goodsDto.setGoodsPackage(packageDtolist);
	}

	/**
	 * 골라담기 구성상품 정보 할인 및 가격 계산
	 *
	 * @param pickGoodsList
	 * @param goodsDiscountList
	 * @param paymentPrice
	 */
	private void convertGetCartDataPickGoodsListPrice(CartGoodsDto goodsDto) {
		// convertGetCartDataGoodsPackageListPrice 로직과 유사, 쿠폰 관련된 부분만 처리하는것 빼고는 동일 함으로 코멘트는 convertGetCartDataGoodsPackageListPrice 에서 확인
		// 해당 로직 수정시 convertGetCartDataGoodsPackageListPrice 도 동일하게 수정해야 할수 있음
		// 골라담기는 상품쿠폰, 장바구니 쿠폰만 추가적으로 계산하기
		if (goodsDto.getDiscount() != null && !goodsDto.getDiscount().isEmpty()) {
			List<CartGoodsDiscountDto> goodsDiscountList = goodsDto.getDiscount().stream().filter(dto -> GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode().equals(dto.getDiscountType())
					|| GoodsEnums.GoodsDiscountType.CART_COUPON.getCode().equals(dto.getDiscountType())).collect(Collectors.toList());

			List<CartPickGoodsDto> pickGoodsList = goodsDto.getPickGoods();
			if (goodsDiscountList != null && !goodsDiscountList.isEmpty() && pickGoodsList != null && !pickGoodsList.isEmpty()) {
				int goodsPaymentPrice = goodsDto.getPaymentPrice();
				int totalPaymentPrice = pickGoodsList.stream().collect(Collectors.summingInt(CartPickGoodsDto::getPaymentPrice));
				int totalDiscountPrice = goodsDiscountList.stream().collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice));
				List<CartGoodsDiscountDto> totalPickDiscountList = new ArrayList<>();

				int index = 0;
				int sumPickPaymentPrice = 0;
				int sumDiscountPrice = 0;
				for (CartPickGoodsDto pickGoods : pickGoodsList) {
					int pickPaymentPrice = 0;
					float rate = (float) pickGoods.getPaymentPrice() / totalPaymentPrice;
					if (index == pickGoodsList.size() - 1) {
						pickPaymentPrice = goodsPaymentPrice - sumPickPaymentPrice;
					} else {
						pickPaymentPrice = Math.round(goodsPaymentPrice * rate);
						sumPickPaymentPrice += pickPaymentPrice;
					}

					int discountIndex = 0;
					for (CartGoodsDiscountDto goodsDiscount : goodsDiscountList) {
						int discountPrice = 0;
						if (index == pickGoodsList.size() - 1) {
							if(discountIndex == goodsDiscountList.size() - 1) {
								discountPrice = totalDiscountPrice - sumDiscountPrice;
							} else {
								discountPrice = goodsDiscount.getDiscountPrice() - totalPickDiscountList.stream()
										.filter(dto -> dto.getDiscountType().equals(goodsDiscount.getDiscountType()))
										.collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice));
								sumDiscountPrice += discountPrice;
							}
						} else {
							discountPrice = Math.round(goodsDiscount.getDiscountPrice() * rate);
							sumDiscountPrice += discountPrice;
						}

						CartGoodsDiscountDto discountDto = new CartGoodsDiscountDto();
						discountDto.setDiscountType(goodsDiscount.getDiscountType());
						discountDto.setDiscountTypeName(goodsDiscount.getDiscountTypeName());
						discountDto.setDiscountPrice(discountPrice);
						discountDto.setPmCouponIssueId(goodsDiscount.getPmCouponIssueId());
						discountDto.setPsEmplDiscGrpId(goodsDiscount.getPsEmplDiscGrpId());

						pickGoods.getDiscount().add(discountDto);
						totalPickDiscountList.add(discountDto);
						discountIndex++;
					}

					pickGoods.setPaymentPrice(pickPaymentPrice);
					index++;
				}
			}
		}
	}

	/**
	 * 추가 구성상품 할인 및 가격 계산
	 *
	 * @param goodsDto
	 */
	private void convertGetCartDataAddGoodsListEmployeeDeiscount(GetCartDataRequestDto reqDto, CartGoodsDto goodsDto,
																 List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo, HashMap<Long, Integer> employeePointInfo) throws Exception {
		if (goodsDto.getAdditionalGoods() != null && !goodsDto.getAdditionalGoods().isEmpty()) {
			for (CartAdditionalGoodsDto addGoods : goodsDto.getAdditionalGoods()) {
				List<CartGoodsDiscountDto> goodsDiscountListDto = new ArrayList<CartGoodsDiscountDto>();
				if (policyBenefitEmployeeInfo != null) {
					PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, addGoods.getUrBrandId());
					PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo,addGoods.getUrBrandId());
					if (policyBenefitEmployeeBrandByUserVo != null) {
						int employeeDiscountRatio = policyBenefitEmployeeBrandByUserVo.getDiscountRatio();

						Long psEmplDiscGrpId = null;
						EmployeeDiscountInfoDto employeeDiscountInfoDto = null;

						if (CartEmployeeYn.EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())) {
							// 임직원 가지고 있는 포인트로 할인 적용 금액 계산
							psEmplDiscGrpId = findPolicyBenefitEmployeeVo.getPsEmplDiscGrpId();

							if (!employeePointInfo.containsKey(psEmplDiscGrpId)) {
								employeePointInfo.put(psEmplDiscGrpId, findPolicyBenefitEmployeeVo.getRemainAmount());
							}

							// 임직원 잔여포인트
							int residualEmployeePoint = employeePointInfo.get(psEmplDiscGrpId);

							// 임직원 할인 계산
							employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio,
									residualEmployeePoint, addGoods.getRecommendedPrice(), addGoods.getQty());

							employeePointInfo.put(psEmplDiscGrpId,
									residualEmployeePoint - employeeDiscountInfoDto.getDiscountPrice());

						} else {
							// 포인트와 상관없이 할인 금액 계산
							employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio,
									BuyerConstants.EMPLOYEE_MAX_POINT, addGoods.getRecommendedPrice(), addGoods.getQty());
						}

						if (employeeDiscountInfoDto != null) {
							CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
							goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCode());
							goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCodeName());
							goodsDiscountDto.setDiscountPrice(employeeDiscountInfoDto.getDiscountPrice());
							goodsDiscountDto.setExceedingLimitPrice(employeeDiscountInfoDto.getExcessPrice());
							goodsDiscountDto.setPsEmplDiscGrpId(psEmplDiscGrpId);

							goodsDiscountListDto.add(goodsDiscountDto);

							addGoods.setSalePrice(employeeDiscountInfoDto.getSalePrice());
							addGoods.setPaymentPrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
						} else {
							addGoods.setSalePrice(addGoods.getRecommendedPrice());
							addGoods.setPaymentPrice(addGoods.getRecommendedPrice() * addGoods.getQty());
						}
					} else {
						addGoods.setSalePrice(addGoods.getRecommendedPrice());
						addGoods.setPaymentPrice(addGoods.getRecommendedPrice() * addGoods.getQty());
					}
				} else {
					addGoods.setSalePrice(addGoods.getRecommendedPrice());
					addGoods.setPaymentPrice(addGoods.getRecommendedPrice() * addGoods.getQty());
				}
				addGoods.setDiscount(goodsDiscountListDto);
			}
		}
	}

	/**
	 * 골라담기 (녹즙 내맘대로 주문 ) 임직원 할인 및 가격 계산
	 *
	 * @param reqDto
	 * @param goodsDto
	 * @param policyBenefitEmployeeInfo
	 * @param employeePointInfo
	 * @throws Exception
	 */
	private void convertGetCartDataPickGoodsListEmployeeDeiscount(GetCartDataRequestDto reqDto, CartGoodsDto goodsDto,
																  List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo, HashMap<Long, Integer> employeePointInfo) throws Exception {
		if (ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {
			for (CartPickGoodsDto pickGoods : goodsDto.getPickGoods()) {
				List<CartGoodsDiscountDto> goodsDiscountListDto = new ArrayList<CartGoodsDiscountDto>();
				if(policyBenefitEmployeeInfo != null) {
					PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, pickGoods.getUrBrandId());
					PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo, pickGoods.getUrBrandId());
					if(policyBenefitEmployeeBrandByUserVo!= null) {
						int employeeDiscountRatio = policyBenefitEmployeeBrandByUserVo.getDiscountRatio();
						int goodsEmployeeDiscountRatio = goodsGoodsBiz.getGoodsEmployeeDiscountRatio(pickGoods.getIlGoodsId());
						if (goodsEmployeeDiscountRatio > 0) {
							employeeDiscountRatio = goodsEmployeeDiscountRatio;
						}
						Long psEmplDiscGrpId = null;
						EmployeeDiscountInfoDto employeeDiscountInfoDto = null;

						if (CartEmployeeYn.EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())) {
							// 임직원 가지고 있는 포인트로 할인 적용 금액 계산
							psEmplDiscGrpId = findPolicyBenefitEmployeeVo.getPsEmplDiscGrpId();

							if (!employeePointInfo.containsKey(psEmplDiscGrpId)) {
								employeePointInfo.put(psEmplDiscGrpId, findPolicyBenefitEmployeeVo.getRemainAmount());
							}

							// 임직원 잔여포인트
							int residualEmployeePoint = employeePointInfo.get(psEmplDiscGrpId);

							// 임직원 할인 계산
							employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio, residualEmployeePoint, pickGoods.getRecommendedPrice(), pickGoods.getDeliveryQty());

							employeePointInfo.put(psEmplDiscGrpId, residualEmployeePoint - employeeDiscountInfoDto.getDiscountPrice());

						} else {
							// 포인트와 상관없이 할인 금액 계산
							employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio, BuyerConstants.EMPLOYEE_MAX_POINT, pickGoods.getRecommendedPrice(), pickGoods.getDeliveryQty());
						}

						if (employeeDiscountInfoDto != null) {
							CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
							goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCode());
							goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCodeName());
							goodsDiscountDto.setDiscountPrice(employeeDiscountInfoDto.getDiscountPrice());
							goodsDiscountDto.setExceedingLimitPrice(employeeDiscountInfoDto.getExcessPrice());
							goodsDiscountDto.setPsEmplDiscGrpId(psEmplDiscGrpId);

							goodsDiscountListDto.add(goodsDiscountDto);

							pickGoods.setSalePrice(employeeDiscountInfoDto.getSalePrice());
							// convertGetCartDataGoodsPackageListEmployeeDeiscount 내용과 동일
							pickGoods.setSaleTotalPrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
							pickGoods.setPaymentPrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
						} else {
							pickGoods.setSalePrice(pickGoods.getRecommendedPrice());
							pickGoods.setSaleTotalPrice(pickGoods.getRecommendedPrice() * pickGoods.getDeliveryQty());
							pickGoods.setPaymentPrice(pickGoods.getRecommendedPrice() * pickGoods.getDeliveryQty());
						}
					} else {
						pickGoods.setSalePrice(pickGoods.getRecommendedPrice());
						pickGoods.setSaleTotalPrice(pickGoods.getRecommendedPrice() * pickGoods.getDeliveryQty());
						pickGoods.setPaymentPrice(pickGoods.getRecommendedPrice() * pickGoods.getDeliveryQty());
					}
				} else {
					pickGoods.setSalePrice(pickGoods.getRecommendedPrice());
					pickGoods.setSaleTotalPrice(pickGoods.getRecommendedPrice() * pickGoods.getDeliveryQty());
					pickGoods.setPaymentPrice(pickGoods.getRecommendedPrice() * pickGoods.getDeliveryQty());
				}
				pickGoods.setDiscount(goodsDiscountListDto);
			}

			List<CartGoodsDiscountDto> goodsDiscountListDto = new ArrayList<CartGoodsDiscountDto>();
			List<CartGoodsDiscountDto> pickGoodsDiscountList = goodsDto.getPickGoods().stream()
					.filter(dto -> dto.getDiscount() != null).map(CartPickGoodsDto::getDiscount)
					.flatMap(Collection::stream).collect(Collectors.toList());
			if(!pickGoodsDiscountList.isEmpty()) {
				CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
				goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCode());
				goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCodeName());
				goodsDiscountDto.setDiscountPrice(pickGoodsDiscountList.stream().collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice)));
				goodsDiscountDto.setExceedingLimitPrice(pickGoodsDiscountList.stream().collect(Collectors.summingInt(CartGoodsDiscountDto::getExceedingLimitPrice)));

				goodsDiscountListDto.add(goodsDiscountDto);

				goodsDto.setSalePrice(goodsDto.getPickGoods().stream().collect(Collectors.summingInt(CartPickGoodsDto::getSalePrice)));
				goodsDto.setSalePriceMltplQty(goodsDto.getRecommendedPriceMltplQty() - goodsDiscountDto.getDiscountPrice());
				goodsDto.setPaymentPrice(goodsDto.getRecommendedPriceMltplQty() - goodsDiscountDto.getDiscountPrice());
			} else {
				goodsDto.setSalePrice(goodsDto.getRecommendedPrice());
				goodsDto.setSalePriceMltplQty(goodsDto.getRecommendedPriceMltplQty());
				goodsDto.setPaymentPrice(goodsDto.getRecommendedPriceMltplQty());
			}
			goodsDto.setDiscount(goodsDiscountListDto);
		}
	}

	/**
	 * 묶음상품 구성상품 할인 및 가격 계산
	 *
	 * @param goodsDto
	 */
	private void convertGetCartDataGoodsPackageListEmployeeDeiscount(GetCartDataRequestDto reqDto, CartGoodsDto goodsDto,
																	 List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo, HashMap<Long, Integer> employeePointInfo) throws Exception {
		if (goodsDto.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
			for (CartGoodsPackageDto goodsPackage : goodsDto.getGoodsPackage()) {
				List<CartGoodsDiscountDto> goodsDiscountListDto = new ArrayList<CartGoodsDiscountDto>();
				if(policyBenefitEmployeeInfo != null) {
					List<GoodsPackageEmployeeDiscountDto> goodsEmployeeDiscountList = goodsGoodsBiz.getPackageGoodsEmployeeDiscountRatio(goodsDto.getIlGoodsId());
					if (!goodsPackage.isGift()) {
						PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, goodsPackage.getUrBrandId());
						PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo, goodsPackage.getUrBrandId());
						if(policyBenefitEmployeeBrandByUserVo!= null) {
							int employeeDiscountRatio = policyBenefitEmployeeBrandByUserVo.getDiscountRatio();
							if (goodsEmployeeDiscountList != null && !goodsEmployeeDiscountList.isEmpty()) {
								GoodsPackageEmployeeDiscountDto goodsPackageEmployeeDiscountDto = goodsGoodsBiz.findGoodsPackageEmployeeDiscountDto(goodsEmployeeDiscountList, goodsPackage.getIlGoodsId());
								if (goodsPackageEmployeeDiscountDto != null && goodsPackageEmployeeDiscountDto.getDiscountRatio() > 0) {
									employeeDiscountRatio = goodsPackageEmployeeDiscountDto.getDiscountRatio();
								}
							}

							Long psEmplDiscGrpId = null;
							EmployeeDiscountInfoDto employeeDiscountInfoDto = null;

							if (CartEmployeeYn.EMPLOY_POINT_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())) {
								// 임직원 가지고 있는 포인트로 할인 적용 금액 계산
								psEmplDiscGrpId = findPolicyBenefitEmployeeVo.getPsEmplDiscGrpId();

								if (!employeePointInfo.containsKey(psEmplDiscGrpId)) {
									employeePointInfo.put(psEmplDiscGrpId, findPolicyBenefitEmployeeVo.getRemainAmount());
								}

								// 임직원 잔여포인트
								int residualEmployeePoint = employeePointInfo.get(psEmplDiscGrpId);

								// 임직원 할인 계산
								employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio, residualEmployeePoint, goodsPackage.getRecommendedPrice(), goodsPackage.getGoodsQty() * goodsDto.getQty());

								employeePointInfo.put(psEmplDiscGrpId, residualEmployeePoint - employeeDiscountInfoDto.getDiscountPrice());

							} else {
								// 포인트와 상관없이 할인 금액 계산
								employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(employeeDiscountRatio,BuyerConstants.EMPLOYEE_MAX_POINT, goodsPackage.getRecommendedPrice(), goodsPackage.getGoodsQty() * goodsDto.getQty());
							}

							if (employeeDiscountInfoDto != null) {
								CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
								goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCode());
								goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCodeName());
								goodsDiscountDto.setDiscountPrice(employeeDiscountInfoDto.getDiscountPrice());
								goodsDiscountDto.setExceedingLimitPrice(employeeDiscountInfoDto.getExcessPrice());
								goodsDiscountDto.setPsEmplDiscGrpId(psEmplDiscGrpId);

								goodsDiscountListDto.add(goodsDiscountDto);

								goodsPackage.setSalePrice(employeeDiscountInfoDto.getSalePrice());
								// goodsGoodsBiz.employeeDiscountCalculation 에서 실제 할인 금액과 단가는 차이가 날수 있다. convertGetCartDataCartPromotionGoods 에 균일가 골라담기 구성상품 가격 계산과 유사
								goodsPackage.setSaleTotalPrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
								goodsPackage.setPaymentPrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
							} else {
								goodsPackage.setSalePrice(goodsPackage.getRecommendedPrice());
								goodsPackage.setSaleTotalPrice(goodsPackage.getRecommendedPrice() * goodsPackage.getGoodsQty() * goodsDto.getQty());
								goodsPackage.setPaymentPrice(goodsPackage.getRecommendedPrice() * goodsPackage.getGoodsQty() * goodsDto.getQty());
							}
						} else {
							goodsPackage.setSalePrice(goodsPackage.getRecommendedPrice());
							goodsPackage.setSaleTotalPrice(goodsPackage.getRecommendedPrice() * goodsPackage.getGoodsQty() * goodsDto.getQty());
							goodsPackage.setPaymentPrice(goodsPackage.getRecommendedPrice() * goodsPackage.getGoodsQty() * goodsDto.getQty());
						}
					}
				} else {
					goodsPackage.setSalePrice(goodsPackage.getRecommendedPrice());
					goodsPackage.setSaleTotalPrice(goodsPackage.getRecommendedPrice() * goodsPackage.getGoodsQty() * goodsDto.getQty());
					goodsPackage.setPaymentPrice(goodsPackage.getRecommendedPrice() * goodsPackage.getGoodsQty() * goodsDto.getQty());
				}
				goodsPackage.setDiscount(goodsDiscountListDto);
			}

			List<CartGoodsDiscountDto> goodsDiscountListDto = new ArrayList<CartGoodsDiscountDto>();
			List<CartGoodsDiscountDto> packageGoodsDiscountList = goodsDto.getGoodsPackage().stream()
					.filter(dto -> dto.getDiscount() != null).map(CartGoodsPackageDto::getDiscount)
					.flatMap(Collection::stream).collect(Collectors.toList());
			if(!packageGoodsDiscountList.isEmpty()) {
				CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
				goodsDiscountDto.setDiscountType(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCode());
				goodsDiscountDto.setDiscountTypeName(GoodsEnums.GoodsDiscountType.EMPLOYEE.getCodeName());
				goodsDiscountDto.setDiscountPrice(packageGoodsDiscountList.stream().collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice)));
				goodsDiscountDto.setExceedingLimitPrice(packageGoodsDiscountList.stream().collect(Collectors.summingInt(CartGoodsDiscountDto::getExceedingLimitPrice)));

				goodsDiscountListDto.add(goodsDiscountDto);

				goodsDto.setSalePrice(goodsDto.getGoodsPackage().stream().collect(Collectors.summingInt(CartGoodsPackageDto::getSalePrice)));
				goodsDto.setSalePriceMltplQty(goodsDto.getRecommendedPriceMltplQty() - goodsDiscountDto.getDiscountPrice());
				goodsDto.setPaymentPrice(goodsDto.getRecommendedPriceMltplQty() - goodsDiscountDto.getDiscountPrice());
			} else {
				goodsDto.setSalePrice(goodsDto.getRecommendedPrice());
				goodsDto.setSalePriceMltplQty(goodsDto.getRecommendedPriceMltplQty());
				goodsDto.setPaymentPrice(goodsDto.getRecommendedPriceMltplQty());
			}
			goodsDto.setDiscount(goodsDiscountListDto);
		}
	}

	/**
	 * 묶음상품 구성상품 할인 및 가격 계산
	 *
	 * @param goodsDto
	 */
	private void convertGetCartDataGoodsPackageListPrice(CartGoodsDto goodsDto) {
		// 묶음 상품은 할인을 묶음 상품에 모두 적용되기때문에 구성상품은 구성상품 판매가 비율로 할인 금액을 세팅한다.
		// convertGetCartDataPickGoodsListPrice 골라담기도 아래와 거의 동일 로직 수정시 같이 수정 필요
		// 증정품은 제외하고 할인금액 계산
		List<CartGoodsPackageDto> goodsPackageList = goodsDto.getGoodsPackage().stream().filter(dto -> !dto.isGift()).collect(Collectors.toList());
		List<CartGoodsDiscountDto> goodsDiscountList = goodsDto.getDiscount();
		List<CartGoodsDiscountDto> totalPackageDiscountList = new ArrayList<>();

		int goodsPaymentPrice = goodsDto.getPaymentPrice();
		int totalPackageSalePrice = goodsPackageList.stream().collect(Collectors.summingInt(CartGoodsPackageDto::getSaleTotalPrice));
		int totalDiscountPrice = goodsDto.getDiscount().stream().collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice));

		int index = 0;
		int sumPackagePaymentPrice = 0;
		int sumDiscountPrice = 0;

		//하나의 묶음 상품에 구성상품 ㄱ(1,000원),ㄴ(2,000원),ㄷ(3,000원) 에 할인 종류가 A(-600원),B(-1,200원),C(-1,800원) 이 있을경우
		// 아래 매트리스는 각 구성상품 및 적용된 할인이며 `???(?)` 에 ???은 할당된 할인금액이며 (?)는 할인 금액 계산되는 순서이다.
		//		A		B		C
		//ㄱ		100(1)	200(2)	300(3)
		//ㄴ		200(4)	400(5)	600(6)
		//ㄷ		300(7)	600(8)	900(9)
		for (CartGoodsPackageDto goodsPackage : goodsPackageList) {
			int packagePaymentPrice = 0;
			float rate = (float) goodsPackage.getSaleTotalPrice() / totalPackageSalePrice;
			if (index == goodsPackageList.size() - 1) {
				// ㄷ 의 구성상품의 판매가는 총 판매가에 ㄱ,ㄴ 금액을 합친 금액에서 뺌
				packagePaymentPrice = goodsPaymentPrice - sumPackagePaymentPrice;
			} else {
				// ㄱ,ㄴ 에 구성상품의 판매가를 총금액에 구성상품 판매가로 비율로 계산
				packagePaymentPrice = Math.round(goodsPaymentPrice * rate);
				sumPackagePaymentPrice += packagePaymentPrice;
			}

			List<CartGoodsDiscountDto> packageDiscountList = new ArrayList<>();
			int discountIndex = 0;
			for (CartGoodsDiscountDto goodsDiscount : goodsDiscountList) {
				int discountPrice = 0;
				if (index == goodsPackageList.size() - 1) {
					if(discountIndex == goodsDiscountList.size() - 1) {
						// 할인 순서 9 에 대한 로직 (총 할인금액에 나머지 할인 금액을 뺀 금액)
						discountPrice = totalDiscountPrice - sumDiscountPrice;
					} else {
						// 할인 순서 7,8 에 대한 로직 (각 할인종류별 마지막 상품 이전에 계산된 금액 에서 뺀 금액 - A-A`(1,4) 또는B-B`(2,5))
						discountPrice = goodsDiscount.getDiscountPrice() - totalPackageDiscountList.stream()
								.filter(dto -> dto.getDiscountType().equals(goodsDiscount.getDiscountType()))
								.collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice));
						sumDiscountPrice += discountPrice;
					}
				} else {
					// 할인순서 1,2,3,4,5,6 에 대한 로직
					discountPrice = Math.round(goodsDiscount.getDiscountPrice() * rate);
					sumDiscountPrice += discountPrice;
				}

				CartGoodsDiscountDto discountDto = new CartGoodsDiscountDto();
				discountDto.setDiscountType(goodsDiscount.getDiscountType());
				discountDto.setDiscountTypeName(goodsDiscount.getDiscountTypeName());
				discountDto.setDiscountPrice(discountPrice);
				discountDto.setPmCouponIssueId(goodsDiscount.getPmCouponIssueId());
				discountDto.setPsEmplDiscGrpId(goodsDiscount.getPsEmplDiscGrpId());

				packageDiscountList.add(discountDto);
				totalPackageDiscountList.add(discountDto);
				discountIndex++;
			}
			goodsPackage.setPaymentPrice(packagePaymentPrice);
			goodsPackage.setDiscount(packageDiscountList);
			index++;
		}
	}

	/**
	 * 추가 구성상품 정보 구성
	 *
	 * @param reqDto
	 * @param shippingDto
	 * @param goodsDto
	 * @param isDawnDelivery
	 * @param arrivalScheduledDate
	 * @throws Exception
	 */
	private void convertGetCartDataAddGoodsList(GetCartDataRequestDto reqDto, CartShippingDto shippingDto,
												CartGoodsDto goodsDto, boolean isDawnDelivery, LocalDate arrivalScheduledDate) throws Exception {
		List<CartAdditionalGoodsDto> addGoodsDtoList = new ArrayList<CartAdditionalGoodsDto>();
		List<SpCartAddGoodsVo> addGoodsList = null;
		if (ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {
			addGoodsList = shoppingCartService.getCartAddGoodsIdListByExhibit(goodsDto.getSpCartId());
		} else {
			addGoodsList = shoppingCartService.getCartAddGoodsIdList(goodsDto.getSpCartId());
		}
		for (SpCartAddGoodsVo addGoods : addGoodsList) {
			CartAdditionalGoodsDto cartAdditionalGoodsDto = new CartAdditionalGoodsDto(addGoods);

			GoodsRequestDto addGoodsRequestDto = new GoodsRequestDto();
			addGoodsRequestDto.setIlGoodsId(cartAdditionalGoodsDto.getIlGoodsId());
			addGoodsRequestDto.setDeviceInfo(reqDto.getDeviceInfo());
			addGoodsRequestDto.setApp(reqDto.isApp());
			addGoodsRequestDto.setMember(reqDto.isMember());
			addGoodsRequestDto.setEmployee(reqDto.isEmployee());
			addGoodsRequestDto.setDawnDelivery(isDawnDelivery);
			addGoodsRequestDto.setArrivalScheduledDate(arrivalScheduledDate);
			addGoodsRequestDto.setBuyQty(addGoods.getQty());

			BasicSelectGoodsVo addGoodsResultVo = goodsGoodsBiz.getGoodsBasicInfo(addGoodsRequestDto);

			cartAdditionalGoodsDto.setGoodsResultVo(addGoodsResultVo);

			// 추가 구성상품 할인
			List<CartGoodsDiscountDto> addGoodsDiscountListDto = new ArrayList<CartGoodsDiscountDto>();

			int discountPrice = (cartAdditionalGoodsDto.getRecommendedPrice() * cartAdditionalGoodsDto.getQty())- cartAdditionalGoodsDto.getPaymentPrice();
			if (discountPrice > 0) {
				CartGoodsDiscountDto goodsDiscountDto = new CartGoodsDiscountDto();
				goodsDiscountDto.setDiscountType(GoodsDiscountType.ADD_GOODS.getCode());
				goodsDiscountDto.setDiscountTypeName(GoodsDiscountType.ADD_GOODS.getCodeName());
				goodsDiscountDto.setDiscountPrice(discountPrice);
				goodsDiscountDto.setExceedingLimitPrice(0);
				addGoodsDiscountListDto.add(goodsDiscountDto);
			}

			cartAdditionalGoodsDto.setDiscount(addGoodsDiscountListDto);

			// 브린지 페이지 이면서 새벽배송 가능할 경우 새벽배송 도착 예정일 처리해야함
			if ("Y".equals(reqDto.getBridgeYn()) && "Y".equals(shippingDto.getDawnDeliveryYn())) {
				addGoodsRequestDto.setDawnDelivery(true);
				BasicSelectGoodsVo dawnAddGoodsResultVo = goodsGoodsBiz.getGoodsBasicInfo(addGoodsRequestDto);
				cartAdditionalGoodsDto
						.setDawnArrivalScheduledDateDto(dawnAddGoodsResultVo.getArrivalScheduledDateDto());
				cartAdditionalGoodsDto
						.setDawnArrivalScheduledDateDtoList(dawnAddGoodsResultVo.getArrivalScheduledDateDtoList());
			}
			addGoodsDtoList.add(cartAdditionalGoodsDto);
		}
		goodsDto.setAdditionalGoods(addGoodsDtoList);
	}

	/**
	 * 예약상품 정보 구성
	 *
	 * @param goodsDto
	 * @throws Exception
	 */
	private void convertGetCartDataReserveGoods(CartDeliveryTypeGroupByVo deliveryTypeData, CartGoodsDto goodsDto) throws Exception {
		if (deliveryTypeData.getDeliveryType().equals(ShoppingEnums.DeliveryType.RESERVATION.getCode())
				&& GoodsEnums.SaleType.RESERVATION.getCode().equals(goodsDto.getSaleType())) {
			// 예약상품 정보 조회 후 스케줄 처리
			GoodsReserveOptionVo goodsReserveOptionVo = goodsGoodsBiz
					.getGoodsReserveOption(goodsDto.getIlGoodsReserveOptionId());
			ArrivalScheduledDateDto reserveArrivalScheduledDateDto = new ArrivalScheduledDateDto();
			if (goodsReserveOptionVo == null) {
				goodsDto.setSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
				goodsDto.setStockQty(0);
				reserveArrivalScheduledDateDto.setStock(0);
			} else {
				if (goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
					LocalDateTime currentDateTime = LocalDateTime.now();
					if (!(goodsReserveOptionVo.getReserveStartDate().compareTo(currentDateTime) <= 0
							&& currentDateTime.compareTo(goodsReserveOptionVo.getReserveEndDate()) <= 0)) {
						// 회차가 기간이 지나면 판매 중지 상태 변경
						goodsDto.setSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
					} else if (!(goodsReserveOptionVo.getStockQty() > 0)) {
						// 회차의 수량이 없을때 일시 품절
						goodsDto.setSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
					} else if (goodsReserveOptionVo.getStockQty() < goodsDto.getQty()) {
						// 회차의 수량 보다 구매 수량이 더 클때
						goodsDto.setSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
					}
				}
				reserveArrivalScheduledDateDto.setStock(goodsReserveOptionVo.getStockQty());
				reserveArrivalScheduledDateDto.setOrderDate(goodsReserveOptionVo.getOrderIfDate());
				reserveArrivalScheduledDateDto.setForwardingScheduledDate(goodsReserveOptionVo.getReleaseDate());
				reserveArrivalScheduledDateDto.setArrivalScheduledDate(goodsReserveOptionVo.getArriveDate());
				reserveArrivalScheduledDateDto.setOrderWeekCode(StoreEnums.WeekCode
						.findByWeekValue(reserveArrivalScheduledDateDto.getOrderDate().getDayOfWeek().getValue())
						.getCode());
				goodsDto.setReserveSaleSeq(goodsReserveOptionVo.getSaleSeq());
				goodsDto.setArrivalScheduledDateDto(reserveArrivalScheduledDateDto);
				goodsDto.setArrivalScheduledDateDtoList(Arrays.asList(reserveArrivalScheduledDateDto));
				goodsDto.setStockQty(goodsReserveOptionVo.getStockQty());
				goodsDto.setArrivalScheduledDate(goodsReserveOptionVo.getArriveDate());
				goodsDto.setForwardingScheduledDate(goodsReserveOptionVo.getReleaseDate());
			}
		}
	}

	/**
	 * 추가 구성상품 존재시 기존 상품 스케줄 및 재고 변경
	 *
	 * @param reqDto
	 * @param shippingDto
	 * @param goodsDto
	 * @throws Exception
	 */
	private void convertGetCartDataExistAddGoods(GetCartDataRequestDto reqDto, CartShippingDto shippingDto,
												 CartGoodsDto goodsDto) throws Exception {
		// 추가 구성상품 존재시 기본 상품과 출고일을 맞추어 처리 해야함
		List<CartAdditionalGoodsDto> addGoodsDtoList = goodsDto.getAdditionalGoods();
		if (addGoodsDtoList.size() > 0) {
			// 추가 구성상품 교집합 도착 예정일 리스트 조회
			List<LocalDate> intersectionAddGoodsArrivalScheduledDateList = goodsGoodsBiz
					.intersectionArrivalScheduledDateListByDto(addGoodsDtoList.stream()
							.map(CartAdditionalGoodsDto::getArrivalScheduledDateDtoList).collect(Collectors.toList()));
			// 추가 구성상품을 반복문 돌리면서 교집합 도착 예정일 기준으로 재고 및 스케줄 수정
			addGoodsDtoList.stream().forEach(addGoodsdto -> {
				try {
					List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = goodsGoodsBiz
							.intersectionArrivalScheduledDateDtoList(addGoodsdto.getArrivalScheduledDateDtoList(),
									intersectionAddGoodsArrivalScheduledDateList);
					addGoodsdto.setArrivalScheduledDateDtoList(arrivalScheduledDateDtoList);
					addGoodsdto.setArrivalScheduledDateDto(
							goodsGoodsBiz.getLatestArrivalScheduledDateDto(arrivalScheduledDateDtoList));
					addGoodsdto.setStockQty(addGoodsdto.getArrivalScheduledDateDto().getStock());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			// 추가 구성상품 도착 예정일 일정으로 상품 도착 예정일 일정과 교집합 찾아서 데이터 처리
			List<ArrivalScheduledDateDto> goodsArrivalScheduledDateDtoList = goodsGoodsBiz
					.intersectionArrivalScheduledDateDtoList(goodsDto.getArrivalScheduledDateDtoList(),
							intersectionAddGoodsArrivalScheduledDateList);
			goodsDto.setArrivalScheduledDateDtoList(goodsArrivalScheduledDateDtoList);
			goodsDto.setArrivalScheduledDateDto(
					goodsGoodsBiz.getLatestArrivalScheduledDateDto(goodsArrivalScheduledDateDtoList));
			if (goodsDto.getArrivalScheduledDateDto() != null) {
				goodsDto.setStockQty(goodsDto.getArrivalScheduledDateDto().getStock());
				goodsDto.setArrivalScheduledDate(goodsDto.getArrivalScheduledDateDto().getArrivalScheduledDate());
			} else {
				goodsDto.setSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
			}

			// 브린지 페이지 이면서 새벽배송 가능할 경우 새벽배송 도착 예정일 처리해야함
			if ("Y".equals(reqDto.getBridgeYn()) && "Y".equals(shippingDto.getDawnDeliveryYn())) {
				// 추가 구성상품 교집합 새벽배송 도착 예정일 리스트 조회
				List<LocalDate> dawnIntersectionAddGoodsArrivalScheduledDateList = goodsGoodsBiz
						.intersectionArrivalScheduledDateListByDto(
								addGoodsDtoList.stream().map(CartAdditionalGoodsDto::getDawnArrivalScheduledDateDtoList)
										.collect(Collectors.toList()));

				// 추가 구성상품을 반복문 돌리면서 교집합 새벽배송 도착 예정일 기준으로 재고 및 스케줄 수정
				addGoodsDtoList.stream().forEach(addGoodsdto -> {
					try {
						List<ArrivalScheduledDateDto> dawnArrivalScheduledDateDtoList = goodsGoodsBiz
								.intersectionArrivalScheduledDateDtoList(
										addGoodsdto.getDawnArrivalScheduledDateDtoList(),
										dawnIntersectionAddGoodsArrivalScheduledDateList);
						addGoodsdto.setDawnArrivalScheduledDateDtoList(dawnArrivalScheduledDateDtoList);
						addGoodsdto.setDawnArrivalScheduledDateDto(
								goodsGoodsBiz.getLatestArrivalScheduledDateDto(dawnArrivalScheduledDateDtoList));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

				// 추가 구성상품 도착 예정일 일정으로 상품 도착 예정일 일정과 교집합 찾아서 데이터 처리
				List<ArrivalScheduledDateDto> dawnGoodsArrivalScheduledDateDtoList = goodsGoodsBiz
						.intersectionArrivalScheduledDateDtoList(goodsDto.getDawnArrivalScheduledDateDtoList(),
								dawnIntersectionAddGoodsArrivalScheduledDateList);
				goodsDto.setDawnArrivalScheduledDateDtoList(dawnGoodsArrivalScheduledDateDtoList);
				goodsDto.setDawnArrivalScheduledDateDto(
						goodsGoodsBiz.getLatestArrivalScheduledDateDto(dawnGoodsArrivalScheduledDateDtoList));
			}
		}
	}

	/**
	 * 기타정보 구성 (찜, 신규특가)
	 *
	 * @param reqDto
	 * @param goodsDto
	 * @throws Exception
	 */
	private void convertGetCartDataEtcGoods(GetCartDataRequestDto reqDto, CartGoodsDto goodsDto) throws Exception {
		boolean isNewBuyerSpecials = false;
		int newBuyerSpecialSsalePrice = 0;
		Long spFavoritesGoodsId = 0L;
		// 정기배송 기존 상품 여부 아닐때만 찜, 신규 특가 처리 하기
		if ("N".equals(goodsDto.getIngRegularGoodsYn())) {
			if (reqDto.isMember()) {
				// 회원인 경우

				// 찜 상품 정보
				spFavoritesGoodsId = shoppingFavoritesBiz.getGoodsFavorites(reqDto.getUrUserId().toString(),
						goodsDto.getIlGoodsId());

				// 신규회원 특가
				int newBuyerSpecialsCouponByUserMap = userBuyerBiz.getNewBuyerSpecialsCouponByUser(
						goodsDto.getIlGoodsId(), reqDto.getUrUserId().toString(), reqDto.getDeviceInfo(),
						reqDto.isApp());
				if (newBuyerSpecialsCouponByUserMap != 0) {
					isNewBuyerSpecials = true;
					newBuyerSpecialSsalePrice = newBuyerSpecialsCouponByUserMap;
				}
			} else {
				// 비회원인 경우

				// 신규회원 특가
				int newBuyerSpecialsCouponByNonMemberMap = promotionCouponBiz.getNewBuyerSpecialsCouponByNonMember(
						goodsDto.getIlGoodsId(), reqDto.getDeviceInfo(), reqDto.isApp());
				if (newBuyerSpecialsCouponByNonMemberMap != 0) {
					isNewBuyerSpecials = true;
					newBuyerSpecialSsalePrice = newBuyerSpecialsCouponByNonMemberMap;
				}
			}
		}
		goodsDto.setNewBuyerSpecials(isNewBuyerSpecials);
		goodsDto.setNewBuyerSpecialSsalePrice(newBuyerSpecialSsalePrice);
		goodsDto.setSpFavoritesGoodsId(spFavoritesGoodsId);
	}

	/**
	 * 주소기반으로 배송 가능 여부 체크
	 *
	 * @param reqDto
	 * @param deliveryDto
	 * @param goodsDto
	 * @param goodsResultVo
	 * @param shippingAreaVo
	 * @return
	 * @throws Exception
	 */
	private ShippingPossibilityStoreDeliveryAreaDto convertGetCartDataShippingPossibility(GetCartDataRequestDto reqDto, CartDeliveryDto deliveryDto,
																						  CartGoodsDto goodsDto, BasicSelectGoodsVo goodsResultVo, ShippingAreaVo shippingAreaVo, ShippingPossibilityStoreDeliveryAreaDto storeDeliveryInfo, boolean isDawnDelivery) throws Exception {
		Boolean isShippingPossibility = true;
		String shippingImpossibilityMsg = "";
		// 주소 데이터가 있을 경우만 체크
		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = null;

		if (!"Y".equals(reqDto.getPresentYn()) && reqDto.getReceiverZipCode() != null && reqDto.getBuildingCode() != null
				&& !reqDto.getReceiverZipCode().isEmpty() && !reqDto.getBuildingCode().isEmpty()) {

			// 출고처별 배송 불가 기능 추가
			WarehouseUnDeliveryableInfoDto warehouseUnDeliveryableInfoDto = storeWarehouseBiz.getUnDeliverableInfo(goodsDto.getUrWareHouseId(), reqDto.getReceiverZipCode(), isDawnDelivery);
			isShippingPossibility = warehouseUnDeliveryableInfoDto.isShippingPossibility();
			shippingImpossibilityMsg = warehouseUnDeliveryableInfoDto.getShippingImpossibilityMsg();

			if (isShippingPossibility) {
				// 상품 타입별 배송 불가 체크
				if (GoodsEnums.SaleType.DAILY.getCode().equals(goodsDto.getSaleType())) {

					// 판매상태에 따른 스토어 타입
					String storeType = goodsGoodsBiz.getStoreTypeBySaleType(goodsDto.getSaleType());

					// 공급처ID에 따른 배송가능 품목유형
					List<String> storeDeliveralbeItemTypeBySupplierIdList = goodsGoodsBiz
							.getStoreDeliverableItemTypeBySupplierId(goodsDto.getUrSupplierId());

					// 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면 isShippingPossibility 가능
					shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(
							storeType, storeDeliveralbeItemTypeBySupplierIdList, reqDto.getReceiverZipCode(),
							reqDto.getBuildingCode());

					if (DeliveryType.NORMAL.getCode().equals(deliveryDto.getDeliveryType())) {
						// 베이비밀 일반 배송일때 권역정보가 없거나 택배 배송 권역이 아닌경우 배송불가 처리
						if (shippingPossibilityStoreDeliveryAreaInfo == null
								|| !StoreApiDeliveryIntervalCode.PARCEL.getCommonCode().equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
							isShippingPossibility = false;
						}
					} else {
						if (shippingPossibilityStoreDeliveryAreaInfo != null) {
							// 요청값이 있을 경우
							String storeDeliveryType = shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryType();
							if (StoreDeliveryType.HD_OD.getCode().equals(storeDeliveryType)) {
								// 한 주소지에 거래처리 2개 있을수 있기때문에 StoreOverlap = true면 다시 조회해야함
								if (shippingPossibilityStoreDeliveryAreaInfo.isStoreOverlap()) {
									if (GoodsDailyType.GREENJUICE.getCode().equals(goodsDto.getGoodsDailyType())) {
										if (CollectionUtils.isNotEmpty(reqDto.getArrivalGoods())) {
											ArrivalGoodsDto arrivalGoodsDto = reqDto
													.findArrivalGoodsDto(goodsDto.getSpCartId());
											if (arrivalGoodsDto != null) {
												storeDeliveryType = arrivalGoodsDto.getStoreDeliveryTypeCode();
											}
										}
									} else {
										// 녹즙이 아닌경우 주소가 중첩일경우 가정집으로 기본 처리
										storeDeliveryType = StoreDeliveryType.HOME.getCode();
									}
									if (StoreDeliveryType.HOME.getCode().equals(storeDeliveryType)
											|| StoreDeliveryType.OFFICE.getCode().equals(storeDeliveryType)) {
										shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz
												.getShippingPossibilityStoreDeliveryAreaInfo(storeType,
														storeDeliveralbeItemTypeBySupplierIdList, reqDto.getReceiverZipCode(),
														reqDto.getBuildingCode(), storeDeliveryType);
									}
								} else {
									// isStoreOverlap = false (UR_STORE_DELIVERY_AREA 가 1개 이지만 HD_OD 코드인경우)
									if (!GoodsDailyType.GREENJUICE.getCode().equals(goodsDto.getGoodsDailyType())) {
										// 녹즙을 제외한 잇슬림 베이비밀은 가정집으로 처리
										storeDeliveryType = StoreDeliveryType.HOME.getCode();
									} else {
										if (CollectionUtils.isNotEmpty(reqDto.getArrivalGoods())) {
											ArrivalGoodsDto arrivalGoodsDto = reqDto
													.findArrivalGoodsDto(goodsDto.getSpCartId());
											if (arrivalGoodsDto != null) {
												storeDeliveryType = arrivalGoodsDto.getStoreDeliveryTypeCode();
											}
										}
									}
								}
							}

							// 베이비밀 일괄 배송 일때
							if("Y".equals(goodsDto.getGoodsDailyBulkYn())) {
								if (!StoreApiDeliveryIntervalCode.EVERY.getCommonCode()
										.equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())
										&& !StoreApiDeliveryIntervalCode.TWO_DAYS.getCommonCode().equals(
										shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
									// 매일, 격일이 아닌 경우 배송 불가 처리
									isShippingPossibility = false;
								}
							} else {
								// 주소 변경시 식단 주기 체크하여 배송 불가 처리
								if (!GoodsCycleType.DAY1_PER_WEEK.getCode().equals(goodsDto.getGoodsDailyCycleType())
										&& !GoodsCycleType.DAYS3_PER_WEEK.getCode().equals(goodsDto.getGoodsDailyCycleType())) {
									if (!StoreApiDeliveryIntervalCode.EVERY.getCommonCode()
											.equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
										isShippingPossibility = false;
									}
								}
								if (StoreApiDeliveryIntervalCode.PARCEL.getCommonCode()
										.equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())
										|| StoreApiDeliveryIntervalCode.NON_DELV.getCommonCode().equals(
										shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
									// 택배, 미배송 일 경우 배송 불가 처리
									isShippingPossibility = false;
								}
							}
							goodsDto.setStoreDeliveryTypeCode(storeDeliveryType);
							goodsDto.setStoreDeliveryTypeName(
									StoreDeliveryType.findByCode(storeDeliveryType).getDeliveryName());
							if (shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId() != null) {
								goodsDto.setUrStoreId(Long.parseLong(shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId()));
							}
						} else {
							isShippingPossibility = false;
						}
					}
					// ---- end 일일상품 주소기반으로 배송 가능 여부 체크
				} else if (DeliveryType.SHOP_DELIVERY.getCode().equals(deliveryDto.getDeliveryType())
						|| DeliveryType.SHOP_PICKUP.getCode().equals(deliveryDto.getDeliveryType())) {
					// 매장 배송일때 권역 정보 확인
					if (storeDeliveryInfo == null) {
						isShippingPossibility = false;
					}
				} else {
					// 품목 도서산간 제주 배송불가 여부
					isShippingPossibility = !goodsShippingTemplateBiz
							.isUnDeliverableArea(goodsResultVo.getUndeliverableAreaType(), shippingAreaVo);

					// 배송 불가 사유값 조회
					shippingImpossibilityMsg = getShippingImpossibilityMsg(isShippingPossibility, goodsResultVo.getUndeliverableAreaType(), shippingAreaVo);

				}
			}

			// 주소기반 배송 정책으로 인한 도서산관,제주 지역 배송 가능여부 체크
			if (isShippingPossibility) {
				if (shippingAreaVo != null) {
					ShippingDataResultVo subShippingDataResultVo = goodsShippingTemplateBiz
							.getShippingInfoByShippingTmplId(goodsDto.getIlShippingTmplId());
					isShippingPossibility = !goodsShippingTemplateBiz
							.isUnDeliverableArea(subShippingDataResultVo.getUndeliverableAreaType(), shippingAreaVo);

					// 배송 불가 사유값 조회
					shippingImpossibilityMsg = getShippingImpossibilityMsg(isShippingPossibility, subShippingDataResultVo.getUndeliverableAreaType(), shippingAreaVo);
				}
			}
		}
		goodsDto.setShippingPossibility(isShippingPossibility);
		goodsDto.setShippingImpossibilityMsg(shippingImpossibilityMsg);
		return shippingPossibilityStoreDeliveryAreaInfo;
	}

	// 배송 불가 사유값 조회 : 한 우편번호에 배송불가권역유형이 두개 이상일때 최초 등록된 배송불가권역유형의 메시지로 안내
	private String getShippingImpossibilityMsg(boolean isShippingPossibility, String undeliverableAreaTp, ShippingAreaVo shippingAreaVo) throws Exception {
		String nonDeliveryAreaMessage = "";
		if(isShippingPossibility){
			return nonDeliveryAreaMessage;
		}

		if(StringUtil.isEmpty(undeliverableAreaTp) || shippingAreaVo == null || StringUtil.isEmpty(shippingAreaVo.getZipCode())){
			return nonDeliveryAreaMessage;
		}

		String[] arrayUndeliverableAreaTp = {undeliverableAreaTp};

		if(GoodsEnums.UndeliverableAreaType.A1_A2.getCode().equals(undeliverableAreaTp)){
			arrayUndeliverableAreaTp = new String[] {WarehouseEnums.UndeliverableType.ISLAND.getCode(), WarehouseEnums.UndeliverableType.JEJU.getCode()};
		}

		if(GoodsEnums.UndeliverableAreaType.A1.getCode().equals(undeliverableAreaTp)){
			arrayUndeliverableAreaTp = new String[] {WarehouseEnums.UndeliverableType.ISLAND.getCode()};
		}

		if(GoodsEnums.UndeliverableAreaType.A2.getCode().equals(undeliverableAreaTp)){
			arrayUndeliverableAreaTp = new String[] {WarehouseEnums.UndeliverableType.JEJU.getCode()};
		}

		NonDeliveryAreaInfoVo nonDeliveryAreaInfoVo = policyShipareaBiz.getNonDeliveryAreaInfo(arrayUndeliverableAreaTp, shippingAreaVo.getZipCode());
		if(nonDeliveryAreaInfoVo != null){
			nonDeliveryAreaMessage = nonDeliveryAreaInfoVo.getNonDeliveryAreaMessage();
		}

		return nonDeliveryAreaMessage;

	}

	/**
	 * 일일배송 기본 선택일 데이터 수정
	 *
	 * @param goodsDto
	 * @param shippingPossibilityStoreDeliveryAreaInfo
	 * @throws Exception
	 */
	private void convertGetCartDataDailyGoodsScheduled(CartGoodsDto goodsDto, ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo) throws Exception {
		if (GoodsEnums.SaleType.DAILY.getCode().equals(goodsDto.getSaleType())) {
			// 주기가 일주일에 한번일때 권역에 따라서 선택일자 수정해야함
			List<ArrivalScheduledDateDto> list = goodsGoodsBiz.returnListByStoreAreaInfo(shippingPossibilityStoreDeliveryAreaInfo, goodsDto.getArrivalScheduledDateDtoList());
			// 배송주기에 따라 선택일자 요일별로 선택 되어야함
			if(!"Y".equals(goodsDto.getGoodsDailyBulkYn())) {
				String weekCode = null;
				if (GoodsCycleType.DAY1_PER_WEEK.getCode().equals(goodsDto.getGoodsDailyCycleType())) {
					weekCode = goodsDto.getGoodsDailyCycleGreenJuiceWeekType()[0];
				}
				list = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(list, goodsDto.getUrWareHouseId(), goodsDto.getGoodsDailyCycleType(), weekCode);
			}
			goodsDto.setArrivalScheduledDateDtoList(list);
			goodsDto.setArrivalScheduledDateDto(goodsGoodsBiz.getLatestArrivalScheduledDateDto(list));
		}
	}

	/**
	 * 배송정책별 상품 summery 금액 처리
	 *
	 * @param goodsDto
	 * @param discountPrice
	 * @return
	 * @throws Exception
	 */
	private GetCartDataGoodsSummeryResposeDto returnGetCartDataGoodsSummery(CartGoodsDto goodsDto) throws Exception {
		GetCartDataGoodsSummeryResposeDto res = new GetCartDataGoodsSummeryResposeDto();
		if(GoodsEnums.GoodsType.RENTAL.getCode().equals(goodsDto.getGoodsType())){
			// 렌탈 상품
			res.setGoodsQtyByShipping(goodsDto.getQty());
		} else if (goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())
				&& goodsDto.getBuyPurchaseType().equals(BuyPurchaseType.BUY_POSSIBLE.getCode())
				&& goodsDto.isShippingPossibility()) {
			// 판매중인 상품 && 회원 구매 권한 타입이 구매 가능일때 && 배송 가능 상품대해서만 금액 합산
			int goodsQtyByShipping = goodsDto.getQty();
			int goodsRecommendedPrice = goodsDto.getRecommendedPriceMltplQty();
			int goodsSalePrice = goodsDto.getSalePriceMltplQty();
			int goodsPaymentPrice = goodsDto.getPaymentPrice();
			int goodsDiscountPrice = goodsRecommendedPrice - goodsPaymentPrice;
			int goodsTaxFreePaymentPrice = 0;
			int goodsTaxPaymentPrice = 0;

			if (goodsDto.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
				// 묶음 상품의 경우
				for (CartGoodsPackageDto goodsPackage : goodsDto.getGoodsPackage()) {
					if ("N".equals(goodsPackage.getTaxYn())) {
						goodsTaxFreePaymentPrice += goodsPackage.getPaymentPrice();
					} else {
						goodsTaxPaymentPrice += goodsPackage.getPaymentPrice();
					}
				}
			} else if (ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())
					|| ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()
					.equals(goodsDto.getCartPromotionType())) {
				goodsQtyByShipping = 0;
				// 골라담기
				for (CartPickGoodsDto pickGoods : goodsDto.getPickGoods()) {
					goodsQtyByShipping += pickGoods.getDeliveryQty();
					if ("N".equals(pickGoods.getTaxYn())) {
						goodsTaxFreePaymentPrice += pickGoods.getPaymentPrice();
					} else {
						goodsTaxPaymentPrice += pickGoods.getPaymentPrice();
					}
				}
			} else {
				// 기타
				if ("N".equals(goodsDto.getTaxYn())) {
					goodsTaxFreePaymentPrice += goodsDto.getPaymentPrice();
				} else {
					goodsTaxPaymentPrice += goodsDto.getPaymentPrice();
				}
			}

			// 추가 구성상품 금액 합산
			// 추가 구성 상품에는 할인이 없음
			for (CartAdditionalGoodsDto additionalGood : goodsDto.getAdditionalGoods()) {
				//추가 구성 상품 수량으로 상품 수량당 배송비 측정은 X
				//goodsQtyByShipping += additionalGood.getQty();
				goodsRecommendedPrice += additionalGood.getRecommendedPrice() * additionalGood.getQty();
				goodsSalePrice += additionalGood.getPaymentPrice();
				goodsDiscountPrice += (additionalGood.getRecommendedPrice() * additionalGood.getQty()) - additionalGood.getPaymentPrice();
				goodsPaymentPrice += additionalGood.getPaymentPrice();
				if ("N".equals(additionalGood.getTaxYn())) {
					goodsTaxFreePaymentPrice += additionalGood.getPaymentPrice();
				} else {
					goodsTaxPaymentPrice += additionalGood.getPaymentPrice();
				}
			}
			res.setGoodsQtyByShipping(goodsQtyByShipping);
			res.setGoodsRecommendedPrice(goodsRecommendedPrice);
			res.setGoodsSalePrice(goodsSalePrice);
			res.setGoodsPaymentPrice(goodsPaymentPrice);
			res.setGoodsDiscountPrice(goodsDiscountPrice);
			res.setGoodsTaxFreePaymentPrice(goodsTaxFreePaymentPrice);
			res.setGoodsTaxPaymentPrice(goodsTaxPaymentPrice);
		}
		return res;
	}

	/**
	 * 배송 정책별 스케줄 정보 구성
	 *
	 * @param reqDto
	 * @param shippingDto
	 * @param goodsListDto
	 * @throws Exception
	 */
	private void convertGetCartDataShippingScheduled(GetCartDataRequestDto reqDto, CartShippingDto shippingDto,
			List<CartGoodsDto> goodsListDto) throws Exception {
		//선물하기는 맨 마지막 일자로 고정
		if ("Y".equals(reqDto.getPresentYn())) {
			// 판매대기상품 판매 가능여부에 따른 판매상태
			List<List<ArrivalScheduledDateDto>> arrivalScheduledDateDtoList = goodsListDto.stream().filter(_goodsDto -> (
								_goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())
								&& _goodsDto.getBuyPurchaseType().equals(BuyPurchaseType.BUY_POSSIBLE.getCode())
								&& _goodsDto.isShippingPossibility()))
					.map(CartGoodsDto::getArrivalScheduledDateDtoList).collect(Collectors.toList());

			List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(arrivalScheduledDateDtoList);
			if(intersectionArrivalScheduledDateList != null && !intersectionArrivalScheduledDateList.isEmpty()) {
				shippingDto.setArrivalScheduledDate(intersectionArrivalScheduledDateList.get(intersectionArrivalScheduledDateList.size() - 1));
			}
		}
		// 상품 별 도착 예정일 교집합 조회 후 배송 정책쪽에 set (만약 교집합 없을경우 합집합 맨처음일자로 처리)
		else if ("Y".equals(reqDto.getBridgeYn())) {
			// 판매대기상품 판매 가능여부에 따른 판매상태
			List<List<ArrivalScheduledDateDto>> arrivalScheduledDateDtoList = goodsListDto.stream().filter(_goodsDto -> (
					((!reqDto.isBosCreateOrder() && _goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode()))
							|| (reqDto.isBosCreateOrder() && (_goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode()) || _goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.WAIT.getCode()))))
							&& _goodsDto.getBuyPurchaseType().equals(BuyPurchaseType.BUY_POSSIBLE.getCode())
							&& _goodsDto.isShippingPossibility()))
					.map(CartGoodsDto::getArrivalScheduledDateDtoList).collect(Collectors.toList());

			List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(arrivalScheduledDateDtoList);
			List<LocalDate> choiceArrivalScheduledDateList = goodsGoodsBiz.unionArrivalScheduledDateListByDto(arrivalScheduledDateDtoList);

			shippingDto.setChoiceArrivalScheduledDateList(choiceArrivalScheduledDateList);
			if(intersectionArrivalScheduledDateList != null && !intersectionArrivalScheduledDateList.isEmpty()) {
				shippingDto.setArrivalScheduledDate(intersectionArrivalScheduledDateList.get(0));
			} else {
				if (choiceArrivalScheduledDateList != null && !choiceArrivalScheduledDateList.isEmpty()) {
					shippingDto.setArrivalScheduledDate(choiceArrivalScheduledDateList.get(0));
				}
			}
			if(!arrivalScheduledDateDtoList.isEmpty()) {
				ArrivalScheduledDateDto findDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(arrivalScheduledDateDtoList.get(0), shippingDto.getArrivalScheduledDate());
				if (findDto != null) {
					shippingDto.setOrderDate(findDto.getOrderDate());
					shippingDto.setForwardingScheduledDate(findDto.getForwardingScheduledDate());
				}
			}

			// 브린지 페이지 이면서 새벽배송 가능할 경우 새벽배송 도착 예정일 처리해야함
			if ("Y".equals(shippingDto.getDawnDeliveryYn())) {

				// 상품 별 새벽배송 도착 예정일 교집합 조회 후 배송 정책쪽에 set (만약 교집합 없을경우 합집합 맨처음일자로 처리)
				List<List<ArrivalScheduledDateDto>> dawnArrivalScheduledDateDtoList = goodsListDto.stream().filter(
						_goodsDto -> (_goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())
								&& _goodsDto.getBuyPurchaseType().equals(BuyPurchaseType.BUY_POSSIBLE.getCode())
								&& _goodsDto.isShippingPossibility()))
						.map(CartGoodsDto::getDawnArrivalScheduledDateDtoList).collect(Collectors.toList());

				List<LocalDate> dawnIntersectionChoiceArrivalScheduledDateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(dawnArrivalScheduledDateDtoList);
				List<LocalDate> dawnChoiceArrivalScheduledDateList = goodsGoodsBiz.unionArrivalScheduledDateListByDto(dawnArrivalScheduledDateDtoList);
				shippingDto.setDawnChoiceArrivalScheduledDateList(dawnChoiceArrivalScheduledDateList);

				if(dawnIntersectionChoiceArrivalScheduledDateList != null && !dawnIntersectionChoiceArrivalScheduledDateList.isEmpty()) {
					shippingDto.setDawnArrivalScheduledDate(dawnIntersectionChoiceArrivalScheduledDateList.get(0));
				} else {
					if (dawnChoiceArrivalScheduledDateList != null && dawnChoiceArrivalScheduledDateList.size() > 0) {
						shippingDto.setDawnArrivalScheduledDate(dawnChoiceArrivalScheduledDateList.get(0));
					}
				}
			}
		}
	}


	/**
	 * 배송비 정보 조회
	 *
	 * @param reqDto
	 * @param shippingDto
	 * @param goodsListDto
	 * @param goodsQtyByShipping
	 * @param goodsPriceByShipping
	 * @param isOnlyGiftShippingTemplate
	 * @return
	 * @throws Exception
	 */
	private GetGetCartDataShippingPriceResposeDto getGetCartDataShippingPrice(GetCartDataRequestDto reqDto,
																			  CartShippingDto shippingDto, List<CartGoodsDto> goodsListDto, int goodsQtyByShipping,
																			  int goodsPriceByShipping, boolean isOnlyGiftShippingTemplate) throws Exception {
		GetGetCartDataShippingPriceResposeDto res = new GetGetCartDataShippingPriceResposeDto();
		// 출고지별 배송정책이 여러개중 배송비가 가장 작은 금액으로 처리 필요
		ShippingDataResultVo shippingDataResultVo = null;
		ShippingPriceResponseDto shippingPriceDto = null;
		if (goodsQtyByShipping > 0 || isOnlyGiftShippingTemplate) {
			if ("N".equals(shippingDto.getBundleYn())) {
				// 묶음 배송 아닐때 단일 계산
				shippingDataResultVo = goodsShippingTemplateBiz
						.getShippingInfoByShippingTmplId(shippingDto.getIlShippingTmplId());
				if ("Y".equals(reqDto.getPresentYn())) {
					// 선물하기는 지역별 배송비 체크 안함
					shippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(shippingDataResultVo,
							goodsPriceByShipping, goodsQtyByShipping);
				} else {
					shippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(shippingDataResultVo,
							goodsPriceByShipping, goodsQtyByShipping, reqDto.getReceiverZipCode());
				}
			} else {

				List<Long> ilShippingTmplIds = null;
				// 증정품이 없거나 or 현재 배송정책에 증정품만 포함된 경우
				if (reqDto.getGift() == null || isOnlyGiftShippingTemplate) {
					ilShippingTmplIds = goodsListDto.stream().map(CartGoodsDto::getIlShippingTmplId).distinct()
							.collect(Collectors.toList());
				} else {

					ilShippingTmplIds = goodsListDto.stream()
							.filter(f -> !GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsType()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsType()))
							.map(CartGoodsDto::getIlShippingTmplId).distinct().collect(Collectors.toList());
				}

				// 상품의 배송정책 중 가장 작은 금액으로 처리
				if (ilShippingTmplIds != null) {
					for (Long ilShippingTmplId : ilShippingTmplIds) {
						ShippingDataResultVo subShippingDataResultVo = goodsShippingTemplateBiz
								.getShippingInfoByShippingTmplId(ilShippingTmplId);
						ShippingPriceResponseDto subShippingPriceDto = null;
						if ("Y".equals(reqDto.getPresentYn())) {
							// 선물하기는 지역별 배송비 체크 안함
							subShippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(subShippingDataResultVo,
									goodsPriceByShipping, goodsQtyByShipping);
						} else {
							subShippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(subShippingDataResultVo,
									goodsPriceByShipping, goodsQtyByShipping, reqDto.getReceiverZipCode());
						}
						if (shippingPriceDto == null
								|| shippingPriceDto.getShippingPrice() > subShippingPriceDto.getShippingPrice()) {
							shippingPriceDto = subShippingPriceDto;
							shippingDto.setIlShippingTmplId(ilShippingTmplId);
						}
					}
				}
			}

			if(StoreDeliveryType.PICKUP.getCode().equals(reqDto.getStoreDeliveryType())
					|| CartType.RENTAL.getCode().equals(reqDto.getCartType())) {
				res.setShippingRecommendedPrice(0);
				res.setOriginShippingPrice(0);
				res.setFreeShippingForNeedGoodsPrice(0);
			} else {
				// 현재 배송정책에 증정품만 있는경우, 배송비는 0원, 정산배송비금액은 배송정책의 배송비 세팅
				if (isOnlyGiftShippingTemplate) {
					res.setBaiscShippingPrice(0);
					res.setRegionShippingPrice(0);
					res.setShippingRecommendedPrice(0);
					res.setOriginShippingPrice(shippingPriceDto.getShippingPrice());
				} else {
					res.setBaiscShippingPrice(shippingPriceDto.getBaiscShippingPrice());
					res.setRegionShippingPrice(shippingPriceDto.getRegionShippingPrice());
					res.setShippingRecommendedPrice(shippingPriceDto.getShippingPrice());
					res.setOriginShippingPrice(shippingPriceDto.getShippingPrice());
				}
				res.setFreeShippingForNeedGoodsPrice(shippingPriceDto.getFreeShippingForNeedGoodsPrice());
			}
		}
		return res;
	}

	/**
	 * 배송비 쿠폰 적용
	 *
	 * @param reqDto
	 * @param shippingTemplateDataIndex
	 * @param shippingDto
	 * @param shippingRecommendedPrice
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	private GetCartDataShippingCouponResposeDto convertGetCartDataShippingCoupon(GetCartDataRequestDto reqDto,
																				 int shippingTemplateDataIndex, CartShippingDto shippingDto, int shippingRecommendedPrice,
																				 DeviceType deviceType) throws Exception {
		GetCartDataShippingCouponResposeDto res = new GetCartDataShippingCouponResposeDto();
		UseShippingCouponDto useShippingCouponDto = reqDto.findUseShippingCoupon(shippingTemplateDataIndex);
		if (useShippingCouponDto != null) {

			// getShippingCouponApplicationListByUser 에서 필요한 금액 정보 SET
			shippingDto.setShippingPaymentPrice(shippingRecommendedPrice);

			CouponDto useShippingCouponInfo = promotionCouponBiz.getShippingCouponApplicationListByUser(
					reqDto.getUrUserId(), shippingDto, deviceType, useShippingCouponDto.getPmCouponIssueId());

			if (useShippingCouponInfo != null && useShippingCouponInfo.isActive() == true) {
				shippingDto.setPmCouponIssueId(useShippingCouponInfo.getPmCouponIssueId());

				res.setShippingDiscountPrice(useShippingCouponInfo.getDiscountPrice());
				res.setShippingPaymentPrice(useShippingCouponInfo.getPaymentPrice());
			} else {
				res.setShippingDiscountPrice(0);
				res.setShippingPaymentPrice(shippingRecommendedPrice);
			}
		} else {
			res.setShippingDiscountPrice(0);
			res.setShippingPaymentPrice(shippingRecommendedPrice);
		}
		return res;
	}

	/**
	 * 상품 도착 예정일 기준으로 상품 정보 일괄 수정
	 *
	 * @param shippingDto
	 * @param goodsListDto
	 * @throws Exception
	 */
	private void convertGetCartDataShippingScheduledGoodsData(CartDeliveryTypeGroupByVo deliveryTypeData, CartShippingDto shippingDto,
			List<CartGoodsDto> goodsListDto) throws Exception {
		// 예약 배송이 아닐 경우만 예정일 변경
		if (!deliveryTypeData.getDeliveryType().equals(ShoppingEnums.DeliveryType.RESERVATION.getCode())) {
			goodsListDto.stream()
					.forEach(_goodsDto -> {
						// 도착 예정일 기준으로 재고 처리
						List<ArrivalScheduledDateDto> arrivalScheduledDateList = _goodsDto
								.getArrivalScheduledDateDtoList();
						if (arrivalScheduledDateList != null && arrivalScheduledDateList.size() > 0) {
							try {
								ArrivalScheduledDateDto arrivalScheduledDateDto = null;
								if (shippingDto.getArrivalScheduledDate() != null) {
									arrivalScheduledDateDto = goodsGoodsBiz
											.getArrivalScheduledDateDtoByArrivalScheduledDate(arrivalScheduledDateList,
													shippingDto.getArrivalScheduledDate());
								} else {
									arrivalScheduledDateDto = _goodsDto.getArrivalScheduledDateDto();
								}
								if (arrivalScheduledDateDto != null) {
									_goodsDto.setArrivalScheduledDateDto(arrivalScheduledDateDto);
									_goodsDto.setStockQty(arrivalScheduledDateDto.getStock());
									_goodsDto.setForwardingScheduledDate(
											arrivalScheduledDateDto.getForwardingScheduledDate());
									_goodsDto
											.setArrivalScheduledDate(arrivalScheduledDateDto.getArrivalScheduledDate());
								} else {
									_goodsDto.setArrivalScheduledDateDto(null);
									_goodsDto.setStockQty(0);
									_goodsDto.setForwardingScheduledDate(null);
									_goodsDto.setArrivalScheduledDate(null);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
			}
	}

	/**
	 * 정기배송 선택 도착예정일 리스트
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	public List<LocalDate> getRegularShippingArrivalScheduledDateList(List<CartDeliveryDto> cartDataDto)
			throws Exception {
		List<List<LocalDate>> goodsArrivalScheduledDateList = shoppingCartService
				.getShippingArrivalScheduledDateList(cartDataDto);
		return goodsGoodsBiz.intersectionArrivalScheduledDateList(goodsArrivalScheduledDateList);
	}

	/**
	 * get 장바구니 summary data
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public CartSummaryDto getCartDataSummary(List<CartDeliveryDto> cartDataDto) throws Exception {
		return shoppingCartService.getCartDataSummary(cartDataDto);
	}

	/**
	 * get 장바구니 summary data (포인트 사용)
	 */
	@Override
	public CartSummaryDto getCartDataSummary(List<CartDeliveryDto> cartDataDto, int usePoint) throws Exception {
		return shoppingCartService.getCartDataSummary(cartDataDto, usePoint);
	}

	/**
	 * 임직원 한도 초가 여부
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isEmployeeDiscountExceedingLimit(List<CartDeliveryDto> cartDataDto) throws Exception {
		return shoppingCartService.isEmployeeDiscountExceedingLimit(cartDataDto);
	}

	/**
	 * 장바구니에서 판매중인 상품만 추출
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Long> getSaleGoodsIdListByCartData(List<CartDeliveryDto> cartDataDto) throws Exception {
		return shoppingCartService.getSaleGoodsIdListByCartData(cartDataDto);
	}

	/**
	 * 무통장 입금 결제 가능 여부
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getVirtualAccountYn(List<CartDeliveryDto> cartDataDto) throws Exception {
		return shoppingCartService.getVirtualAccountYn(cartDataDto);
	}

	/**
	 * 무통장 입금 결제 가능 여부 - 선물하기 포함
	 */
	@Override
	public String getVirtualAccountYn(List<CartDeliveryDto> cartDataDto, String presentYn) throws Exception {
		return shoppingCartService.getVirtualAccountYn(cartDataDto, presentYn);
	}

	/**
	 * 배송타입&출고처별 장바구니 예정금액 조회
	 *
	 * @param cartDataDto
	 * @return List<CartSummaryShippingDto>
	 * @throws Exception
	 */
	@Override
	public List<CartSummaryShippingDto> getCartShippingSummary(List<CartDeliveryDto> cartDataDto) throws Exception {
		return shoppingCartService.getCartShippingSummary(cartDataDto);
	}

	/**
	 * cartData에서 배송정책별 리스트 추출
	 *
	 * @param List<CartDeliveryDto>
	 * @return List<CartShippingDto>
	 * @throws Exception
	 */
	@Override
	public List<CartShippingDto> getCartShippingList(List<CartDeliveryDto> cartDataDto) throws Exception {
		return shoppingCartService.getCartShippingList(cartDataDto);
	}

	/**
	 * 배송정책별 리스트에서 상품정보만 추출
	 *
	 * @param List<CartShippingDto>
	 * @return List<CartGoodsDto>
	 * @throws Exception
	 */
	@Override
	public List<CartGoodsDto> getCartGoodsList(List<CartShippingDto> cartShippingDto) throws Exception {
		return shoppingCartService.getCartGoodsList(cartShippingDto);
	}

	/**
	 * 골라라담기 상품 목록 조회
	 *
	 * @param spCartId, isMember, isEmployee, isDawnDelivery, arrivalScheduledDate
	 * @return List<CartPickGoodsDto>
	 * @throws Exception
	 */
	@Override
	public List<CartPickGoodsDto> getCartPickGoodsList(Long spCartId, boolean isMember, boolean isEmployee,
			boolean isDawnDelivery, LocalDate arrivalScheduledDate, int buyQty, String goodsDailyCycleType, HashMap<String, Integer> overlapBuyItem) throws Exception {
		List<CartPickGoodsDto> pickGoodsDtoList = new ArrayList<>();
		List<SpCartPickGoodsVo> cartPickGoodsList = shoppingCartService.getCartPickGoodsList(spCartId);

		// 골라담기 상품 정보 set
		for (SpCartPickGoodsVo cartPickGoods : cartPickGoodsList) {
			CartPickGoodsDto pickGoodsDto = new CartPickGoodsDto(cartPickGoods);

			GoodsRequestDto pickGoodsRequestDto = GoodsRequestDto.builder().ilGoodsId(pickGoodsDto.getIlGoodsId())
					.deviceInfo(DeviceUtil.getDirInfo()).isApp(DeviceUtil.isApp()).isMember(isMember)
					.isEmployee(isEmployee).isDawnDelivery(isDawnDelivery).arrivalScheduledDate(arrivalScheduledDate)
					.goodsDailyCycleType(goodsDailyCycleType)
					.buyQty(buyQty * pickGoodsDto.getGoodsQty())
					.overlapBuyItem(overlapBuyItem)
					.build();

			BasicSelectGoodsVo packageGoods = goodsGoodsBiz.getGoodsBasicInfo(pickGoodsRequestDto);
			pickGoodsDto.setByBasicSelectGoodsVo(packageGoods, buyQty);
			pickGoodsDtoList.add(pickGoodsDto);
		}

		// 골라담기 구성 상품은 도착 예정일 같은 상품끼리 재고 처리 해야함
		List<LocalDate> pickIntersectionArrivalScheduledDateList = goodsGoodsBiz
				.intersectionArrivalScheduledDateListByDto(pickGoodsDtoList.stream()
						.map(CartPickGoodsDto::getArrivalScheduledDateDtoList).collect(Collectors.toList()));

		pickGoodsDtoList.stream().forEach(dto -> {
			try {
				List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = goodsGoodsBiz
						.intersectionArrivalScheduledDateDtoList(dto.getArrivalScheduledDateDtoList(),
								pickIntersectionArrivalScheduledDateList);
				dto.setArrivalScheduledDateDtoList(arrivalScheduledDateDtoList);
				dto.setArrivalScheduledDateDto(
						goodsGoodsBiz.getLatestArrivalScheduledDateDto(arrivalScheduledDateDtoList));

				// 골라담기상품 재고정보
				int stock = 0;
				if (arrivalScheduledDateDtoList != null && !arrivalScheduledDateDtoList.isEmpty()) {
					stock = dto.getArrivalScheduledDateDto().getStock();
					if (stock > 0) {
						// 골라담기상품 구성상품의 실 재고수량 = 구성상품 재고 / 골라담기상품 구성수량
						stock = (int) Math.floor(stock / dto.getGoodsQty());
					}
				}
				dto.setStockQty(stock);
			} catch (Exception e) {
				log.error("getCartPickGoodsList error =====>", e);
			}
		});

		return pickGoodsDtoList;
	}

	/**
	 * 골라담기상품 재계산
	 *
	 * @param List<PackageGoodsListDto>
	 * @return RecalculationPackageDto
	 * @throws Exception
	 */
	@Override
	public RecalculationPackageDto getRecalculationCartPickGoodsList(String goodsSaleStatus, List<CartPickGoodsDto> cartPickGoodsDtoList)
			throws Exception {
		RecalculationPackageDto recalculationPackageDto = new RecalculationPackageDto();
		recalculationPackageDto.setSaleStatus(goodsSaleStatus);

		List<ArrivalScheduledDateDto> tempPickArrivalScheduledDateDtoList = null;
		ArrivalScheduledDateDto tempArrivalScheduledDateDto = null;

		for (CartPickGoodsDto dto : cartPickGoodsDtoList) {

			// 골라담기 구성상품중 하나라도 판매상태가 판매중이 아니면 일시품절 처리
			if (!dto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
				recalculationPackageDto.setSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
			}

			// 골라담기 상품 스케줄별 최소 수량 구하기 위한 로직
			if (tempPickArrivalScheduledDateDtoList == null || dto.getArrivalScheduledDateDtoList() == null) {
				tempPickArrivalScheduledDateDtoList = dto.getArrivalScheduledDateDtoList();
			} else {
				tempPickArrivalScheduledDateDtoList = goodsGoodsBiz.getMinStockArrivalScheduledDateDtoList(
						tempPickArrivalScheduledDateDtoList, dto.getArrivalScheduledDateDtoList());
			}
		}

		tempArrivalScheduledDateDto = goodsGoodsBiz
				.getLatestArrivalScheduledDateDto(tempPickArrivalScheduledDateDtoList);

		int stock = 0;
		if (tempPickArrivalScheduledDateDtoList != null) {
			stock = tempPickArrivalScheduledDateDtoList.stream().map(ArrivalScheduledDateDto::getStock).max(Integer::compare).orElse(0);
		}

		recalculationPackageDto.setArrivalScheduledDateDtoList(tempPickArrivalScheduledDateDtoList);
		recalculationPackageDto.setArrivalScheduledDateDto(tempArrivalScheduledDateDto);
		recalculationPackageDto.setStockQty(stock);
		recalculationPackageDto.setSaleStatus(goodsGoodsBiz.getSaleStatus(recalculationPackageDto.getSaleStatus(), stock));

		return recalculationPackageDto;
	}

	/**
	 * 증정품 기획전 조회 및 validation
	 *
	 * @param List<CartDeliveryDto>
	 * @return List<GiftListResponseDto>
	 * @throws Exception
	 */
	@Override
	public List<GiftListResponseDto> giftGetValidation(List<CartDeliveryDto> cartDataDto, String userStatus,
													   Long urGroupId, boolean isMember, boolean isEmployee, String presentYn) throws Exception {
		String dirInfo = DeviceUtil.getDirInfo();
		boolean isApp = DeviceUtil.isApp();
		List<Long> cartGoodsId = new ArrayList<>();
		List<Long> cartDpBrandId = new ArrayList<>();
		List<GiftListVo> giftList = new ArrayList<>();

		for (CartDeliveryDto cartData : cartDataDto) {
			for (CartShippingDto cartShipping : cartData.getShipping()) {
				for (CartGoodsDto cartGoods : cartShipping.getGoods()) {
					// 녹즙 골라담기, 균일가 골라담기는 증정행사에 제외
					// 매장배송, 매장 픽업, 렌탈 배송, 무형은 증정행사 제외
					// 일일 배송상품 증정행사 제외
					if (!CartPromotionType.EXHIBIT_SELECT.getCode().equals(cartGoods.getCartPromotionType())
							&& !CartPromotionType.GREENJUICE_SELECT.getCode().equals(cartGoods.getCartPromotionType())
							&& !DeliveryType.SHOP_DELIVERY.getCode().equals(cartData.getDeliveryType())
							&& !DeliveryType.SHOP_PICKUP.getCode().equals(cartData.getDeliveryType())
							&& !DeliveryType.RENTAL.getCode().equals(cartData.getDeliveryType())
							&& !DeliveryType.INCORPOREITY.getCode().equals(cartData.getDeliveryType())
							&& !GoodsEnums.SaleType.DAILY.getCode().equals(cartGoods.getSaleType())) {

						cartGoodsId.add(cartGoods.getIlGoodsId());
						cartDpBrandId.add(cartGoods.getDpBrandId());
						List<GiftListVo> giftResponseDto = promotionExhibitBiz.getGiftList(GiftListRequestDto.builder()
								.ilGoodsId(cartGoods.getIlGoodsId()).urBrandId(cartGoods.getUrBrandId())
								.dpBrandId(cartGoods.getDpBrandId()).userStatus(userStatus).urGroupId(urGroupId)
								.deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode()).build());
						giftList.addAll(giftResponseDto);
					}
				}
			}
		}

		// 카트 금액 가져오기
		CartSummaryDto cartSummaryDto = shoppingCartService.getCartDataSummary(cartDataDto);
		List<CartGoodsDto> cartGoodsList = cartDataDto.stream()
				.map(CartDeliveryDto::getShipping)
				.flatMap(Collection::stream)
				.collect(Collectors.toList())
				.stream()
				.map(CartShippingDto::getGoods)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		List<GiftListResponseDto> result = new ArrayList<>();
		List<GiftListVo> giftTargetList = new ArrayList<>();
		Set<Long> exhibitSet = new HashSet<>();

		// 증정행사 중복 제거
		giftList.forEach(vo -> {
			if (!exhibitSet.contains(vo.getEvExhibitId())) {
				exhibitSet.add(vo.getEvExhibitId());
				giftTargetList.add(vo);
			}
		});

		for (GiftListVo vo : giftTargetList) {
			// 상품별일 경우
			if (vo.getGiftType().equals(ExhibitEnums.GiftType.GOODS.getCode())) {
				result.add(new GiftListResponseDto(vo));
				continue;
			}
			// 장바구니별일 경우
			if (vo.getGiftType().equals(ExhibitEnums.GiftType.CART.getCode())) {
				if (vo.getGiftRangeType().equals(ExhibitEnums.GiftRangeType.INCLUDE.getCode())) { // 포함일 경우
					// n원 미만이면 제외
					if (cartSummaryDto.getPaymentPrice() < vo.getOverPrice()) {
						continue;
					}
					result.add(new GiftListResponseDto(vo));
				} else if (vo.getGiftRangeType().equals(ExhibitEnums.GiftRangeType.EQUAL.getCode())) { // 동일일 경우
					// 상품일 경우
					if (vo.getGiftTargetType().equals(ExhibitEnums.GiftTargetType.GOODS.getCode())) {
						// 카트 - 상품별 금액 가져오기
						Map<Long, Integer> cartGoodsRecommendedMap = new HashMap<>();
						for (CartGoodsDto dto : cartGoodsList) {
							cartGoodsRecommendedMap.put(dto.getIlGoodsId(),
									cartGoodsRecommendedMap.getOrDefault(dto.getIlGoodsId(), 0) + dto.getRecommendedPriceMltplQty()
							);
						}

						// 증정조건 설정
						Set<Long> targetGoodsSet = new HashSet<>(vo.getTargetGoods());
						boolean targetCheck = false;
						for (Long goodsId : cartGoodsId) {
							if (targetGoodsSet.contains(goodsId)) {
								// n원 만족이면 포함
								if (cartGoodsRecommendedMap.get(goodsId) >= vo.getOverPrice()) {
									targetCheck = true;
									break;
								}
							}
						}
						if (targetCheck) {
							result.add(new GiftListResponseDto(vo));
							continue;
						}
					}
					// 브랜드일 경우 - 전시 브랜드
					if (vo.getGiftTargetType().equals(ExhibitEnums.GiftTargetType.BRAND.getCode())) {
						// 카트 - 브랜드별 금액 가져오기
						Map<Long, Integer> cartDpBrandRecommendedMap = new HashMap<>();
						for (CartGoodsDto dto : cartGoodsList) {
							cartDpBrandRecommendedMap.put(dto.getDpBrandId(),
									cartDpBrandRecommendedMap.getOrDefault(dto.getDpBrandId(), 0) + dto.getRecommendedPriceMltplQty()
							);
						}

						// 증정조건 설정
						Set<Long> targetBrandSet = new HashSet<>(vo.getTargetBrand());
						boolean targetCheck = false;
						for (Long brandId : cartDpBrandId) {
							if (targetBrandSet.contains(brandId)) {
								// n원 만족이면 포함
								if (cartDpBrandRecommendedMap.get(brandId) >= vo.getOverPrice()) {
									targetCheck = true;
									break;
								}
							}
						}
						if (targetCheck) {
							result.add(new GiftListResponseDto(vo));
						}
					}
				}
			}
		}

		if (!result.isEmpty()) {
			HashMap<Long, CartShippingDto> shippingMap = new HashMap<>();
			for (CartShippingDto shippingDto : shoppingCartService.getCartShippingList(cartDataDto)) {
				if ("Y".equals(shippingDto.getBundleYn())) {
					shippingMap.put(shippingDto.getUrWarehouseId(), shippingDto);
				}
			}
			setGiftStock(result, shippingMap, dirInfo, isApp, isMember, isEmployee, presentYn);
		}

		return result;
	}

	@Override
	public void setGiftStock(List<GiftListResponseDto> giftResponseDto, HashMap<Long, CartShippingDto> shippingMap, String dirInfo, boolean isApp,
							 boolean isMember, boolean isEmployee, String presentYn) throws Exception {
		for (GiftListResponseDto giftVo : giftResponseDto) {
			List<GiftGoodsVo> giftGoodsList = giftVo.getGoods();

			// 증정수량 확인 - 증정 전체
			List<Long> goodsList = giftGoodsList.stream().map(GiftGoodsVo::getIlGoodsId).collect(Collectors.toList());
			int orderCount = orderFrontBiz.getOrderInfoFromGift(giftVo.getEvExhibitId(), goodsList);
			if(giftVo.getGiftCount() <= orderCount){
				continue;
			}

			for (GiftGoodsVo vo : giftGoodsList) {

				boolean isDawnDelivery = false;
				LocalDate arrivalScheduledDate = null;
				// 사은품이 합배송이고 출고처정보가 있다면 해당 일자로 재고 세팅
				if ("Y".equals(vo.getBundleYn())) {
					CartShippingDto shippingDto = shippingMap.get(vo.getUrWareHouseId());
					if (shippingDto != null) {
						isDawnDelivery = "Y".equals(shippingDto.getDawnDeliveryYn());
						arrivalScheduledDate = shippingDto.getArrivalScheduledDate();
					}
				}

				GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(vo.getIlGoodsId())
						.deviceInfo(dirInfo).isApp(isApp).isMember(isMember).isEmployee(isEmployee)
						.isDawnDelivery(isDawnDelivery).arrivalScheduledDate(arrivalScheduledDate)
						.build();

				BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

				// 상품이 없을 경우
				if (goods == null) {
					continue;
				}

				// 상품이 판매중이 아닐때
				if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goods.getSaleStatus())) {
					continue;
				}

				// 상품 재고가 증정 수량보다 작을경우
				if (goods.getStockQty() - vo.getGiftCount() < 0) {
					continue;
				}

				// 선물하기일때
				if ("Y".equals(presentYn)) {
					// 선물 가능 여부 체크
					if (!(PresentYn.Y.getCode().equals(goods.getPresentYn())|| PresentYn.NA.getCode().equals(goods.getPresentYn()))
						|| !goodsGoodsBiz.isPresentPossible(goods.getArrivalScheduledDateDtoList())) {
						continue;
					}
				}

				vo.setStock(goods.getStockQty());
			}
		}
	}

	@Override
	public CheckCartResponseDto checkBuyPossibleCart(GetSessionShippingResponseDto shippingDto, GetCartDataRequestDto cartDataReqDto, List<CartDeliveryDto> cartDataDto, CartStoreDto cartStoreDto) throws Exception {
		CheckCartResponseDto resDto = new CheckCartResponseDto();
		resDto.setResult(ApplyPayment.SUCCESS);

		List<CheckCartGoodsDto> notBuyGoodslist  = new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> goodsLackStockList  = new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> lackLimitMinCntList  = new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> overLimitMaxCntList  = new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> impossiblePresentlist  = new ArrayList<CheckCartGoodsDto>();

		if(cartDataDto == null || cartDataDto.isEmpty()) {
			resDto.setResult(ApplyPayment.NO_CART_LIST);
			return resDto;
		}

		// 배송지 주소 길이 체크
		if (shippingDto.getReceiverAddress2() != null && shippingDto.getReceiverAddress2().length() > 30 && shippingDto.getBosTp() == null) { //FRONT
			resDto.setResult(ApplyPayment.FAIL_RECEIVER_ADDRSS2_LENGTH);
			return resDto;
		} else if(shippingDto.getReceiverAddress2() != null && shippingDto.getReceiverAddress2().length() > 50 && shippingDto.getBosTp().equals("BOS")) { //BOS
			resDto.setResult(ApplyPayment.FAIL_RECEIVER_ADDRSS2_LENGTH_BOS);
			return resDto;
		}

		// 매장 배송일경우 회차 유효성 체크
		if (cartStoreDto != null && cartStoreDto.getUrStoreScheduleId() != null && cartStoreDto.getUrStoreScheduleId() > 0) {
			StoreDeliveryScheduleDto storeDeliveryScheduleDto = storeDeliveryBiz.getStoreScheduleByUrStoreScheduleId(cartStoreDto.getUrStoreScheduleId(), cartStoreDto.getStoreArrivalScheduledDate());
			if (storeDeliveryScheduleDto == null) {
				resDto.setResult(ApplyPayment.REQUIRED_STORE_SCHEDULE_ID);
				return resDto;
			}
			if(!storeDeliveryScheduleDto.isPossibleSelect()) {
				resDto.setResult(ApplyPayment.IMPOSSIBLE_STORE_SCHEDULE);
				return resDto;
			}
		}

		List<CartShippingDto> shippingDtoList = getCartShippingList(cartDataDto);
		List<CartGoodsDto> goodsDtoList = getCartGoodsList(shippingDtoList);
		for (CartGoodsDto cartGoodsDto : goodsDtoList) {

			// 상품 판매 가능 여부 체크
			if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(cartGoodsDto.getSystemSaleStatus())
					|| (!GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(cartGoodsDto.getSaleStatus()) && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(cartGoodsDto.getSaleStatus()))
					|| !cartGoodsDto.isShippingPossibility()
					|| !BuyPurchaseType.BUY_POSSIBLE.getCode().equals(cartGoodsDto.getBuyPurchaseType())
					|| (cartGoodsDto.getStockQty() == 0 && cartGoodsDto.getArrivalScheduledDate() == null)) {

				notBuyGoodslist.add(new CheckCartGoodsDto(cartGoodsDto.getSpCartId()
						, cartGoodsDto.getIlGoodsId()
						, cartGoodsDto.getGoodsName()
						, cartGoodsDto.getSaleStatus()
						, cartGoodsDto.isShippingPossibility()
						, cartGoodsDto.getBuyPurchaseType()
						, cartGoodsDto.getShippingImpossibilityMsg()));
			}

			// 재고가 있지만 구매 수량보다 적을경우
			if (cartGoodsDto.getStockQty() - cartGoodsDto.getQty() < 0) {
				goodsLackStockList.add(new CheckCartGoodsDto(cartGoodsDto.getSpCartId(), cartGoodsDto.getIlGoodsId(), cartGoodsDto.getGoodsName(), cartGoodsDto.getStockQty()));
			}

			// 최소 구매수량 체크
			if(cartGoodsDto.getQty() < cartGoodsDto.getLimitMinCnt()) {
				lackLimitMinCntList.add(new CheckCartGoodsDto(cartGoodsDto.getSpCartId(), cartGoodsDto.getIlGoodsId(), cartGoodsDto.getGoodsName(), cartGoodsDto.getLimitMinCnt()));
			}

			// 최대 구매수량 체크
			if ("Y".equals(cartGoodsDto.getLimitMaxCntYn())) {
				int systemLimitMaxCnt = cartGoodsDto.getLimitMaxCnt();
				int orderGoodsBuyQty = systemLimitMaxCnt;
				if (cartDataReqDto.getUrUserId() != null && cartDataReqDto.getUrUserId() > 0) {
					orderGoodsBuyQty = orderOrderBiz.getOrderGoodsBuyQty(cartGoodsDto.getIlGoodsId(), cartDataReqDto.getUrUserId().toString());
				}
				int limitMaxCnt = systemLimitMaxCnt - orderGoodsBuyQty;
				if (cartGoodsDto.getQty() > limitMaxCnt) {
					overLimitMaxCntList.add(new CheckCartGoodsDto(cartGoodsDto.getSpCartId(), cartGoodsDto.getIlGoodsId(), cartGoodsDto.getGoodsName(), limitMaxCnt));
				}
			}

			// 선물하기 불가 체크
			if ("Y".equals(cartDataReqDto.getPresentYn())) {
				List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = cartGoodsDto.getArrivalScheduledDateDtoList();

				if (!goodsGoodsBiz.isPresentPossible(arrivalScheduledDateDtoList)) {
					impossiblePresentlist.add(new CheckCartGoodsDto(cartGoodsDto.getSpCartId(),cartGoodsDto.getIlGoodsId(), cartGoodsDto.getGoodsName(), cartGoodsDto.getQty()));
				} else {
					// 증정행사 상품의 경우
					if (GoodsEnums.GoodsType.GIFT.getCode().equals(cartGoodsDto.getGoodsType())
							|| GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(cartGoodsDto.getGoodsType())) {
						if (!(PresentYn.Y.getCode().equals(cartGoodsDto.getPresentYn()) || PresentYn.NA.getCode().equals(cartGoodsDto.getPresentYn()))) {
							impossiblePresentlist.add(new CheckCartGoodsDto(cartGoodsDto.getSpCartId(),
									cartGoodsDto.getIlGoodsId(), cartGoodsDto.getGoodsName(), cartGoodsDto.getQty()));
						}
					} else {
						if (!PresentYn.Y.getCode().equals(cartGoodsDto.getPresentYn())) {
							impossiblePresentlist.add(new CheckCartGoodsDto(cartGoodsDto.getSpCartId(),
									cartGoodsDto.getIlGoodsId(), cartGoodsDto.getGoodsName(), cartGoodsDto.getQty()));
						}
					}
				}
			}
		}

		if (!notBuyGoodslist.isEmpty()) {
			resDto.setResult(ApplyPayment.NOT_BUY_GOODS);
			resDto.setGoodsList(notBuyGoodslist);
		} else if (!overLimitMaxCntList.isEmpty()) {
			resDto.setResult(ApplyPayment.OVER_LIMIT_MAX_CNT);
			resDto.setGoodsList(overLimitMaxCntList);
		} else if (!lackLimitMinCntList.isEmpty()) {
			resDto.setResult(ApplyPayment.LACK_LIMIT_MIN_CNT);
			resDto.setGoodsList(lackLimitMinCntList);
		} else if (!goodsLackStockList.isEmpty()) {
			resDto.setResult(ApplyPayment.GOODS_LACK_STOCK);
			resDto.setGoodsList(goodsLackStockList);
		} else if (!impossiblePresentlist.isEmpty()) {
			resDto.setResult(ApplyPayment.IMPOSSIBLE_PRESENT);
			resDto.setGoodsList(impossiblePresentlist);
		} else {
			resDto.setCart(cartDataDto);
		}
		return resDto;
	}

	@Override
	public List<CartStoreShippingDto> getStoreShippingScheduleList(List<CartDeliveryDto> cartData, ShippingPossibilityStoreDeliveryAreaDto storeDeliveryAreaInfo) throws Exception {
		List<CartShippingDto> cartShippingList = getCartShippingList(cartData);
		List<CartGoodsDto> cartGoodsList = getCartGoodsList(cartShippingList);
		List<List<ArrivalScheduledDateDto>> arrivalScheduledDateDtoList = cartGoodsList.stream().filter(dto -> dto.getArrivalScheduledDateDtoList()!= null).map(CartGoodsDto::getArrivalScheduledDateDtoList).collect(Collectors.toList());
		List<CartStoreShippingDto> resDto = new ArrayList<CartStoreShippingDto>();
		if (!arrivalScheduledDateDtoList.isEmpty()) {
			List<LocalDate> arrivalScheduledDateList = goodsGoodsBiz.unionArrivalScheduledDateListByDto(arrivalScheduledDateDtoList);
			List<ArrivalScheduledDateDto> dateDtoList = new ArrayList<ArrivalScheduledDateDto>();
			if (arrivalScheduledDateList != null) {
				arrivalScheduledDateList = arrivalScheduledDateList.stream().limit(2).collect(Collectors.toList());
				for(LocalDate date : arrivalScheduledDateList) {
					ArrivalScheduledDateDto dto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(arrivalScheduledDateDtoList.get(0), date);
					dateDtoList.add(dto);
				}
			}
			resDto.add(convertCartStoreShippingDto(dateDtoList, storeDeliveryAreaInfo));

			String storeDeliveryType = "";
			if (StoreDeliveryType.PICKUP.getCode().equals(storeDeliveryAreaInfo.getStoreDeliveryType())) {
				storeDeliveryType = StoreDeliveryType.DIRECT.getCode();
			} else {
				storeDeliveryType = StoreDeliveryType.PICKUP.getCode();
			}

			// 매장픽업 가능할 경우 스토어정보 조회
			storeDeliveryAreaInfo = storeDeliveryBiz.getUrStoreDeliveryAreaId(storeDeliveryAreaInfo.getUrStoreId(), storeDeliveryType, storeDeliveryAreaInfo.getStoreDeliverableItemType());
			resDto.add(convertCartStoreShippingDto(dateDtoList, storeDeliveryAreaInfo));
		}
		return resDto;
	}

	private CartStoreShippingDto convertCartStoreShippingDto(List<ArrivalScheduledDateDto> dateDtoList, ShippingPossibilityStoreDeliveryAreaDto storeDeliveryAreaInfo) throws Exception {
		CartStoreShippingDto storeShippingDto = new CartStoreShippingDto();
		storeShippingDto.setStoreDeliveryType(storeDeliveryAreaInfo.getStoreDeliveryType());
		storeShippingDto.setStoreDeliveryTypeName(StoreDeliveryType.findByCode(storeDeliveryAreaInfo.getStoreDeliveryType()).getCodeName());
		if (dateDtoList != null) {
			List<CartStoreShippingDateDto> storeShippingDateList = new ArrayList<CartStoreShippingDateDto>();
			for (ArrivalScheduledDateDto dateDto : dateDtoList) {
				CartStoreShippingDateDto storeShippingDateDto = storeDeliveryBiz.convertCartStoreShippingDateDto(dateDto, storeDeliveryAreaInfo);
				storeShippingDateList.add(storeShippingDateDto);
			}
			storeShippingDto.setDate(storeShippingDateList);
		}
		return storeShippingDto;
	}

	@Override
	public CartStoreShippingFastestScheduleDto getStoreFastestSchedule(List<CartStoreShippingDto> list) throws Exception {
		if (list != null) {
			for (CartStoreShippingDto storeShippingDto : list) {
				// 가장 빠른 일자는 매장배송 기준으로
				if (StoreDeliveryType.DIRECT.getCode().equals(storeShippingDto.getStoreDeliveryType())) {
					for (CartStoreShippingDateDto storeShippingDateDto : storeShippingDto.getDate()) {
						if (storeDeliveryBiz.isPossibleSelectStoreSchedule(storeShippingDateDto.getSchedule())) {
							CartStoreShippingFastestScheduleDto resDto = new CartStoreShippingFastestScheduleDto();
							resDto.setArrivalScheduledDate(storeShippingDateDto.getArrivalScheduledDate());
							CartStoreShippingDateScheduleDto scheduleDto = storeShippingDateDto.getSchedule().stream().filter(dto -> dto.isPossibleSelect()).findAny().orElse(null);
							resDto.setStartTime(scheduleDto.getStartTime());
							resDto.setEndTime(scheduleDto.getEndTime());
							return resDto;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean isExistDailyGoodsCart(AddCartInfoRequestDto reqDto) throws Exception{
		return shoppingCartService.isExistDailyGoodsCart(reqDto);
	}

	@Override
	public List<Long> getCartIdList(GetCartDataRequestDto reqDto) throws Exception{
		return shoppingCartService.getCartIdList(reqDto);
	}

	@Override
	public List<SpCartAddGoodsVo> getCartAddGoodsIdList(Long spCartId) throws Exception{
		return shoppingCartService.getCartAddGoodsIdList(spCartId);
	}

	/**
	 * 장바구니 담기 허용 최대 개수 체크
	 *
	 * @param reqDto
	 * @throws Exception
	 */
	public void checkCartMaxCountExceeded(AddCartInfoRequestDto reqDto) throws Exception{

		GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
		boolean isMember = StringUtil.isNotEmpty(reqDto.getUrUserId());
		getCartDataRequestDto.setUrPcidCd(reqDto.getUrPcidCd());
		getCartDataRequestDto.setUrUserId(reqDto.getUrUserId());
		getCartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		getCartDataRequestDto.setApp(DeviceUtil.isApp());
		getCartDataRequestDto.setMember(isMember);
		getCartDataRequestDto.setEmployee(true);
		List<Long> spCartIdList = shoppingCartService.getCartIdList(getCartDataRequestDto);

		if(spCartIdList.size() > CART_MAX_COUNT){
			Collections.sort(spCartIdList, Collections.reverseOrder());

			List<Long> delSpCartIdList = new ArrayList<>(spCartIdList.subList(CART_MAX_COUNT, spCartIdList.size()));
			DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
			delCartRequestDto.setSpCartId(delSpCartIdList);
			delCartAndAddGoods(delCartRequestDto);
		}

	}
}
