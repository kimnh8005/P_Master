package kr.co.pulmuone.v1.shopping.cart.service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.DeliveryType;
import kr.co.pulmuone.v1.comm.mapper.shopping.cart.ShoppingCartMapper;
import kr.co.pulmuone.v1.shopping.cart.dto.*;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 *  1.0    20200831   	 홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartService {

	private final ShoppingCartMapper shoppingCartMapper;

	/**
	 * 배송비 절약 상품 목록
	 *
	 * @param getSaveShippingCostGoodsListRequestDto
	 * @return List<Long>
	 * @throws Exception
	 */
	protected List<Long> getSaveShippingCostGoodsList(
			GetSaveShippingCostGoodsListRequestDto getSaveShippingCostGoodsListRequestDto) throws Exception {
		return shoppingCartMapper.getSaveShippingCostGoodsList(getSaveShippingCostGoodsListRequestDto);
	}

	/**
	 * 장바구니 수정
	 *
	 * @param spCartVo
	 * @return
	 * @throws Exception
	 */
	protected void putCart(SpCartVo spCartVo) throws Exception {
		shoppingCartMapper.putCart(spCartVo);
	}

	/**
	 * 장바구니 삭제
	 *
	 * @param spCartId
	 * @return
	 * @throws Exception
	 */
	protected void delCart(Long spCartId) throws Exception {
		shoppingCartMapper.delCart(spCartId);
	}

	/**
	 * 장바구니 PK로 장바구니 추가 구성상품 삭제
	 *
	 * @param spCartId
	 * @return
	 * @throws Exception
	 */
	protected void delCartAddGoodsBySpCartId(Long spCartId) throws Exception {
		shoppingCartMapper.delCartAddGoodsBySpCartId(spCartId);
	}

	/**
	 * 장바구니 PK로 장바구니 기획전 구성상품 삭제
	 *
	 * @param spCartId
	 * @throws Exception
	 */
	protected void delCartPickGoodsBySpCartId(Long spCartId) throws Exception {
		shoppingCartMapper.delCartPickGoodsBySpCartId(spCartId);
	}

	/**
	 * 장바구니 추가 구성상품 삭제
	 *
	 * @param spCartAddGoodsId
	 * @throws Exception
	 */
	protected void delCartAddGoods(Long spCartAddGoodsId) throws Exception {
		shoppingCartMapper.delCartAddGoods(spCartAddGoodsId);
	}

	/**
	 * 장바구니 타입별 정보
	 *
	 * @param deliveryTypeList, spCartIds
	 * @return int
	 * @throws Exception
	 */
	protected int getCartTypeSummary(List<String> deliveryTypeList, List<Long> spCartIds) throws Exception {
		return shoppingCartMapper.getCartTypeSummary(deliveryTypeList, spCartIds);
	}

	/**
	 * add 장바구니
	 *
	 * @param spCartVo
	 * @return
	 * @throws Exception
	 */
	protected int addCart(SpCartVo spCartVo) throws Exception {
		return shoppingCartMapper.addCart(spCartVo);
	}

	/**
	 * add 장바구니 추가 구성상품
	 *
	 * @param SpCartAddGoodsVo
	 * @return
	 * @throws Exception
	 */
	protected int addCartAddGoods(SpCartAddGoodsVo SpCartAddGoodsVo) throws Exception {
		return shoppingCartMapper.addCartAddGoods(SpCartAddGoodsVo);
	}

	/**
	 * add 장바구니 기획전 유형 골라담기
	 *
	 * @param spCartPickGoodsVo
	 * @return
	 * @throws Exception
	 */
	protected int addCartPickGoods(SpCartPickGoodsVo spCartPickGoodsVo) throws Exception {
		return shoppingCartMapper.addCartPickGoods(spCartPickGoodsVo);
	}

	/**
	 * 장바구니 머지 상품 여부
	 *
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	protected Boolean ifCheckAddCartMerge(Long ilGoodsId) throws Exception {
		return shoppingCartMapper.ifCheckAddCartMerge(ilGoodsId);
	}

	/**
	 * 상품 정보로 장바구니 spCardId get
	 *
	 * @param urPcidCd
	 * @param urUserId
	 * @param ilGoodsId
	 * @param ilGoodsReserveOptionId
	 * @param deliveryType
	 * @return
	 * @throws Exception
	 */
	protected Long getCartIdByIlGoodsId(String urPcidCd, Long urUserId, Long ilGoodsId, Long ilGoodsReserveOptionId, String deliveryType)
			throws Exception {
		return shoppingCartMapper.getCartIdByIlGoodsId(urPcidCd, urUserId, ilGoodsId, ilGoodsReserveOptionId, deliveryType);
	}

	/**
	 * 장바구니 수량 + 업데이트
	 *
	 * @param spCartId
	 * @param qty
	 * @return
	 * @throws Exception
	 */
	protected int putCartPlusQty(Long spCartId, int qty) throws Exception {
		return shoppingCartMapper.putCartPlusQty(spCartId, qty);
	}

	/**
	 * 상품 정보로 장바구니 추가 구성상품 spCardAddGoodsId get
	 *
	 * @param spCartId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	protected Long getCartAddGoodsIdByIlGoodsId(Long spCartId, Long ilGoodsId) throws Exception {
		return shoppingCartMapper.getCartAddGoodsIdByIlGoodsId(spCartId, ilGoodsId);
	}

	/**
	 * 장바구니 추가 구성 수량 + 업데이트
	 *
	 * @param spCartAddGoodsId
	 * @param qty
	 * @return
	 * @throws Exception
	 */
	protected int putCartAddGoodsPlusQty(Long spCartAddGoodsId, int qty) throws Exception {
		return shoppingCartMapper.putCartAddGoodsPlusQty(spCartAddGoodsId, qty);
	}

	/**
	 * 상품 saleType 으로 deliverytype get
	 *
	 * @param saleType
	 * @return
	 * @throws Exception
	 */
	protected DeliveryType getDeliveryTypeBySaleType(String saleType) throws Exception {

		DeliveryType deliveryTypeEnum = null;

		switch (saleType) {
			case "SALE_TYPE.NORMAL":
				deliveryTypeEnum = DeliveryType.NORMAL;
				break;
			case "SALE_TYPE.RESERVATION":
				deliveryTypeEnum = DeliveryType.RESERVATION;
				break;
			case "SALE_TYPE.REGULAR":
				deliveryTypeEnum = DeliveryType.REGULAR;
				break;
			case "SALE_TYPE.DAILY":
				deliveryTypeEnum = DeliveryType.DAILY;
				break;
			case "SALE_TYPE.SHOP":
				deliveryTypeEnum = DeliveryType.SHOP_DELIVERY;
				break;
			default:
				new Exception();
				break;
		}
		return deliveryTypeEnum;
	}

	/**
	 * get 장바구니
	 *
	 * @param spCartId
	 * @return
	 * @throws Exception
	 */
	protected SpCartVo getCart(Long spCartId) throws Exception {
		return shoppingCartMapper.getCart(spCartId);
	}

	/**
	 * 장바구니 담기 validation 체크
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	protected boolean isValidationAddCartInfo(AddCartInfoRequestDto reqDto) throws Exception {
		if (reqDto.getIlGoodsId() == null || !(reqDto.getIlGoodsId() > 0)) {
			return false;
		} else if (!(reqDto.getQty() > 0)) {
			return false;
		} else if (!("Y".equals(reqDto.getBuyNowYn()) || "N".equals(reqDto.getBuyNowYn()))) {
			return false;
		} else if (!"Y".equals(reqDto.getStoreDeliveryYn()) && GoodsEnums.SaleType.RESERVATION.getCode().equals(reqDto.getSaleType())) {
			// 매장배송 아닌 예약판매
			if (reqDto.getIlGoodsReserveOptionId() == null || !(reqDto.getIlGoodsReserveOptionId() > 0)) {
				return false;
			} else {
				return true;
			}
		} else if (GoodsEnums.SaleType.DAILY.getCode().equals(reqDto.getSaleType())) {
			// 일일판매
			if (GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(reqDto.getGoodsDailyType())) {
				// 베이비밀
				if (!("Y".equals(reqDto.getGoodsDailyBulkYn()) || "N".equals(reqDto.getGoodsDailyBulkYn()))) {
					return false;
				} else if (!("Y".equals(reqDto.getGoodsDailyAllergyYn())
						|| "N".equals(reqDto.getGoodsDailyAllergyYn()))) {
					return false;
				} else if ("N".equals(reqDto.getGoodsDailyBulkYn())) {
					// 일일 배송일떄
					if (reqDto.getGoodsDailyCycleType() == null || reqDto.getGoodsDailyCycleType().length() == 0) {
						return false;
					} else if (reqDto.getGoodsDailyCycleTermType() == null
							|| reqDto.getGoodsDailyCycleTermType().length() == 0) {
						return false;
					} else {
						return true;
					}
				} else {
					// 일괄 배송일때
					if (reqDto.getGoodsBulkType() == null || reqDto.getGoodsBulkType().length() == 0) {
						return false;
					} else {
						return true;
					}
				}
			} else if (GoodsEnums.GoodsDailyType.EATSSLIM.getCode().equals(reqDto.getGoodsDailyType())) {
				// 잇슬림
				if (reqDto.getGoodsDailyCycleType() == null || reqDto.getGoodsDailyCycleType().length() == 0) {
					return false;
				} else if (reqDto.getGoodsDailyCycleTermType() == null
						|| reqDto.getGoodsDailyCycleTermType().length() == 0) {
					return false;
				} else {
					return true;
				}
			} else if (GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(reqDto.getGoodsDailyType())) {
				// 녹즙
				if (reqDto.getGoodsDailyCycleType() == null || reqDto.getGoodsDailyCycleType().length() == 0) {
					return false;
				} else if (reqDto.getGoodsDailyCycleTermType() == null
						|| reqDto.getGoodsDailyCycleTermType().length() == 0) {
					return false;
				} else if (reqDto.getGoodsDailyCycleGreenJuiceWeekType() == null
						|| reqDto.getGoodsDailyCycleGreenJuiceWeekType().length == 0) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * get 나의 장바구니 spCartIdList
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	protected List<Long> getCartIdList(GetCartDataRequestDto reqDto) throws Exception {
		return shoppingCartMapper.getCartIdList(reqDto);
	}

	/**
	 * 장바구니 배송타입 group by 리스트 조회
	 *
	 * @param spCartIds
	 * @return
	 * @throws Exception
	 */
	protected CartDeliveryTypeGroupByVo[] getCartDeliveryTypeGroupByList(List<String> deliveryTypeList,
																		 List<Long> spCartIds, List<Long> gift) throws Exception {
		int giftSize = 0;
		if(gift != null && !gift.isEmpty()){
			giftSize = gift.size();
		}
		return shoppingCartMapper.getCartDeliveryTypeGroupByList(deliveryTypeList, spCartIds, gift, giftSize);
	}

	/**
	 * 장바구니 배송타입 으로 출고처별 배송정책 group by 리스트 조회
	 *
	 * @param deliveryType
	 * @param spCartIds
	 * @return
	 * @throws Exception
	 */
	protected ShippingTemplateGroupByVo[] getShippingTemplateGroupByListByDeliveryType(String deliveryType,
																					   List<Long> spCartIds, String bridgeYn, Long urUserId, List<Long> gift) throws Exception {
		return shoppingCartMapper.getShippingTemplateGroupByListByDeliveryType(deliveryType, spCartIds, bridgeYn,
				urUserId, gift);
	}

	/**
	 * get 출고처별 배송 정책 정보로 상품리스트 조회
	 *
	 * @param deliveryType
	 * @param shippingTemplateData
	 * @param spCartIds
	 * @return
	 * @throws Exception
	 */
	protected List<SpCartVo> getGoodsListByShippingPolicy(String deliveryType,ShippingTemplateGroupByVo shippingTemplateData,
														  List<Long> spCartIds) throws Exception {
		return shoppingCartMapper.getGoodsListByShippingPolicy(deliveryType, shippingTemplateData, spCartIds);
	}

	/**
	 * 정기배송 선택 도착예정일 리스트
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	protected List<List<LocalDate>> getShippingArrivalScheduledDateList(List<CartDeliveryDto> cartDataDto)
			throws Exception {

		List<CartShippingDto> cartShippingDtoList = cartDataDto.stream().map(CartDeliveryDto::getShipping)
				.flatMap(Collection::stream).collect(Collectors.toList());

		return cartShippingDtoList.stream().map(CartShippingDto::getChoiceArrivalScheduledDateList)
				.collect(Collectors.toList());
	}

	/**
	 * get 장바구니 summary data
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	protected CartSummaryDto getCartDataSummary(List<CartDeliveryDto> cartDataDto) throws Exception {

		CartSummaryDto reqDto = new CartSummaryDto();

		List<CartShippingDto> cartShippingDtoList = cartDataDto.stream().map(CartDeliveryDto::getShipping)
				.flatMap(Collection::stream).collect(Collectors.toList());

		reqDto.setGoodsRecommendedPrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getGoodsRecommendedPrice)));

		reqDto.setGoodsSalePrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getGoodsSalePrice)));

		reqDto.setGoodsDiscountPrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getGoodsDiscountPrice)));

		reqDto.setGoodsPaymentPrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getGoodsPaymentPrice)));

		reqDto.setGoodsTaxPaymentPrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getGoodsTaxPaymentPrice)));

		reqDto.setGoodsTaxFreePaymentPrice(cartShippingDtoList.stream()
				.collect(Collectors.summingInt(CartShippingDto::getGoodsTaxFreePaymentPrice)));

		reqDto.setShippingRecommendedPrice(cartShippingDtoList.stream()
				.collect(Collectors.summingInt(CartShippingDto::getShippingRecommendedPrice)));

		reqDto.setShippingDiscountPrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getShippingDiscountPrice)));

		reqDto.setShippingPaymentPrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getShippingPaymentPrice)));

		reqDto.setPaymentPrice(
				cartShippingDtoList.stream().collect(Collectors.summingInt(CartShippingDto::getPaymentPrice)));

		reqDto.setTaxPaymentPrice(reqDto.getGoodsTaxPaymentPrice() + reqDto.getShippingPaymentPrice());

		reqDto.setTaxFreePaymentPrice(reqDto.getGoodsTaxFreePaymentPrice());

		// 상품 할인 Group by
		List<CartGoodsDto> cartGoodsDtoList = cartShippingDtoList.stream().map(CartShippingDto::getGoods)
				.flatMap(Collection::stream).collect(Collectors.toList());

		List<CartGoodsDiscountDto> cartGoodsDiscountDtoList = cartGoodsDtoList.stream().map(CartGoodsDto::getDiscount)
				.flatMap(Collection::stream).collect(Collectors.toList());

		// 추가 구성상품 할인내역 sum
		List<CartAdditionalGoodsDto> cartAddGoodsList = cartGoodsDtoList.stream().map(CartGoodsDto::getAdditionalGoods).flatMap(Collection::stream).collect(Collectors.toList());

		List<CartGoodsDiscountDto> cartAddGoodsDiscountList = cartAddGoodsList.stream().map(CartAdditionalGoodsDto::getDiscount).flatMap(Collection::stream).collect(Collectors.toList());
		if(!cartAddGoodsDiscountList.isEmpty()) {
			cartGoodsDiscountDtoList = Stream.concat(cartGoodsDiscountDtoList.stream(), cartAddGoodsDiscountList.stream())
					.collect(Collectors.toList());
		}

		List<CartGoodsDiscountDto> cartGoodsDiscountGroupbyList = new ArrayList<CartGoodsDiscountDto>();

		cartGoodsDiscountDtoList.stream().collect(Collectors.groupingBy(CartGoodsDiscountDto::getDiscountType))
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

		reqDto.setDiscount(cartGoodsDiscountGroupbyList);

		if (!cartGoodsDtoList.isEmpty()) {
			List<CartGoodsDto> goodsDist = cartGoodsDtoList.stream()
					.filter(obj -> !GoodsEnums.GoodsType.GIFT.getCode().equals(obj.getGoodsType()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsType())
							&& !obj.getGoodsType().equals(GoodsEnums.GoodsType.ADDITIONAL.getCode())
					)
					.distinct()
					.collect(Collectors.toList());

			String goodsSummaryName = "";

			if(CollectionUtils.isNotEmpty(goodsDist)){
				goodsSummaryName = goodsDist.get(goodsDist.size() - 1).getGoodsName();
				reqDto.setSummaryGoodsId(goodsDist.get(goodsDist.size() - 1).getIlGoodsId());
				if (goodsDist.size() > 1) {
					goodsSummaryName = goodsSummaryName + " 외 " + (goodsDist.size() - 1) + "건";
				}
			}else{
				// BOS 주문생성 > 증정품만 구매한 경우
				goodsSummaryName = cartGoodsDtoList.get(cartGoodsDtoList.size() - 1).getGoodsName();
				reqDto.setSummaryGoodsId(cartGoodsDtoList.get(cartGoodsDtoList.size() - 1).getIlGoodsId());
				if (cartGoodsDtoList.size() > 1) {
					goodsSummaryName = goodsSummaryName + " 외 " + (cartGoodsDtoList.size() - 1) + "건";
				}
			}

			reqDto.setGoodsSummaryName(goodsSummaryName);
		}
		return reqDto;
	}

	/**
	 * get 장바구니 summary data (포인트 사용)
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	protected CartSummaryDto getCartDataSummary(List<CartDeliveryDto> cartDataDto, int usePoint) throws Exception {

		CartSummaryDto reqDto = getCartDataSummary(cartDataDto);

		int useTaxPoint = 0;
		int useTaxFreePoint = 0;

		int taxPaymentPrice = reqDto.getTaxPaymentPrice();
		int taxFreePaymentPrice = reqDto.getTaxFreePaymentPrice();

		if (taxPaymentPrice > 0 && usePoint > 0) {
			if (taxPaymentPrice > usePoint) {
				useTaxPoint = usePoint;
				taxPaymentPrice -= usePoint;
			} else {
				useTaxPoint = taxPaymentPrice;
				taxPaymentPrice = 0;
			}
		}

		useTaxFreePoint = usePoint - useTaxPoint;
		taxFreePaymentPrice = taxFreePaymentPrice - useTaxFreePoint;

		reqDto.setUsePoint(usePoint);
		reqDto.setUseTaxPoint(useTaxPoint);
		reqDto.setUseTaxFreePoint(useTaxFreePoint);
		reqDto.setPaymentPrice(reqDto.getPaymentPrice() - usePoint);
		reqDto.setTaxPaymentPrice(taxPaymentPrice);
		reqDto.setTaxFreePaymentPrice(taxFreePaymentPrice);

		return reqDto;
	}

	/**
	 * 장바구니 추가 구성 상품 리스트
	 *
	 * @param spCartId
	 * @return
	 * @throws Exception
	 */
	protected List<SpCartAddGoodsVo> getCartAddGoodsIdList(Long spCartId) throws Exception {
		return shoppingCartMapper.getCartAddGoodsIdList(spCartId);
	}

	/**
	 * 장바구니 추가 구성 상품 리스트 (기획전 골라담기)
	 *
	 * @param spCartId
	 * @return
	 * @throws Exception
	 */
	protected List<SpCartAddGoodsVo> getCartAddGoodsIdListByExhibit(Long spCartId) throws Exception {
		return shoppingCartMapper.getCartAddGoodsIdListByExhibit(spCartId);
	}

	/**
	 * 임직원 한도 초가 여부
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	protected boolean isEmployeeDiscountExceedingLimit(List<CartDeliveryDto> cartDataDto) throws Exception {

		List<CartShippingDto> cartShippingDtoList = cartDataDto.stream().map(CartDeliveryDto::getShipping)
				.flatMap(Collection::stream).collect(Collectors.toList());

		List<CartGoodsDto> cartGoodsDtoList = cartShippingDtoList.stream().map(CartShippingDto::getGoods)
				.flatMap(Collection::stream).collect(Collectors.toList());

		List<CartGoodsDiscountDto> cartGoodsDiscountDtoList = cartGoodsDtoList.stream().map(CartGoodsDto::getDiscount)
				.flatMap(Collection::stream).collect(Collectors.toList());

		// 추가 구성상품 할인에 임직원 할인 체크 추가
		List<CartAdditionalGoodsDto> cartAddGoodsList = cartGoodsDtoList.stream().map(CartGoodsDto::getAdditionalGoods).flatMap(Collection::stream).collect(Collectors.toList());

		List<CartGoodsDiscountDto> cartAddGoodsDiscountList = cartAddGoodsList.stream().map(CartAdditionalGoodsDto::getDiscount).flatMap(Collection::stream).collect(Collectors.toList());
		if(!cartAddGoodsDiscountList.isEmpty()) {
			cartGoodsDiscountDtoList = Stream.concat(cartGoodsDiscountDtoList.stream(), cartAddGoodsDiscountList.stream())
					.collect(Collectors.toList());
		}

		for (CartGoodsDiscountDto dto : cartGoodsDiscountDtoList) {
			if (GoodsEnums.GoodsDiscountType.EMPLOYEE.getCode().equals(dto.getDiscountType())
					&& dto.getExceedingLimitPrice() > 0) {
				return true;
			}

		}
		return false;
	}

	/**
	 * 장바구니에서 판매중인 상품만 추출
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	protected List<Long> getSaleGoodsIdListByCartData(List<CartDeliveryDto> cartDataDto) throws Exception {
		List<Long> spCartIds = new ArrayList<>();

		for (int i = 0; i < cartDataDto.size(); i++) {
			List<CartShippingDto> shipping = new ArrayList<>();
			shipping = cartDataDto.get(i).getShipping();

			for (int j = 0; j < shipping.size(); j++) {
				List<CartGoodsDto> goodsList = new ArrayList<>();
				goodsList = shipping.get(j).getGoods();

				for (Iterator<CartGoodsDto> goodsIterator = goodsList.iterator(); goodsIterator.hasNext();) {
					CartGoodsDto goodsDto = goodsIterator.next();

					if (goodsDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
						spCartIds.add(goodsDto.getSpCartId());
					}
				}
			}
		}

		return spCartIds;
	}

	/**
	 * 무통장 입금 결제 가능 여부
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	protected String getVirtualAccountYn(List<CartDeliveryDto> cartDataDto) throws Exception {

		// 매장배송, 예약배송, FRM(제조상품) 포함되어 있는 경우 무통장 입금 제한
		List<CartShippingDto> cartShippingDtoList = cartDataDto.stream().map(CartDeliveryDto::getShipping)
				.flatMap(Collection::stream).collect(Collectors.toList());

		List<CartGoodsDto> cartGoodsDtoList = cartShippingDtoList.stream().map(CartShippingDto::getGoods)
				.flatMap(Collection::stream).collect(Collectors.toList());

		// 매장배송일 경우
		for (CartDeliveryDto cartDeliveryDto : cartDataDto) {
			if (cartDeliveryDto.getDeliveryType().equals(ShoppingEnums.CartType
					.findByCode(ShoppingEnums.CartType.SHOP.getCode()).getDeliveryTypeList().get(0))) {
				return "N";
			}
		}

//		// 새벽배송일 경우
//		for (CartShippingDto cartShippingDto : cartShippingDtoList) {
//			if ("Y".equals(cartShippingDto.getDawnDeliveryYn()))
//				return "N";
//		}

		LocalDate now = LocalDate.now();

		for (CartGoodsDto cartGoodsDto : cartGoodsDtoList) {

			// 예약배송,FRM(제조상품)일 경우
			if (cartGoodsDto.getSaleType().equals(GoodsEnums.SaleType.RESERVATION.getCode())
					|| cartGoodsDto.getSaleType().equals(GoodsEnums.SaleType.SHOP.getCode())) {
				return "N";
			}

			if (cartGoodsDto.getArrivalScheduledDateDto() != null) {
				if (cartGoodsDto.getArrivalScheduledDateDto().getOrderDate() == null
						|| cartGoodsDto.getArrivalScheduledDateDto().getForwardingScheduledDate() == null) {
					return "N";
				}
				// 주문일과 I/F일자가 같은 경우
				if (now.isEqual(cartGoodsDto.getArrivalScheduledDateDto().getOrderDate())) {
					return "N";
				}
				// 주문일과 출고 지시일이 같은 경우
				if (now.isEqual(cartGoodsDto.getArrivalScheduledDateDto().getForwardingScheduledDate())) {
					return "N";
				}
			} else {
				return "N";
			}

//			// 잇슬림일 경우
//			if (GoodsEnums.GoodsDailyType.EATSSLIM.getCode().equals(cartGoodsDto.getGoodsDailyType())) {
//				return "N";
//			}
		}

		return "Y";
	}

	/**
	 * 무통장 입금 결제 가능 여부 - 선물하기 포함
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	protected String getVirtualAccountYn(List<CartDeliveryDto> cartDataDto, String presentYn) throws Exception {
		return "Y".equals(presentYn) ? "N" : getVirtualAccountYn(cartDataDto);
	}

	/**
	 * 배송타입&출고처별 장바구니 예정금액 조회
	 *
	 * @param cartDataDto
	 * @return List<CartSummaryShippingDto>
	 * @throws Exception
	 */
	protected List<CartSummaryShippingDto> getCartShippingSummary(List<CartDeliveryDto> cartDataDto) throws Exception {
		List<CartSummaryShippingDto> cartSummaryShippingDtoList = new ArrayList<>();

		for (CartDeliveryDto deliveryDto : cartDataDto) {
			for (CartShippingDto shippingDto : deliveryDto.getShipping()) {
				CartSummaryShippingDto cartSummaryShippingDto = new CartSummaryShippingDto();
				cartSummaryShippingDto.setDeliveryType(deliveryDto.getDeliveryType());
				cartSummaryShippingDto.setUrWarehouseId(shippingDto.getUrWarehouseId());
				cartSummaryShippingDto.setIlShippingTmplId(shippingDto.getIlShippingTmplId());
				cartSummaryShippingDto.setGoodsRecommendedPrice(shippingDto.getGoodsRecommendedPrice());
				cartSummaryShippingDto.setGoodsDiscountPrice(shippingDto.getGoodsDiscountPrice());
				cartSummaryShippingDto.setShippingRecommendedPrice(shippingDto.getShippingRecommendedPrice());
				cartSummaryShippingDto.setPaymentPrice(shippingDto.getPaymentPrice());
				cartSummaryShippingDto.setFreeShippingForNeedGoodsPrice(shippingDto.getFreeShippingForNeedGoodsPrice());
				cartSummaryShippingDtoList.add(cartSummaryShippingDto);
			}
		}

		return cartSummaryShippingDtoList;
	}

	/**
	 * cartData에서 배송정책별 리스트 추출
	 *
	 * @param cartDataDto
	 * @return List<CartShippingDto>
	 * @throws Exception
	 */
	protected List<CartShippingDto> getCartShippingList(List<CartDeliveryDto> cartDataDto) throws Exception {
		List<CartShippingDto> cartShippingDtoList = new ArrayList<>();

		cartShippingDtoList = cartDataDto.stream().map(CartDeliveryDto::getShipping).flatMap(Collection::stream)
				.collect(Collectors.toList());

		return cartShippingDtoList;
	}

	/**
	 * 배송정책리스트 에서 상품정보만 추출
	 *
	 * @param cartShippingDto
	 * @return List<CartGoodsDto>
	 * @throws Exception
	 */
	protected List<CartGoodsDto> getCartGoodsList(List<CartShippingDto> cartShippingDto) throws Exception {
		List<CartGoodsDto> cartGoodsList = new ArrayList<>();

		cartGoodsList = cartShippingDto.stream().map(CartShippingDto::getGoods).flatMap(Collection::stream)
				.collect(Collectors.toList());

		return cartGoodsList;
	}


	protected List<SpCartPickGoodsVo> getCartPickGoodsList(Long spCartId) throws Exception {
		return shoppingCartMapper.getCartPickGoodsList(spCartId);
	}


	/**
	 * 장바구니용 증정품 리스트
	 */
	protected List<SpCartVo> getGiftGoodsListByShippingPolicy(ShippingTemplateGroupByVo shippingTemplateData, List<CartGiftDto> gift)
			throws Exception {
		return shoppingCartMapper.getGiftGoodsListByShippingPolicy(shippingTemplateData, gift);
	}

	protected void putCartUrUserId(String urPcidCd, Long urUserId) throws Exception {
		shoppingCartMapper.putCartUrUserId(urPcidCd, urUserId);
	}

	protected List<SpCartVo> getMergeCartListByUrUserId(Long urUserId) throws Exception {
		return shoppingCartMapper.getMergeCartListByUrUserId(urUserId);
	}

	/** 장바구니내 일일배송상품 동일상품 담겨있는지 확인 */
	protected boolean isExistDailyGoodsCart(AddCartInfoRequestDto reqDto) throws Exception{
		List<Long> spCartIdList = shoppingCartMapper.getSpCardIdsByIlGoodsId(reqDto);

		if(CollectionUtils.isNotEmpty(spCartIdList)){
			return true;
		}
		return false;
	}
}
