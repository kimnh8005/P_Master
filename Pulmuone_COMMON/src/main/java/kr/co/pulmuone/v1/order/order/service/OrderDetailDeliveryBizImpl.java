package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsStoreStockBiz;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberUploadDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import kr.co.pulmuone.v1.order.delivery.service.OrderBulkTrackingNumberBiz;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.mall.MallArriveDateRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.vo.OrderDetlScheduleVo;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.  이명수            최초작성
 *  1.1    2021. 01. 08.  이규한            주문상세 배송상태 업데이트 추가
 * =======================================================================
 * </PRE>
 */
@Service
public class OrderDetailDeliveryBizImpl implements OrderDetailDeliveryBiz {

	@Autowired
	GoodsGoodsBiz goodsGoodsBiz;	// 상품 Biz

	@Autowired
	OrderBulkTrackingNumberBiz orderBulkTrackingNumberBiz;		// 일괄 송장 Biz

	@Autowired
	OrderDetailDeliveryService orderDetailDeliveryService;		// 주문 상세 관련 Service

	@Autowired
	StoreWarehouseBiz storeWarehouseBiz;	// 출고처 Biz

	@Autowired
	StoreDeliveryBiz storeDeliveryBiz; // 배송정보 Biz

	@Autowired
	OrderScheduleBiz orderScheduleBiz;  // 주문 상세 스케쥴

	@Autowired
	private OrderOrderBiz orderOrderBiz; // 주문

	@Autowired
	private MallOrderDetailBiz mallOrderDetailBiz;

	@Autowired
	private GoodsStockOrderBiz goodsStockOrderBiz;
	
	@Autowired
	private GoodsStoreStockBiz goodsStoreStockBiz;

	private static final String ORDER_CREATE_DIR_INFO 			= "PC";		// 디바이스 정보
	private static final boolean ORDER_CREATE_IS_APP 			= false;	// 앱 여부
	private static final boolean ORDER_CREATE_IS_EMPLOYEE 		= false;	// 임직원 여부

