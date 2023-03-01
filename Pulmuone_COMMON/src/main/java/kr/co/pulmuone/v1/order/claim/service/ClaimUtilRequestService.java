package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimRequestMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.util.ClaimPriceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ClaimUtilRequestService {

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    private ClaimRequestMapper claimRequestMapper;

	private ClaimPriceUtil claimPriceUtil = new ClaimPriceUtil();

	/**
	 * 증정상품 클레임 제외여부 유효성 체크 - 상품포함여부 확인
	 * @param item
	 * @param orderClaimGiftGoodsInfoList
	 * @param goodsList
	 * @param orderDetailList
	 * @param goodsSelectList
	 */
    private void validGiftGoodsClaimReqYnByGoods(OrderClaimGoodsInfoDto item,
												  List<OrderClaimGiftGoodsInfoDto> orderClaimGiftGoodsInfoList,
												  List<OrderClaimGoodsInfoDto> goodsList,
												  List<OrderClaimGoodsInfoDto> orderDetailList,
												  List<OrderClaimSearchGoodsDto> goodsSelectList) {

		// 선택한 주문 상세번호의 증정품이 아닌 상품정보를 얻는다
		List<OrderClaimGoodsInfoDto> ilGoodsIdList = goodsList.stream().filter(x -> goodsSelectList.stream().anyMatch(selectItem -> x.getOdOrderDetlId() == selectItem.getOdOrderDetlId()) &&
																					!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																					!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd())).collect(toList());
		// 증정품이 아닌 상품 대상, 전체 취소가 아니고, 현재 선택되지 않은 상품 목록
		List<OrderClaimGoodsInfoDto> validGoodsList = orderDetailList.stream()	.filter(x -> (x.getOrgOrderCnt() - x.getCancelCnt() > 0) &&
																								ilGoodsIdList.stream().noneMatch(selectItem -> x.getIlGoodsId() == selectItem.getIlGoodsId()))
																				.collect(toList());
		long matchCount = 0;
		if(CollectionUtils.isNotEmpty(validGoodsList)) {
			// 주문 상품 중 전체 취소가 아닌 상품 목록에
			// 증정품 대상 상품 목록에 포함되어 있을 경우 클레임 요청 제외 처리
			// ex) 증정품 대상 상품 목록 -> 1, 2, 3
			//     취소 대상 상품 목록 -> 2
			//     위와 같을 경우 아직 1, 3이 존재하기 때문에 증정품은 클레임취소제외 처리한다
			matchCount = orderClaimGiftGoodsInfoList.stream().filter(targetItem -> validGoodsList.stream().anyMatch(orderItem -> orderItem.getIlGoodsId() == targetItem.getTargetIlGoodsId())).count();
			if (matchCount > 0) {
				// 증정품클레임처리요청여부가 N : 클레임처리 요청제외 처리
				item.setGiftClaimReqYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
			}
		}

		// 증정품클레임요청 값이 N 이 아닐 경우 체크
		if(!OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode().equals(item.getGiftClaimReqYn())) {
			// 선택한 상품들 중 전체 취소가 아닌 건 조회
			List<OrderClaimSearchGoodsDto> selectOrderList = goodsSelectList.stream()
																			.filter(obj -> obj.getOrderCnt() - obj.getCancelCnt() - obj.getClaimCnt() > 0)
																			.collect(toList());
			if (CollectionUtils.isNotEmpty(selectOrderList)) {

				// 선택한 주문 상세번호의 증정품이 아닌 상품정보를 얻는다
				List<OrderClaimGoodsInfoDto> selectIdGoodsIdList = goodsList.stream().filter(x -> selectOrderList.stream().anyMatch(selectItem -> x.getOdOrderDetlId() == selectItem.getOdOrderDetlId()) &&
																									!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																									!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd())).collect(toList());
				// 선택 상품 중 전체 취소가 아닌 상품 중 대상 상품 목록에 포함되어 있을 경우 클레임 요청 제외 처리
				// ex) 취소 대상 상품 수량정보 -> 주문수량 : 2, 기존 취소수량 : 0, 클레임 신청수량 : 1
				//     위와 같을 경우 해당 주문의 전체취소가 아니기 때문에 클레임취소제외 처리 한다
				matchCount = orderClaimGiftGoodsInfoList.stream().filter(targetItem -> selectIdGoodsIdList.stream().anyMatch(orderItem -> orderItem.getIlGoodsId() == targetItem.getTargetIlGoodsId())).count();
				if (matchCount > 0) {
					// 증정품클레임처리요청여부가 N : 클레임처리 요청제외 처리
					item.setGiftClaimReqYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
				}
			}
		}
	}

	/**
	 * 증정상품 클레임 제외여부 유효성 체크 - 브랜드포함여부 확인
	 * @param item
	 * @param orderClaimGiftGoodsInfoList
	 * @param goodsList
	 * @param orderDetailList
	 * @param goodsSelectList
	 */
    private void validGiftGoodsClaimReqYnByBrand(OrderClaimGoodsInfoDto item,
												  List<OrderClaimGiftGoodsInfoDto> orderClaimGiftGoodsInfoList,
												  List<OrderClaimGoodsInfoDto> goodsList,
												  List<OrderClaimGoodsInfoDto> orderDetailList,
												  List<OrderClaimSearchGoodsDto> goodsSelectList) {

		// 선택한 주문 상세번호의 증정품이 아닌 상품정보를 얻는다
		List<OrderClaimGoodsInfoDto> ilGoodsIdList = goodsList.stream().filter(x -> goodsSelectList.stream().anyMatch(selectItem -> x.getOdOrderDetlId() == selectItem.getOdOrderDetlId()) &&
																					!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																					!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd())).collect(toList());
		// 증정품이 아닌 상품 대상, 전체 취소가 아니고, 전시브랜드가 존재하고, 현재 선택되지 않은 상품 목록
		List<OrderClaimGoodsInfoDto> validGoodsList = orderDetailList.stream()	.filter(x -> (x.getOrgOrderCnt() - x.getCancelCnt() > 0) &&
																								ilGoodsIdList.stream().noneMatch(selectItem -> x.getIlGoodsId() == selectItem.getIlGoodsId()) &&
																								x.getDpBrandId() > 0)
																				.collect(toList());
		long matchCount = 0;
		if(CollectionUtils.isNotEmpty(validGoodsList)) {
			// 주문 상품 중 전체 취소가 아닌 상품 목록에
			// 증정품 대상 브랜드 목록에 포함되어 있을 경우 클레임 요청 제외 처리
			// ex) 증정품 대상 브랜드 목록 -> 1, 2, 3
			//     취소 대상 상품 목록의 브랜드 -> 2
			//     위와 같을 경우 아직 1, 3이 존재하기 때문에 증정품은 클레임취소제외 처리한다
			matchCount = orderClaimGiftGoodsInfoList.stream().filter(targetItem -> validGoodsList.stream().anyMatch(orderItem -> orderItem.getDpBrandId() == targetItem.getTargetBrandId())).count();
			if (matchCount > 0) {
				// 증정품클레임처리요청여부가 N : 클레임처리 요청제외 처리
				item.setGiftClaimReqYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
			}
		}

		// 증정품클레임요청 값이 N 이 아닐 경우 체크
		if(!OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode().equals(item.getGiftClaimReqYn())) {
			// 선택한 상품들 중 전체 취소가 아닌 건 조회
			List<OrderClaimSearchGoodsDto> selectOrderList = goodsSelectList.stream()
																			.filter(obj -> obj.getOrderCnt() - obj.getCancelCnt() - obj.getClaimCnt() > 0)
																			.collect(toList());
			if (CollectionUtils.isNotEmpty(selectOrderList)) {

				// 선택한 주문 상세번호의 증정품이 아닌 상품정보를 얻는다
				List<OrderClaimGoodsInfoDto> selectIdGoodsIdList = goodsList.stream().filter(x -> selectOrderList.stream().anyMatch(selectItem -> x.getOdOrderDetlId() == selectItem.getOdOrderDetlId()) &&
																									!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																									!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd())).collect(toList());
				// 선택 상품 중 전체 취소가 아닌 상품 중 대상 상품 목록에 포함되어 있을 경우 클레임 요청 제외 처리
				// ex) 취소 대상 상품 수량정보 -> 주문수량 : 2, 기존 취소수량 : 0, 클레임 신청수량 : 1
				//     위와 같을 경우 해당 주문의 전체취소가 아니기 때문에 클레임취소제외 처리 한다
				matchCount = orderClaimGiftGoodsInfoList.stream().filter(targetItem -> selectIdGoodsIdList.stream().anyMatch(orderItem -> orderItem.getDpBrandId() == targetItem.getTargetBrandId())).count();
				if (matchCount > 0) {
					// 증정품클레임처리요청여부가 N : 클레임처리 요청제외 처리
					item.setGiftClaimReqYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
				}
			}
		}
	}

	/**
	 * 증정상품 클레임 제외여부 유효성 체크 - 포함상품금액 계산
	 * @param orderClaimGiftGoodsInfoList
	 * @param goodsList
	 * @param goodsSelectList
	 */
    private int validGiftGoodsClaimModPriceByGoods(List<OrderClaimGiftGoodsInfoDto> orderClaimGiftGoodsInfoList,
													List<OrderClaimGoodsInfoDto> goodsList,
													List<OrderClaimSearchGoodsDto> goodsSelectList) {
    	int modPrice = 0;
		// 선택한 주문 상세번호의 증정품이 아닌 상품정보를 얻는다
		List<OrderClaimGoodsInfoDto> ilGoodsIdList = goodsList.stream().filter(x -> goodsSelectList.stream().anyMatch(selectItem -> x.getOdOrderDetlId() == selectItem.getOdOrderDetlId()) &&
																					!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																					!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd())).collect(toList());
		List<OrderClaimGoodsInfoDto> matchGoodsList = ilGoodsIdList.stream()
																	.filter(selectItem -> orderClaimGiftGoodsInfoList.stream().anyMatch(targetItem -> targetItem.getTargetIlGoodsId() == selectItem.getIlGoodsId()))
																	.collect(toList());
		if(CollectionUtils.isNotEmpty(matchGoodsList)) {
			for (OrderClaimGoodsInfoDto matchItem : matchGoodsList) {
				int claimCnt = goodsSelectList.stream().filter(x -> x.getOdOrderDetlId() == matchItem.getOdOrderDetlId()).mapToInt(x -> x.getClaimCnt()).findAny().orElse(0);
				// 전체 취소가 아닐 경우
				if ((matchItem.getCancelCnt() + claimCnt) < matchItem.getOrgOrderCnt()) {
					// 전체 주문금액 - ((기존 신청된 클레임 수량 + 현재 신청 클레임 수량)의 비율 계산)
					modPrice += matchItem.getTotSalePrice() - claimPriceUtil.getFloorRatePrice(matchItem.getTotSalePrice(),
																								(matchItem.getCancelCnt() + claimCnt),
																								matchItem.getOrgOrderCnt());
				}
			}
		}

		// 증정품이 아닌 상품 대상, 전체 취소가 아니고, 전시브랜드가 존재하고, 현재 선택되지 않은 상품 목록
		List<OrderClaimGoodsInfoDto> validGoodsList = goodsList.stream()	.filter(x -> (x.getOrgOrderCnt() - x.getCancelCnt() > 0) &&
																							ilGoodsIdList.stream().noneMatch(selectItem -> x.getIlGoodsId() == selectItem.getIlGoodsId()) &&
																							!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																							!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd()))
																			.collect(toList());
		if(CollectionUtils.isNotEmpty(validGoodsList)) {
			// 주문 상품 중 전체 취소가 아닌 상품 목록에
			// 증정품 대상 상품 목록에 포함되어 있을 경우 잔액 합산 처리
			// ex) 증정품 대상 상품 목록 -> 1, 2, 3
			//     취소 대상 상품 목록 -> 2
			//     위와 같을 경우 아직 1, 3이 존재하기 때문에 해당 상품 잔액을 합산 한다
			List<OrderClaimGoodsInfoDto> nonMatchGoodsList = validGoodsList.stream().filter(orderItem -> orderClaimGiftGoodsInfoList.stream().anyMatch(targetItem -> orderItem.getIlGoodsId() == targetItem.getTargetIlGoodsId())).collect(toList());
			if(CollectionUtils.isNotEmpty(nonMatchGoodsList)) {
				for (OrderClaimGoodsInfoDto nonMatchItem : nonMatchGoodsList) {
					// 전체 취소가 아닐 경우
					if (nonMatchItem.getCancelCnt() < nonMatchItem.getOrgOrderCnt()) {
						// 전체 주문금액 - ((기존 신청된 클레임 수량 + 현재 신청 클레임 수량)의 비율 계산)
						modPrice += nonMatchItem.getTotSalePrice() - claimPriceUtil.getFloorRatePrice(nonMatchItem.getTotSalePrice(),
																										nonMatchItem.getCancelCnt(),
																										nonMatchItem.getOrgOrderCnt());
					}
				}
			}
		}
    	return modPrice;
	}

	/**
	 * 증정상품 클레임 제외여부 유효성 체크 - 포함브랜드금액 계산
	 * @param orderClaimGiftGoodsInfoList
	 * @param goodsList
	 * @param goodsSelectList
	 */
    private int validGiftGoodsClaimModPriceByBrand(List<OrderClaimGiftGoodsInfoDto> orderClaimGiftGoodsInfoList,
													List<OrderClaimGoodsInfoDto> goodsList,
													List<OrderClaimSearchGoodsDto> goodsSelectList) {
    	int modPrice = 0;
		// 선택한 주문 상세번호의 증정품이 아닌 상품정보를 얻는다
		List<OrderClaimGoodsInfoDto> ilGoodsIdList = goodsList.stream().filter(x -> goodsSelectList.stream().anyMatch(selectItem -> x.getOdOrderDetlId() == selectItem.getOdOrderDetlId()) &&
																					!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																					!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd())).collect(toList());
		List<OrderClaimGoodsInfoDto> matchGoodsList = ilGoodsIdList.stream()
																	.filter(selectItem -> orderClaimGiftGoodsInfoList.stream().anyMatch(targetItem -> targetItem.getTargetBrandId() == selectItem.getDpBrandId()))
																	.collect(toList());
		if(CollectionUtils.isNotEmpty(matchGoodsList)) {
			for (OrderClaimGoodsInfoDto matchItem : matchGoodsList) {
				int claimCnt = goodsSelectList.stream().filter(x -> x.getOdOrderDetlId() == matchItem.getOdOrderDetlId()).mapToInt(x -> x.getClaimCnt()).findAny().orElse(0);
				// 전체 취소가 아닐 경우
				if ((matchItem.getCancelCnt() + claimCnt) < matchItem.getOrgOrderCnt()) {
					// 전체 주문금액 - ((기존 신청된 클레임 수량 + 현재 신청 클레임 수량)의 비율 계산)
					modPrice += matchItem.getTotSalePrice() - claimPriceUtil.getFloorRatePrice(matchItem.getTotSalePrice(),
																								(matchItem.getCancelCnt() + claimCnt),
																								matchItem.getOrgOrderCnt());
				}
			}
		}

		// 증정품이 아닌 상품 대상, 전체 취소가 아니고, 전시브랜드가 존재하고, 현재 선택되지 않은 상품 목록
		List<OrderClaimGoodsInfoDto> validGoodsList = goodsList.stream()	.filter(x -> (x.getOrgOrderCnt() - x.getCancelCnt() > 0) &&
																					ilGoodsIdList.stream().noneMatch(selectItem -> x.getIlGoodsId() == selectItem.getIlGoodsId()) &&
																					x.getDpBrandId() > 0 &&
																					!GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) &&
																					!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd()))
																			.collect(toList());
		if(CollectionUtils.isNotEmpty(validGoodsList)) {
			// 주문 상품 중 전체 취소가 아닌 상품 목록에
			// 증정품 대상 브랜드 목록에 포함되어 있을 경우 잔액 합산 처리
			// ex) 증정품 대상 브랜드 목록 -> 1, 2, 3
			//     취소 대상 상품 목록의 브랜드 -> 2
			//     1, 3 브랜드에 대한 상품 잔액정보를 합산한다
			List<OrderClaimGoodsInfoDto> nonMatchGoodsList = validGoodsList.stream().filter(orderItem -> orderClaimGiftGoodsInfoList.stream().anyMatch(targetItem -> orderItem.getDpBrandId() == targetItem.getTargetBrandId())).collect(toList());
			if(CollectionUtils.isNotEmpty(nonMatchGoodsList)) {
				for (OrderClaimGoodsInfoDto nonMatchItem : nonMatchGoodsList) {
					// 전체 취소가 아닐 경우
					if (nonMatchItem.getCancelCnt() < nonMatchItem.getOrgOrderCnt()) {
						// 전체 주문금액 - ((기존 신청된 클레임 수량 + 현재 신청 클레임 수량)의 비율 계산)
						modPrice += nonMatchItem.getTotSalePrice() - claimPriceUtil.getFloorRatePrice(nonMatchItem.getTotSalePrice(),
																										nonMatchItem.getCancelCnt(),
																										nonMatchItem.getOrgOrderCnt());
					}
				}
			}
		}

    	return modPrice;
	}

	/**
	 * 증정품 취소 가능 여부 Set
	 * @param goodsList
	 * @param goodsSelectList
	 * @param refundInfoDto
	 * @return
	 */
	public List<OrderClaimGoodsInfoDto> setClaimGiftGoodsPsbYn(List<OrderClaimGoodsInfoDto> goodsList, List<OrderClaimSearchGoodsDto> goodsSelectList, OrderClaimPriceInfoDto refundInfoDto) {
		List<OrderClaimGoodsInfoDto> giftList = goodsList.stream()
														.filter(obj -> (GoodsEnums.GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd())
																		|| GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
																		&& obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum()
																		&& obj.getClaimRegistCnt() < 1)
														.collect(toList());

		// 기획전 증정품 목록 Set
		List<OrderClaimGoodsInfoDto> evExhibitList = giftList.stream().filter(x -> x.getEvExhibitId() > 0).collect(toList());
		// 기획전 증정품이 존재할 경우
		if(CollectionUtils.isNotEmpty(evExhibitList)) {

			// 증정품 제거
			List<OrderClaimGoodsInfoDto> orderDetailList = goodsList.stream()
																	.filter(obj -> !GoodsEnums.GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
																	.collect(toList());

			evExhibitList.stream().forEach(item -> {
				long evExhibitId = item.getEvExhibitId();
				// 증정품 대상 상품 목록 정보 조회
				List<OrderClaimGiftGoodsInfoDto> orderClaimGiftGoodsInfoList = claimRequestMapper.getOdClaimTargetEvExhibitInfo(evExhibitId);
				// 증정품 대상 상품 목록 정보 존재 시
				if(CollectionUtils.isNotEmpty(orderClaimGiftGoodsInfoList)) {

					// 적용대상타입이 상품일경우 상품포함여부 확인
					if (ExhibitEnums.GiftTargetType.GOODS.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftTargetTp())) {
						this.validGiftGoodsClaimReqYnByGoods(item, orderClaimGiftGoodsInfoList, goodsList, orderDetailList, goodsSelectList);
					}
					// 적용대상타입이 브랜드일경우 브랜드포함여부 확인
					else if (ExhibitEnums.GiftTargetType.BRAND.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftTargetTp())) {
						this.validGiftGoodsClaimReqYnByBrand(item, orderClaimGiftGoodsInfoList, goodsList, orderDetailList, goodsSelectList);
					}

					// 증정조건이 상품별 증정일 경우 금액 확인은 하지 않는다
					// 장바구니별 증정일 경우 금액까지 확인한다
					if (ExhibitEnums.GiftType.CART.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftTp())) {
						// 클레임처리 요청 제외 되어 있으면 포함 된 상품
						// 포함 되어 있을 경우, 금액 체크
						if (OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode().equals(item.getGiftClaimReqYn())) {
							item.setGiftClaimReqYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());
							// 해당 상품의 잔여 금액 조회
							int modPaymentPrice = 0;
							// 적용대상상품이 동일일 경우 : 적용대상 상품금액 합계 금액이 000 원 이상인 경우
							// 해당 상품 및 브랜드가 무조건 있고,
							// 해당 상품 및 브랜드의 잔여 금액이 000원 이상인 경우
							if (ExhibitEnums.GiftRangeType.EQUAL.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftRangeTp())) {
								// 적용대상타입이 상품일경우 상품잔액 가져오기
								if (ExhibitEnums.GiftTargetType.GOODS.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftTargetTp())) {
									modPaymentPrice = this.validGiftGoodsClaimModPriceByGoods(orderClaimGiftGoodsInfoList, goodsList, goodsSelectList);
								}
								// 적용대상타입이 브랜드일경우 브랜드잔액 가져오기
								else if (ExhibitEnums.GiftTargetType.BRAND.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftTargetTp())) {
									modPaymentPrice = this.validGiftGoodsClaimModPriceByBrand(orderClaimGiftGoodsInfoList, goodsList, goodsSelectList);
								}
								if (modPaymentPrice >= orderClaimGiftGoodsInfoList.get(0).getOverPrice()) {
									// 증정품클레임처리요청여부가 N : 클레임처리 요청제외 처리
									item.setGiftClaimReqYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
								}
							}
							// 적용대상상품이 포함일 경우 : 적용대상 상품금액이 1개라도 포함되어있고, 잔여 금액이 000 원 이상인 경우
							else if (ExhibitEnums.GiftRangeType.INCLUDE.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftRangeTp())) {
								// 클레임 처리 후 남은 결제정보 합계
								modPaymentPrice = refundInfoDto.getRemainPaymentPrice() + refundInfoDto.getRemainPointPrice();
								// 해당 금액 이상일 경우
								if (modPaymentPrice >= orderClaimGiftGoodsInfoList.get(0).getOverPrice()) {
									// 증정품클레임처리요청여부가 N : 클레임처리 요청제외 처리
									item.setGiftClaimReqYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
								}
							}
						}
					} // END else if(ExhibitEnums.GiftType.CART.getCode().equals(orderClaimGiftGoodsInfoList.get(0).getGiftTp())) {
				}
			});
		}
		// 증정품 세팅
		return giftList;
	}

	public List<OrderClaimGoodsInfoDto> getClaimGiftGoodsList(List<OrderClaimGoodsInfoDto> goodsList){
		// 증정품 세팅
		return goodsList.stream()
				.filter(obj -> (GoodsEnums.GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd())
								|| GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
								&& obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum()
								&& obj.getClaimRegistCnt() < 1)
				.collect(toList());
	}

	public List<OrderClaimGoodsListDto> getClaimGoodsList(List<OrderClaimGoodsInfoDto> goodsList, OrderClaimViewRequestDto reqDto){
		List<OrderClaimGoodsListDto> goodsResultList = new ArrayList<>();
		String claimStatusTp = reqDto.getClaimStatusTp();
		// 증정품 제거
		List<OrderClaimGoodsInfoDto> orderDetailList = goodsList.stream()
				.filter(obj -> !GoodsEnums.GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
				.collect(toList());

		// 주문상세 뎁스가 2일 경우 목록 얻기 - 묶음상품
		Map<Long, List<OrderClaimGoodsInfoDto>> subItemInfo = goodsList.stream()
				.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_SECOND.getDepthNum())
				.collect(groupingBy(OrderClaimGoodsInfoDto::getOdOrderDetlParentId, LinkedHashMap::new, toList()));
		// 주문상세 뎁스가 3일 경우 목록 얻기 - 재배송
		Map<Long, List<OrderClaimGoodsInfoDto>> reDeliveryItemInfo = goodsList.stream()
				.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_THIRD.getDepthNum())
				.collect(groupingBy(OrderClaimGoodsInfoDto::getOdOrderDetlParentId, LinkedHashMap::new, toList()));
		// 상품 그룹핑
		// 취소
