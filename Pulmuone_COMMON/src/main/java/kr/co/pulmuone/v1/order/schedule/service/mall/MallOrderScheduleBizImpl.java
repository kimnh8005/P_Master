package kr.co.pulmuone.v1.order.schedule.service.mall;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.schedule.dto.*;
import kr.co.pulmuone.v1.order.schedule.factory.MallOrderScheduleFactory;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@Service
public class MallOrderScheduleBizImpl implements MallOrderScheduleBiz {

	@Autowired
    private MallOrderScheduleService orderScheduleListService;

	@Autowired
    private StoreDeliveryBiz storeDeliveryBiz;

	@Autowired
	private MallOrderScheduleFactory mallOrderScheduleFactory;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

    /**
     * 주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회
     * @param odOrderDetlId(주문상세 PK)
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@Override
    public ApiResult<?> getOrderScheduleList(Long odOrderDetlId) throws Exception {
    	MallOrderScheduleBindBiz orderScheduleBindBiz = null;
		if (Integer.parseInt(getOrderScheduleBind(odOrderDetlId).getCode()) < 0) return getOrderScheduleBind(odOrderDetlId);
		orderScheduleBindBiz = (MallOrderScheduleBindBiz) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderScheduleBindBiz");

    	// 각 일일상품별 스케줄 리스트 조회
		return ApiResult.success(orderScheduleBindBiz.getOrderScheduleList(
				(OrderDetailScheduleListRequestDto) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderDetailScheduleListRequestDto")));
    }

    /**
     * 주문 녹즙/잇슬림/베이비밀 스케줄 도착일,수량 변경
     * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		MallOrderScheduleBindBiz orderScheduleBindBiz = null;
		Long odOrderDetlId = orderDetailScheduleUpdateRequestDto.getOdOrderDetlId();
		if (Integer.parseInt(getOrderScheduleBind(odOrderDetlId).getCode()) < 0) return getOrderScheduleBind(odOrderDetlId);
		orderScheduleBindBiz = (MallOrderScheduleBindBiz) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderScheduleBindBiz");

    	// 각 일일상품별 스케줄 도착일,수량 변경 (수량변경은 녹즙만 가능)
		return ApiResult.success(orderScheduleBindBiz.putScheduleArrivalDate(orderDetailScheduleUpdateRequestDto));
	}

	/**
     * 주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경
     * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		MallOrderScheduleBindBiz orderScheduleBindBiz = null;
		Long odOrderDetlId = orderDetailScheduleUpdateRequestDto.getOdOrderDetlId();
		if (Integer.parseInt(getOrderScheduleBind(odOrderDetlId).getCode()) < 0) return getOrderScheduleBind(odOrderDetlId);

		// 일일 상품유형 체크 (현재는 녹즙만 가능)
		OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = (OrderDetailScheduleListRequestDto) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderDetailScheduleListRequestDto");
		if (!OrderScheduleEnums.ScheduleCd.GREENJUICE.getCode().equals(orderDetailScheduleListRequestDto.getGoodsDailyTp())) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.REQUIRED_GOODS_DAILY_TP);

		orderScheduleBindBiz = (MallOrderScheduleBindBiz) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderScheduleBindBiz");
    	// 각 일일상품별 스케줄 배송요일 변경
		return ApiResult.success(orderScheduleBindBiz.putScheduleArrivalDay(orderDetailScheduleUpdateRequestDto));
	}

	/**
     * 주문 일일상품 유형에 따른 Biz 조회
     * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
     * @return ApiResult
     * @throws Exception
     */
	private ApiResult<?> getOrderScheduleBind(Long odOrderDetlId) throws Exception {
		// 주문 스케줄 리스트 Request 정보 조회(주문번호, 주문상세  상품 PK, 일일상품 유형)
    	OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = orderScheduleListService.getOrderScheduleRequestInfo(odOrderDetlId);

    	// 해당 주문상세정보가 존재하지 않음
    	if (orderDetailScheduleListRequestDto == null) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.VALUE_EMPTY);