    /**
	 * 주문상세 배송상태 업데이트
	 * @param orderDetailDeliveryStatusRequestDtoList
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putOrderDetailDeliveryStatus(List<OrderDetailDeliveryStatusRequestDto> orderDetailDeliveryStatusRequestDtoList) {

		for(OrderDetailDeliveryStatusRequestDto orderDetailDeliveryStatusRequestDto : orderDetailDeliveryStatusRequestDtoList) {
			orderBulkTrackingNumberBiz.putOrderDetailDeliveryStatus(OrderTrackingNumberVo.builder()
					.odOrderDetlId(orderDetailDeliveryStatusRequestDto.getOdOrderDetlId())					// 주문상세 PK
					.psShippingCompId(orderDetailDeliveryStatusRequestDto.getPsShippingCompId())			// 택배사 PK
					.trackingNo(orderDetailDeliveryStatusRequestDto.getTrackingNo())						// 송장번호
					.createId(Long.parseLong(orderDetailDeliveryStatusRequestDto.getUserVo().getUserId()))	// 등록자
					.build(), OrderEnums.OrderStatus.DELIVERY_ING.getCode());										// 배송상태(배송중)
		}

		return ApiResult.success();
	}

	/**
	 * 주문상세 주문I/F 출고지시일 목록 조회
	 * @param orderDetailIfDateListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getIfDayList(OrderDetailIfDateListRequestDto orderDetailIfDateListRequestDto) throws Exception {
		List<ArrivalScheduledDateDto> deliveryList = new ArrayList<>();
		List<ArrivalScheduledDateDto> dawnDeliveryList = new ArrayList<>();

		List<Long> odOrderDetlIds = Arrays.asList(orderDetailIfDateListRequestDto.getOdOrderDetlId());
		List<ArrivalScheduledDateDto> disposalArrivalScheduledDatelist = orderOrderBiz.getOrderDetailDisposalGoodsArrivalScheduledList(odOrderDetlIds);

		if (disposalArrivalScheduledDatelist != null && !disposalArrivalScheduledDatelist.isEmpty()) {
			deliveryList = disposalArrivalScheduledDatelist;
			dawnDeliveryList = disposalArrivalScheduledDatelist;
		} else {
			LocalDate goodsDailyTpIfDate = LocalDate.now();
			
			//주문상세정보 조회
			StockCheckOrderDetailDto stockCheckOrderDetail = orderOrderBiz.getStockCheckOrderDetailListByOdOrderDetlId(orderDetailIfDateListRequestDto.getOdOrderDetlId()).get(0);
			
			// 출고지시일 리스트
			if (orderOrderBiz.isOrderDetailDailyDelivery(orderDetailIfDateListRequestDto.getOdOrderDetlId())) {
				deliveryList = goodsGoodsBiz.getDailyDeliveryArrivalScheduledDateDtoList(
						orderDetailIfDateListRequestDto.getUrWarehouseId(),
						orderDetailIfDateListRequestDto.getIlGoodsId(),
						orderDetailIfDateListRequestDto.getOrderCnt());
			} else {
				if (StringUtil.isEmpty(orderDetailIfDateListRequestDto.getGoodsDailyCycleType()) && GoodsEnums.GoodsDeliveryType.DAILY.getCode().equals(stockCheckOrderDetail.getGoodsDeliveryType())) {
					deliveryList = goodsGoodsBiz.getArrivalScheduledDateDtoListForBabymealBulk(
							orderDetailIfDateListRequestDto.getUrWarehouseId(),			// 출고처ID(출고처PK)
							orderDetailIfDateListRequestDto.getIlGoodsId(),				// 상품ID(상품PK)
							false,														// 새벽배송여부 (true/false)
							orderDetailIfDateListRequestDto.getOrderCnt(),				// 주문수량
							orderDetailIfDateListRequestDto.getGoodsDailyCycleType());	// 일일 배송주기코드
				} else {
					deliveryList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
							orderDetailIfDateListRequestDto.getUrWarehouseId(),			// 출고처ID(출고처PK)
							orderDetailIfDateListRequestDto.getIlGoodsId(),				// 상품ID(상품PK)
							false,														// 새벽배송여부 (true/false)
							orderDetailIfDateListRequestDto.getOrderCnt(),				// 주문수량
							orderDetailIfDateListRequestDto.getGoodsDailyCycleType());	// 일일 배송주기코드	
				}				
				
				goodsDailyTpIfDate = deliveryList.get(0).getOrderDate();
			}
			
			
			
			boolean isStockPreOrder = goodsStoreStockBiz.isStockPreOrderWithGoodsId(orderDetailIfDateListRequestDto.getIlGoodsId());
			if(isStockPreOrder) {
				List<ArrivalScheduledDateDto> delvSchdPreOrderList = goodsGoodsBiz.getAddDeliveryScheduleDateForPreOrderGoods(
						orderDetailIfDateListRequestDto.getUrWarehouseId()
						, false
						, GoodsConstants.GOODS_STOCK_MAX_DDAY
						, GoodsConstants.GOODS_STOCK_ADD_DDAY);
				deliveryList.addAll(delvSchdPreOrderList);
			}
			
			// 출고 지시일 리스트 요일별로 처리
			if (StringUtil.isNotEmpty(orderDetailIfDateListRequestDto.getGoodsDailyCycleType())) {
				deliveryList = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(deliveryList, orderDetailIfDateListRequestDto.getUrWarehouseId(), orderDetailIfDateListRequestDto.getGoodsDailyCycleType(), orderDetailIfDateListRequestDto.getWeekCode());
				
				// 일일배송상품일경우 인터페이스 일자는 [주문마감 전 : 금일, 이후 : 익일] 로 고정
				goodsGoodsBiz.getDailyGoodsFixedFastestOrderIfDate(goodsDailyTpIfDate, deliveryList);
			}

			// 상품 출고지가 새벽배송이 가능 && 회원의 주문배송지가 새벽배송이 가능한 경우에만 새벽배송 출고지시일리스트 요청
			UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(orderDetailIfDateListRequestDto.getUrWarehouseId());
			ShippingAddressPossibleDeliveryInfoDto shippingAddressPossibleDeliveryInfo
				=  storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(orderDetailIfDateListRequestDto.getZipCode(), orderDetailIfDateListRequestDto.getRecvBldNo());

			if("Y".equals(warehouseVo.getDawnDeliveryYn()) && shippingAddressPossibleDeliveryInfo.isDawnDelivery()) {
				// 새벽배송 출고지시일 리스트
				dawnDeliveryList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
						orderDetailIfDateListRequestDto.getUrWarehouseId(),			// 출고처ID(출고처PK)
						orderDetailIfDateListRequestDto.getIlGoodsId(),				// 상품ID(상품PK)
						true,														// 새벽배송여부 (true/false)
						orderDetailIfDateListRequestDto.getOrderCnt(),				// 주문수량
						orderDetailIfDateListRequestDto.getGoodsDailyCycleType());
				
				if(isStockPreOrder) {
					List<ArrivalScheduledDateDto> delvSchdPreOrderList = goodsGoodsBiz.getAddDeliveryScheduleDateForPreOrderGoods(
							orderDetailIfDateListRequestDto.getUrWarehouseId()
							, true
							, GoodsConstants.GOODS_STOCK_MAX_DDAY
							, GoodsConstants.GOODS_STOCK_ADD_DDAY);
					dawnDeliveryList.addAll(delvSchdPreOrderList);
				}
			}
		}

		// RESPONSE DTO 세팅
		List<IfDateListDto> responseDeliveryList = new ArrayList<>();
		List<IfDateListDto> responseDawnDeliveryList = new ArrayList<>();

		//출고 예정일 리스트
		for(ArrivalScheduledDateDto dto : deliveryList) {
			String forwardingScheduledDateStr = dto.getForwardingScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));
			String arrivalScheduledDateStr = dto.getArrivalScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));

			IfDateListDto ifDateDto = new IfDateListDto(dto);
			ifDateDto.setForwardingScheduledDateStr(forwardingScheduledDateStr);
			ifDateDto.setArrivalScheduledDateStr(arrivalScheduledDateStr);
			responseDeliveryList.add(ifDateDto);
		}

		//도착 예정일 리스트
		if(CollectionUtils.isNotEmpty(dawnDeliveryList)) {
			for(ArrivalScheduledDateDto dto : dawnDeliveryList) {
				String forwardingScheduledDateStr = dto.getForwardingScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));
				String arrivalScheduledDateStr = dto.getArrivalScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));

				//리턴 DTO 생성
				IfDateListDto ifDateDto = new IfDateListDto(dto);
				ifDateDto.setForwardingScheduledDateStr(forwardingScheduledDateStr);
				ifDateDto.setArrivalScheduledDateStr(arrivalScheduledDateStr);
				responseDawnDeliveryList.add(ifDateDto);
			}
		}

		return ApiResult.success(OrderDetailIfDateListResponseDto.builder()
										.deliveryList(responseDeliveryList)
										.dawnDeliveryList(responseDawnDeliveryList)
										.build());
	}

	/**
	 * 주문상세 주문I/F 정보 업데이트 (데이터 셋팅)
	 * @param orderDetailDateUpdateRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putIfDay(OrderDetailDateUpdateRequestDto orderDetailDateUpdateRequestDto) throws Exception{
		long createId = Long.parseLong(orderDetailDateUpdateRequestDto.getUserVo().getUserId());
		return putIfDay(orderDetailDateUpdateRequestDto, createId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> putIfDay(OrderDetailDateUpdateRequestDto orderDetailDateUpdateRequestDto, long createId) throws Exception {
		List<ArrivalScheduledDateDto> disposalArrivalScheduledDatelist = null;

		if (orderDetailDateUpdateRequestDto.getAllChangeYn() != null && "Y".equals(orderDetailDateUpdateRequestDto.getAllChangeYn())) {
			List<Long> odOrderDetlIdList = orderDetailDeliveryService.getUrWarehouseOdOrderDetlIdList(orderDetailDateUpdateRequestDto.getOdOrderDetlId());
			disposalArrivalScheduledDatelist = orderOrderBiz.getOrderDetailDisposalGoodsArrivalScheduledList(odOrderDetlIdList);
			if (disposalArrivalScheduledDatelist != null && !disposalArrivalScheduledDatelist.isEmpty()) {
				return ApiResult.success();
			} else {
				List<Long> odOrderIds = new ArrayList<Long>();
				for (Long odOrderDetlId : odOrderDetlIdList) {
					ApiResult<?> result = putOrderIfDay(odOrderDetlId, orderDetailDateUpdateRequestDto, createId);
					if(!result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
						return result;
					} else {
						Long odOrderId = mallOrderDetailBiz.getOrderInfoByOdOrderDetlId(odOrderDetlId).getOdOrderId();
						odOrderIds.add(odOrderId);
					}
				}

				// 일자별 출고처별 출고예정수량 업데이트
				orderOrderBiz.putWarehouseDailyShippingCount(odOrderIds);

				return ApiResult.success();
			}
		}

		List<Long> odOrderDetlIds = Arrays.asList(orderDetailDateUpdateRequestDto.getOdOrderDetlId());
		disposalArrivalScheduledDatelist = orderOrderBiz.getOrderDetailDisposalGoodsArrivalScheduledList(odOrderDetlIds);

		if (disposalArrivalScheduledDatelist != null && !disposalArrivalScheduledDatelist.isEmpty()) {
			return ApiResult.success();
		} else {
			ApiResult<?> result = putOrderIfDay(orderDetailDateUpdateRequestDto.getOdOrderDetlId() , orderDetailDateUpdateRequestDto, createId);
			if (result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
				Long odOrderId = mallOrderDetailBiz.getOrderInfoByOdOrderDetlId(orderDetailDateUpdateRequestDto.getOdOrderDetlId()).getOdOrderId();

				// 일자별 출고처별 출고예정수량 업데이트
				orderOrderBiz.putWarehouseDailyShippingCount(Arrays.asList(odOrderId));
			}
			return result;
		}
	}


	/**
	 * 주문상세 주문I/F 정보 업데이트
	 * @param odOrderDetlId(주문상세PK), OrderDetailDateUpdateRequestDto(주문 상세 상태 일자 업데이트 RequestDto)
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	public ApiResult<?> putOrderIfDay(Long odOrderDetlId, OrderDetailDateUpdateRequestDto orderDetailDateUpdateRequestDto, long createId) throws Exception{
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 처리이력 메세지
		String histMsg = "";
		OrderDetlVo histMsgVo = orderDetailDeliveryService.getHistMsgOdOrderDetlId(odOrderDetlId);
		String prevOrderStatusDeliTpNm = histMsgVo.getOrderStatusDeliTp();
		String prevOrderIfDt = histMsgVo.getOrderIfDt().format(dateFormatter);
		String prevDeliveryDt = histMsgVo.getDeliveryDt().format(dateFormatter);

		// 출고 마감시간 체크
		UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(orderDetailDateUpdateRequestDto.getUrWarehouseId());
		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();

		if(nowDate.isEqual(LocalDate.parse(orderDetailDateUpdateRequestDto.getShippingDt(), dateFormatter))){
			if(StringUtils.isNotEmpty(orderDetailDateUpdateRequestDto.getOrderIfDawnYn())
				&& "Y".equals(orderDetailDateUpdateRequestDto.getOrderIfDawnYn())){
				// 새벽 배송일때
				if (nowTime.isAfter(warehouseVo.getDawnDeliveryCutoffTime())) {
					throw new BaseException(DeliveryEnums.ChangeArriveDateValidation.OVER_WAREHOUSE_CUTOFF_TIME);
				}
			}else{
				// 일반 배송일때
				if (nowTime.isAfter(warehouseVo.getCutoffTime())) {
					throw new BaseException(DeliveryEnums.ChangeArriveDateValidation.OVER_WAREHOUSE_CUTOFF_TIME);
				}
			}
		}

		/* 기존 도착일 재고 수정 */
		Long odOrderId = mallOrderDetailBiz.getOrderInfoByOdOrderDetlId(odOrderDetlId).getOdOrderId();
		ApiResult<?> originStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(odOrderDetlId, "N");
		if (!originStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			throw new BaseException(originStockRes.getMessageEnum());
		}