//		Map<String, List<OrderClaimGoodsInfoDto>> resultMap = new LinkedHashMap<>();
		Map<Long, List<OrderClaimGoodsInfoDto>> resultMap = new LinkedHashMap<>();
		if(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(claimStatusTp)){
			resultMap = orderDetailList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum()
							&&(OrderEnums.OrderStatus.INCOM_READY.getCode().equals(obj.getOrderStatusCd())
							||OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(obj.getOrderStatusCd())
							||OrderEnums.OrderStatus.DELIVERY_READY.getCode().equals(obj.getOrderStatusCd())))
//					.collect(groupingBy(OrderClaimGoodsInfoDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, toList()));
					.collect(groupingBy(OrderClaimGoodsInfoDto::getOdShippingPriceId, LinkedHashMap::new, toList()));
			// 반품
		}else{
			resultMap = orderDetailList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum()
							&&(OrderEnums.OrderStatus.DELIVERY_ING.getCode().equals(obj.getOrderStatusCd())
							||OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode().equals(obj.getOrderStatusCd())
							||OrderEnums.OrderStatus.BUY_FINALIZED.getCode().equals(obj.getOrderStatusCd())))
//					.collect(groupingBy(OrderClaimGoodsInfoDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, toList()));
					.collect(groupingBy(OrderClaimGoodsInfoDto::getOdShippingPriceId, LinkedHashMap::new, toList()));
		}
		resultMap.entrySet().forEach(entry -> {
			OrderClaimGoodsListDto dto = new OrderClaimGoodsListDto();

			// 배송정책명 세팅
			ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
			shippingDataResultVo.setConditionType(entry.getValue().get(0).getConditionTp());
			shippingDataResultVo.setConditionValue(entry.getValue().get(0).getConditionVal());
			String shippingtmplNm = this.getShippingPriceText(shippingDataResultVo);
			dto.setIlShippingTmplNm(shippingtmplNm);

			dto.setShippingPrice(String.valueOf(entry.getValue().get(0).getTemplateShippingPrice()));
			dto.setIlShippingTmplId(entry.getValue().get(0).getIlShippingTmplId());
			List<OrderClaimGoodsInfoDto> shippingTmpllList = new ArrayList<OrderClaimGoodsInfoDto>();
			// 추가상품 Set
			List<OrderClaimGoodsInfoDto> addGoodsLiat = null;
			List<OrderClaimGoodsInfoDto> packageGoodsList = null;
			List<OrderClaimGoodsInfoDto> reDeliveryGoodsList = null;
			List<OrderClaimGoodsInfoDto> pickNormalList = null;
			List<OrderClaimGoodsInfoDto> pickMonList = null;
			List<OrderClaimGoodsInfoDto> pickTueList = null;
			List<OrderClaimGoodsInfoDto> pickWedList = null;
			List<OrderClaimGoodsInfoDto> pickThuList = null;
			List<OrderClaimGoodsInfoDto> pickFriList = null;
			List<OrderClaimGoodsInfoDto> pickTotalList = null;
			List<String> weekDayNmList = null;

			for(OrderClaimGoodsInfoDto goodsItem : entry.getValue()) {
				addGoodsLiat = new ArrayList<>();
				packageGoodsList = new ArrayList<>();
				reDeliveryGoodsList = new ArrayList<>();
				pickNormalList	= new ArrayList<>();
				pickMonList		= new ArrayList<>();
				pickTueList		= new ArrayList<>();
				pickWedList		= new ArrayList<>();
				pickThuList		= new ArrayList<>();
				pickFriList		= new ArrayList<>();
				weekDayNmList	= new ArrayList<>();
				pickTotalList   = new ArrayList<>();
				String redeliveryIncludeYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode();	// 재배송 상품 포함 여부

				// 묶음상품 구성상품이 포함 되어 있을 경우
				if(subItemInfo.containsKey(goodsItem.getOdOrderDetlId())) {
					List<OrderClaimGoodsInfoDto> subGoodsList = subItemInfo.get(goodsItem.getOdOrderDetlId());
					List<OrderClaimGoodsInfoDto> subReDeliveryGoodsList = null;

					List<String> orderStatusCd = subGoodsList.stream().filter(x -> x.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlId())
																		.map(OrderClaimGoodsInfoDto::getOrderStatusCd)
																		.distinct()
																		.collect(toList());
					// 주문 상태 값이 비어있지 않고, 서로 다른 상태가 존재할 경우 PASS
					if(!orderStatusCd.isEmpty() && orderStatusCd.size() > 1) {
						log.debug("주문 상태값 존재함. 주문 상태 가 2개 이상");
						continue;
					}

					for(OrderClaimGoodsInfoDto subGoods : subGoodsList) {
						subReDeliveryGoodsList = new ArrayList<>();
						// 묶음상품일 경우
						if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsItem.getGoodsTpCd())
								&& !GoodsEnums.GoodsType.DAILY.getCode().equals(subGoods.getGoodsTpCd())
								&& !ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(subGoods.getPromotionTp())
								&& subGoods.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlId()){
							packageGoodsList.add(subGoods);
						}
						// 추가상품일 경우
						if(GoodsEnums.GoodsType.ADDITIONAL.getCode().equals(subGoods.getGoodsTpCd()) && subGoods.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlId()){
							addGoodsLiat.add(subGoods);
						}
						// 재배송 상품이 존재할 경우, 재배송 상품 정보 얻기
						if(reDeliveryItemInfo.containsKey(subGoods.getOdOrderDetlId())) {
							subReDeliveryGoodsList = reDeliveryItemInfo.get(subGoods.getOdOrderDetlId());
							// 재배송 상품 포함여부 "Y" 설정
							redeliveryIncludeYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode();
						}
						subGoods.setReDeliveryGoodsList(subReDeliveryGoodsList);
						if(StringUtil.nvlInt(subGoods.getMonCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.MON.getCodeName()); }
						if(StringUtil.nvlInt(subGoods.getTueCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.TUE.getCodeName()); }
						if(StringUtil.nvlInt(subGoods.getWedCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.WED.getCodeName()); }
						if(StringUtil.nvlInt(subGoods.getThuCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.THU.getCodeName()); }
						if(StringUtil.nvlInt(subGoods.getFriCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.FRI.getCodeName()); }
					}
					// 골라담기
					pickNormalList = subGoodsList.stream()
							.filter(obj -> ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(obj.getPromotionTp())
									&& obj.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlParentId())
							.collect(toList());
					// 골라담기 월요일
					pickMonList = subGoodsList.stream()
							.filter(obj -> Integer.parseInt(obj.getMonCnt()) > 0 && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(obj.getPromotionTp()))
							.collect(toList());
					// 골라담기 화요일
					pickTueList = subGoodsList.stream()
							.filter(obj -> Integer.parseInt(obj.getTueCnt()) > 0 && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(obj.getPromotionTp()))
							.collect(toList());
					// 골라담기 수요일
					pickWedList = subGoodsList.stream()
							.filter(obj -> Integer.parseInt(obj.getWedCnt()) > 0 && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(obj.getPromotionTp()))
							.collect(toList());
					// 골라담기 목요일
					pickThuList = subGoodsList.stream()
							.filter(obj -> Integer.parseInt(obj.getThuCnt()) > 0 && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(obj.getPromotionTp()))
							.collect(toList());
					// 골라담기 금요일
					pickFriList = subGoodsList.stream()
							.filter(obj -> Integer.parseInt(obj.getFriCnt()) > 0 && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(obj.getPromotionTp()))
							.collect(toList());

					// 골라담기 모든 상품담긴 리스트
					pickTotalList = subGoodsList.stream()
							.filter(obj -> ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(obj.getPromotionTp()))
							.collect(toList());
				}else{
					if(StringUtil.nvlInt(goodsItem.getMonCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.MON.getCodeName()); }
					if(StringUtil.nvlInt(goodsItem.getTueCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.TUE.getCodeName()); }
					if(StringUtil.nvlInt(goodsItem.getWedCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.WED.getCodeName()); }
					if(StringUtil.nvlInt(goodsItem.getThuCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.THU.getCodeName()); }
					if(StringUtil.nvlInt(goodsItem.getFriCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.FRI.getCodeName()); }
				}
				// 재배송 상품이 존재할 경우
				if(reDeliveryItemInfo.containsKey(goodsItem.getOdOrderDetlId())) {
					reDeliveryGoodsList = reDeliveryItemInfo.get(goodsItem.getOdOrderDetlId());
					// 재배송 상품 포함여부 "Y" 설정
					redeliveryIncludeYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode();
				}

				// 일일상품인 경우 ORDER_CNT 주기*기간에 맞춰 수정  -> 일괄배송인 경우 제외
				//if(StringUtil.isNotEmpty(goodsItem.getGoodsDailyTp())){
				if(GoodsEnums.GoodsDeliveryType.DAILY.getCode().equals(goodsItem.getGoodsDeliveryType()) && !"Y".equals(goodsItem.getDailyBulkYn())){
					// 배송기간
					int goodsCycleTermTypeCnt = Integer.parseInt(GoodsEnums.GoodsCycleTermType.findByCode(goodsItem.getGoodsCycleTermTpCode()).getTypeQty());

					// 녹즙 내맘대로인 경우
					if(StringUtils.isNotEmpty(goodsItem.getPromotionTp()) && CollectionUtils.isNotEmpty(pickTotalList)){
						int pickTotalCnt = pickTotalList.stream().mapToInt(m-> m.getOrderCnt()).sum();
						goodsItem.setOrderCnt(pickTotalCnt * goodsCycleTermTypeCnt);
						goodsItem.setClaimCnt(pickTotalCnt * goodsCycleTermTypeCnt);
						// 녹즙, 베이비밀, 잇슬림인 경우
					}else{
						// 주문수량
						int orderCnt = goodsItem.getOrderCnt();
						// 배송주기
						int goodsCycleTypeCnt = Integer.parseInt(GoodsEnums.GoodsCycleType.findByCode(goodsItem.getGoodsCycleTpCode()).getTypeQty());
						/**
						 * 2021.06.11 KMJ 수정 START
						 * if(ClaimEnums.ClaimFrontTp.FRONT.getCode().equals(StringUtil.nvl(String.valueOf(reqDto.getFrontTp())))) 조건 추가
						 */
						if(ClaimEnums.ClaimFrontTp.FRONT.getCode().equals(StringUtil.nvl(String.valueOf(reqDto.getFrontTp())))) {
							goodsItem.setOrderCnt(orderCnt * goodsCycleTypeCnt * goodsCycleTermTypeCnt);
							goodsItem.setClaimCnt(orderCnt * goodsCycleTypeCnt * goodsCycleTermTypeCnt);
						}
						/**
						 * 2021.06.11 KMJ 수정 END
						 */
					}
				}

				goodsItem.setRedeliveryIncludeYn(redeliveryIncludeYn);
				goodsItem.setAddGoodsList(addGoodsLiat);
				goodsItem.setPackageGoodsList(packageGoodsList);
				goodsItem.setReDeliveryGoodsList(reDeliveryGoodsList);
				goodsItem.setPickNormalList(pickNormalList);
				goodsItem.setPickMonList(pickMonList);
				goodsItem.setPickTueList(pickTueList);
				goodsItem.setPickWedList(pickWedList);
				goodsItem.setPickThuList(pickThuList);
				goodsItem.setPickFriList(pickFriList);
				goodsItem.setWeekDayNm(StringUtil.nvl(weekDayNmList.stream()
						.distinct()
						.collect(Collectors.joining("/"))));
				shippingTmpllList.add(goodsItem);
			}
			dto.setGoodsList(shippingTmpllList);
			goodsResultList.add(dto);
		});

		return goodsResultList;
	}

	/**
	 * 출고처 정보를 조회 한다.
	 * @param goodsList
	 * @return
	 * @throws Exception
	 */
	public List<OrderClaimViewDeliveryDto> getDelivery(List<OrderClaimGoodsInfoDto> goodsList, List<OrderClaimSearchGoodsDto> goodSearchList) throws Exception {
		List<OrderClaimViewDeliveryDto> orderClaimDeliveryList = new ArrayList<>();
		Map<Long, List<List<ArrivalScheduledDateDto>>> urWarehouseIdMap = new HashMap<>();

		if (CollectionUtils.isNotEmpty(goodsList)) {

			// 묶음배송인것 제외
			List<OrderClaimGoodsInfoDto> filterList = goodsList.stream().filter(x -> !GoodsEnums.GoodsType.PACKAGE.getCode().equals(x.getGoodsTpCd())).collect(toList());

			for (OrderClaimGoodsInfoDto goodsDto : filterList) {

				//새벽배송여부 가져오기
				if (GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(StringUtil.nvl(goodsDto.getDeliveryTypeCd())))
					goodsDto.setIsDawnDelivery(true);
				else
					goodsDto.setIsDawnDelivery(false);

				OrderClaimSearchGoodsDto goodSearchInfo = null;
				if(CollectionUtils.isNotEmpty(goodSearchList)) {
					goodSearchInfo = goodSearchList.stream().filter(x -> goodsDto.getOdOrderDetlId() == x.getOdOrderDetlId()).findAny().orElse(null);
				}
				int claimCnt = ObjectUtils.isNotEmpty(goodSearchInfo) ? goodSearchInfo.getClaimCnt() : goodsDto.getClaimCnt();

				List<ArrivalScheduledDateDto> orderIfDtoList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
																											goodsDto.getUrWarehouseId(),	//출고처ID(출고처PK)
																											goodsDto.getIlGoodsId(),		//상품ID(상품PK)
																											goodsDto.getIsDawnDelivery(),	//새벽배송여부 (true/false)
																											claimCnt,						//주문수량
																											goodsDto.getGoodsCycleTp());	//일일 배송주기코드

				DeliveryInfoDto deliveryInfo = DeliveryInfoDto.builder()
																.urWarehouseId(goodsDto.getUrWarehouseId())
																.ilGoodsId(goodsDto.getIlGoodsId())
																.arrivalScheduleList(orderIfDtoList)
																.build();
				OrderClaimViewDeliveryDto deliveryItem = OrderClaimViewDeliveryDto.builder()
																					.urWarehouseId(goodsDto.getUrWarehouseId())
																					.urWarehouseNm(goodsDto.getWarehouseNm())
																					.deliveryInfoList(deliveryInfo)
																					.build();

				List<List<ArrivalScheduledDateDto>> orderIfList = new ArrayList<>();
				if(!urWarehouseIdMap.containsKey(goodsDto.getUrWarehouseId())) {
					orderIfList.add(orderIfDtoList);
					orderClaimDeliveryList.add(deliveryItem);
				}
				else {
					orderIfList = urWarehouseIdMap.get(goodsDto.getUrWarehouseId());
					orderIfList.add(orderIfDtoList);
				}
				urWarehouseIdMap.put(goodsDto.getUrWarehouseId(), orderIfList);
			}

			log.debug("-----------------------출고예정일 교집합 조회 START----------------------------");
			if(!urWarehouseIdMap.isEmpty()) {
				Map<Long, List<LocalDate>> scheduleList = new HashMap<>();
				for (long urWarehouseId : urWarehouseIdMap.keySet()) {
					scheduleList.put(urWarehouseId, goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(urWarehouseIdMap.get(urWarehouseId)));
				}
				log.debug("scheduleList ::: <{}>", scheduleList.toString());

				log.debug("deliveryInfoList :: <{}>", orderClaimDeliveryList.toString());

				for (OrderClaimGoodsInfoDto goodsInfo : filterList) {
					for (OrderClaimViewDeliveryDto deliveryInfo : orderClaimDeliveryList) {
						if (deliveryInfo.getUrWarehouseId() == goodsInfo.getUrWarehouseId()) {
							List<ArrivalScheduledDateDto> arriveList = goodsGoodsBiz.intersectionArrivalScheduledDateDtoList(deliveryInfo.getDeliveryInfoList().getArrivalScheduleList(), scheduleList.get(deliveryInfo.getUrWarehouseId()));
							deliveryInfo.getDeliveryInfoList().setArrivalScheduleList(arriveList);
						}
					}
				}
			}
			log.debug("-----------------------출고예정일 교집합 조회 END----------------------------");
//			for (OrderClaimGoodsInfoDto goodsDto : goodsList) {
//
//				if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsDto.getGoodsTpCd())) {
//					continue;
//				}
//
//				//새벽배송여부 가져오기
//				if (GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(StringUtil.nvl(goodsDto.getDeliveryTypeCd())))
//					goodsDto.setIsDawnDelivery(true);
//				else
//					goodsDto.setIsDawnDelivery(false);
//
//				List<ArrivalScheduledDateDto> orderIfDtoList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
//						goodsDto.getUrWarehouseId(),	//출고처ID(출고처PK)
//						goodsDto.getIlGoodsId(),		//상품ID(상품PK)
//						goodsDto.getIsDawnDelivery(),	//새벽배송여부 (true/false)
//						goodsDto.getOrderCnt(),			//주문수량
//						goodsDto.getGoodsCycleTp());	//일일 배송주기코드
//
//				log.info("출고지 정보를 조회 한다. orderIfDtoList :: <{}>", orderIfDtoList);
//
//				if(!urWarehouseIds.contains(goodsDto.getUrWarehouseId())) {
//					DeliveryInfoDto deliveryInfo = DeliveryInfoDto.builder()
//							.odOrderDetlId(goodsDto.getOdOrderDetlId())	//주문상세 PK
//							.ilGoodsId(goodsDto.getIlGoodsId())
//							.urWarehouseId(goodsDto.getUrWarehouseId())
//							.arrivalScheduleList(orderIfDtoList)
//							.build();
//
//					OrderClaimViewDeliveryDto orderClaimDeliveryInfo = OrderClaimViewDeliveryDto.builder()
//							.urWarehouseId(goodsDto.getUrWarehouseId())
//							.urWarehouseNm(goodsDto.getWarehouseNm())
//							.deliveryInfoList(deliveryInfo)
//							.build();
//					log.info("출고지 정보 deliveryInfo 를 조회 한다. deliveryInfo :: <{}>", deliveryInfo);
//					orderClaimDeliveryList.add(orderClaimDeliveryInfo);
//					urWarehouseIds.add(goodsDto.getUrWarehouseId());
//				}
//			}
		}

		return orderClaimDeliveryList;
	}

	public String getShippingPriceText(ShippingDataResultVo vo){
		String shippingPriceText = "";
		DecimalFormat formatter = new DecimalFormat("###,###");
		String conditionType = vo.getConditionType();
		String conditionValue = formatter.format(vo.getConditionValue());
		//String shippingPrice = formatter.format(dto.getTemplateShippingPrice());

		// 배송정보 금액 세팅
		if (GoodsEnums.ConditionType.TYPE1.getCode().equals(conditionType)) { // 무료배송
			shippingPriceText = "무료배송";
		} else if (GoodsEnums.ConditionType.TYPE2.getCode().equals(conditionType)) { // 고정배송비
			shippingPriceText = "고정배송비";
		} else if (GoodsEnums.ConditionType.TYPE3.getCode().equals(conditionType)) { // 결제금액당 배송비
			shippingPriceText = conditionValue + "원 이상 구매시 무료배송";
		} else if (GoodsEnums.ConditionType.TYPE5.getCode().equals(conditionType)) { // 상품1개단위별 배송비
			shippingPriceText = "1개당 배송비";
		}

		return shippingPriceText;
	}
}
