package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDeliveryType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingInfoByOdOrderIdResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimUtilRequestService;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import kr.co.pulmuone.v1.order.present.service.OrderPresentBiz;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultShippingZoneListDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.EtcDataCartDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.policy.clause.dto.GetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.service.PolicyClauseBiz;
import kr.co.pulmuone.v1.policy.payment.dto.PayUseListDto;
import kr.co.pulmuone.v1.policy.payment.service.PolicyPaymentBiz;
import kr.co.pulmuone.v1.promotion.point.dto.vo.GoodsFeedbackPointRewardSettingVo;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 관련 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class MallOrderDetailBizImpl implements MallOrderDetailBiz {

	@Autowired
	private MallOrderDetailService mallOrderDetailService;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
	private ClaimUtilRequestService claimUtilRequestService;

	@Autowired
	private PolicyPaymentBiz policyPaymentbiz;

	@Autowired
	public UserBuyerBiz userBuyerBiz;

	@Autowired
	private PolicyClauseBiz policyClauseBiz;

	@Autowired
	public PgBiz pgBiz;

	@Autowired
	OrderOrderBiz orderOrderBiz;

	@Autowired
	private PromotionPointBiz promotionPointBiz;

	@Autowired
	private OrderPresentBiz orderPresentBiz;

	/**
	 * @return odOrderId
	 * @throws
	 * @Desc 주문상세 조회
	 */
	@Override
	public ApiResult<?> getOrderDetail(Long odOrderId) {

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();


		// 주문정보 조회
		MallOrderDto order = mallOrderDetailService.getOrder(odOrderId, buyerVo.getUrUserId(), buyerVo.getNonMemberCiCd());

		if (order == null){
			return ApiResult.result(OrderEnums.OrderErrMsg.VALUE_EMPTY);
		}

		MallOrderDetailListResponseDto mallOrderDetailListResponseDto = getOrderDetailResponseDto(odOrderId, false);

		mallOrderDetailListResponseDto.setOdid(StringUtil.nvl(order.getOdid(), ""));            // 배송유형
		mallOrderDetailListResponseDto.setCreateDt(StringUtil.nvl(order.getCreateDt(), "").substring(0, 10));    // 주문일자(등록일자)
		mallOrderDetailListResponseDto.setOrder(order);

		// 선물하기 정보
		try {
			mallOrderDetailListResponseDto.setPresent(orderPresentBiz.getOrderPresentByOdOrderId(odOrderId));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ApiResult.success(mallOrderDetailListResponseDto);
	}

	@Override
	public MallOrderDetailListResponseDto getOrderDetailResponseDto(Long odOrderId, boolean isGroupByOdShippingPriceId) {
		MallOrderDetailListResponseDto mallOrderDetailListResponseDto = new MallOrderDetailListResponseDto();

		// 정기배송 조회
		MallOrderRegularDto mallOrderRegularDto = mallOrderDetailService.getRegular(odOrderId);
		// 주문상세 상품 리스트 조회
		List<MallOrderDetailGoodsDto> orderDetailList = mallOrderDetailService.getOrderDetailGoodsList(odOrderId);
		// 패키지 정보

		// 클레임 정보
		List<MallOrderDetailGoodsDto> orderClaimDetailList = mallOrderDetailService.getClaimDetailGoodsList(odOrderId, 0);

		if (!orderClaimDetailList.isEmpty()) {

			// 클레임 정보에서 OD_ORDER_DETL_ID, ORDER_STATUS_CD 같은 주문건 중복 제거
			orderClaimDetailList = orderClaimDetailList.stream().filter(distinctByKey(f -> Arrays.asList(f.getOdOrderDetlId(), f.getOrderStatusCd())))
										.collect(toList());


			List<Long> odOrderDetilIds = orderDetailList.stream().map(MallOrderDetailGoodsDto::getOdOrderDetlId).collect(Collectors.toList());
			if (!odOrderDetilIds.isEmpty()) {
				orderClaimDetailList = orderClaimDetailList.stream().filter(dto -> (dto.getGrpDeliveryDt().indexOf("PACK") == -1 || (dto.getGrpDeliveryDt().indexOf("PACK") != -1 && !odOrderDetilIds.contains(dto.getOdOrderDetlId())))).collect(toList());
			}

			orderDetailList = Stream.concat(orderDetailList.stream(), orderClaimDetailList.stream()).collect(Collectors.toList());
		}

		if (!orderDetailList.isEmpty()) {

			// 증정품 목록
			List<MallOrderDetailGoodsDto> giftGoodsList = orderDetailList.stream()
					.filter(obj -> (GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd()) || GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
									&& obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum())
					.collect(toList());

			// 1차 배송형태, 2차 도착예정일, 3차 송장번호로 그룹
			Map<String, Map<String, Map<String, List<MallOrderDetailGoodsDto>>>> resultMap = null;
			if (isGroupByOdShippingPriceId) {
				resultMap = orderDetailList.stream().filter(
						obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum()
								&& !GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd())
								&& !GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
						.collect(groupingBy(MallOrderDetailGoodsDto::getGrpGoodsDeliveryType, LinkedHashMap::new,
								groupingBy(MallOrderDetailGoodsDto::getOdShippingPriceId, LinkedHashMap::new,
										groupingBy(MallOrderDetailGoodsDto::getTrackingNo, LinkedHashMap::new,
												toList()))));
			} else {
				resultMap = orderDetailList.stream().filter(
						obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum()
								&& !GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd())
								&& !GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
						.collect(
								groupingBy(MallOrderDetailGoodsDto::getGrpGoodsDeliveryType, LinkedHashMap::new,
										groupingBy(MallOrderDetailGoodsDto::getGrpDeliveryDt, LinkedHashMap::new,
												groupingBy(MallOrderDetailGoodsDto::getTrackingNo, LinkedHashMap::new,
														toList()))));
			}


			// 주문상세 뎁스가 2일 경우 목록 얻기
			// 재배송은 원주문에 포함 시키기
			Map<Long, List<MallOrderDetailGoodsDto>> subItemInfo = orderDetailList.stream()
																				.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_SECOND.getDepthNum())
																				.collect(groupingBy(MallOrderDetailGoodsDto::getOdOrderDetlParentId, LinkedHashMap::new, toList()));

//			Map<Long, List<MallOrderDetailGoodsDto>> subClaimItemInfo = orderDetailList.stream()
//																				.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_SECOND.getDepthNum()
//																						&& OrderOrderByType.ORDER_BY_CLAIM.getCode().equals(obj.getOrderType())
//																						&& !OrderStatus.EXCHANGE_COMPLETE.getCode().equals(obj.getOrderStatusCd()))
//																				.collect(groupingBy(MallOrderDetailGoodsDto::getOdOrderDetlParentId, LinkedHashMap::new, toList()));

			// 주문상세 뎁스가 3일 경우 목록 얻기 - 재배송
			Map<Long, List<MallOrderDetailGoodsDto>> reDeliveryItemInfo = orderDetailList.stream()
																						.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_THIRD.getDepthNum())
																						.collect(groupingBy(MallOrderDetailGoodsDto::getOdOrderDetlParentId, LinkedHashMap::new, toList()));

			// 주문상세 목록
			List<MallOrderDetailDeliveryTypeDto> resultOrderList = new ArrayList<MallOrderDetailDeliveryTypeDto>();

			// 배송유형별 주문상세 forEach
			resultMap.entrySet().forEach(entry -> {

				String key = entry.getKey();

				// 배송유형별 주문상세 Dto 셋팅
				MallOrderDetailDeliveryTypeDto mallOrderDetailDeliveryTypeDto = new MallOrderDetailDeliveryTypeDto();
				mallOrderDetailDeliveryTypeDto.setGoodsDeliveryType(key);                    // 배송유형

				// 배송유형명
				mallOrderDetailDeliveryTypeDto.setGoodsDeliveryTypeNm("");
				Stream.of(GoodsEnums.GoodsDeliveryType.values()).forEach(grpGoodsDeliveryType -> {
					if (key.equals(grpGoodsDeliveryType.getCode()))
						mallOrderDetailDeliveryTypeDto.setGoodsDeliveryTypeNm(grpGoodsDeliveryType.getCodeName());
				});

				// 배송유형별 주문상세목록
				List<MallOrderDetailDeliveryDtDto> deliveryTypeOrderDetailList = new ArrayList<MallOrderDetailDeliveryDtDto>();

				// 도착예정일별 주문상세Map
				Map<String, Map<String, List<MallOrderDetailGoodsDto>>> deliveryDtMap = entry.getValue();

				// 도착예정일별 주문상세 forEach
				deliveryDtMap.entrySet().forEach(deliveryDate -> {
					// 도착예정일별 주문상세 Dto 셋팅
					MallOrderDetailDeliveryDtDto mallOrderDetailDeliveryDtDto = new MallOrderDetailDeliveryDtDto();

					String [] arrDeliveryDt = deliveryDate.getKey().split("\\|\\|");
					mallOrderDetailDeliveryDtDto.setDeliveryDt(arrDeliveryDt[0]);    // 도착예정일
					if (arrDeliveryDt.length > 1) {
						mallOrderDetailDeliveryDtDto.setGoodsDeliveryType(arrDeliveryDt[1]);    // 배송유형
					}

					/*Stream.of(GoodsEnums.GoodsDeliveryType.values()).forEach(goodsDeliveryType -> {
						System.out.println("key : " + key + "   / goodsDeliveryType.getCode() : " + goodsDeliveryType.getCode());

						if (key.equals(goodsDeliveryType.getCode()))
							mallOrderDetailDeliveryDtDto.setGoodsDeliveryType(goodsDeliveryType.getCodeName());
					});*/

					Map<String, List<MallOrderDetailGoodsDto>> trackingNoMap = deliveryDate.getValue();
					List<MallOrderDetailtrackingNoDto> deliveryDtOrderDetailList = new ArrayList<MallOrderDetailtrackingNoDto>();

					// 송장번호별 주문상세 forEach
					trackingNoMap.entrySet().forEach(trackingNumber -> {
						MallOrderDetailtrackingNoDto mallOrderDetailtrackingNoDto = new MallOrderDetailtrackingNoDto();
						mallOrderDetailtrackingNoDto.setTrackingNo(trackingNumber.getKey());                    // 송장번호
						mallOrderDetailtrackingNoDto.setTrackingNoOrderDetailList(trackingNumber.getValue());    // 송장번호별 주문상세 리스트

						// 추가상품 Set
						List<MallOrderDetailGoodsDto> addGoodsLiat = null;
						List<MallOrderDetailGoodsDto> packageGoodsList = null;
						List<MallOrderDetailGoodsDto> reDeliveryGoodsList = null;
						List<MallOrderDetailGoodsDto> pickNormalList = null;
						List<MallOrderDetailGoodsDto> pickMonList = null;
						List<MallOrderDetailGoodsDto> pickTueList = null;
						List<MallOrderDetailGoodsDto> pickWedList = null;
						List<MallOrderDetailGoodsDto> pickThuList = null;
						List<MallOrderDetailGoodsDto> pickFriList = null;
						List<MallOrderDetailGoodsDto> pickTotalList = null;
						List<String> weekDayNmList = null;
						// 재배송 상품 추가 여부
						boolean isReDeliveryFlag = false;
						for(MallOrderDetailGoodsDto goodsItem : mallOrderDetailtrackingNoDto.getTrackingNoOrderDetailList()) {
							addGoodsLiat = new ArrayList<>();
							packageGoodsList = new ArrayList<>();
							reDeliveryGoodsList = new ArrayList<>();

							pickNormalList	= new ArrayList<>();
							pickMonList		= new ArrayList<>();
							pickTueList		= new ArrayList<>();
							pickWedList		= new ArrayList<>();
							pickThuList		= new ArrayList<>();
							pickFriList		= new ArrayList<>();
							pickTotalList   = new ArrayList<>();

							weekDayNmList	= new ArrayList<>();

//							Map<Long, List<MallOrderDetailGoodsDto>> subItemInfo = null;
//							if(OrderOrderByType.ORDER_BY_ORDER.getCode().equals(goodsItem.getOrderType())) {
//								subItemInfo = subOrderItemInfo;
//							} else {
//								subItemInfo = subClaimItemInfo;
//							}

							if(subItemInfo.containsKey(goodsItem.getOdOrderDetlId())) {
								List<MallOrderDetailGoodsDto> subGoodsList = subItemInfo.get(goodsItem.getOdOrderDetlId());
								List<MallOrderDetailGoodsDto> subReDeliveryGoodsList = null;

								for(MallOrderDetailGoodsDto subGoods : subGoodsList) {

									subReDeliveryGoodsList = new ArrayList<>();
									// 묶음상품일 경우
									if (GoodsType.PACKAGE.getCode().equals(goodsItem.getGoodsTpCd())
											&& !GoodsType.DAILY.getCode().equals(subGoods.getGoodsTpCd())
											&& !ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(subGoods.getPromotionTp())
											&& subGoods.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlId()
											&& !GoodsType.ADDITIONAL.getCode().equals(subGoods.getGoodsTpCd())){
										packageGoodsList.add(subGoods);
									}
									// 추가상품일 경우
									if(GoodsType.ADDITIONAL.getCode().equals(subGoods.getGoodsTpCd()) && subGoods.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlId() && subGoods.getOrderType().equals(goodsItem.getOrderType())){
										addGoodsLiat.add(subGoods);
									}
									// 재배송 상품이 존재할 경우, 재배송 상품 정보 얻기
									if(reDeliveryItemInfo.containsKey(subGoods.getOdOrderDetlId())) {
										subReDeliveryGoodsList = reDeliveryItemInfo.get(subGoods.getOdOrderDetlId());
										isReDeliveryFlag = true;
									}

									subGoods.setReDeliveryGoodsList(subReDeliveryGoodsList);

								}

								// 골라담기
								pickNormalList = subGoodsList.stream()
										.filter(obj -> obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode())
												&& obj.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlParentId()
												&& !obj.getGoodsTpCd().equals(GoodsType.ADDITIONAL.getCode()))
										.collect(toList());

								// 골라담기 월요일
								pickMonList = subGoodsList.stream()
										.filter(obj -> Integer.parseInt(obj.getMonCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
										.collect(toList());

								pickMonList = pickMonList.size() > 1 ? pickMonList.stream().filter(obj -> obj.getOdClaimId() == 0).collect(toList()) : pickMonList;
								// 골라담기 화요일
								pickTueList = subGoodsList.stream()
										.filter(obj -> Integer.parseInt(obj.getTueCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
										.collect(toList());

								pickTueList = pickTueList.size() > 1 ? pickTueList.stream().filter(obj -> obj.getOdClaimId() == 0).collect(toList()) : pickTueList;

								// 골라담기 수요일
								pickWedList = subGoodsList.stream()
										.filter(obj -> Integer.parseInt(obj.getWedCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
										.collect(toList());

								pickWedList = pickWedList.size() > 1 ? pickWedList.stream().filter(obj -> obj.getOdClaimId() == 0).collect(toList()) : pickWedList;
								// 골라담기 목요일
								pickThuList = subGoodsList.stream()
										.filter(obj -> Integer.parseInt(obj.getThuCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
										.collect(toList());
								pickThuList = pickThuList.size() > 1 ? pickThuList.stream().filter(obj -> obj.getOdClaimId() == 0).collect(toList()) : pickThuList;
								// 골라담기 금요일
								pickFriList = subGoodsList.stream()
										.filter(obj -> Integer.parseInt(obj.getFriCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
										.collect(toList());
								pickFriList = pickFriList.size() > 1 ? pickFriList.stream().filter(obj -> obj.getOdClaimId() == 0).collect(toList()) : pickFriList;
								// 골라담기 모든 상품담긴 리스트
								pickTotalList = subGoodsList.stream()
										.filter(obj -> obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
										.collect(toList());
							}else{
								List<MallOrderDetailGoodsDto> subReDeliveryGoodsList = new ArrayList<>();

								// 재배송 상품이 존재할 경우, 재배송 상품 정보 얻기
								if(reDeliveryItemInfo.containsKey(goodsItem.getOdOrderDetlId())) {
									subReDeliveryGoodsList = reDeliveryItemInfo.get(goodsItem.getOdOrderDetlId());
								}

								goodsItem.setReDeliveryGoodsList(subReDeliveryGoodsList);

							}
							// subItemInfo에 재배송이 포함되지 않았고, 재배송 상품이 존재할 경우
							if(!isReDeliveryFlag && reDeliveryItemInfo.containsKey(goodsItem.getOdOrderDetlId())) {
								reDeliveryGoodsList = reDeliveryItemInfo.get(goodsItem.getOdOrderDetlId());
							}

							goodsItem.setAddGoodsList(addGoodsLiat);
							goodsItem.setPackageGoodsList(packageGoodsList);
							goodsItem.setReDeliveryGoodsList(reDeliveryGoodsList);
							goodsItem.setPickNormalList(pickNormalList);
							goodsItem.setPickMonList(pickMonList);
							goodsItem.setPickTueList(pickTueList);
							goodsItem.setPickWedList(pickWedList);
							goodsItem.setPickThuList(pickThuList);
							goodsItem.setPickFriList(pickFriList);


							if(StringUtil.nvlInt(goodsItem.getMonCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.MON.getCodeName()); }
							if(StringUtil.nvlInt(goodsItem.getTueCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.TUE.getCodeName()); }
							if(StringUtil.nvlInt(goodsItem.getWedCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.WED.getCodeName()); }
							if(StringUtil.nvlInt(goodsItem.getThuCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.THU.getCodeName()); }
							if(StringUtil.nvlInt(goodsItem.getFriCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.FRI.getCodeName()); }

							goodsItem.setWeekDayNm(StringUtil.nvl(weekDayNmList.stream()
									.distinct()
									.collect(Collectors.joining("/"))));

							// 묶음상품일 경우 대표상품에 전체상품 후기작성여부 업데이트
							if(CollectionUtils.isNotEmpty(packageGoodsList)){
								long packageGoodsFeedbackCnt = packageGoodsList.stream().filter(f->f.getFeedbackWriteCnt()>0).count();
								int minFeedbackWriteUseDay = packageGoodsList.stream().filter(f->f.getFeedbackWriteUseDay()>=0 && f.getFeedbackWriteCnt() == 0).map(m->m.getFeedbackWriteUseDay()).collect(Collectors.toList())
																							.stream().min(Integer::compare).orElse(-1);

								long totalOrderGoodsCnt = packageGoodsList.size();

								if(packageGoodsFeedbackCnt > 0 && packageGoodsFeedbackCnt == totalOrderGoodsCnt){
									goodsItem.setFeedbackWriteCnt(1);
								}else{
									goodsItem.setFeedbackWriteCnt(0);
									goodsItem.setFeedbackWriteUseDay(minFeedbackWriteUseDay);
								}
							}

							// 골라담기일 경우 대표상품에 전체상품 후기작성여부 업데이트
							if(CollectionUtils.isNotEmpty(pickNormalList)){
								long pickGoodsFeedbackCnt = pickNormalList.stream().filter(f->f.getFeedbackWriteCnt()>0).count();
								int minFeedbackWriteUseDay = pickNormalList.stream().filter(f->f.getFeedbackWriteUseDay()>=0 && f.getFeedbackWriteCnt() == 0).map(m->m.getFeedbackWriteUseDay()).collect(Collectors.toList())
										.stream().min(Integer::compare).orElse(-1);
								long totalOrderGoodsCnt = pickNormalList.size();

								if(pickGoodsFeedbackCnt > 0 && pickGoodsFeedbackCnt == totalOrderGoodsCnt){
									goodsItem.setFeedbackWriteCnt(1);
								}else{
									goodsItem.setFeedbackWriteCnt(0);
									goodsItem.setFeedbackWriteUseDay(minFeedbackWriteUseDay);
								}
							}

							// 내맘대로일 경우 대표상품에 전체상품 후기작성여부 업데이트
							if(CollectionUtils.isNotEmpty(pickTotalList)){

								// ilGoodsId 기준으로 grouping
								Map<String, List<MallOrderDetailGoodsDto>> goodsIdMap = pickTotalList.stream()
										.collect(groupingBy(MallOrderDetailGoodsDto::getIlGoodsId,LinkedHashMap::new,toList()));

								AtomicLong pickGoodsFeedbackCnt = new AtomicLong(0);
								long totalOrderGoodsCnt = goodsIdMap.size();

								goodsIdMap.entrySet().forEach(idEntry -> {
									boolean isFeedbackWrite = false;
									for(MallOrderDetailGoodsDto goodsDto : idEntry.getValue()){
										if(goodsDto.getFeedbackWriteCnt() > 0){
											isFeedbackWrite = true;
										}
									}
									if(isFeedbackWrite){
										pickGoodsFeedbackCnt.getAndIncrement();
									}
								});

								int minFeedbackWriteUseDay = pickTotalList.stream().filter(f->f.getFeedbackWriteUseDay()>=0 && f.getFeedbackWriteCnt() == 0).map(m->m.getFeedbackWriteUseDay()).collect(Collectors.toList())
										.stream().min(Integer::compare).orElse(-1);

								if(pickGoodsFeedbackCnt.get() > 0 && pickGoodsFeedbackCnt.get() == totalOrderGoodsCnt){
									goodsItem.setFeedbackWriteCnt(1);
								}else{
									goodsItem.setFeedbackWriteCnt(0);
									goodsItem.setFeedbackWriteUseDay(minFeedbackWriteUseDay);
								}
							}

							// 일일상품인 경우 ORDER_CNT 주기*기간에 맞춰 수정 -> 일괄배송인 경우 제외
							if(GoodsDeliveryType.DAILY.getCode().equals(mallOrderDetailDeliveryTypeDto.getGoodsDeliveryType()) && StringUtil.isNotEmpty(goodsItem.getGoodsDailyTp())
							&& !"Y".equals(goodsItem.getDailyBulkYn())){
								// 배송기간
								int goodsCycleTermTypeCnt = Integer.parseInt(GoodsEnums.GoodsCycleTermType.findByCode(goodsItem.getGoodsCycleTermTpCode()).getTypeQty());

								// 녹즙 내맘대로인 경우
								if(StringUtils.isNotEmpty(goodsItem.getPromotionTp()) && CollectionUtils.isNotEmpty(pickTotalList)){
									int pickTotalCnt = pickTotalList.stream().mapToInt(m-> m.getOrderCnt()).sum();
									goodsItem.setOrderCnt(pickTotalCnt * goodsCycleTermTypeCnt);

								}
							}

							// 적립금 정보 설정
							List<GoodsFeedbackPointRewardSettingVo> pointInfo = promotionPointBiz.getGoodsFeedbackPointRewardSettingList(goodsItem.getUrGroupId());
							if(pointInfo != null && !pointInfo.isEmpty()){
								goodsItem.setExistPointYn("Y");
								goodsItem.setNormalAmount(pointInfo.get(0).getNomalAmount());
								goodsItem.setPhotoAmount(pointInfo.get(0).getPhotoAmount());
								goodsItem.setPremiumAmount(pointInfo.get(0).getPremiumAmount());
							}else{
								goodsItem.setExistPointYn("N");
							}


							mallOrderDetailDeliveryDtDto.setShippingPrice(Integer.parseInt(goodsItem.getShippingPrice())); // 배송비
							mallOrderDetailDeliveryDtDto.setDeliveryDtPaymentPrice(mallOrderDetailDeliveryDtDto.getDeliveryDtPaymentPrice() + goodsItem.getPaidPrice());
						}

						mallOrderDetailDeliveryDtDto.setDeliveryDtPaymentPrice(mallOrderDetailDeliveryDtDto.getDeliveryDtPaymentPrice() + mallOrderDetailDeliveryDtDto.getShippingPrice());

						// 도착예정일별 주문상세 리스트에서 송장번호별 주문상세 내용 Add
						deliveryDtOrderDetailList.add(mallOrderDetailtrackingNoDto);
					});
					mallOrderDetailDeliveryDtDto.setDeliveryDtOrderDetailList(deliveryDtOrderDetailList);    // 도착예정일별 주문상세 목록

					// 배송유형별 주문상세목록에  도착예정일별 주문상세Dto Add
					deliveryTypeOrderDetailList.add(mallOrderDetailDeliveryDtDto);
				});
				mallOrderDetailDeliveryTypeDto.setDeliveryTypeOrderDetailList(deliveryTypeOrderDetailList);    // 배송유형별 주문상세 목록

				// 주문상세 목록에 배송유형별 주문상세 Dto Add
				resultOrderList.add(mallOrderDetailDeliveryTypeDto);
			});

			// 배송지 정보
			MallOrderDetailShippingZoneDto shippingInf = mallOrderDetailService.getOrderDetailShippingInfo(odOrderId);
			mallOrderDetailListResponseDto.setShippingAddress(shippingInf);                        // 배송지 정보

			// 결제 정보
			MallOrderDetailPayResultDto payDto = mallOrderDetailService.getOrderDetailPayInfo(odOrderId);
			// 정기배송 주문 일 때, 결제 완료 상태가 아닐 경우 결제 정보는 생략
			if(ObjectUtils.isNotEmpty(mallOrderRegularDto) && OrderStatus.INCOM_READY.getCode().equals(payDto.getOrderStatusCd())) {
				payDto.setPayTp("");
				payDto.setInfo("");
				payDto.setCardNumber("");
				payDto.setPgService("");
			}
			payDto.setSalesSlipUrl(mallOrderDetailService.getOrderBillUrl(payDto));
			mallOrderDetailListResponseDto.setPayInfo(payDto);

			// 할인정보
			List<MallOrderDetailPayDiscountDto> discountList = mallOrderDetailService.getOrderDetailDiscountList(odOrderId);
			mallOrderDetailListResponseDto.setDiscountList(discountList);

			// 주문 취소 / 반품 신청내역
			List<MallOrderDetailClaimListDto> claimList = mallOrderDetailService.getOrderDetailClaimList(odOrderId);
			if (Objects.isNull(claimList)) {
				claimList = new ArrayList<>();
			}else{
				claimList = claimList.stream().filter(f->f.getGoodsList() != null).collect(Collectors.toList());
			}

			mallOrderDetailListResponseDto.setClaimList(claimList);
			mallOrderDetailListResponseDto.setOrderDetailList(resultOrderList);                    // 주문상세 목록
			mallOrderDetailListResponseDto.setOdOrderId(odOrderId);                                // 주문PK'
			mallOrderDetailListResponseDto.setRegularInfo(mallOrderRegularDto);
			mallOrderDetailListResponseDto.setGiftGoodsList(giftGoodsList);

		}
		return mallOrderDetailListResponseDto;
	}

	@Override
	public ApiResult<?> getDirectOrderDetail(Long odOrderId) throws Exception {
		//주문 상담원 주문 데이터는 주문 상세와 동일하게 사용
		ApiResult<MallOrderDetailListResponseDto> result = (ApiResult<MallOrderDetailListResponseDto>) getOrderDetail(odOrderId);
		MallDirectOrderDetailListResponseDto resDto = new MallDirectOrderDetailListResponseDto(result.getData());
		List<GetLatestJoinClauseListResultVo> clauseList = new ArrayList<>();
		List<String> psClauseGrpCdList = new ArrayList<>();
		GetClauseRequestDto clauseReqDto = new GetClauseRequestDto();
		HashMap<String, String> userPayment = new HashMap<>();

		Long urUserId = resDto.getOrder().getUrUserId();
		if (urUserId != null && urUserId > 0) {
			// 회원 사용 카드 정보 조회
			userPayment = userBuyerBiz.getUserPaymentInfo(urUserId.toString());

			// 구매약관 정보 조회
			psClauseGrpCdList.add(ShoppingEnums.ClauseGroupCode.PURCHASE_TERMS.getCode());
		} else {
			// 비회원구매일 경우 이용약관 추가
			psClauseGrpCdList.add(ShoppingEnums.ClauseGroupCode.NON_MEMBER_PRIVACY_POLICY.getCode());
		}

		// 약관
		clauseReqDto.setPsClauseGrpCdList(psClauseGrpCdList);
		ApiResult<?> clauseResult = policyClauseBiz.getPurchaseTermsClauseList(clauseReqDto);
		clauseList = (List<GetLatestJoinClauseListResultVo>) clauseResult.getData();

		// 결제 및 카드 정보 조회
		PayUseListDto payUseListDto = policyPaymentbiz.getPayUseList();
		// 결제 및 카드 정보
		resDto.setPaymentType(payUseListDto.getPaymentType());
		resDto.setCardList(payUseListDto.getCardList());
		resDto.setInstallmentPeriod(payUseListDto.getInstallmentPeriod());
		resDto.setCartBenefit(payUseListDto.getCartBenefit());
		// 약관리스트
		resDto.setClause(clauseList);
		// 무통장 입금 결제 가능 여부
		resDto.setVirtualAccountYn("N");
		// 회원 사용 카드정보
		resDto.setUserPayment(userPayment);

		return ApiResult.success(resDto);
	}

	@Override
	public ApiResult<?> applyPaymentDirectOrder(MallApplyPaymentDirectOrderRequestDto reqDto) throws Exception {
		//주문 상담원 주문 데이터는 주문 상세와 동일하게 사용
		ApiResult<MallOrderDetailListResponseDto> result = (ApiResult<MallOrderDetailListResponseDto>) getOrderDetail(reqDto.getOdOrderId());
		MallOrderDetailListResponseDto orderDetailDto = result.getData();
		MallOrderDto orderDto = orderDetailDto.getOrder();

		// 주문번호로 주문데이터 조회
		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(orderDetailDto.getOdid());

		// PG
		PaymentType paymentType = PaymentType.findByCode(reqDto.getPsPayCd());
		PgAbstractService<?, ?> pgService = pgBiz.getService(paymentType, reqDto.getCardCode());
		String pgBankCode = pgBiz.getPgBankCode(pgService.getServiceType().getCode(), paymentType.getCode(), reqDto.getCardCode());

		// 기본 결제
		BasicDataRequestDto paymentRequestFormDataDto = new BasicDataRequestDto();
		paymentRequestFormDataDto.setOdid(orderDetailDto.getOdid());
		paymentRequestFormDataDto.setPaymentType(paymentType);
		paymentRequestFormDataDto.setPgBankCode(pgBankCode);
		paymentRequestFormDataDto.setQuota(reqDto.getInstallmentPeriod());
		paymentRequestFormDataDto.setGoodsName(orderDto.getGoodsNm());
		paymentRequestFormDataDto.setPaymentPrice(orderData.getPaymentPrice());
		paymentRequestFormDataDto.setTaxPaymentPrice(orderData.getTaxablePrice());
		paymentRequestFormDataDto.setTaxFreePaymentPrice(orderData.getNonTaxablePrice());
		paymentRequestFormDataDto.setBuyerName(orderDto.getBuyerNm());
		paymentRequestFormDataDto.setBuyerEmail(orderDto.getBuyerMail());
		paymentRequestFormDataDto.setBuyerMobile(orderDto.getBuyerHp());
		paymentRequestFormDataDto.setLoginId(StringUtil.isNotEmpty(orderDto.getUrUserId()) ? orderDto.getLoginId() : "비회원");
		if (PaymentType.VIRTUAL_BANK.getCode().equals(reqDto.getPsPayCd())) {
			paymentRequestFormDataDto.setVirtualAccountDateTime(pgBiz.getVirtualAccountDateTime());
		}
		EtcDataCartDto etcDataCartDto = new EtcDataCartDto();
		etcDataCartDto.setCartType("");
		etcDataCartDto.setOrderInputUrl(reqDto.getOrderInputUrl());
		etcDataCartDto.setOdid(orderDetailDto.getOdid());
		etcDataCartDto.setPaymentType(paymentType.getCode());
		paymentRequestFormDataDto.setEtcData(pgService.toStringEtcData(etcDataCartDto));
//		paymentRequestFormDataDto.setCashReceipt(false);
//		paymentRequestFormDataDto.setCashReceiptEnum(OrderEnums.cashReceipt.DEDUCTION);
//		paymentRequestFormDataDto.setCashReceiptNumber("01038874023");

		BasicDataResponseDto resDto = pgService.getBasicData(paymentRequestFormDataDto);

		return ApiResult.success(resDto);
	}

	/**
	 * 주문 클레임 상세 조회
	 * @param odClaimId
	 * @return
	 */
	@Override
	public ApiResult<?> getClaimDetail(Long odClaimId) {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		// 주문정보 조회
		Map<String, Object> orderMap = mallOrderDetailService.getOrderClaim(odClaimId, buyerVo.getUrUserId(), buyerVo.getNonMemberCiCd());

		MallOrderDetailListResponseDto mallOrderDetailListResponseDto = new MallOrderDetailListResponseDto();

		if (orderMap == null){
			return ApiResult.result(OrderEnums.OrderErrMsg.VALUE_EMPTY);
		}

		long odOrderId = StringUtil.nvlLong(orderMap.get("OD_ORDER_ID"));

		MallOrderDto order = mallOrderDetailService.getOrder(odOrderId, buyerVo.getUrUserId(), buyerVo.getNonMemberCiCd());

		// 주문상세 상품 리스트 조회
		List<MallOrderDetailGoodsDto> orderDetailList = mallOrderDetailService.getClaimDetailGoodsList(odOrderId, odClaimId);

		if (!orderDetailList.isEmpty()) {

			// 증정품 목록
			List<MallOrderDetailGoodsDto> giftGoodsList = orderDetailList.stream()
					.filter(obj -> (GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd()) || GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
									&& obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum())
					.collect(toList());


			// 증정품 목록 제거
			orderDetailList = orderDetailList.stream()
					.filter(obj -> !((GoodsType.GIFT.getCode().equals(obj.getGoodsTpCd()) || GoodsType.GIFT_FOOD_MARKETING.getCode().equals(obj.getGoodsTpCd()))
							&& obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum()))
					.collect(toList());

			// 1차 배송형태, 2차 도착예정일, 3차 송장번호로 그룹
			/*
			Map<String, Map<String, Map<String, List<MallOrderDetailGoodsDto>>>> resultMap = orderDetailList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum())
					.collect(groupingBy(MallOrderDetailGoodsDto::getGrpGoodsDeliveryType, LinkedHashMap::new,
							groupingBy(MallOrderDetailGoodsDto::getGrpDeliveryDt, LinkedHashMap::new,
									groupingBy(MallOrderDetailGoodsDto::getTrackingNo, LinkedHashMap::new, toList()))));
			*/
			// 배송비 기준으로 그룹
			Map<String, List<MallOrderDetailGoodsDto>> resultMap = orderDetailList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum())
					.collect(groupingBy(MallOrderDetailGoodsDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, toList()));


			// 주문상세 뎁스가 2일 경우 목록 얻기
			Map<Long, List<MallOrderDetailGoodsDto>> subItemInfo = orderDetailList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_SECOND.getDepthNum())
					.collect(groupingBy(MallOrderDetailGoodsDto::getOdOrderDetlParentId, LinkedHashMap::new, toList()));

			// 주문상세 뎁스가 3일 경우 목록 얻기 - 재배송
			Map<Long, List<MallOrderDetailGoodsDto>> reDeliveryItemInfo = orderDetailList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_THIRD.getDepthNum())
					.collect(groupingBy(MallOrderDetailGoodsDto::getOdOrderDetlParentId, LinkedHashMap::new, toList()));



			// 주문상세 목록
			List<MallOrderDetailGoodsDto> resultOrderList = new ArrayList<MallOrderDetailGoodsDto>();

			resultMap.entrySet().forEach(entry -> {
				MallOrderDetailGoodsDto dto = new MallOrderDetailGoodsDto();

				dto.setShippingPrice(entry.getValue().get(0).getShippingPrice());

				String [] arrGrpWarehouseShippingTmplId = entry.getKey().split(Constants.ARRAY_SEPARATORS);
				long ilShippingTmplId = Long.parseLong(arrGrpWarehouseShippingTmplId[1]);
				RegularResultShippingZoneListDto regularResultShippingZoneInfo = new RegularResultShippingZoneListDto();
				regularResultShippingZoneInfo.setUrWarehouseId(ilShippingTmplId);
				ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
				try {
					shippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(ilShippingTmplId);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String shippingPriceText = "";
				try {
					//shippingPriceText = goodsShippingTemplateBiz.getShippingPriceText(shippingDataResultVo);
					shippingPriceText  = claimUtilRequestService.getShippingPriceText(shippingDataResultVo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dto.setIlShippingTmplNm(shippingPriceText);


				List<MallOrderDetailGoodsDto> shippingTmpllList = new ArrayList<MallOrderDetailGoodsDto>();
				// 추가상품 Set
				List<MallOrderDetailGoodsDto> addGoodsLiat = null;
				List<MallOrderDetailGoodsDto> packageGoodsList = null;
				List<MallOrderDetailGoodsDto> reDeliveryGoodsList = null;
				List<MallOrderDetailGoodsDto> pickNormalList = null;
				List<MallOrderDetailGoodsDto> pickMonList = null;
				List<MallOrderDetailGoodsDto> pickTueList = null;
				List<MallOrderDetailGoodsDto> pickWedList = null;
				List<MallOrderDetailGoodsDto> pickThuList = null;
				List<MallOrderDetailGoodsDto> pickFriList = null;
				List<MallOrderDetailGoodsDto> pickTotalList = null;
				List<String> weekDayNmList = null;
				for(MallOrderDetailGoodsDto goodsItem : entry.getValue()) {
					addGoodsLiat = new ArrayList<>();
					packageGoodsList = new ArrayList<>();
					reDeliveryGoodsList = new ArrayList<>();

					pickNormalList	= new ArrayList<>();
					pickMonList		= new ArrayList<>();
					pickTueList		= new ArrayList<>();
					pickWedList		= new ArrayList<>();
					pickThuList		= new ArrayList<>();
					pickFriList		= new ArrayList<>();
					pickTotalList   = new ArrayList<>();

					weekDayNmList	= new ArrayList<>();


					if(subItemInfo.containsKey(goodsItem.getOdOrderDetlId())) {
						List<MallOrderDetailGoodsDto> subGoodsList = subItemInfo.get(goodsItem.getOdOrderDetlId());
						List<MallOrderDetailGoodsDto> subReDeliveryGoodsList = null;

						for(MallOrderDetailGoodsDto subGoods : subGoodsList) {


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
								.filter(obj -> obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode())
										&& obj.getOdOrderDetlParentId() == goodsItem.getOdOrderDetlParentId())
								.collect(toList());

						// 골라담기 월요일
						pickMonList = subGoodsList.stream()
								.filter(obj -> Integer.parseInt(obj.getMonCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
								.collect(toList());
						// 골라담기 화요일
						pickTueList = subGoodsList.stream()
								.filter(obj -> Integer.parseInt(obj.getTueCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
								.collect(toList());
						// 골라담기 수요일
						pickWedList = subGoodsList.stream()
								.filter(obj -> Integer.parseInt(obj.getWedCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
								.collect(toList());
						// 골라담기 목요일
						pickThuList = subGoodsList.stream()
								.filter(obj -> Integer.parseInt(obj.getThuCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
								.collect(toList());
						// 골라담기 금요일
						pickFriList = subGoodsList.stream()
								.filter(obj -> Integer.parseInt(obj.getFriCnt()) > 0 && obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
								.collect(toList());

						// 골라담기 모든 상품담긴 리스트
						pickTotalList = subGoodsList.stream()
								.filter(obj -> obj.getPromotionTp().equals(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()))
								.collect(toList());

						// 일일상품인 경우 ORDER_CNT 주기*기간에 맞춰 수정
						if(GoodsDeliveryType.DAILY.getCode().equals(goodsItem.getGoodsDeliveryType()) && StringUtil.isNotEmpty(goodsItem.getGoodsDailyTp())){
							// 배송기간
							int goodsCycleTermTypeCnt = Integer.parseInt(GoodsEnums.GoodsCycleTermType.findByCode(goodsItem.getGoodsCycleTermTpCode()).getTypeQty());

							// 녹즙 내맘대로인 경우
							if(StringUtils.isNotEmpty(goodsItem.getPromotionTp()) && CollectionUtils.isNotEmpty(pickTotalList)){
								int pickTotalCnt = pickTotalList.stream().mapToInt(m-> m.getOrderCnt()).sum();
								goodsItem.setOrderCnt(pickTotalCnt * goodsCycleTermTypeCnt);

								// 녹즙, 베이비밀, 잇슬림인 경우
							}else{
								// 주문수량
								int orderCnt = goodsItem.getOrderCnt();
								// 배송주기
								int goodsCycleTypeCnt = Integer.parseInt(GoodsEnums.GoodsCycleType.findByCode(goodsItem.getGoodsCycleTpCode()).getTypeQty());
								goodsItem.setOrderCnt(orderCnt * goodsCycleTypeCnt * goodsCycleTermTypeCnt);

							}
						}
					}
					else {
						List<MallOrderDetailGoodsDto> subReDeliveryGoodsList = new ArrayList<>();

						goodsItem.setReDeliveryGoodsList(subReDeliveryGoodsList);

						if(StringUtil.nvlInt(goodsItem.getMonCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.MON.getCodeName()); }
						if(StringUtil.nvlInt(goodsItem.getTueCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.TUE.getCodeName()); }
						if(StringUtil.nvlInt(goodsItem.getWedCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.WED.getCodeName()); }
						if(StringUtil.nvlInt(goodsItem.getThuCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.THU.getCodeName()); }
						if(StringUtil.nvlInt(goodsItem.getFriCnt()) > 0){ weekDayNmList.add(GoodsEnums.WeekCodeByGreenJuice.FRI.getCodeName()); }
					}
					// 재배송 상품이 존재할 경우
					if(reDeliveryItemInfo.containsKey(goodsItem.getOdOrderDetlId())) {
						reDeliveryGoodsList = reDeliveryItemInfo.get(goodsItem.getOdOrderDetlId());
					}

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

					// 재배송상품 있을경우
					if(!reDeliveryGoodsList.isEmpty()){
						shippingTmpllList.addAll(reDeliveryGoodsList);
					}
				}

				dto.setGoodsDetailList(shippingTmpllList);
				resultOrderList.add(dto);
			});

			// 결과 목록이 비어 있고, 재배송 상품이 존재할 경우
			// 재배송 상품을 취소 한 것 ... !
			if(resultOrderList.isEmpty() && !reDeliveryItemInfo.isEmpty()) {
				List<MallOrderDetailGoodsDto> redeliveryInfoList = new ArrayList<>();
				for(long key : reDeliveryItemInfo.keySet()) {
					redeliveryInfoList.addAll(reDeliveryItemInfo.get(key));
				}
				Map<String, List<MallOrderDetailGoodsDto>> rediliveryShippingTmplList = redeliveryInfoList.stream()
																											.collect(groupingBy(MallOrderDetailGoodsDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, toList()));
				rediliveryShippingTmplList.entrySet().forEach(rediliveryShippingMap -> {
					MallOrderDetailGoodsDto dto = new MallOrderDetailGoodsDto();
					dto.setShippingPrice(rediliveryShippingMap.getValue().get(0).getShippingPrice());
					List<MallOrderDetailGoodsDto> shippingTmpllList = new ArrayList<MallOrderDetailGoodsDto>();
					shippingTmpllList.addAll(rediliveryShippingMap.getValue());
					dto.setGoodsDetailList(shippingTmpllList);
					resultOrderList.add(dto);
				});
			}

			// 배송지 정보
			MallOrderDetailShippingZoneDto shippingInf = mallOrderDetailService.getOrderDetailShippingInfo(odOrderId);
			mallOrderDetailListResponseDto.setShippingAddress(shippingInf);                        // 배송지 정보

			// 결제 정보
			MallOrderDetailPayResultDto payDto = mallOrderDetailService.getOrderDetailPayInfo(odOrderId);
			if(OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(payDto.getStatus())){  // 입금전취소일 경우 환불방법 미노출(HGRM-9323)
				payDto.setPayTp("");
			}
			mallOrderDetailListResponseDto.setPayInfo(payDto);

			// 할인정보
			List<MallOrderDetailPayDiscountDto> discountList = mallOrderDetailService.getOrderDetailDiscountList(odOrderId);
			mallOrderDetailListResponseDto.setDiscountList(discountList);

			// 반품 수거지 주소 정보
			mallOrderDetailListResponseDto.setClaimShippingAddress(mallOrderDetailService.getClaimShippingZone(odClaimId));

			// 클레임 정보
			mallOrderDetailListResponseDto.setClaimInfo(
					MallClaimInfoDto.builder()
					.claimStatusTp(StringUtil.nvl(orderMap.get("CLAIM_STATUS_TP"), ""))
					.claimStatusTpNm(StringUtil.nvl(orderMap.get("CLAIM_STATUS_TP_NM"), ""))
					.reasonMsg(StringUtil.nvl(orderMap.get("REASON_MSG"), ""))
					.claimReasonMsg(StringUtil.nvl(orderMap.get("CLAIM_REASON_MSG"), ""))
					.build()
				);

			// 클레임 첨부파일정보
			mallOrderDetailListResponseDto.setClaimAttcList(mallOrderDetailService.getClaimAttcList(odClaimId));

			// 클레임 결제 정보
			MallOrderDetailPayResultDto claimPayInfo = mallOrderDetailService.getClaimDetailPayInfo(odOrderId, odClaimId);
			if(Objects.isNull(claimPayInfo) || OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(payDto.getStatus())){ // 입금전취소일 경우 환불정보 미노출(HGRM-9323)
				claimPayInfo = new MallOrderDetailPayResultDto();
				claimPayInfo.setApprovalDt(StringUtil.nvl(orderMap.get("CLAIM_CREATE_DT"), ""));
			}
			mallOrderDetailListResponseDto.setClaimPayInfo(claimPayInfo);


			mallOrderDetailListResponseDto.setOdid(StringUtil.nvl(orderMap.get("ODID"), ""));            // 배송유형
			mallOrderDetailListResponseDto.setCreateDt(StringUtil.nvl(orderMap.get("CREATE_DT"), "").substring(0, 10));    // 주문일자(등록일자)
			mallOrderDetailListResponseDto.setGoodsDetailList(resultOrderList);                    // 주문상세 목록
			mallOrderDetailListResponseDto.setOdOrderId(odOrderId);                                // 주문PK'
			mallOrderDetailListResponseDto.setGiftGoodsList(giftGoodsList);
			mallOrderDetailListResponseDto.setOrder(order);

			// 선물하기 정보
			try {
				mallOrderDetailListResponseDto.setPresent(orderPresentBiz.getOrderPresentByOdOrderId(odOrderId));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return ApiResult.success(mallOrderDetailListResponseDto);
	}


	/**
	 * @Desc 주문 상세 결제 정보
	 * @param odOrderId
	 * @return MallOrderDetailPayResultDto
	 */
	@Override
	public MallOrderDetailPayResultDto getOrderDetailPayInfo(long odOrderId) {
   		return mallOrderDetailService.getOrderDetailPayInfo(odOrderId);
	}

   	/**
   	 * @Desc 주문상세 배송지  조회
   	 * @param odOrderId(주문 PK)
   	 * @return MallOrderDetailShippingZoneDto
   	 * @throws
   	 */
	@Override
   	public MallOrderDetailShippingZoneDto getOrderDetailShippingInfo(long odOrderId) {
   		return mallOrderDetailService.getOrderDetailShippingInfo(odOrderId);
   	}

    /**
     * @Desc 주문상세 리스트  조회
     * @param odOrderId(주문 PK)
     * @return List<MallOrderDetailListDto>
     * @throws
     */
	@Override
   	public List<MallOrderDetailGoodsDto> getOrderDetailGoodsList(long odOrderId) {
        return mallOrderDetailService.getOrderDetailGoodsList(odOrderId);
	}

	/**
	 * 주문배송지PK로 주문정보 조회
	 * @param odShippingZoneId
	 * @return List<OrderDetailByOdShippingZondIdResultDto>
	 */
	@Override
	public List<OrderDetailByOdShippingZondIdResultDto> getOrderDetailInfoByOdShippingZoneId(Long odShippingZoneId){
		return mallOrderDetailService.getOrderDetailInfoByOdShippingZoneId(odShippingZoneId);
	}

	/**
	 * 주문PK로 배송정책별 주문정보 조회
	 * @param odOrderId
	 * @return List<ShippingInfoByOdOrderIdResultVo>
	 */
	@Override
	public List<ShippingInfoByOdOrderIdResultVo> getShippingInfoByOdOrderId(Long odOrderId) throws Exception {
		return mallOrderDetailService.getShippingInfoByOdOrderId(odOrderId);
	}

	/**
	 * 주문PK로 상품PK별 주문정보 조회
	 * @param odOrderId
	 * @return List<ShippingInfoByOdOrderIdResultVo>
	 */
	@Override
	public List<ShippingInfoByOdOrderIdResultVo> getOrderDetailInfoByOdOrderId(Long odOrderId) throws Exception {
		return mallOrderDetailService.getOrderDetailInfoByOdOrderId(odOrderId);
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * @Desc 주문상세PK로 주문정보 조회
	 * @param odOrderDetlId
	 * @return MallOrderDto
	 */
	@Override
	public MallOrderDto getOrderInfoByOdOrderDetlId(Long odOrderDetlId){
		return mallOrderDetailService.getOrderInfoByOdOrderDetlId(odOrderDetlId);
	}

	/**
	 * @Desc 주문 조회
	 * @param odOrderId
	 * @param urUserId
	 * @param guestCi
	 * @return MallOrderDto
	 * @throws
	 */
	@Override
	public MallOrderDto getOrder(long odOrderId, String urUserId, String guestCi) {
		return mallOrderDetailService.getOrder(odOrderId, urUserId, guestCi);
	}

	/**
	 * @Desc 배송가능여부 체크 위한 주문정보 조회
	 * @param odOrderId
	 * @param odOrderDetlId
	 * @return List<HashMap>
	 */
	@Override
	public List<HashMap> getOrderInfoForShippingPossibility(Long odOrderId, Long odOrderDetlId){
		return mallOrderDetailService.getOrderInfoForShippingPossibility(odOrderId, odOrderDetlId);
	}
}