		/* 도착예정일 변경 */
		OrderDetlVo orderDetlVo = OrderDetlVo.builder()
				.odOrderDetlId(odOrderDetlId)
				.orderIfId(createId)
				.orderIfDt(LocalDate.parse(orderDetailDateUpdateRequestDto.getOrderIfDt(), dateFormatter))
				.shippingId(createId)
				.shippingDt(LocalDate.parse(orderDetailDateUpdateRequestDto.getShippingDt(), dateFormatter))
				.deliveryId(createId)
				.deliveryDt(LocalDate.parse(orderDetailDateUpdateRequestDto.getDeliveryDt(), dateFormatter))
				.urWarehouseId(orderDetailDateUpdateRequestDto.getUrWarehouseId())
				.odShippingZoneId(orderDetailDateUpdateRequestDto.getOdShippingZoneId())
				.promotionTp(orderDetailDateUpdateRequestDto.getPromotionTp())
				.build();

		orderDetailDeliveryService.putOrderDetailDt(orderDetlVo);

		/* 변경 도착일 재고 수정 */
		ApiResult<?> changeStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(odOrderDetlId, "Y");
		if (!changeStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			throw new BaseException(changeStockRes.getMessageEnum());
		}
		String orderStatusDetailTypeNm = "";
		/* 배송방법 변경 */
		if(StringUtils.isNotEmpty(orderDetailDateUpdateRequestDto.getOrderIfDawnYn())){
			String goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL.getCode();
			String orderStatusDetailType =  OrderEnums.OrderStatusDetailType.NORMAL.getCode();
			orderStatusDetailTypeNm = OrderEnums.OrderStatusDetailType.NORMAL.getCodeName();

			if("Y".equals(orderDetailDateUpdateRequestDto.getOrderIfDawnYn())){
				goodsDeliveryType = GoodsEnums.GoodsDeliveryType.DAWN.getCode();
				orderStatusDetailType =  OrderEnums.OrderStatusDetailType.DAWN.getCode();
				orderStatusDetailTypeNm = OrderEnums.OrderStatusDetailType.DAWN.getCodeName();
			}

			try{
				orderOrderBiz.putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(odOrderDetlId,goodsDeliveryType,orderStatusDetailType);
			}catch(Exception e){
				throw new BaseException(BaseEnums.Default.FAIL);
			}
		} else {
			orderStatusDetailTypeNm = prevOrderStatusDeliTpNm;
		}

