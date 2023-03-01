package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.DeliveryEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.date.service.OrderDateBiz;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.service.MallOrderDailyDetailBiz;
import kr.co.pulmuone.v1.order.order.service.MallOrderDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderDetailDeliveryService;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.order.status.service.OrderStatusService;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 배송 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 18.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class MallOrderDeliveryBizImpl implements MallOrderDeliveryBiz {

    @Autowired
    private MallOrderDeliveryService mallOrderDeliveryService;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;	// 상품 Biz

	@Autowired
	private OrderDateBiz orderDateBiz;

	@Autowired
	private OrderStatusService orderStatusService;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

	@Autowired
	private OrderScheduleBiz orderScheduleBiz;

	@Autowired
	private MallOrderDetailBiz mallOrderDetailBiz;

	@Autowired
	private GoodsStockOrderBiz goodsStockOrderBiz;

	@Autowired
	private OrderDetailDeliveryService orderDetailDeliveryService;


    /**
     * @Desc 도착예정일 변경일자 조회
     * @param mallArriveDateListRequestDto
     * @return ApiResult<?>
     * @throws
     */
	@Override
	public ApiResult<?> getArriveDateList(MallArriveDateListRequestDto mallArriveDateListRequestDto) throws Exception {

		// 개별 상품별(주문상세 상품별) 도착예정일 리스트의 리스트
		List<List<ArrivalScheduledDateDto>> arrivalScheduledDateDtoList= new ArrayList<List<ArrivalScheduledDateDto>>();

		List<Long> odOrderDetlIds = mallArriveDateListRequestDto.getMallArriveDateList().stream().map(MallArriveDateRequestDto::getOdOrderDetlId).distinct().collect(Collectors.toList());
		List<ArrivalScheduledDateDto> disposalArrivalScheduledDatelist = orderOrderBiz.getOrderDetailDisposalGoodsArrivalScheduledList(odOrderDetlIds);

		if (disposalArrivalScheduledDatelist != null && !disposalArrivalScheduledDatelist.isEmpty()) {
			// 폐기임박 상품이 포함되어 있는경우는 변경 안됨
			arrivalScheduledDateDtoList.add(disposalArrivalScheduledDatelist);
		} else {
			mallArriveDateListRequestDto.getMallArriveDateList().stream().forEach(mallArriveDateRequestDto -> {
				try {

					List<ArrivalScheduledDateDto> arrivalScheduledDatelist = null;
					// 개별 상품별(주문상세 상품별) 도착예정일 리스트 조회
					// 일일상품중 택배배송일때 (베이비밀 일괄배송에 택배권역일경우) 분기 처리
					if (orderOrderBiz.isOrderDetailDailyDelivery(mallArriveDateRequestDto.getOdOrderDetlId())) {
						arrivalScheduledDatelist = goodsGoodsBiz.getDailyDeliveryArrivalScheduledDateDtoList(
								mallArriveDateRequestDto.getUrWarehouseId(),
								mallArriveDateRequestDto.getIlGoodsId(),
								mallArriveDateRequestDto.getOrderCnt());
					} else {
						arrivalScheduledDatelist = goodsGoodsBiz.getArrivalScheduledDateDtoList(
								mallArriveDateRequestDto.getUrWarehouseId(),			// 출고처ID(출고처PK)
								mallArriveDateRequestDto.getIlGoodsId(),				// 상품ID(상품PK)
								mallArriveDateRequestDto.getIsDawnDelivery(),			// 새벽배송여부 (true/false)
								mallArriveDateRequestDto.getOrderCnt(),					// 주문수량
								mallArriveDateRequestDto.getGoodsDailyCycleType());	// 일일 배송주기코드

						// 출고 지시일 리스트 요일별로 처리
						if (StringUtil.isNotEmpty(mallArriveDateRequestDto.getGoodsDailyCycleType())) {
							String weekCode = "";
							if(StringUtil.isNotEmpty(mallArriveDateRequestDto.getWeekCode())){
								weekCode = DeliveryEnums.WeekType.findByCodeName(mallArriveDateRequestDto.getWeekCode()).getCode();
							}
							arrivalScheduledDatelist = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(arrivalScheduledDatelist, mallArriveDateRequestDto.getUrWarehouseId(), mallArriveDateRequestDto.getGoodsDailyCycleType(), weekCode);
						}
					}

					arrivalScheduledDateDtoList.add(arrivalScheduledDatelist);

				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		// 각 상품별 도착예정일 리스트의 교집합 도착예정일 리스트 조회
		List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(arrivalScheduledDateDtoList);
		List<LocalDate> resultDateList = new ArrayList<>();
		for (int i=0; i<intersectionArrivalScheduledDateList.size(); i++) {
			resultDateList.add(intersectionArrivalScheduledDateList.get(i));
			if(i==4) break;
		}

		MallArriveDateListResponseDto mallArriveDateListResponseDto = MallArriveDateListResponseDto.builder()
				.list(resultDateList).build();

		return ApiResult.success(mallArriveDateListResponseDto);
	}

	/**
	 * 도착 예정일 변경
	 * @param mallArriveDateUpdateRequestDto (도착예정일 변경요청 상품목록정보, 변경할 도착예정일 일자, 도착예정일 등록자)
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> putArriveDate(MallArriveDateUpdateRequestDto mallArriveDateUpdateRequestDto, long createId) throws Exception {

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		List<Long> odOrderDetlIds = mallArriveDateUpdateRequestDto.getMallArriveDateList().stream().map(MallArriveDateRequestDto::getOdOrderDetlId).distinct().collect(Collectors.toList());
		List<ArrivalScheduledDateDto> disposalArrivalScheduledDatelist = orderOrderBiz.getOrderDetailDisposalGoodsArrivalScheduledList(odOrderDetlIds);

		if (disposalArrivalScheduledDatelist == null || disposalArrivalScheduledDatelist.isEmpty()) {

			List<Long> odOrderIds = new ArrayList<Long>();

			for(MallArriveDateRequestDto mallArriveDateRequestDto: mallArriveDateUpdateRequestDto.getMallArriveDateList()){
				
				// 처리이력 메세지
				String histMsg = "";
				OrderDetlVo histMsgVo = orderDetailDeliveryService.getHistMsgOdOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId());
				String prevOrderStatusDeliTpNm = histMsgVo.getOrderStatusDeliTp();
				String prevOrderIfDt = histMsgVo.getOrderIfDt().format(dateFormatter);
				String prevDeliveryDt = histMsgVo.getDeliveryDt().format(dateFormatter);

				LocalDate goodsDailyTpIfDate = LocalDate.now();
				
				String goodsDailyCycleType = "";
				if(StringUtils.isNotEmpty(mallArriveDateRequestDto.getGoodsDailyCycleType())){
					goodsDailyCycleType = GoodsEnums.GoodsCycleType.findByCodeName(mallArriveDateRequestDto.getGoodsDailyCycleType()).getCode();
				}

				List<ArrivalScheduledDateDto> arrivalScheduledDatelist = null;
				// 개별 상품별(주문상세 상품별) 도착예정일 리스트 조회
				// 일일상품중 택배배송일때 (베이비밀 일괄배송에 택배권역일경우) 분기 처리
				if (orderOrderBiz.isOrderDetailDailyDelivery(mallArriveDateRequestDto.getOdOrderDetlId())) {
					arrivalScheduledDatelist = goodsGoodsBiz.getDailyDeliveryArrivalScheduledDateDtoList(
							mallArriveDateRequestDto.getUrWarehouseId(),
							mallArriveDateRequestDto.getIlGoodsId(),
							mallArriveDateRequestDto.getOrderCnt());
				} else {
					
					//베이비밀 가맹점배송 일괄배송일 경우
					if (StringUtil.isEmpty(goodsDailyCycleType) && OrderEnums.OrderStatusDetailType.DAILY.getCodeName().equals(histMsgVo.getOrderStatusDeliTp())) {
						arrivalScheduledDatelist = goodsGoodsBiz.getArrivalScheduledDateDtoListForBabymealBulk(
								mallArriveDateRequestDto.getUrWarehouseId(),            // 출고처ID(출고처PK)
								mallArriveDateRequestDto.getIlGoodsId(),                // 상품ID(상품PK)
								mallArriveDateRequestDto.getIsDawnDelivery(),            // 새벽배송여부 (true/false)
								mallArriveDateRequestDto.getOrderCnt(),                    // 주문수량
								goodsDailyCycleType);                                    // 일일 배송주기코드
					} else {
						arrivalScheduledDatelist = goodsGoodsBiz.getArrivalScheduledDateDtoList(
								mallArriveDateRequestDto.getUrWarehouseId(),            // 출고처ID(출고처PK)
								mallArriveDateRequestDto.getIlGoodsId(),                // 상품ID(상품PK)
								mallArriveDateRequestDto.getIsDawnDelivery(),            // 새벽배송여부 (true/false)
								mallArriveDateRequestDto.getOrderCnt(),                    // 주문수량
								goodsDailyCycleType);                                    // 일일 배송주기코드
					}
					
					goodsDailyTpIfDate = arrivalScheduledDatelist.get(0).getOrderDate();
				}
				
						
				ArrivalScheduledDateDto arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(
						arrivalScheduledDatelist, LocalDate.parse(mallArriveDateUpdateRequestDto.getDeliveryDt(), dateFormatter));

				// 상품의 배송주기가 주1일일때
				if(arrivalScheduledDateDto == null && GoodsEnums.GoodsCycleType.DAY1_PER_WEEK.getCode().equals(goodsDailyCycleType)){
					// 출고 지시일 리스트 요일별로 처리
					if (StringUtil.isNotEmpty(mallArriveDateRequestDto.getGoodsDailyCycleType())) {
						String weekCode = "";
						if(StringUtil.isNotEmpty(mallArriveDateRequestDto.getWeekCode())){
							weekCode = DeliveryEnums.WeekType.findByCodeName(mallArriveDateRequestDto.getWeekCode()).getCode();
						}
						arrivalScheduledDatelist = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(arrivalScheduledDatelist, mallArriveDateRequestDto.getUrWarehouseId(), goodsDailyCycleType, weekCode);
						
						// 일일배송상품일경우 인터페이스 일자는 [주문마감 전 : 금일, 이후 : 익일] 로 고정
						goodsGoodsBiz.getDailyGoodsFixedFastestOrderIfDate(goodsDailyTpIfDate, arrivalScheduledDatelist);
					}					

					arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(
							arrivalScheduledDatelist, LocalDate.parse(mallArriveDateUpdateRequestDto.getDeliveryDt(), dateFormatter));

					// 변경하려는 도착예정일에 재고가 없을 경우
				}else if(arrivalScheduledDateDto == null){
					throw new BaseException(DeliveryEnums.ChangeArriveDateValidation.LACK_STOCK);
					//return ApiResult.result(DeliveryEnums.ChangeArriveDateValidation.LACK_STOCK);
				}

				// 출고 마감시간 체크
				ApiResult<?> checkOverWarehouseCutoffTimeRes = storeWarehouseBiz.checkOverWarehouseCutoffTime(mallArriveDateRequestDto.getUrWarehouseId(), arrivalScheduledDateDto.getForwardingScheduledDate(), mallArriveDateRequestDto.getIsDawnDelivery());
				if(!checkOverWarehouseCutoffTimeRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
					throw new BaseException(checkOverWarehouseCutoffTimeRes.getMessageEnum());
					//return checkOverWarehouseCutoffTimeRes;
				}

				// 기존 도착일 재고 수정
				Long odOrderId = mallOrderDetailBiz.getOrderInfoByOdOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId()).getOdOrderId();
				odOrderIds.add(odOrderId);
				ApiResult<?> originStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId(), "N");
				if (!originStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
					throw new BaseException(originStockRes.getMessageEnum());
					//return originStockRes;
				}

				// 도착예정일 변경
				OrderDetlVo orderDetlVo = OrderDetlVo.builder()
						.odOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId())
						.orderIfId(createId)
						.orderIfDt(arrivalScheduledDateDto.getOrderDate())
						.shippingId(createId)
						.shippingDt(arrivalScheduledDateDto.getForwardingScheduledDate())
						.deliveryId(createId)
						.deliveryDt(arrivalScheduledDateDto.getArrivalScheduledDate())
						.build();

				// update
				orderDateBiz.putOrderDetailDt(orderDetlVo);

				// 변경 도착일 재고 수정
				ApiResult<?> changeStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId(), "Y");
				if (!changeStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
					throw new BaseException(changeStockRes.getMessageEnum());
					//return changeStockRes;
				}

				String orderStatusDetailTypeNm = "";
				// 새벽배송 -> 택배배송 변경인 경우
				if(GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(mallArriveDateUpdateRequestDto.getDeliveryType())
					&& !mallArriveDateRequestDto.getIsDawnDelivery()) {
					// 주문건의 배송유형 새벽-> 택배로 변경
					String goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL.getCode();
					String orderStatusDetailType = OrderEnums.OrderStatusDetailType.NORMAL.getCode();
					orderStatusDetailTypeNm = OrderEnums.OrderStatusDetailType.NORMAL.getCodeName();

					orderOrderBiz.putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId(),goodsDeliveryType, orderStatusDetailType);
				} else {
					orderStatusDetailTypeNm = prevOrderStatusDeliTpNm;
				}
				/* 일일상품인 경우 ->  일일배송 스케쥴 변경*/
				// 주문상세PK로 일일상품 정보 조회
				MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId());

				if(GoodsEnums.GoodsType.DAILY.getCode().equals(orderDetailGoodsDto.getGoodsTpCd())){

					OrderDetlVo dailyOrderDetlVo = OrderDetlVo.builder()
							.odOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId())
							.deliveryDt(arrivalScheduledDateDto.getArrivalScheduledDate())
							.urWarehouseId(mallArriveDateRequestDto.getUrWarehouseId())
							.odShippingZoneId(orderDetailGoodsDto.getOdShippingZoneId())
							.promotionTp(orderDetailGoodsDto.getPromotionTp())
							.build();

					//스케쥴 변경
					orderScheduleBiz.changeOrderDetlDaily(dailyOrderDetlVo,mallArriveDateRequestDto.getOdOrderDetlId());

				}

				// 처리이력 메세지 > {배송방법} 주문I/F : {주문I/F} / 도착예정일 : {도착예정일} → {배송방법} 주문I/F : {주문I/F} / 도착예정일 : {도착예정일}
				histMsg = MessageFormat.format(OrderEnums.OrderDetailStatusHistMsg.DELIVERY_DT_CHANGE_MSG.getMessage(),
												prevOrderStatusDeliTpNm, prevOrderIfDt, prevDeliveryDt,
												orderStatusDetailTypeNm, arrivalScheduledDateDto.getOrderDate().format(dateFormatter), arrivalScheduledDateDto.getArrivalScheduledDate().format(dateFormatter));

				//UserVo userVo = SessionUtil.getBosUserVO();
				OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
						.odOrderDetlId(orderDetlVo.getOdOrderDetlId())	// 주문상세PK
						.statusCd(orderDetlVo.getOrderStatusCd())		// 변경상태값
						.histMsg(histMsg)		// 메세지
						.createId(createId)	// 등록자
						.build();


				// 주문상세상태 이력 등록 (테이블 : OD_ORDER_DETL_HIST)
				// 일일배송상품 스케쥴 변경 시 "원스케쥴정보" >> "변경스케쥴정보" 확인
				orderStatusService.putOrderDetailStatusHist(orderDetlHistVo);
			}

			// 일자별 출고처별 출고예정수량 업데이트
			orderOrderBiz.putWarehouseDailyShippingCount(odOrderIds);

			// 일일상품인 경우 OD_ORDER_DETL_DAILY_ZONE 정보 수정
			for(MallArriveDateRequestDto mallArriveDateRequestDto: mallArriveDateUpdateRequestDto.getMallArriveDateList()){
				// 주문상세PK로 일일상품 정보 조회
				MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(mallArriveDateRequestDto.getOdOrderDetlId());
				if(GoodsEnums.GoodsType.DAILY.getCode().equals(orderDetailGoodsDto.getGoodsTpCd())){
					orderScheduleBiz.saveOrderDetlDailyZone(orderDetailGoodsDto.getOdOrderDetlId(), orderDetailGoodsDto.getPromotionTp(), null);
				}
			}


		}

		return ApiResult.success();
	}
}