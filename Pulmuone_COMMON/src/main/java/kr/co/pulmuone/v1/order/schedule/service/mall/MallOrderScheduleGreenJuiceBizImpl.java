package kr.co.pulmuone.v1.order.schedule.service.mall;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustOrdInpHeaderRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustOrdInpLineRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustOrdInpRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordConditionRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordSearchResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordSerachLineResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailGreenJuiceListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDateInfoDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDayOfWeekListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDelvDateDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleGoodsDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleShippingInfoDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.vo.OrderDetlScheduleVo;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
*
* <PRE>
* Forbiz Korea
* 주문 스캐줄 리스트 관련 Interface
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 2. 26.       석세동         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service("mallOrderScheduleGreenJuiceBizImpl")
public class MallOrderScheduleGreenJuiceBizImpl implements MallOrderScheduleBindBiz {

	// 주문 녹즙/잇슬림/베이비밀 스캐줄  관련 Service
	@Autowired
	private MallOrderScheduleService orderScheduleService;

	// 스토어 배송권역 Biz
	@Autowired
	private StoreDeliveryBiz storeDeliveryBiz;

    /**
     * 주문 녹즙 스캐줄 리스트 조회
     * @param orderDetailScheduleListRequestDto
     * @return List<OrderDetailScheduleListDto>
     */
	@Override
	public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception {

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		BaseApiResponseVo baseApiResponseVo = null;

		List<ErpIfCustordSearchResponseDto> erpCustordApiList = new ArrayList<>();

		String scheduleBatchYn = orderScheduleService.getOrderDetailBatchInfo(orderDetailScheduleListRequestDto.getOdOrderDetlId());

		if(scheduleBatchYn.equals(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode())) {

			//취소 내역
	    	baseApiResponseVo = orderScheduleService.getErpCustordApiList(orderDetailScheduleListRequestDto.getOdid(), OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());

	        if (!baseApiResponseVo.isSuccess()) {
	        	return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
	        }

	        erpCustordApiList.addAll(baseApiResponseVo.deserialize(ErpIfCustordSearchResponseDto.class));

	        //주문 내역
	        baseApiResponseVo = orderScheduleService.getErpCustordApiList(orderDetailScheduleListRequestDto.getOdid(), OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode());


	        if (!baseApiResponseVo.isSuccess()) {
	        	return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
	        }

	        erpCustordApiList.addAll(baseApiResponseVo.deserialize(ErpIfCustordSearchResponseDto.class));

	        if (!erpCustordApiList.isEmpty()) {

	        	List<ErpIfCustordRequestDto> erpCustordFlagApiList = new ArrayList<>();


	        	for(ErpIfCustordSearchResponseDto erpIfCustordSearchResponseDto : erpCustordApiList) {
	        		ErpIfCustordConditionRequestDto erpIfCustordConditionRequestDto = new ErpIfCustordConditionRequestDto();
	    		    erpIfCustordConditionRequestDto.setHrdSeq(erpIfCustordSearchResponseDto.getHrdSeq());
	    		    erpIfCustordConditionRequestDto.setOrdNum(erpIfCustordSearchResponseDto.getOrderNumber());

	    		    List<ErpIfCustordConditionRequestDto> scheduleList = new ArrayList<>();
	    		    scheduleList.add(erpIfCustordConditionRequestDto);

	    		    ErpIfCustordRequestDto erpIfCustordRequestDto = new ErpIfCustordRequestDto();
				    erpIfCustordRequestDto.setHrdSeq(erpIfCustordSearchResponseDto.getHrdSeq());
				    erpIfCustordRequestDto.setOrdNum(erpIfCustordSearchResponseDto.getOrderNumber());
				    erpIfCustordRequestDto.setLine(scheduleList);

				    erpCustordFlagApiList.add(erpIfCustordRequestDto);
	        	}

				baseApiResponseVo = orderScheduleService.putIfDlvFlagByErp(erpCustordFlagApiList);

				if(!baseApiResponseVo.isSuccess()) {
					return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				}

	        	List<OrderDetlScheduleVo> insertScheduleList = new ArrayList<>();
	    		List<OrderDetlScheduleVo> updateScheduleList = new ArrayList<>();
	    		String deliveryDate = "";
	            for(ErpIfCustordSearchResponseDto erpIfCustordSearchResponseDto : erpCustordApiList) {
	            	if(erpIfCustordSearchResponseDto.getLine() != null) {
			        	for( ErpIfCustordSerachLineResponseDto erpIfCustordSerachLineResponseDto : erpIfCustordSearchResponseDto.getLine()){
			        		OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();

			        		orderDetlScheduleVo.setOdid(Long.parseLong(erpIfCustordSearchResponseDto.getOrderNumber()));
			        		orderDetlScheduleVo.setOdOrderDetlId(orderDetailScheduleListRequestDto.getOdOrderDetlId());
				       		orderDetlScheduleVo.setOdOrderDetlSeq(erpIfCustordSerachLineResponseDto.getOrdNoDtl());
				       		orderDetlScheduleVo.setOdOrderDetlDailySchSeq(erpIfCustordSerachLineResponseDto.getSchLinNo());
							deliveryDate = erpIfCustordSerachLineResponseDto.getDlvReqDat().substring(0, 4) + "-"
									+ erpIfCustordSerachLineResponseDto.getDlvReqDat().substring(4, 6) + "-"
									+ erpIfCustordSerachLineResponseDto.getDlvReqDat().substring(6, 8);
				       		orderDetlScheduleVo.setDeliveryDt(deliveryDate);
				       		orderDetlScheduleVo.setOrderSchStatus(erpIfCustordSearchResponseDto.getHrdType());
				       		orderDetlScheduleVo.setOrderCnt(Integer.parseInt(erpIfCustordSerachLineResponseDto.getOrdCnt()));

							if (OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode().equals(erpIfCustordSearchResponseDto.getHrdType())) {
								orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_N.getCode());
								updateScheduleList.add(orderDetlScheduleVo);
							}else {
								orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_Y.getCode());
								insertScheduleList.add(orderDetlScheduleVo);
							}
			        	}
		        	}
	            }

	            int result = 0;

				if (!updateScheduleList.isEmpty())
					result = orderScheduleService.putErpOrderDetlSchedule(updateScheduleList);

				if (!insertScheduleList.isEmpty())
					result = orderScheduleService.addOrderDetlSchedule(insertScheduleList);

	        }
		}
		OrderDetailScheduleGoodsDto orderDetailScheduleGoodsDto = new OrderDetailScheduleGoodsDto();
		if(orderDetailScheduleListRequestDto.getPromotionYn().equals(OrderScheduleEnums.ScheduleSelectType.SCHEDULE_SELECT_Y.getCode())) {
			orderDetailScheduleGoodsDto = orderScheduleService.getOrderScheduleSelectGoodsInfo(Long.parseLong(orderDetailScheduleListRequestDto.getOdOrderId()));
			orderDetailScheduleGoodsDto.setGoodsDailyTp(orderDetailScheduleListRequestDto.getGoodsDailyTp());
			orderDetailScheduleGoodsDto.setOdOrderDetlId(orderDetailScheduleListRequestDto.getOdOrderDetlId());
		}else
			orderDetailScheduleGoodsDto = orderScheduleService.getOrderScheduleGoodsInfo(orderDetailScheduleListRequestDto.getOdOrderDetlId());

		if (orderDetailScheduleGoodsDto == null)
			return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_NO_DATA);

		if(orderDetailScheduleGoodsDto.getDailyPattern() != null)
			orderDetailScheduleGoodsDto.setDailyPattern(orderDetailScheduleGoodsDto.getDailyPattern().replace(Constants.ARRAY_SEPARATORS, "/"));

 		String goodsDailyCycleTermDays = "";

 		//배송 요일
 		goodsDailyCycleTermDays = (orderDetailScheduleGoodsDto.getMonCnt() > 0 ? GoodsEnums.WeekCodeByGreenJuice.MON.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getTueCnt() > 0 ? GoodsEnums.WeekCodeByGreenJuice.TUE.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getWedCnt() > 0 ? GoodsEnums.WeekCodeByGreenJuice.WED.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getThuCnt() > 0 ? GoodsEnums.WeekCodeByGreenJuice.THU.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getFriCnt() > 0 ? GoodsEnums.WeekCodeByGreenJuice.FRI.getCodeName(): "");
 		goodsDailyCycleTermDays = goodsDailyCycleTermDays.endsWith("/") ? goodsDailyCycleTermDays.substring(0, goodsDailyCycleTermDays.length()-1) : goodsDailyCycleTermDays;
 		orderDetailScheduleGoodsDto.setGoodsDailyCycleTermDays(goodsDailyCycleTermDays);

 		// 배송가능한 배송권역정보 조회
 		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(
						Long.parseLong(orderDetailScheduleListRequestDto.getIlGoodsId()),	// 주문상세 상품ID
						orderDetailScheduleListRequestDto.getRecvZipCd(),						// 수령인 우편번호
						orderDetailScheduleListRequestDto.getRecvBldNo());						// 건물번호

		if (shippingPossibilityStoreDeliveryAreaDto == null || shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType().isEmpty()) {
			return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.VALUE_EMPTY);
		}

		orderDetailScheduleGoodsDto.setScheduleDeliveryInterval(shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType());

 		orderDetailScheduleListRequestDto.setListType("N");
 		List<OrderDetailScheduleListDto> orderDetailScheduleList = orderScheduleService.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);

		// 스케줄 목록 배송예정일 기준으로 그룹
		Map<String, List<OrderDetailScheduleListDto>> delvDateMap = orderDetailScheduleList.stream()
				.collect(groupingBy(OrderDetailScheduleListDto::getDelvDate, LinkedHashMap::new, toList()));

		List<OrderDetailScheduleDelvDateDto> scheduleDelvDateList = new ArrayList<OrderDetailScheduleDelvDateDto>();
		delvDateMap.entrySet().forEach(entry -> {
			String key = entry.getKey();

			OrderDetailScheduleDelvDateDto orderDetailScheduleDelvDateDto = new OrderDetailScheduleDelvDateDto();
			orderDetailScheduleDelvDateDto.setDeliveryYn(OrderScheduleEnums.ScheduleDeliveryType.SCHEDULE_DELIVERY_Y.getCode());
			for (int i = 0; i < entry.getValue().size(); i++) {
				if (entry.getValue().get(i).getDeliveryYn().equals(OrderScheduleEnums.ScheduleDeliveryType.SCHEDULE_DELIVERY_N.getCode()))
					orderDetailScheduleDelvDateDto.setDeliveryYn(OrderScheduleEnums.ScheduleDeliveryType.SCHEDULE_DELIVERY_N.getCode());
			}

			orderDetailScheduleDelvDateDto.setDelvDate(key);
			orderDetailScheduleDelvDateDto.setDelvDateWeekDay(entry.getValue().get(0).getDelvDateWeekDay());
			orderDetailScheduleDelvDateDto.setRows(entry.getValue());
			orderDetailScheduleDelvDateDto.setCutoffTimeYn(orderDetailScheduleListRequestDto.getCutoffTimeYn());

			// 도착일변경 버튼 노출여부
			String changeDeliveryDateBtnYn = orderScheduleService.isChangeDeliveryDate(orderDetailScheduleListRequestDto,key) ? "Y" : "N";
			orderDetailScheduleDelvDateDto.setChangeDeliveryDateBtnYn(changeDeliveryDateBtnYn);
			scheduleDelvDateList.add(orderDetailScheduleDelvDateDto);
		});
 		OrderDetailScheduleListResponseDto orderDetailScheduleListResponseDto = OrderDetailScheduleListResponseDto.builder()
				.goodsInfo(orderDetailScheduleGoodsDto)
				.scheduleDelvDateList(scheduleDelvDateList)
				.build();

        return ApiResult.success(orderDetailScheduleListResponseDto);
	}

	/**
	 * 녹즙 스케줄 도착일,수량 변경
	 *
	 * @param orderDetailScheduleUpdateRequestDto
	 * @return ApiResult<?>
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto)
			throws Exception {

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		// 해당 주문상세의 기본 배송 스케줄 조회
		OrderDetailScheduleGoodsDto orderDetailScheduleInfo = orderScheduleService.getOrderScheduleGoodsInfo(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());

		//스캐줄 변경 내역 확인
		BaseApiResponseVo baseApiResponseVo = null;
		baseApiResponseVo = orderScheduleService.getErpCustordApiList(Long.toString(orderDetailScheduleInfo.getOdid()), OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());

        if (!baseApiResponseVo.isSuccess()) {
        	return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
        }else if(baseApiResponseVo.getResponseCode().equals("000")){
        	return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_INSERT_FAILED);
        }

		for (int i = 0; i < orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().size(); i++) {

			String deliveryDt = orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().get(i).getDelvDate();
			String odOrderDetlDailySchId = orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().get(i).getId();
			String orderCnt = orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().get(i).getOrderCnt();
			long odOrderDetlId = orderScheduleService.getOdOrderDetlId(Long.parseLong(odOrderDetlDailySchId));
			List<OrderDetailScheduleDateInfoDto> orderDetailScheduleDateInfoDto = orderScheduleService.getOrderDetailScheduleDeliveryDt(odOrderDetlId);

			// 필수 정보 확인
			if (StringUtils.isEmpty(deliveryDt) || StringUtils.isEmpty(odOrderDetlDailySchId) || StringUtils.isEmpty(orderCnt))
				return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_VALUE_FAILED);

			// 스케줄 일자 변경 정합성 체크
			ApiResult<?> validResult = putScheduleValidation(orderDetailScheduleInfo, deliveryDt);
			if (Integer.parseInt(validResult.getCode()) < 0) return validResult;

			List<OrderDetailScheduleListDto> insertScheduleList = new ArrayList<>();

			List<OrderDetailScheduleListDto> insertScheduleCancelList = new ArrayList<>();

			List<OrderDetlScheduleVo> updateScheduleList = new ArrayList<>();

			OrderDetailScheduleListDto orderDetailScheduleArrivalDateInfo = orderScheduleService
					.getOrderDetailScheduleArrivalInfo(odOrderDetlId, deliveryDt,
							null);

			// 변경할 일정의 데이터가 있을 경우 수량 증가
			if (orderDetailScheduleArrivalDateInfo != null) {

				insertScheduleCancelList.add(orderDetailScheduleArrivalDateInfo);
				orderCnt = Integer.toString(Integer.parseInt(orderCnt) + Integer.parseInt(orderDetailScheduleArrivalDateInfo.getOrderCnt()));

			}


			OrderDetailScheduleListDto orderDetailScheduleArrivalInfo = orderScheduleService
					.getOrderDetailScheduleArrivalInfo(odOrderDetlId, null,
							Long.parseLong(odOrderDetlDailySchId));

			if (deliveryDt.equals(orderDetailScheduleArrivalInfo.getDelvDate()))
				return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_CHANGE_FROM_DATE);

			insertScheduleCancelList.add(orderDetailScheduleArrivalInfo);

			if(orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().get(i).getOrderCnt().equals("0"))
				return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_GREENJUICE_ORDER_CNT_FAILED);

			if(Integer.parseInt(orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().get(i).getOrderCnt()) > Integer.parseInt(orderDetailScheduleArrivalInfo.getOrderCnt()))
				return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_GREENJUICE_ORDER_CNT_FAILED);

			// 수량 감소의 경우 일정 추가 생성
			if(Integer.parseInt(orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().get(i).getOrderCnt()) < Integer.parseInt(orderDetailScheduleArrivalInfo.getOrderCnt())) {
				int changeOrderCnt = Integer.parseInt(orderDetailScheduleArrivalInfo.getOrderCnt())-Integer.parseInt(orderDetailScheduleUpdateRequestDto.getScheduleUpdateList().get(i).getOrderCnt());

				OrderDetailScheduleListDto orderDetailScheduleListDto = new OrderDetailScheduleListDto();

				orderDetailScheduleListDto.setGoodsNm(orderDetailScheduleArrivalInfo.getGoodsNm());
				orderDetailScheduleListDto.setDelvDate(orderDetailScheduleArrivalInfo.getDelvDate());
				orderDetailScheduleListDto.setOrderCnt(Integer.toString(changeOrderCnt));
				orderDetailScheduleListDto.setOdOrderDetlDailySchSeq(orderDetailScheduleArrivalInfo.getOdOrderDetlDailySchSeq());
				orderDetailScheduleListDto.setOdOrderDetlDailyId(orderDetailScheduleArrivalInfo.getOdOrderDetlDailyId());
				orderDetailScheduleListDto.setDeliveryYn(orderDetailScheduleArrivalInfo.getDeliveryYn());
				orderDetailScheduleListDto.setOdOrderDetlDailySchId(orderDetailScheduleArrivalInfo.getOdOrderDetlDailySchId());

				insertScheduleList.add(orderDetailScheduleListDto);
			}

			// 변경된 일정
			OrderDetailScheduleListDto orderDetailScheduleListDto = new OrderDetailScheduleListDto();

			orderDetailScheduleListDto.setGoodsNm(orderDetailScheduleArrivalInfo.getGoodsNm());
			orderDetailScheduleListDto.setDelvDate(deliveryDt);
			orderDetailScheduleListDto.setOrderCnt(orderCnt);
			orderDetailScheduleListDto.setOdOrderDetlDailySchSeq(orderDetailScheduleArrivalInfo.getOdOrderDetlDailySchSeq());
			orderDetailScheduleListDto.setOdOrderDetlDailyId(orderDetailScheduleArrivalInfo.getOdOrderDetlDailyId());
			orderDetailScheduleListDto.setDeliveryYn(orderDetailScheduleArrivalInfo.getDeliveryYn());
			orderDetailScheduleListDto.setOdOrderDetlDailySchId(orderDetailScheduleArrivalInfo.getOdOrderDetlDailySchId());

			insertScheduleList.add(orderDetailScheduleListDto);


			insertScheduleCancelList = insertScheduleCancelList.stream().sorted(Comparator.comparing(OrderDetailScheduleListDto::getDelvDate)).collect(Collectors.toList());

			//스캐줄 배치 여부
			String scheduleBatchYn = orderScheduleService.getOrderDetailBatchInfo(odOrderDetlId);

			//int orderDetailDailySchSeq = orderScheduleService.getOrderDetailDailySchSeq(odOrderDetlId);

			if(scheduleBatchYn.equals(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode())) {

				//스캐줄 취소
				for(OrderDetailScheduleListDto cancelOrderDetailScheduleListDto : insertScheduleCancelList) {

					OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();
					orderDetlScheduleVo.setOdOrderDetlDailyId(cancelOrderDetailScheduleListDto.getOdOrderDetlDailyId());
					orderDetlScheduleVo.setOdOrderDetlDailySchSeq(cancelOrderDetailScheduleListDto.getOdOrderDetlDailySchSeq());
					orderDetlScheduleVo.setOdOrderDetlId(odOrderDetlId);
					orderDetlScheduleVo.setDeliveryDt(cancelOrderDetailScheduleListDto.getDelvDate());
					orderDetlScheduleVo.setOrderCnt(Integer.parseInt(cancelOrderDetailScheduleListDto.getOrderCnt()));
					orderDetlScheduleVo.setOrderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());
					orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_N.getCode());

					updateScheduleList.add(orderDetlScheduleVo);

					//orderScheduleService.addChangeOrderDetailSchedule(orderDetlScheduleVo);

				}

				int result = 0;

				if (!updateScheduleList.isEmpty())
					result = orderScheduleService.putOrderDetlSchedule(updateScheduleList);

			}else {
				//스캐줄 삭제
				for(OrderDetailScheduleListDto cancelOrderDetailScheduleListDto : insertScheduleCancelList) {

					OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();

					orderDetlScheduleVo.setOdOrderDetlId(odOrderDetlId);
					orderDetlScheduleVo.setOdOrderDetlDailySchId(cancelOrderDetailScheduleListDto.getOdOrderDetlDailySchId());
					orderScheduleService.delOdOrderDetlDailySch(orderDetlScheduleVo);

				}
			}

			insertScheduleList = insertScheduleList.stream().sorted(Comparator.comparing(OrderDetailScheduleListDto::getDelvDate)).collect(Collectors.toList());

			// erpInsertList
			List<OrderDetailScheduleListDto> erpInsertScheduleList = new ArrayList<>();
			//스캐줄 변경
			for(OrderDetailScheduleListDto addOrderDetailScheduleListDto :	insertScheduleList) {

				OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();
				orderDetlScheduleVo.setOdOrderDetlDailyId(addOrderDetailScheduleListDto.getOdOrderDetlDailyId());
				orderDetlScheduleVo.setDeliveryDt(addOrderDetailScheduleListDto.getDelvDate());
				orderDetlScheduleVo.setOrderCnt(Integer.parseInt(addOrderDetailScheduleListDto.getOrderCnt()));
				orderDetlScheduleVo.setOrderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode());
				orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_Y.getCode());
				orderDetlScheduleVo.setOdOrderDetlId(odOrderDetlId);

				if(addOrderDetailScheduleListDto.getDelvDate().compareTo(orderDetailScheduleDateInfoDto.get(0).getStartDate()) <= 0)
					orderDetlScheduleVo.setOdShippingZoneId(orderDetailScheduleDateInfoDto.get(0).getOdShippingZoneId());
				else if(addOrderDetailScheduleListDto.getDelvDate().compareTo(orderDetailScheduleDateInfoDto.get(orderDetailScheduleDateInfoDto.size()-1).getEndDate()) >= 0)
					orderDetlScheduleVo.setOdShippingZoneId(orderDetailScheduleDateInfoDto.get(orderDetailScheduleDateInfoDto.size()-1).getOdShippingZoneId());

				orderScheduleService.addChangeOrderDetailSchedule(orderDetlScheduleVo);
				OrderDetailScheduleListDto erpInsertScheduleItem = new OrderDetailScheduleListDto();
				erpInsertScheduleItem.setOdOrderDetlDailySchSeq(orderDetlScheduleVo.getOdOrderDetlDailySchSeq());
				erpInsertScheduleList.add(erpInsertScheduleItem);
			}

			//ERP 스캐줄 입력
			if(scheduleBatchYn.equals(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode())) {

				baseApiResponseVo = this.sendErpGreenJuice(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId(), updateScheduleList, erpInsertScheduleList);
				if(ObjectUtils.isNotEmpty(baseApiResponseVo) && !baseApiResponseVo.isSuccess()) {
					throw new BaseException(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				}

				/*
				List<OrderDetailGreenJuiceListDto> orderDetailGreenJuiceList = orderScheduleService
						.getErpOrderDetailScheduleList(odOrderDetlId, orderDetailDailySchSeq, null);

				List<ErpIfCustOrdInpLineRequestDto> erpCustordLineApiList = new ArrayList<>();

				List<ErpIfCustOrdInpLineRequestDto> erpCustCancelLineApiList = new ArrayList<>();

				List<OrderDetailGreenJuiceListDto> erpGreenJuiceList = new ArrayList<>();

				// 스캐줄 취소 내역 입력

				erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(
						x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode().equals(x.getOrderSchStatus()))
						.collect(toList());

				Map<Long, List<OrderDetailGreenJuiceListDto>> cancelShippingZoneMap = erpGreenJuiceList.stream()
						.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

				List<ErpIfCustOrdInpHeaderRequestDto> cancelHeaderList = new ArrayList<>();

				cancelShippingZoneMap.entrySet().forEach(entry -> {
					Long key = entry.getKey();

					for (int j = 0; j < entry.getValue().size(); j++) {
						erpCustCancelLineApiList.add(orderScheduleService.getScheduleDailyDeliveryOrderLine(entry.getValue().get(j)));
					}
					OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleService
							.getOrderDetailScheduleShippingInfo(
									Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
					ErpIfCustOrdInpHeaderRequestDto erpIfCustCancelHeaderRequestDto = orderScheduleService
							.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto,
									erpCustCancelLineApiList);
					cancelHeaderList.add(erpIfCustCancelHeaderRequestDto);
				});

				ErpIfCustOrdInpRequestDto erpIfCustCancelRequestDto =  ErpIfCustOrdInpRequestDto.builder()
	                    .totalPage(1)
	                    .currentPage(1)
	                    .header(cancelHeaderList)
	                    .build();

				baseApiResponseVo = orderScheduleService.addIfDlvFlagByErp(erpIfCustCancelRequestDto);

				if (!baseApiResponseVo.isSuccess()) {
					return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				}

				// 스캐줄 주문 내역 입력

				erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(
						x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode().equals(x.getOrderSchStatus()))
						.collect(toList());

				Map<Long, List<OrderDetailGreenJuiceListDto>> ShippingZoneMap = erpGreenJuiceList.stream()
						.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

				List<ErpIfCustOrdInpHeaderRequestDto> orderHeaderList = new ArrayList<>();

				ShippingZoneMap.entrySet().forEach(entry -> {
					Long key = entry.getKey();
					for (int j = 0; j < entry.getValue().size(); j++) {
						erpCustordLineApiList.add(orderScheduleService.getScheduleDailyDeliveryOrderLine(entry.getValue().get(j)));
					}
					OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleService
							.getOrderDetailScheduleShippingInfo(
									Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
					ErpIfCustOrdInpHeaderRequestDto erpIfCustOrdInpHeaderRequestDto = orderScheduleService
							.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto,
									erpCustordLineApiList);
					orderHeaderList.add(erpIfCustOrdInpHeaderRequestDto);
				});

		        ErpIfCustOrdInpRequestDto erpIfCustOrdInpRequestDto =  ErpIfCustOrdInpRequestDto.builder()
	                    .totalPage(1)
	                    .currentPage(1)
	                    .header(orderHeaderList)
	                    .build();

				baseApiResponseVo = orderScheduleService.addIfDlvFlagByErp(erpIfCustOrdInpRequestDto);

				if (!baseApiResponseVo.isSuccess()) {
					return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				}
				*/
			}
		}

		return ApiResult.success();
	}

	/**
	 * 녹즙 ERP 취소 / 주문 전송
	 * @param odOrderDetlId
	 * @param updateScheduleList
	 * @param insertScheduleList
	 * @return
	 * @throws Exception
	 */
	private BaseApiResponseVo sendErpGreenJuice(long odOrderDetlId, List<OrderDetlScheduleVo> updateScheduleList, List<OrderDetailScheduleListDto> insertScheduleList) throws Exception {

		BaseApiResponseVo baseApiResponseVo = null;

		List<ErpIfCustOrdInpLineRequestDto> erpCustordLineApiList = new ArrayList<>();

		List<ErpIfCustOrdInpLineRequestDto> erpCustCancelLineApiList = new ArrayList<>();

		List<OrderDetailGreenJuiceListDto> erpGreenJuiceList = new ArrayList<>();

		// update 건 기준으로 ERP 전송 목록 조회
		List<OrderDetailGreenJuiceListDto> orderDetailGreenJuiceList = orderScheduleService.getErpOrderDetailScheduleListByCancelUpdateList(odOrderDetlId, updateScheduleList);

		erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode().equals(x.getOrderSchStatus())).collect(toList());

		// 스캐줄 취소 내역 입력
		if(!erpGreenJuiceList.isEmpty()) {
			Map<Long, List<OrderDetailGreenJuiceListDto>> cancelShippingZoneMap = erpGreenJuiceList.stream()
					.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

			List<ErpIfCustOrdInpHeaderRequestDto> cancelHeaderList = new ArrayList<>();

			cancelShippingZoneMap.entrySet().forEach(entry -> {
				Long key = entry.getKey();

				for (int j = 0; j < entry.getValue().size(); j++) {
					erpCustCancelLineApiList.add(orderScheduleService.getScheduleDailyDeliveryOrderLine(entry.getValue().get(j)));
				}
				OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleService.getOrderDetailScheduleShippingInfo(Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
				orderDetailScheduleShippingInfoDto.setSeqNo(entry.getValue().get(0).getSeqNo());
				ErpIfCustOrdInpHeaderRequestDto erpIfCustCancelHeaderRequestDto = orderScheduleService.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto,erpCustCancelLineApiList);
				cancelHeaderList.add(erpIfCustCancelHeaderRequestDto);
			});

			ErpIfCustOrdInpRequestDto erpIfCustCancelRequestDto = ErpIfCustOrdInpRequestDto.builder()
					.totalPage(1)
					.currentPage(1)
					.header(cancelHeaderList)
					.build();

			baseApiResponseVo = orderScheduleService.addIfDlvFlagByErp(erpIfCustCancelRequestDto);

			if (!baseApiResponseVo.isSuccess()) {
				//throw new BaseException(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				return baseApiResponseVo;
			}

			// 스케쥴 취소 성공 후 API_CANCEL_SEND_YN 상태 'Y' 업데이트 처리
			orderScheduleService.putOrderDetlScheduleApiCancelSendYn(erpGreenJuiceList);
		}

		// insert 건 기준으로 ERP 전송 목록 조회
		orderDetailGreenJuiceList = orderScheduleService.getErpOrderDetailScheduleListByInsertList(odOrderDetlId, insertScheduleList);

		// 스캐줄 주문 내역 입력
		erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode().equals(x.getOrderSchStatus()) &&
																			OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_Y.getCode().equals(x.getUseYn())).collect(toList());

		// 스케쥴 주문 내역이 존재할 경우 처리
		if(erpGreenJuiceList != null && !erpGreenJuiceList.isEmpty()) {

			Map<Long, List<OrderDetailGreenJuiceListDto>> ShippingZoneMap = erpGreenJuiceList.stream()
					.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

			List<ErpIfCustOrdInpHeaderRequestDto> orderHeaderList = new ArrayList<>();

			ShippingZoneMap.entrySet().forEach(entry -> {
				Long key = entry.getKey();
				for (int j = 0; j < entry.getValue().size(); j++) {
					erpCustordLineApiList.add(orderScheduleService.getScheduleDailyDeliveryOrderLine(entry.getValue().get(j)));
				}
				OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleService
						.getOrderDetailScheduleShippingInfo(
								Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
				orderDetailScheduleShippingInfoDto.setSeqNo(entry.getValue().get(0).getSeqNo());
				ErpIfCustOrdInpHeaderRequestDto erpIfCustOrdInpHeaderRequestDto = orderScheduleService
						.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto,
								erpCustordLineApiList);
				orderHeaderList.add(erpIfCustOrdInpHeaderRequestDto);
			});

			ErpIfCustOrdInpRequestDto erpIfCustOrdInpRequestDto = ErpIfCustOrdInpRequestDto.builder()
					.totalPage(1)
					.currentPage(1)
					.header(orderHeaderList)
					.build();

			baseApiResponseVo = orderScheduleService.addIfDlvFlagByErp(erpIfCustOrdInpRequestDto);

			if (!baseApiResponseVo.isSuccess()) {
				//throw new BaseException(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				return baseApiResponseVo;
			}
		}
		return baseApiResponseVo;
	}

	/**
	 * 녹즙 스케줄 배송요일 변경
	 *
	 * @param orderDetailScheduleUpdateRequestDto
	 * @return ApiResult<?>
	 */
	@Override
	public ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto)
			throws Exception {

		// 필수 정보 확인
		if (StringUtils.isEmpty(orderDetailScheduleUpdateRequestDto.getChangeDate()) || StringUtils.isEmpty(orderDetailScheduleUpdateRequestDto.getDeliveryDayOfWeek()))
			return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_VALUE_FAILED);

		// 주문변경 가능 시작일
		String deliverableDate = DateUtil.addDays(DateUtil.getCurrentDate(), OrderScheduleEnums.ScheduleChangeStartDate.START_DATE.getAddDate(), "yyyy-MM-dd");

		if ((orderDetailScheduleUpdateRequestDto.getChangeDate().compareTo(deliverableDate) < 0))
			return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_CHANGE_TO_DATE);

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		String deliveryDayOfWeekList = "";

		//배송요일 패턴

		deliveryDayOfWeekList = (orderDetailScheduleUpdateRequestDto.getDeliveryDayOfWeek().indexOf(GoodsEnums.WeekCodeByGreenJuice.MON.getCode()) > -1 ? GoodsEnums.WeekCodeByGreenJuice.MON.getDayNum() + Constants.ARRAY_SEPARATORS : "") +
 				(orderDetailScheduleUpdateRequestDto.getDeliveryDayOfWeek().indexOf(GoodsEnums.WeekCodeByGreenJuice.TUE.getCode()) > -1 ? GoodsEnums.WeekCodeByGreenJuice.TUE.getDayNum() + Constants.ARRAY_SEPARATORS : "") +
 				(orderDetailScheduleUpdateRequestDto.getDeliveryDayOfWeek().indexOf(GoodsEnums.WeekCodeByGreenJuice.WED.getCode()) > -1 ? GoodsEnums.WeekCodeByGreenJuice.WED.getDayNum() + Constants.ARRAY_SEPARATORS : "") +
 				(orderDetailScheduleUpdateRequestDto.getDeliveryDayOfWeek().indexOf(GoodsEnums.WeekCodeByGreenJuice.THU.getCode()) > -1  ? GoodsEnums.WeekCodeByGreenJuice.THU.getDayNum() + Constants.ARRAY_SEPARATORS : "") +
 				(orderDetailScheduleUpdateRequestDto.getDeliveryDayOfWeek().indexOf(GoodsEnums.WeekCodeByGreenJuice.FRI.getCode()) > -1  ? GoodsEnums.WeekCodeByGreenJuice.FRI.getDayNum(): "");
		deliveryDayOfWeekList = deliveryDayOfWeekList.endsWith(Constants.ARRAY_SEPARATORS) ? deliveryDayOfWeekList.substring(0, deliveryDayOfWeekList.length()-1) : deliveryDayOfWeekList;

		orderDetailScheduleUpdateRequestDto.setDeliveryDayOfWeekList(orderScheduleService.getSearchKeyToSearchKeyList(
				deliveryDayOfWeekList, Constants.ARRAY_SEPARATORS));

		int scheduleOrderCnt = orderScheduleService.getOrderDetailScheduleOrderCnt(orderDetailScheduleUpdateRequestDto);

		// 해당 주문상세의 기본 배송 스케줄 조회
		OrderDetailScheduleGoodsDto orderDetailScheduleInfo = orderScheduleService.getOrderScheduleGoodsInfo(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());

		// 내만대로 여부 확인
		if (orderDetailScheduleInfo.getPromotionYn().equals("Y"))
			return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_GREENJUICE_SELECT_FAILED);

		//스캐줄 변경 내역 확인
		BaseApiResponseVo baseApiResponseVo = null;
		baseApiResponseVo = orderScheduleService.getErpCustordApiList(Long.toString(orderDetailScheduleInfo.getOdid()), OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());

        if (!baseApiResponseVo.isSuccess()) {
        	return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
        }else if(baseApiResponseVo.getResponseCode().equals("000")){
        	return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_INSERT_FAILED);
        }

		orderDetailScheduleUpdateRequestDto.setUrWarehouseId(orderDetailScheduleInfo.getUrWarehouseId());
		List<OrderDetailScheduleDayOfWeekListDto> orderDetailScheduleDayOfWeekList = orderScheduleService.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto);

		List<OrderDetailScheduleDayOfWeekListDto> insertScheduleList = new ArrayList<>();

		List<OrderDetlScheduleVo> updateScheduleList = new ArrayList<>();

		int result = 0;

		List<OrderDetailScheduleDateInfoDto> orderDetailScheduleDateInfoDto = orderScheduleService.getOrderDetailScheduleDeliveryDt(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());

		if(orderDetailScheduleDayOfWeekList.size() > 0) {

			int dayOfWeekCntTotal = 0;
			int orderCnt = 0;
			for(OrderDetailScheduleDayOfWeekListDto orderDetailScheduleDayOfWeekListDto : orderDetailScheduleDayOfWeekList) {

				dayOfWeekCntTotal = dayOfWeekCntTotal + orderDetailScheduleInfo.getOrderCnt();

				orderCnt = orderDetailScheduleInfo.getOrderCnt();

				if(dayOfWeekCntTotal > scheduleOrderCnt)
				{
					orderCnt = orderCnt - (dayOfWeekCntTotal-scheduleOrderCnt);
					if(orderCnt > 0) {
						orderDetailScheduleDayOfWeekListDto.setOrderCnt(orderCnt);
						insertScheduleList.add(orderDetailScheduleDayOfWeekListDto);
					}
					break;
				}
				orderDetailScheduleDayOfWeekListDto.setOrderCnt(orderCnt);

				insertScheduleList.add(orderDetailScheduleDayOfWeekListDto);
			}
			OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();
			orderDetailScheduleListRequestDto.setOdOrderDetlId(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());
			orderDetailScheduleListRequestDto.setChangeDate(orderDetailScheduleUpdateRequestDto.getChangeDate());
			orderDetailScheduleListRequestDto.setPromotionYn(OrderScheduleEnums.ScheduleSelectType.SCHEDULE_SELECT_N.getCode());
			orderDetailScheduleListRequestDto.setListType("Y");

			String scheduleBatchYn = orderScheduleService.getOrderDetailBatchInfo(orderDetailScheduleListRequestDto.getOdOrderDetlId());

			int orderDetailDailySchSeq = orderScheduleService.getOrderDetailDailySchSeq(orderDetailScheduleListRequestDto.getOdOrderDetlId());

			List<OrderDetailScheduleListDto> orderDetailScheduleList = orderScheduleService.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);

			if(scheduleBatchYn.equals(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode())) {
				//스캐줄 취소
				for(OrderDetailScheduleListDto cancelorderDetailScheduleListDto : orderDetailScheduleList) {

					OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();
					orderDetlScheduleVo.setOdOrderDetlDailyId(cancelorderDetailScheduleListDto.getOdOrderDetlDailyId());
					orderDetlScheduleVo.setOdOrderDetlDailySchSeq(cancelorderDetailScheduleListDto.getOdOrderDetlDailySchSeq());
					orderDetlScheduleVo.setOdOrderDetlId(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());
					orderDetlScheduleVo.setDeliveryDt(cancelorderDetailScheduleListDto.getDelvDate());
					orderDetlScheduleVo.setOrderCnt(Integer.parseInt(cancelorderDetailScheduleListDto.getOrderCnt()));
					orderDetlScheduleVo.setOrderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());
					orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_N.getCode());

					updateScheduleList.add(orderDetlScheduleVo);
					orderScheduleService.addChangeOrderDetailSchedule(orderDetlScheduleVo);
				}
				if (!updateScheduleList.isEmpty())
					result = orderScheduleService.putOrderDetlSchedule(updateScheduleList);
			}else {
				//스캐줄 삭제
				for(OrderDetailScheduleListDto cancelOrderDetailScheduleUpdateDto : orderDetailScheduleList) {

					OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();

					orderDetlScheduleVo.setOdOrderDetlId(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());
					orderDetlScheduleVo.setOdOrderDetlDailySchId(cancelOrderDetailScheduleUpdateDto.getOdOrderDetlDailySchId());
					orderScheduleService.delOdOrderDetlDailySch(orderDetlScheduleVo);

				}
			}

			//스캐줄 변경
			for(OrderDetailScheduleDayOfWeekListDto addOrderDetailScheduleListDto : insertScheduleList) {

				OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();
				orderDetlScheduleVo.setOdOrderDetlDailyId(orderDetailScheduleList.get(0).getOdOrderDetlDailyId());
				orderDetlScheduleVo.setOdOrderDetlId(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());
				orderDetlScheduleVo.setDeliveryDt(addOrderDetailScheduleListDto.getDelvDate());
				orderDetlScheduleVo.setOrderCnt(addOrderDetailScheduleListDto.getOrderCnt());
				orderDetlScheduleVo.setOrderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode());
				orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_Y.getCode());

				if(addOrderDetailScheduleListDto.getDelvDate().compareTo(orderDetailScheduleDateInfoDto.get(0).getStartDate()) <= 0)
					orderDetlScheduleVo.setOdShippingZoneId(orderDetailScheduleDateInfoDto.get(0).getOdShippingZoneId());
				else if(addOrderDetailScheduleListDto.getDelvDate().compareTo(orderDetailScheduleDateInfoDto.get(orderDetailScheduleDateInfoDto.size()-1).getEndDate()) >= 0)
					orderDetlScheduleVo.setOdShippingZoneId(orderDetailScheduleDateInfoDto.get(orderDetailScheduleDateInfoDto.size()-1).getOdShippingZoneId());

				orderScheduleService.addChangeOrderDetailSchedule(orderDetlScheduleVo);

			}

			result = orderScheduleService.putOrderDetlSchedulePattern(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId(), deliveryDayOfWeekList);

			//ERP 스캐줄 입력
			if(scheduleBatchYn.equals(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode())) {
				List<OrderDetailGreenJuiceListDto> orderDetailGreenJuiceList = orderScheduleService
						.getErpOrderDetailScheduleList(orderDetailScheduleListRequestDto.getOdOrderDetlId(), orderDetailDailySchSeq, null);

				List<ErpIfCustOrdInpLineRequestDto> erpCustordLineApiList = new ArrayList<>();

				List<ErpIfCustOrdInpLineRequestDto> erpCustCancelLineApiList = new ArrayList<>();

				List<OrderDetailGreenJuiceListDto> erpGreenJuiceList = new ArrayList<>();

				// 스캐줄 취소 내역 입력

				erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(
						x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.SCHEDULE_ORDER_TYPE_CANCEL
								.getCode().equals(x.getOrderSchStatus()))
						.collect(toList());

				Map<Long, List<OrderDetailGreenJuiceListDto>> cancelShippingZoneMap = erpGreenJuiceList.stream()
						.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

				List<ErpIfCustOrdInpHeaderRequestDto> cancelHeaderList = new ArrayList<>();

				cancelShippingZoneMap.entrySet().forEach(entry -> {
					Long key = entry.getKey();

					for (int j = 0; j < entry.getValue().size(); j++) {
						erpCustCancelLineApiList.add(orderScheduleService.getScheduleDailyDeliveryOrderLine(entry.getValue().get(j)));
					}
					OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleService
							.getOrderDetailScheduleShippingInfo(
									Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
					ErpIfCustOrdInpHeaderRequestDto erpIfCustCancelHeaderRequestDto = orderScheduleService
							.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto,
									erpCustCancelLineApiList);
					cancelHeaderList.add(erpIfCustCancelHeaderRequestDto);
				});

				ErpIfCustOrdInpRequestDto erpIfCustCancelRequestDto =  ErpIfCustOrdInpRequestDto.builder()
	                    .totalPage(1)
	                    .currentPage(1)
	                    .header(cancelHeaderList)
	                    .build();

				baseApiResponseVo = orderScheduleService.addIfDlvFlagByErp(erpIfCustCancelRequestDto);

				if (!baseApiResponseVo.isSuccess()) {
					return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				}

				// 스캐줄 주문 내역 입력

				erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(
						x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.SCHEDULE_ORDER_TYPE_ORDER
								.getCode().equals(x.getOrderSchStatus()))
						.collect(toList());

				Map<Long, List<OrderDetailGreenJuiceListDto>> ShippingZoneMap = erpGreenJuiceList.stream()
						.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

				List<ErpIfCustOrdInpHeaderRequestDto> orderHeaderList = new ArrayList<>();

				ShippingZoneMap.entrySet().forEach(entry -> {
					Long key = entry.getKey();
					for (int j = 0; j < entry.getValue().size(); j++) {
						erpCustordLineApiList.add(orderScheduleService.getScheduleDailyDeliveryOrderLine(entry.getValue().get(j)));
					}
					OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleService
							.getOrderDetailScheduleShippingInfo(
									Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
					ErpIfCustOrdInpHeaderRequestDto erpIfCustOrdInpHeaderRequestDto = orderScheduleService
							.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto,
									erpCustordLineApiList);
					orderHeaderList.add(erpIfCustOrdInpHeaderRequestDto);
				});

		        ErpIfCustOrdInpRequestDto erpIfCustOrdInpRequestDto =  ErpIfCustOrdInpRequestDto.builder()
	                    .totalPage(1)
	                    .currentPage(1)
	                    .header(orderHeaderList)
	                    .build();

				baseApiResponseVo = orderScheduleService.addIfDlvFlagByErp(erpIfCustOrdInpRequestDto);

				if (!baseApiResponseVo.isSuccess()) {
					return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
				}
			}
		}

		return ApiResult.success();
	}

	/**
     * 스케줄 변경 Validation
     * @param orderDetailScheduleGoodsDto(주문 상세 스케줄 정보), delvDate(변경할 배송예정일)
     * @return ApiResult<?>
	 * @throws Exception
     */
	private ApiResult<?> putScheduleValidation(OrderDetailScheduleGoodsDto orderDetailScheduleGoodsDto, String delvDate) throws Exception {

		// 해당 주문의 배송일자 변경 가능여부 체크(From)
		String fromDelvDate = delvDate.replaceAll("-", "");
		String fromWeekday 	= DateUtil.getWeekDay(fromDelvDate, Constants.SCHDULE_DELIVERY_DATE_CODE);
		Map<String, Object> fromDateMap = new HashMap<String, Object>();
		Stream.of(OrderScheduleEnums.ScheduleChangeDate.values()).forEach(scheduleChangeDate -> {
			if (fromWeekday.equals(scheduleChangeDate.getDeliveryDay())) fromDateMap.put("addDate", scheduleChangeDate.getAddDate());
		});
		if (fromDateMap.get("addDate") == null) return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.VALUE_EMPTY);
		String chkDelvDate = DateUtil.addDays(fromDelvDate, (int)fromDateMap.get("addDate"), "yyyyMMdd");
		String currentDate = DateUtil.getCurrentDate();
		if (Integer.parseInt(currentDate) > Integer.parseInt(chkDelvDate)) return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_CHANGE_FROM_DATE);

		String toDelvDate	= delvDate.replaceAll("-", "");
		// 주말(토,일) 제외
		if (OrderScheduleEnums.DailyUnAbledWeekDay.SAT.getCodeName().equals(DateUtil.getWeekDay(toDelvDate, Constants.SCHDULE_DELIVERY_DATE_CODE))
				|| OrderScheduleEnums.DailyUnAbledWeekDay.SUN.getCodeName().equals(DateUtil.getWeekDay(toDelvDate, Constants.SCHDULE_DELIVERY_DATE_CODE))) {
			return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_WEEKEND);
		}
		// 휴무일 제외
		if ("Y".equals(orderScheduleService.getOrderDetlScheduleHolidayYn(delvDate))) return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_HOLIDAY);

		// 해당 주문의 배송일자 변경 가능여부 체크(To)
		String toWeekday 	= DateUtil.getWeekDay(toDelvDate, Constants.SCHDULE_DELIVERY_DATE_CODE);
		Map<String, Object> toDateMap = new HashMap<String, Object>();
		Stream.of(OrderScheduleEnums.ScheduleChangeDate.values()).forEach(scheduleChangeDate -> {
			if (toWeekday.equals(scheduleChangeDate.getDeliveryDay())) toDateMap.put("addDate", scheduleChangeDate.getAddDate());
		});
		if (toDateMap.get("addDate") == null) return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.VALUE_EMPTY);
		chkDelvDate = DateUtil.addDays(toDelvDate, (int)toDateMap.get("addDate"), "yyyyMMdd");
		if (Integer.parseInt(currentDate) > Integer.parseInt(chkDelvDate)) return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_CHANGE_TO_DATE);

		// 패턴 불가능 권역 체크
		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(
						Long.parseLong(orderDetailScheduleGoodsDto.getIlGoodsId()),	// 주문상세 상품ID
						orderDetailScheduleGoodsDto.getRecvZipCd(),						// 수령인 우편번호
						orderDetailScheduleGoodsDto.getRecvBldNo());						// 건물번호

		if (shippingPossibilityStoreDeliveryAreaDto == null || shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType().isEmpty()) {
			return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.VALUE_EMPTY);
		}

		// 격일 배송(월,수,금)인 경우 화,목 체크
		if (GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode().equals(shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType())) {
			if (OrderScheduleEnums.ScheduleChangeDate.TUE.getDeliveryDay().equals(DateUtil.getWeekDay(toDelvDate, Constants.SCHDULE_DELIVERY_DATE_CODE))
					|| OrderScheduleEnums.ScheduleChangeDate.THU.getDeliveryDay().equals(DateUtil.getWeekDay(toDelvDate, Constants.SCHDULE_DELIVERY_DATE_CODE))) {
				return ApiResult.result(OrderScheduleEnums.ScheduleChangeErrMsg.REQUIRED_STORE_DELIVERY_AREA);
			}
		}

		return ApiResult.success();
	}

	public OrderDetailScheduleListDto getOrderDetailScheduleArrivalInfo(Long odOrderDetlId, String deliveryDt, Long odOrderDetlDailySchId){
		return orderScheduleService.getOrderDetailScheduleArrivalInfo(odOrderDetlId, deliveryDt,odOrderDetlDailySchId);
	}
}