package kr.co.pulmuone.mall.order.present.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderPresentErrorCode;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.util.google.Recaptcha;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailDeliveryDtDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailDeliveryTypeDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailListResponseDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailtrackingNoDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDto;
import kr.co.pulmuone.v1.order.order.service.MallOrderDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.present.dto.IsShippingPossibilityRequestDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentArrivalScheduledDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentAuthResponseDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentChoiceDateDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentReceiveRequestDto;
import kr.co.pulmuone.v1.order.present.service.OrderPresentBiz;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;
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
 *  1.0    20210715   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class OrderPresentMallServiceImpl implements OrderPresentMallService {

	@Autowired
	protected OrderPresentBiz orderPresentBiz;

	@Autowired
	protected Recaptcha googleRecaptcha;

	@Autowired
	private MallOrderDetailBiz mallOrderDetailBiz;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
	OrderOrderBiz orderOrderBiz;

	/**
	 * 선물 확인
	 */
	@Override
	public ApiResult<?> getOrderPresentConfirm(String presentId) throws Exception {

		OrderPresentDto orderPresentDto = orderPresentBiz.getOrderPresentByPresentId(presentId);

		if (orderPresentDto == null) {
			return ApiResult.result(OrderPresentErrorCode.EMPTY_ORDER_PRESENT_DATA);
		} else {
			return ApiResult.success(orderPresentDto);
		}
	}

	/**
	 * 선물인증하기
	 */
	@Override
	public ApiResult<?> getOrderPresentAuth(String presentId, String presentAuthCd, String captcha) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		// 캡차 보안
		if (StringUtil.isNotEmpty(captcha)) {
			// API 통신 처리
			if (!googleRecaptcha.siteVerify(captcha)) {
				// 캡차 인증 실패 리턴
				return ApiResult.result(OrderPresentErrorCode.FAIL_ORDER_PRESENT_AUTH_CAPTCHA);
			}
		}

		// 인증코드로 데이터 조회
		OrderPresentDto orderPresent = orderPresentBiz.getOrderPresentByPresentIdAndAuthCode(presentId, presentAuthCd);

		// 조회 실패시 캡차 리턴 처리
		if (orderPresent == null) {
			int intFailCnt = Integer.valueOf(StringUtil.nvl(buyerVo.getFailCnt(), "0"));
			if (intFailCnt >= 5) {
				return ApiResult.result(OrderPresentErrorCode.FAIL_ORDER_PRESENT_AUTH_5_CNT);
			} else {
				intFailCnt++;
				buyerVo.setFailCnt(String.valueOf(intFailCnt));
				SessionUtil.setUserVO(buyerVo);
				return ApiResult.result(OrderPresentErrorCode.FAIL_ORDER_PRESENT_AUTH);
			}
		}

		boolean isComplet = OrderEnums.PresentOrderStatus.RECEIVE_COMPLET.getCode().equals(orderPresent.getPresentOrderStatus());

		// 주문 데이터 조회
		MallOrderDetailListResponseDto mallOrderDetailListResDto = mallOrderDetailBiz
				.getOrderDetailResponseDto(orderPresent.getOdOrderId(), !isComplet);

		// 응답 set
		OrderPresentAuthResponseDto result = new OrderPresentAuthResponseDto();
		result.setOrderPresent(orderPresent);
		result.setShippingAddress(mallOrderDetailListResDto.getShippingAddress());
		result.setOrderDetailList(mallOrderDetailListResDto.getOrderDetailList());
		result.setChoiceDate(isComplet ? null : choiceDate(mallOrderDetailListResDto.getOrderDetailList()));
		result.setGiftGoodsList(mallOrderDetailListResDto.getGiftGoodsList());
		return ApiResult.success(result);
	}

	private List<MallOrderDetailGoodsDto> getMallOrderDetailGoodsDtoList(
			List<MallOrderDetailDeliveryTypeDto> orderDetailList) {
		return orderDetailList.stream().map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
				.flatMap(Collection::stream).map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
				.flatMap(Collection::stream).map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
				.flatMap(Collection::stream).collect(toList());
	}

	private List<OrderPresentChoiceDateDto> choiceDate(List<MallOrderDetailDeliveryTypeDto> orderDetailList) {
		List<OrderPresentChoiceDateDto> result = new ArrayList<OrderPresentChoiceDateDto>();
		Map<String, List<MallOrderDetailGoodsDto>> map = getMallOrderDetailGoodsDtoList(orderDetailList).stream()
				.collect(groupingBy(MallOrderDetailGoodsDto::getOdShippingPriceId, LinkedHashMap::new, toList()));

		map.entrySet().forEach(entry -> {
			try {
				String odShippingPriceId = entry.getKey();
				List<MallOrderDetailGoodsDto> list = entry.getValue();

				UrWarehouseVo warehouseVo = null;
				List<List<ArrivalScheduledDateDto>> arrivalScheduledDateDtoList = new ArrayList<List<ArrivalScheduledDateDto>>();
				List<List<ArrivalScheduledDateDto>> dawnArrivalScheduledDateDtoList = new ArrayList<List<ArrivalScheduledDateDto>>();

				for (MallOrderDetailGoodsDto dto : list) {
					if (warehouseVo == null) {
						warehouseVo = storeWarehouseBiz.getWarehouse(dto.getUrWarehouseId());
					}

					// 팩키지인 경우
					if (GoodsType.PACKAGE.getCode().equals(dto.getGoodsTpCd())) {
						for (MallOrderDetailGoodsDto packageDto : dto.getPackageGoodsList()) {
							arrivalScheduledDateDtoList.add(goodsGoodsBiz.getArrivalScheduledDateDtoList(
									packageDto.getUrWarehouseId(), Long.valueOf(packageDto.getIlGoodsId()), false,
									packageDto.getOrderCnt(), null));
						}
					} else {
						arrivalScheduledDateDtoList
								.add(goodsGoodsBiz.getArrivalScheduledDateDtoList(dto.getUrWarehouseId(),
										Long.valueOf(dto.getIlGoodsId()), false, dto.getOrderCnt(), null));
					}
					// 새벽 배송인 경우
					if ("Y".equals(warehouseVo.getDawnDeliveryYn())) {
						// 팩키지인 경우
						if (GoodsType.PACKAGE.getCode().equals(dto.getGoodsTpCd())) {
							for (MallOrderDetailGoodsDto packageDto : dto.getPackageGoodsList()) {
								dawnArrivalScheduledDateDtoList.add(goodsGoodsBiz.getArrivalScheduledDateDtoList(
										packageDto.getUrWarehouseId(), Long.valueOf(packageDto.getIlGoodsId()), true,
										packageDto.getOrderCnt(), null));
							}
						} else {
							dawnArrivalScheduledDateDtoList
									.add(goodsGoodsBiz.getArrivalScheduledDateDtoList(dto.getUrWarehouseId(),
											Long.valueOf(dto.getIlGoodsId()), true, dto.getOrderCnt(), null));
						}
					}
				}

				OrderPresentChoiceDateDto choiceDateDto = new OrderPresentChoiceDateDto();
				choiceDateDto.setOdShippingPriceId(odShippingPriceId);
				choiceDateDto.setDawnDeliveryYn(warehouseVo.getDawnDeliveryYn());

				// 교집합 처리
				List<LocalDate> intersectionDateList = goodsGoodsBiz
						.intersectionArrivalScheduledDateListByDto(arrivalScheduledDateDtoList);
				if (intersectionDateList != null && !intersectionDateList.isEmpty()) {
					choiceDateDto.setArrivalScheduledDate(intersectionDateList.get(0));
				}
				choiceDateDto.setChoiceArrivalScheduledDateList(intersectionDateList);
				// 새벽배송
				if ("Y".equals(warehouseVo.getDawnDeliveryYn())) {
					// 새벽배송 교집합 처리
					List<LocalDate> dawnIntersectionDateList = goodsGoodsBiz
							.intersectionArrivalScheduledDateListByDto(dawnArrivalScheduledDateDtoList);
					if (dawnIntersectionDateList != null && !dawnIntersectionDateList.isEmpty()) {
						choiceDateDto.setDawnArrivalScheduledDate(dawnIntersectionDateList.get(0));
					}
					choiceDateDto.setDawnChoiceArrivalScheduledDateList(dawnIntersectionDateList);
				}

				result.add(choiceDateDto);
			} catch (Exception e) {
				log.error("=======선물하기 choiceDate 에러======={}", e);
			}
		});
		return result;
	}

	/**
	 * 선물하기 배송가능 체크
	 */
	@Override
	public ApiResult<?> isShippingPossibility(IsShippingPossibilityRequestDto reqDto) throws Exception {
		// 유효 주문번호인지 검증
		if (!orderPresentBiz.isOrderPresentAuth(reqDto.getPresentId(), reqDto.getOdOrderId())) {
			return ApiResult.result(OrderPresentErrorCode.FAIL_AUTH_PRESENT_ID);
		}

		// 주문 데이터 조회
		MallOrderDetailListResponseDto mallOrderDetailListResDto = mallOrderDetailBiz
				.getOrderDetailResponseDto(reqDto.getOdOrderId(), true);

		// 도서산관 및 제주 배송 정보 조회
		ShippingAreaVo shippingAreaVo = goodsShippingTemplateBiz
				.getShippingArea(reqDto.getShippingZone().getRecvZipCd());

		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();

		for (MallOrderDetailGoodsDto goods : getMallOrderDetailGoodsDtoList(
				mallOrderDetailListResDto.getOrderDetailList())) {

			// 출고 마감시간 체크
			UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(goods.getUrWarehouseId());

			// 새벽배송 선택 여부
			OrderPresentArrivalScheduledDto scheduledDto = reqDto.getOrderPresentArrivalScheduledDto(Long.valueOf(goods.getOdShippingPriceId()));
			if(scheduledDto == null) {
				return ApiResult.result(OrderPresentErrorCode.NO_SEARCH_ARRIVAL_SCHEDULED);
			}

			boolean isDawnDelivery = "Y".equals(scheduledDto.getDawnDeliveryYn());

			// 도착예정일로 출고일자 조회
			ArrivalScheduledDateDto arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(Long.valueOf(goods.getIlGoodsId()), scheduledDto.getArrivalScheduledDate());
			// 배송 마감시간 체크
			if (nowDate.isEqual(arrivalScheduledDateDto.getOrderDate())) {
				if (isDawnDelivery) {
					// 새벽 배송일때
					if (nowTime.isAfter(warehouseVo.getDawnDeliveryCutoffTime())) {
						return ApiResult.result(OrderPresentErrorCode.OVER_WAREHOUSE_CUTOFF_TIME);
					}
				} else {
					// 일반 배송일때
					if (nowTime.isAfter(warehouseVo.getCutoffTime())) {
						return ApiResult.result(OrderPresentErrorCode.OVER_WAREHOUSE_CUTOFF_TIME);
					}
				}
			}

			// 출고처별 배송 불가 기능 추가
			WarehouseUnDeliveryableInfoDto warehouseUnDeliveryableInfoDto = storeWarehouseBiz.getUnDeliverableInfo(
					goods.getUrWarehouseId(), reqDto.getShippingZone().getRecvZipCd(), isDawnDelivery);
			if (!warehouseUnDeliveryableInfoDto.isShippingPossibility()) {
				return ApiResult.result(OrderPresentErrorCode.FAIL_SHIPPING_IMPOSSIBLE);
			}

			// 상품 배송 정책
			ShippingDataResultVo shippingDataResultVo = goodsShippingTemplateBiz
					.getShippingInfoByShippingTmplId(Long.valueOf(goods.getIlShippingTmplId()));
			if (goodsShippingTemplateBiz.isUnDeliverableArea(shippingDataResultVo.getUndeliverableAreaType(),
					shippingAreaVo)) {
				return ApiResult.result(OrderPresentErrorCode.FAIL_SHIPPING_IMPOSSIBLE);
			}

			// 상품 개별 정책
			BasicSelectGoodsVo goodsVo = goodsGoodsBiz.getBasicSelectGoods(
					GoodsRequestDto.builder().ilGoodsId(Long.valueOf(goods.getIlGoodsId())).build());
			if (goodsShippingTemplateBiz.isUnDeliverableArea(goodsVo.getUndeliverableAreaType(), shippingAreaVo)) {
				return ApiResult.result(OrderPresentErrorCode.FAIL_SHIPPING_IMPOSSIBLE);
			}

			// 추가 배송비 체크
			ShippingPriceResponseDto shippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(shippingDataResultVo,
					0, 0, reqDto.getShippingZone().getRecvZipCd());
			if (shippingPriceDto.getRegionShippingPrice() > 0) {
				return ApiResult.result(OrderPresentErrorCode.FAIL_ADD_SHIPPING_PRICE);
			}
		}

		return ApiResult.success();
	}

	/**
	 * 선물 거절
	 */
	@Override
	public ApiResult<?> reject(String presentId, Long odOrderId) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		// 유효 주문번호인지 검증
		if (!orderPresentBiz.isOrderPresentAuth(presentId, odOrderId)) {
			return ApiResult.result(OrderPresentErrorCode.FAIL_AUTH_PRESENT_ID);
		}

		OrderPresentErrorCode result = orderPresentBiz.reject(odOrderId, buyerVo.getUrUserId());
		return OrderPresentErrorCode.SUCCESS.equals(result) ? ApiResult.success() : ApiResult.result(result);
	}

	/**
	 * 선물 받기
	 */
	@Override
	public ApiResult<?> receive(OrderPresentReceiveRequestDto reqDto) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		// 유효 주문번호인지 검증
		if (!orderPresentBiz.isOrderPresentAuth(reqDto.getPresentId(), reqDto.getOdOrderId())) {
			return ApiResult.result(OrderPresentErrorCode.FAIL_AUTH_PRESENT_ID);
		}

		// 선물하기 배송지 가능 체크
		ApiResult<?> isShippingPossibilityRes = isShippingPossibility(reqDto);
		if (!BaseEnums.Default.SUCCESS.getCode().equals(isShippingPossibilityRes.getCode())) {
			return isShippingPossibilityRes;
		}

		Long receiveUrUserId = Constants.GUEST_CREATE_USER_ID;
        if (StringUtil.isNotEmpty(buyerVo.getUrUserId())) {
        	receiveUrUserId = Long.parseLong(buyerVo.getUrUserId());
        }

		OrderPresentErrorCode result = orderPresentBiz.receive(reqDto, receiveUrUserId);
		return OrderPresentErrorCode.SUCCESS.equals(result) ? ApiResult.success() : ApiResult.result(result);
	}

	/**
	 * 선물하기 메세지 재발송
	 */
	@Override
	public ApiResult<?> reSendMessage(String odid) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

		// 주문정보 조회
		MallOrderDto order = mallOrderDetailBiz.getOrder(orderData.getOdOrderId(), buyerVo.getUrUserId(), buyerVo.getNonMemberCiCd());

		if (order == null){
			return ApiResult.result(OrderEnums.OrderErrMsg.VALUE_EMPTY);
		}

		// 선물하기 메세지 재발송
		OrderPresentErrorCode result = orderPresentBiz.reSendMessage(odid, BaseEnums.EnumSiteType.MALL.getCode());
		return OrderPresentErrorCode.SUCCESS.equals(result) ? ApiResult.success() : ApiResult.result(result);
	}
}