		// 처리이력 메세지 > {배송방법} 주문I/F : {주문I/F} / 도착예정일 : {도착예정일} → {배송방법} 주문I/F : {주문I/F} / 도착예정일 : {도착예정일}
		histMsg = MessageFormat.format(OrderEnums.OrderDetailStatusHistMsg.DELIVERY_DT_CHANGE_MSG.getMessage(),
				prevOrderStatusDeliTpNm, prevOrderIfDt, prevDeliveryDt,
				orderStatusDetailTypeNm, orderDetailDateUpdateRequestDto.getOrderIfDt(), orderDetailDateUpdateRequestDto.getDeliveryDt());

		/* 일일상품 ->  일일배송 스케쥴 변경*/
		if(GoodsEnums.GoodsType.DAILY.getCode().equals(orderDetailDateUpdateRequestDto.getGoodsTpCd())){
			try{
				//스케쥴 변경
				orderScheduleBiz.changeOrderDetlDaily(orderDetlVo,odOrderDetlId);

				// OD_ORDER_DETL_DAILY_ZONE 정보 수정
				// 녹즙 내맘대로 일 경우 -> 해당 주문건의 모든 OD_ORDER_DETL_DAILY_ZONE 정보 업데이트
				if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetlVo.getPromotionTp())){
					orderScheduleBiz.saveOrderDetlDailyZone(orderDetlVo.getOdOrderDetlId(), orderDetlVo.getPromotionTp(), odOrderId);
				}else{
					orderScheduleBiz.saveOrderDetlDailyZone(orderDetlVo.getOdOrderDetlId(), orderDetlVo.getPromotionTp(), null);
				}
			}catch(Exception e){
				throw new BaseException(BaseEnums.Default.FAIL);
			}

		}



		/* 이력 등록 */
		OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
				.odOrderDetlId(odOrderDetlId)
				.statusCd("")
				.histMsg(histMsg)
				.createId(createId)
				.build();

		orderDetailDeliveryService.putOrderDetailStatusHist(orderDetlHistVo);

		return ApiResult.success();
	}


	/**
	 * 주문생성 주문I/F 출고지시일 목록 조회
	 * @param orderDetailIfDateListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getIfDayListForOrderCreate(OrderDetailIfDateListRequestDto orderDetailIfDateListRequestDto) throws Exception {
		// 응답 list
		List<IfDateListDto> responseDeliveryList = new ArrayList<>();
		List<IfDateListDto> responseDawnDeliveryList = new ArrayList<>();

		// 상품별 출고지시일 merge list
		List<List<ArrivalScheduledDateDto>> mergeDeliveryList = new ArrayList<>();
		List<List<ArrivalScheduledDateDto>> mergeDawnDeliveryList = new ArrayList<>();

		// 상품별 출고지시일 map
		Map<Long,List<ArrivalScheduledDateDto>> goodsDeliveryDateMap = new HashMap<>();
		Map<Long,List<ArrivalScheduledDateDto>> goodsDawnDeliveryDateMap = new HashMap<>();

		if(CollectionUtils.isNotEmpty(orderDetailIfDateListRequestDto.getOrderCreateGoodsList())){
			for(OrderDetailIfDateListRequestDto dto : orderDetailIfDateListRequestDto.getOrderCreateGoodsList()){
				List<ArrivalScheduledDateDto> deliveryList = new ArrayList<>();
				List<ArrivalScheduledDateDto> dawnDeliveryList = new ArrayList<>();
				boolean isDawnDelivery = false;

				// 출고지시일 리스트
				if (orderOrderBiz.isOrderDetailDailyDelivery(dto.getOdOrderDetlId())) {
					deliveryList = goodsGoodsBiz.getDailyDeliveryArrivalScheduledDateDtoList(
							dto.getUrWarehouseId(),
							dto.getIlGoodsId(),
							dto.getOrderCnt());
				} else {
					deliveryList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
							dto.getUrWarehouseId(),			// 출고처ID(출고처PK)
							dto.getIlGoodsId(),				// 상품ID(상품PK)
							false,				// 새벽배송여부 (true/false)
							dto.getOrderCnt(),				// 주문수량
							dto.getGoodsDailyCycleType());	// 일일 배송주기코드
				}
				// 출고 지시일 리스트 요일별로 처리
				if (StringUtil.isNotEmpty(dto.getGoodsDailyCycleType())) {
					deliveryList = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(deliveryList, dto.getUrWarehouseId(), dto.getGoodsDailyCycleType(), dto.getWeekCode());
				}

				// 상품 출고지가 새벽배송이 가능 && 회원의 주문배송지가 새벽배송이 가능한 경우에만 새벽배송 출고지시일리스트 요청
				UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(dto.getUrWarehouseId());
				ShippingAddressPossibleDeliveryInfoDto shippingAddressPossibleDeliveryInfo
						=  storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(orderDetailIfDateListRequestDto.getZipCode(), orderDetailIfDateListRequestDto.getRecvBldNo());

				if("Y".equals(warehouseVo.getDawnDeliveryYn()) && shippingAddressPossibleDeliveryInfo.isDawnDelivery()) {
					isDawnDelivery = true;
					// 새벽배송 출고지시일 리스트
					dawnDeliveryList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
							dto.getUrWarehouseId(),			// 출고처ID(출고처PK)
							dto.getIlGoodsId(),				// 상품ID(상품PK)
							true,				// 새벽배송여부 (true/false)
							dto.getOrderCnt(),				// 주문수량
							dto.getGoodsDailyCycleType());
				}

				mergeDeliveryList.add(deliveryList);
				mergeDawnDeliveryList.add(dawnDeliveryList);

				// 묶음상품일 경우 재고 계산 수정
				GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(dto.getIlGoodsId()).deviceInfo(ORDER_CREATE_DIR_INFO)
						.isApp(ORDER_CREATE_IS_APP).isMember(orderDetailIfDateListRequestDto.isMember()).isEmployee(ORDER_CREATE_IS_EMPLOYEE).isBosCreateOrder(true).build();

				BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

				if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
					// 일반배송
					for(ArrivalScheduledDateDto  arrivalScheduledDate: deliveryList){
						List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(
								goods.getIlGoodsId(), orderDetailIfDateListRequestDto.isMember(), ORDER_CREATE_IS_EMPLOYEE, isDawnDelivery, arrivalScheduledDate.getArrivalScheduledDate(), dto.getOrderCnt());
						RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);
						arrivalScheduledDate.setStock(recalculationPackage.getStockQty());
					}
					// 새벽배송
					for(ArrivalScheduledDateDto  arrivalScheduledDate: dawnDeliveryList){
						List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(
								goods.getIlGoodsId(), orderDetailIfDateListRequestDto.isMember(), ORDER_CREATE_IS_EMPLOYEE, isDawnDelivery, arrivalScheduledDate.getArrivalScheduledDate(), dto.getOrderCnt());
						RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);
						arrivalScheduledDate.setStock(recalculationPackage.getStockQty());
					}
				}
				goodsDeliveryDateMap.put(dto.getIlGoodsId(),deliveryList);
				goodsDawnDeliveryDateMap.put(dto.getIlGoodsId(),dawnDeliveryList);
			}
		}


		// 상품별 출고 예정일 교집합 리스트 리턴(일반배송)
		if(CollectionUtils.isNotEmpty(mergeDeliveryList)) {
			List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(mergeDeliveryList);

			List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = goodsGoodsBiz.intersectionArrivalScheduledDateDtoList(mergeDeliveryList.get(0),intersectionArrivalScheduledDateList);
			for(ArrivalScheduledDateDto dto : arrivalScheduledDateDtoList) {
				String forwardingScheduledDateStr = dto.getForwardingScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));
				String arrivalScheduledDateStr = dto.getArrivalScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));

				IfDateListDto ifDateDto = new IfDateListDto(dto);
				ifDateDto.setForwardingScheduledDateStr(forwardingScheduledDateStr);
				ifDateDto.setArrivalScheduledDateStr(arrivalScheduledDateStr);
				responseDeliveryList.add(ifDateDto);
			}
		}

		// 상품별 출고 예정일 교집합 리스트 리턴(새벽배송)
		if(CollectionUtils.isNotEmpty(mergeDawnDeliveryList)){
			List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(mergeDawnDeliveryList);

			List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = goodsGoodsBiz.intersectionArrivalScheduledDateDtoList(mergeDawnDeliveryList.get(0),intersectionArrivalScheduledDateList);
			for(ArrivalScheduledDateDto dto : arrivalScheduledDateDtoList) {
				String forwardingScheduledDateStr = dto.getForwardingScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));
				String arrivalScheduledDateStr = dto.getArrivalScheduledDate().format(DateTimeFormatter.ofPattern("MM-dd(E)"));

				IfDateListDto ifDateDto = new IfDateListDto(dto);
				ifDateDto.setForwardingScheduledDateStr(forwardingScheduledDateStr);
				ifDateDto.setArrivalScheduledDateStr(arrivalScheduledDateStr);
				responseDawnDeliveryList.add(ifDateDto);
			}
		}

		return ApiResult.success(OrderDetailIfDateListResponseDto.builder()
				.deliveryList(responseDeliveryList)
				.dawnDeliveryList(responseDawnDeliveryList)
				.goodsDeliveryDateMap(goodsDeliveryDateMap)
				.goodsDawnDeliveryDateMap(goodsDawnDeliveryDateMap)
				.build());
	}

	/**
	 * 주문상세 처리이력 메세지 정보 주문상세ID 조회
	 * @param odOrderDetlId (주문상세 PK)
	 * @return OrderDetlVo
	 */
	@Override
	public OrderDetlVo getHistMsgOdOrderDetlId(Long odOrderDetlId, String type) throws Exception{
		return orderDetailDeliveryService.getHistMsgOdOrderDetlId(odOrderDetlId, type);
	}

	/**
	 * 도착예정일 변경 이력 등록
	 * @param orderDetlHistVo
	 * @return
	 */
	@Override
	public int putOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo) throws Exception{
		return orderDetailDeliveryService.putOrderDetailStatusHist(orderDetlHistVo);
	}

}