    	// 해당 주문의 일일상품 유형코드가 존재하지 않음
    	if (orderDetailScheduleListRequestDto.getGoodsDailyTp() == null || orderDetailScheduleListRequestDto.getGoodsDailyTp().isEmpty()) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.GOODS_DAILY_TP_EMPTY);

    	Map<String, Object> result = new HashMap<String, Object>();

    	// 일일상품별 Biz 선택(녹즙/잇슬림/베이비밀)
    	//MallOrderScheduleFactory mallOrderScheduleFactory = new MallOrderScheduleFactory();
    	MallOrderScheduleBindBiz mallOrderScheduleBindBiz = mallOrderScheduleFactory.getOrderScheduleBind(orderDetailScheduleListRequestDto.getGoodsDailyTp());
    	result.put("orderScheduleBindBiz", mallOrderScheduleBindBiz);
    	result.put("orderDetailScheduleListRequestDto", orderDetailScheduleListRequestDto);
    	return ApiResult.success(result);
	}

	/**
     * 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회
     * @param odOrderDetlId(주문상세 PK)
     * @return ApiResult
     * @throws Exception
     */
	@Override
	public ApiResult<?> getOrderDeliverableScheduleList(Long odOrderDetlId) throws Exception {
		// 주문 스케줄 리스트 Request 정보 조회(주문번호, 주문상세  상품 PK, 일일상품 유형)
		OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = orderScheduleListService
				.getOrderScheduleRequestInfo(odOrderDetlId);

		Long urWarehouseId = orderDetailScheduleListRequestDto.getUrWarehouseId();

		// 배송가능한 배송권역정보 조회
		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(
						Long.parseLong(orderDetailScheduleListRequestDto.getOrgIlGoodsId()),	// 주문상세 상품ID
						orderDetailScheduleListRequestDto.getRecvZipCd(),						// 수령인 우편번호
						orderDetailScheduleListRequestDto.getRecvBldNo());						// 건물번호

		if (shippingPossibilityStoreDeliveryAreaDto == null || shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType().isEmpty()) {
			return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.VALUE_EMPTY);
		}

		OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto = new OrderDetailScheduleUpdateRequestDto();

		// 주문변경 가능 시작일
		// 녹즙		: 배송일 기준 D-3 출고처 마감시간까지
		// 베이비밀 	: 배송일 기준 D-2 출고처 마감시간까지
		// 잇슬림 	: 배송일 기준 D-3 출고처 마감시간까지
		//String deliverableDate = DateUtil.addDays(DateUtil.getCurrentDate(), OrderScheduleEnums.ScheduleChangeStartDate.START_DATE.getAddDate(), "yyyy-MM-dd");

		String deliverableDate = "";

		if ("N".equals(orderDetailScheduleListRequestDto.getCutoffTimeYn())) {
			deliverableDate = DateUtil.addDays(DateUtil.getCurrentDate(),
					OrderScheduleEnums.ScheduleChangePossibleDate.findByCode(orderDetailScheduleListRequestDto.getGoodsDailyTp()).getChangePossibleAddDate(), "yyyy-MM-dd");
		}else{
			deliverableDate = DateUtil.addDays(DateUtil.getCurrentDate(),
					(OrderScheduleEnums.ScheduleChangePossibleDate.findByCode(orderDetailScheduleListRequestDto.getGoodsDailyTp()).getChangePossibleAddDate()+1), "yyyy-MM-dd");
		}

		// 권역정보 체크 후 배송 가능 패턴정보 조회
		List<String> deliveryDayOfWeekList = new ArrayList<String>();
		// 매일 배송 (월~금)
		if (GoodsEnums.StoreDeliveryInterval.EVERY.getCode().equals(shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType())) {
			for (int i=2; i<7; i++) {
				deliveryDayOfWeekList.add(String.valueOf(i));
			}
		// 격일 배송(월,수,금)
		} else if (GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode().equals(shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType())) {
			for (int i=2; i<7; i++) {
				if (i%2 == 0) deliveryDayOfWeekList.add(String.valueOf(i));
			}
		}
		orderDetailScheduleUpdateRequestDto.setChangeDate(deliverableDate);
		//orderDetailScheduleUpdateRequestDto.setChangeDate(orderDetailScheduleListRequestDto.getLastDeliveryDt());
		orderDetailScheduleUpdateRequestDto.setDeliveryDayOfWeekList(deliveryDayOfWeekList);
		orderDetailScheduleUpdateRequestDto.setUrWarehouseId(urWarehouseId);
		
		// 마지막배송일부터 최대 도착일 변경할 수 있는 날짜 수
		int changeScheduleMaxDate = GoodsEnums.GoodsCycleTermType.findByCode(orderDetailScheduleListRequestDto.getGoodsCycleTermTp()).getChangeScheduleMaxDate();
		//orderDetailScheduleUpdateRequestDto.setChangeScheduleMaxDate(changeScheduleMaxDate);

		List<OrderDetailScheduleDayOfWeekListDto> orderDeliverableScheduleList = orderScheduleListService.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto);
		List<String> scheduleDelvDateList = new ArrayList<>();
		List<String> scheduleDelvDayOfWeekList = new ArrayList<>();

		// 일일배송 마지막 배송일
		String lastDeliveryDate = orderDetailScheduleListRequestDto.getLastDeliveryDt();

		// 1. 마지막 배송일 전 날짜는 모두 노출
		List<String> deliveryDateList = orderDeliverableScheduleList.stream().filter(f-> lastDeliveryDate.compareTo(f.getDelvDate()) >= 0).map(OrderDetailScheduleDayOfWeekListDto::getDelvDate).collect(Collectors.toList());
		List<String> deliveryDateDayOfWeekList = orderDeliverableScheduleList.stream().filter(f-> lastDeliveryDate.compareTo(f.getDelvDate()) >= 0).map(OrderDetailScheduleDayOfWeekListDto::getDelvDateWeekDay).collect(Collectors.toList());

		// 일일배송 출고 예정일 리스트로 가져와서 출고처 휴무일 제거
		if(CollectionUtils.isNotEmpty(deliveryDateList)){
			deliveryDateList = goodsGoodsBiz.getDaliyForwardingScheduledDateDtoList(urWarehouseId, deliveryDateList);

			scheduleDelvDateList.addAll(deliveryDateList);
			scheduleDelvDayOfWeekList.addAll(deliveryDateDayOfWeekList);
		}
		// 2. 마지막 배송일 이후는 최대 도착일 변경할 수 있는 날짜 수만큼 노출
		List<String> changePossibleDeliveryDateList = orderDeliverableScheduleList.stream().filter(f-> lastDeliveryDate.compareTo(f.getDelvDate()) < 0).map(OrderDetailScheduleDayOfWeekListDto::getDelvDate).collect(Collectors.toList());
		List<String> changePossibleDeliveryDayOfWeekList = orderDeliverableScheduleList.stream().filter(f-> lastDeliveryDate.compareTo(f.getDelvDate()) < 0).map(OrderDetailScheduleDayOfWeekListDto::getDelvDateWeekDay).collect(Collectors.toList());

		// 일일배송 출고 예정일 리스트로 가져와서 출고처 휴무일 제거
		if(CollectionUtils.isNotEmpty(changePossibleDeliveryDateList)){
			changePossibleDeliveryDateList = goodsGoodsBiz.getDaliyForwardingScheduledDateDtoList(urWarehouseId, changePossibleDeliveryDateList);

			scheduleDelvDateList.addAll(changePossibleDeliveryDateList.subList(0,changeScheduleMaxDate));
			scheduleDelvDayOfWeekList.addAll(changePossibleDeliveryDayOfWeekList.subList(0,changeScheduleMaxDate));
		}

//		List<String> scheduleDelvDateList = orderDeliverableScheduleList.stream().map(OrderDetailScheduleDayOfWeekListDto::getDelvDate).collect(Collectors.toList()).subList(0,changeScheduleMaxDate);
//		List<String> scheduleDelvDayOfWeekList = orderDeliverableScheduleList.stream().map(OrderDetailScheduleDayOfWeekListDto::getDelvDateWeekDay).collect(Collectors.toList()).subList(0,changeScheduleMaxDate);

		return ApiResult.success(OrderDetailScheduleDeliverableListResponseDto.builder()
				.scheduleDelvDateList(scheduleDelvDateList)
				.scheduleDelvDayOfWeekList(scheduleDelvDayOfWeekList)
				.build());
	}

	@Override
	public List<OrderDetailScheduleListDto> getOrderDetailScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto){
		return orderScheduleListService.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
	}

	@Override
	public int putOrderDetlScheduleOdShippingZoneId(Long odShippingZoneId, Long odOrderDetlDailyId, String changeDate){
		return orderScheduleListService.putOrderDetlScheduleOdShippingZoneId(odShippingZoneId,odOrderDetlDailyId,changeDate);
	}
}