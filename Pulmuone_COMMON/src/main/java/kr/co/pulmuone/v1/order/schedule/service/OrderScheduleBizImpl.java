package kr.co.pulmuone.v1.order.schedule.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDailySchVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDailyVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.service.MallOrderDailyDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.schedule.dto.*;
import kr.co.pulmuone.v1.order.schedule.dto.vo.OrderDetlScheduleVo;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.schedule.factory.OrderScheduleFactory;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문 녹즙/잇슬림/베이비밀 스케줄 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                	:  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 1. 20.	       석세동            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class OrderScheduleBizImpl implements OrderScheduleBiz {

	@Autowired
    private OrderScheduleService orderScheduleListService;

	@Autowired
    private StoreDeliveryBiz storeDeliveryBiz;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private OrderScheduleFactory orderScheduleFactory;

	@Autowired
	private OrderDetailBiz orderDetailBiz;

	@Autowired
	private OrderScheduleBiz orderScheduleBiz;

	@Autowired
	private OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	private MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

	@Autowired
	private MallOrderScheduleBiz mallOrderScheduleBiz;

    /**
     * 주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회
     * @param odOrderDetlId(주문상세 PK)
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@Override
    public ApiResult<?> getOrderScheduleList(Long odOrderDetlId) throws Exception {
    	OrderScheduleBindBiz orderScheduleBindBiz = null;
		if (Integer.parseInt(getOrderScheduleBind(odOrderDetlId).getCode()) < 0) return getOrderScheduleBind(odOrderDetlId);
		orderScheduleBindBiz = (OrderScheduleBindBiz) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderScheduleBindBiz");

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
		OrderScheduleBindBiz orderScheduleBindBiz = null;
		Long odOrderDetlId = orderDetailScheduleUpdateRequestDto.getOdOrderDetlId();
		if (Integer.parseInt(getOrderScheduleBind(odOrderDetlId).getCode()) < 0) return getOrderScheduleBind(odOrderDetlId);
		orderScheduleBindBiz = (OrderScheduleBindBiz) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderScheduleBindBiz");

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
		OrderScheduleBindBiz orderScheduleBindBiz = null;
		Long odOrderDetlId = orderDetailScheduleUpdateRequestDto.getOdOrderDetlId();
		if (Integer.parseInt(getOrderScheduleBind(odOrderDetlId).getCode()) < 0) return getOrderScheduleBind(odOrderDetlId);

		// 일일 상품유형 체크 (현재는 녹즙만 가능)
		OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = (OrderDetailScheduleListRequestDto) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderDetailScheduleListRequestDto");
		if (!OrderScheduleEnums.ScheduleCd.GREENJUICE.getCode().equals(orderDetailScheduleListRequestDto.getGoodsDailyTp())) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.REQUIRED_GOODS_DAILY_TP);

		orderScheduleBindBiz = (OrderScheduleBindBiz) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderScheduleBindBiz");
    	// 각 일일상품별 스케줄 배송요일 변경
		return ApiResult.success(orderScheduleBindBiz.putScheduleArrivalDay(orderDetailScheduleUpdateRequestDto));
	}

	/**
     * 주문 녹즙/잇슬림/베이비밀 스케줄 건너뛰기
     * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> putScheduleSkip(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		OrderScheduleBindBiz orderScheduleBindBiz = null;
		Long odOrderDetlId = orderDetailScheduleUpdateRequestDto.getOdOrderDetlId();
		if (Integer.parseInt(getOrderScheduleBind(odOrderDetlId).getCode()) < 0) return getOrderScheduleBind(odOrderDetlId);
		orderScheduleBindBiz = (OrderScheduleBindBiz) ((Map<String, Object>) getOrderScheduleBind(odOrderDetlId).getData()).get("orderScheduleBindBiz");

    	// 각 일일상품별 스케줄 건너뛰기
		return ApiResult.success(orderScheduleBindBiz.putScheduleSkip(orderDetailScheduleUpdateRequestDto));
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
//    	OrderScheduleFactory orderScheduleFactory = new OrderScheduleFactory();
    	OrderScheduleBindBiz orderScheduleBindBiz = orderScheduleFactory.getOrderScheduleBind(orderDetailScheduleListRequestDto.getGoodsDailyTp());
    	result.put("orderScheduleBindBiz", orderScheduleBindBiz);
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

		// 같은 주문건내 같은 일일상품유형의 상품PK 조회
		List<Long> orderGoodsIdList = mallOrderDailyDetailBiz.getOrderGoodsIdListByOdOrderDetlId(odOrderDetlId);

		for(Long orderIlGoodsId : orderGoodsIdList){
			// 배송가능한 배송권역정보 조회
			ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(
					orderIlGoodsId,															// 주문상세 상품ID
					orderDetailScheduleListRequestDto.getRecvZipCd(),						// 수령인 우편번호
					orderDetailScheduleListRequestDto.getRecvBldNo());						// 건물번호

			if (shippingPossibilityStoreDeliveryAreaDto == null || shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType().isEmpty()) {
				return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.VALUE_EMPTY);
			}
		}

		// 녹즙 - 배송지 변경 가능한 가장 빠른 도착예정일 리스트 세팅
		List<String> scheduleDelvDateList = new ArrayList<>();
		List<String> scheduleDelvDayOfWeekList = new ArrayList<>();

		// 1. 일일배송 스케쥴 정보 조회
		OrderDetailScheduleListRequestDto orderDetailScheduleListReqDto = new OrderDetailScheduleListRequestDto();
		List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList = new ArrayList<>();

		// 1.1 - 주문상세PK로 일일상품 정보 조회
		MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(odOrderDetlId);

		if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){
			// 1.2 - 녹즙-내맘대로 인 경우 -> OD_ORDER_ID 세팅
			orderDetailScheduleListReqDto.setOdOrderId(String.valueOf(orderDetailGoodsDto.getOdOrderId()));
			orderDetailScheduleListReqDto.setPromotionYn("Y");
			orderDetailScheduleArrivalDateList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
		}else{
			// 1.3 - 녹즙인 경우 - > 같은 주문건에서 녹즙주문건 모두 조회
			List<MallOrderDetailGoodsDto> orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getGoodsDailyTp());

			for(MallOrderDetailGoodsDto dto : orderDetailGoodsList){
				// OD_ORDER_DETL_ID 세팅
				orderDetailScheduleListRequestDto.setOdOrderDetlId(dto.getOdOrderDetlId());
				List<OrderDetailScheduleListDto>  orderDetailList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
				orderDetailScheduleArrivalDateList.addAll(orderDetailList);
			}
		}


		// 2. 스케쥴정보에서 변경 가능한 도착예정일 리스트만 추출해서 세팅
		// 2.1 - 도착일 중복제거
		orderDetailScheduleArrivalDateList = orderDetailScheduleArrivalDateList.stream().filter(distinctByKey(f -> Arrays.asList(f.getDelvDate()))).collect(Collectors.toList());

		// 2-2 - 도착예정일 리스트 세팅
		if(CollectionUtils.isNotEmpty(orderDetailScheduleArrivalDateList)){

			//리스트 정렬
			List<OrderDetailScheduleListDto>  sortedOrderDetailScheduleArrivalDateList = orderDetailScheduleArrivalDateList.stream()
					.sorted(Comparator.comparing(OrderDetailScheduleListDto::getDelvDate))
					.collect(toList());

			for(OrderDetailScheduleListDto schDto : sortedOrderDetailScheduleArrivalDateList){
				String originDelvDate = schDto.getDelvDate();
				String delvDate = schDto.getDelvDate().replaceAll("-", ""); // 도착예정일
				String delvDateWeekDay = schDto.getDelvDateWeekDay(); // 도착예정일의 요일
				int addDate = OrderScheduleEnums.ScheduleChangeDate.findByCode(delvDateWeekDay).getAddDate();

				String possibleChangeDate = DateUtil.addDays(delvDate, addDate, "yyyyMMdd");// 해당 도착예정일의 스케쥴 변경 가능일자
				String currentDate = DateUtil.getCurrentDate();
				if (Integer.parseInt(currentDate) <= Integer.parseInt(possibleChangeDate)){
					scheduleDelvDateList.add(originDelvDate);
					scheduleDelvDayOfWeekList.add(delvDateWeekDay);
				}

			}
		}

		return ApiResult.success(OrderDetailScheduleDeliverableListResponseDto.builder()
				.scheduleDelvDateList(scheduleDelvDateList)
				.scheduleDelvDayOfWeekList(scheduleDelvDayOfWeekList)
				.build());
	}

	/**
     * 주문 녹즙/잇슬림/베이비밀 배송가능한 배송권영정보로 배송가능 스케쥴 조회
     * @param ShippingPossibilityStoreDeliveryAreaDto
     * @return HashMap<String,List<String>>
     * @throws Exception
     */
	@Override
	public HashMap<String,List<String>> getScheduleDelvDateList(ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaDto, Long urWarehouseId) throws Exception{
		HashMap<String,List<String>> result = new HashMap<>();

		OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto = new OrderDetailScheduleUpdateRequestDto();

		// 주문변경 가능 시작일
		String deliverableDate = DateUtil.addDays(DateUtil.getCurrentDate(), OrderScheduleEnums.ScheduleChangeStartDate.START_DATE.getAddDate(), "yyyy-MM-dd");

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
		orderDetailScheduleUpdateRequestDto.setDeliveryDayOfWeekList(deliveryDayOfWeekList);
		orderDetailScheduleUpdateRequestDto.setUrWarehouseId(urWarehouseId);

		List<OrderDetailScheduleDayOfWeekListDto> orderDeliverableScheduleList = orderScheduleListService.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto);
		List<String> scheduleDelvDateList = orderDeliverableScheduleList.stream().map(OrderDetailScheduleDayOfWeekListDto::getDelvDate).collect(Collectors.toList());
		List<String> scheduleDelvDayOfWeekList = orderDeliverableScheduleList.stream().map(OrderDetailScheduleDayOfWeekListDto::getDelvDateWeekDay).collect(Collectors.toList());

		result.put("scheduleDelvDateList",scheduleDelvDateList);
		result.put("scheduleDelvDayOfWeekList",scheduleDelvDayOfWeekList);

		return result;
	}

	/**
	 * @Desc 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 > 주문I/F일자, 출고예정일자, 도착예정일자 업데이트
	 * @param orderDetlVo
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderArrivalScheduledDate(long odOrderDetlId, long ilGoodsId, LocalDate deliveryDt) throws Exception {

		//주문I/F일자, 출고예정일자 조회
		ArrivalScheduledDateDto returnList = goodsGoodsBiz.getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(ilGoodsId, deliveryDt);

		//주문I/F일자, 출고예정일자 도착예정일자 업데이트
		OrderDetlVo orderDetlVo = OrderDetlVo.builder()
					.odOrderDetlId(odOrderDetlId)
					.orderIfDt(returnList.getOrderDate())
					.shippingDt(returnList.getForwardingScheduledDate())
					.deliveryDt(returnList.getArrivalScheduledDate())
					.build();

		return ApiResult.success(orderOrderBiz.putOrderDetailArrivalScheduledDate(orderDetlVo));
	}
	/**
	 * 일일 배송예정일자 조회
	 * @param orderDetailScheduleUpdateRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderDetailScheduleDayOfWeekList(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		return ApiResult.success(orderScheduleListService.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto));
	}


	@Override
	public void changeOrderDetlDaily(OrderDetlVo orderDetlVo, Long odOrderDetlId) throws Exception{
		// 1. 주문상세 PK로 일일배송 패턴 조회
		OrderDetlDailyVo orderDetlDailyVo = orderDetailBiz.getOrderDetlDailySchByOdOrderDetlId(odOrderDetlId);

		if ("N".equals(orderDetlDailyVo.getDailyPackYn()) && !"Y".equals(orderDetlDailyVo.getDailyBulkYn())) {

			// 2. 기존 스케쥴 삭제
			OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();

			orderDetlScheduleVo.setOdOrderDetlId(odOrderDetlId);
			orderDetlScheduleVo.setOdOrderDetlDailyId(orderDetlDailyVo.getOdOrderDetlDailyId());
			orderScheduleListService.delOdOrderDetlDailySch(orderDetlScheduleVo);


			// 3. 새로운 스케쥴 생성
			// 배송 주기
			GoodsEnums.GoodsCycleTermType goodsCycleTermTp = GoodsEnums.GoodsCycleTermType.findByCode(orderDetlDailyVo.getGoodsCycleTermTp());
			int goodsCycleTermTpWeekNum = StringUtil.nvlInt(goodsCycleTermTp.getTypeQty());

			// 배송 요일
			//GoodsEnums.GoodsCycleType goodsCycleTypeEnums = GoodsEnums.GoodsCycleType.findByCode(orderDetlDailyVo.getGoodsCycleTp());
			int goodsCycleTypeDayNum = ((orderDetlDailyVo.getMonCnt() > 0)? 1:0)
					+ ((orderDetlDailyVo.getTueCnt()  > 0)? 1:0)
					+ ((orderDetlDailyVo.getWedCnt()  > 0)? 1:0)
					+ ((orderDetlDailyVo.getThuCnt()  > 0)? 1:0)
					+ ((orderDetlDailyVo.getFriCnt() > 0)? 1:0)
					; //StringUtil.nvlInt(goodsCycleTypeEnums.getTypeQty()) + 1;

			// 배송 횟수
			int deliveryCnt = goodsCycleTermTpWeekNum * goodsCycleTypeDayNum;//goodsCycleTermTpWeekNum  * goodsCycleTypeDayNum;
			String deliverableDate = orderDetlVo.getDeliveryDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			List<String> deliveryDayOfWeekList = new ArrayList<String>();

			int dailyOrderCnt = 0;
			if (orderDetlDailyVo.getMonCnt() > 0) {
				deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.MON.getDayNum()));
				dailyOrderCnt += orderDetlDailyVo.getMonCnt();
			}
			if (orderDetlDailyVo.getTueCnt() > 0) {
				deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.TUE.getDayNum()));
				dailyOrderCnt += orderDetlDailyVo.getTueCnt();
			}
			if (orderDetlDailyVo.getWedCnt() > 0) {
				deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.WED.getDayNum()));
				dailyOrderCnt += orderDetlDailyVo.getWedCnt();
			}
			if (orderDetlDailyVo.getThuCnt() > 0) {
				deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.THU.getDayNum()));
				dailyOrderCnt += orderDetlDailyVo.getThuCnt();
			}
			if (orderDetlDailyVo.getFriCnt() > 0) {
				deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.FRI.getDayNum()));
				dailyOrderCnt += orderDetlDailyVo.getFriCnt();
			}

			// 일일 상품 스케쥴
			// TODO : 녹즙일경우에만 데이터 넣지만 임시로 다 넣음
			//if (orderDetlVo.getGoodsDailyTp().equals(GoodsEnums.GoodsDailyType.GREENJUICE.getCode())) {
			OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto = new OrderDetailScheduleUpdateRequestDto();
			orderDetailScheduleUpdateRequestDto.setUrWarehouseId(orderDetlVo.getUrWarehouseId());
			orderDetailScheduleUpdateRequestDto.setChangeDate(deliverableDate);
			orderDetailScheduleUpdateRequestDto.setDeliveryDayOfWeekList(deliveryDayOfWeekList);

			ApiResult<?> apiResult = orderScheduleBiz.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto);
			List<OrderDetailScheduleDayOfWeekListDto> orderDeliverableScheduleList = (List<OrderDetailScheduleDayOfWeekListDto>) apiResult.getData();

			int dayCnt = 0;
			for (OrderDetailScheduleDayOfWeekListDto dailySchItem : orderDeliverableScheduleList) {

				LocalDate thisDay   = LocalDate.parse(dailySchItem.getDelvDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				int thisWeekDayNum  = thisDay.getDayOfWeek().getValue();

				if (GoodsEnums.WeekCodeByGreenJuice.MON.getLocalDateDayNum() == thisWeekDayNum){
					dailyOrderCnt = orderDetlDailyVo.getMonCnt();
				} else if (GoodsEnums.WeekCodeByGreenJuice.TUE.getLocalDateDayNum() == thisWeekDayNum) {
					dailyOrderCnt = orderDetlDailyVo.getTueCnt();
				} else if (GoodsEnums.WeekCodeByGreenJuice.WED.getLocalDateDayNum() == thisWeekDayNum) {
					dailyOrderCnt = orderDetlDailyVo.getWedCnt();
				} else if (GoodsEnums.WeekCodeByGreenJuice.THU.getLocalDateDayNum() == thisWeekDayNum) {
					dailyOrderCnt = orderDetlDailyVo.getThuCnt();
				} else if (GoodsEnums.WeekCodeByGreenJuice.FRI.getLocalDateDayNum() == thisWeekDayNum) {
					dailyOrderCnt = orderDetlDailyVo.getFriCnt();
				} else {
					dailyOrderCnt = 0;
				}

				orderRegistrationBiz.addOrderDetlDailySch(OrderDetlDailySchVo.builder()
						.odOrderDetlDailySchSeq(++dayCnt)
						.odOrderDetlDailyId(orderDetlDailyVo.getOdOrderDetlDailyId())
						.deliveryDt(thisDay)
						.psShippingCompId(0)
						.trackingNo("")
						.orderCnt(dailyOrderCnt)
						.deliveryYn("N")
						.orderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode())
						.odShippingZoneId(orderDetlVo.getOdShippingZoneId())
						.useYn("Y")
						.build());
				if (deliveryCnt <= dayCnt) {
					break;
				}
			}
		}
		//}
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * @Desc 주문상세 일일배송 배송지 정보 수정
	 * @param odOrderDetlId
	 * @param odOrderId
	 * @return
	 */
	@Override
	public int putOrderDetlDailyZone(Long odOrderDetlId,Long odOrderId){
		return orderScheduleListService.putOrderDetlDailyZone(odOrderDetlId, odOrderId);
	}

	/**
	 * @Desc 주문상세 일일배송 배송지 정보 등록
	 * @param odOrderDetlId
	 * @param promotionTp
	 * @param odOrderId
	 * @return
	 */
	@Override
	public int addOrderDetlDailyZoneByOdOrderDetlId(Long odOrderDetlId, String promotionTp, Long odOrderId){
		return orderScheduleListService.addOrderDetlDailyZoneByOdOrderDetlId(odOrderDetlId, promotionTp, odOrderId);
	}

	/**
	 * @Desc 주문상세 일일배송 배송지 정보 업데이트
	 * @param odOrderDetlId
	 * @param promotionTp
	 * @param odOrderId
	 * @return
	 */
	public void saveOrderDetlDailyZone(Long odOrderDetlId, String promotionTp, Long odOrderId){
		// 기존 배송지 정보 N으로 업데이트
		orderScheduleListService.putOrderDetlDailyZone(odOrderDetlId, odOrderId);
		// 새로운 배송지 정보 등록
		orderScheduleListService.addOrderDetlDailyZoneByOdOrderDetlId(odOrderDetlId, promotionTp, odOrderId);
	}

	/**
	 * @Desc 주문상세 일일배송 스토어정보 수정
	 * @param odOrderId
	 * @param odOrderDetlId
	 * @param odShippingZoneId
	 * @return
	 */
	@Override
	public void putOrderDetlDailyUrStoreId(long odOrderId, long odOrderDetlId, long odShippingZoneId){
		// OD_ORDER_DETL_DAILY 테이블 스토어 PK 수정 처리
		orderScheduleListService.putOrderDetlDailyUrStoreId(odOrderId, odOrderDetlId, odShippingZoneId);
	}

	/**
	 * 주문 스케줄 seq
	 * @param odOrderDetlId
	 * @return int
	 */
	@Override
	public int getOrderDetailDailySchSeq(Long odOrderDetlId) {
		return orderScheduleListService.getOrderDetailDailySchSeq(odOrderDetlId);
	}

	/**
	 * @Desc ( ERP 주문|취소 조회 API ) 주문정보 ERP API 에서 주문 코드로 ERP 녹즙 정보 조회
	 * @param odid
	 * @parma hdrType
	 * @return List<ErpIfCustordSearchResponseDto> : ERP 연동 API 를 통해 조회된 녹즙 주문 목록
	 */
	@Override
	public BaseApiResponseVo getErpCustordApiList(String odid, String hdrType){
		return orderScheduleListService.getErpCustordApiList(odid,hdrType);
	}

	/**
	 * @Desc 일일배송 배송지 수정시 ERP 스케쥴 수정
	 * @param odOrderDetlId
	 * @param deliveryDt
	 * @return
	 */
	@Override
	//public ApiResult<?> putIfDlvFlagByErp(Long odOrderDetlId, int orderDetailDailySchSeq){
	public ApiResult<?> putIfDlvFlagByErp(Long odOrderDetlId, String deliveryDt){
//		List<OrderDetailGreenJuiceListDto> orderDetailGreenJuiceList = orderScheduleListService
//				.getErpOrderDetailScheduleList(odOrderDetlId, orderDetailDailySchSeq, null);
		List<OrderDetailGreenJuiceListDto> orderDetailGreenJuiceList = orderScheduleListService.getErpOrderDetailScheduleListByDeliveryDt(odOrderDetlId, deliveryDt);

		if(CollectionUtils.isEmpty(orderDetailGreenJuiceList)){
			return ApiResult.success();
		}

		List<ErpIfCustOrdInpLineRequestDto> erpCustordLineApiList = new ArrayList<>();
		List<ErpIfCustOrdInpLineRequestDto> erpCustCancelLineApiList = new ArrayList<>();
		List<OrderDetailGreenJuiceListDto> erpGreenJuiceList = new ArrayList<>();
		BaseApiResponseVo baseApiResponseVo = null;

		// 1. 스캐줄 취소 내역 입력
		erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode().equals(x.getOrderSchStatus())).collect(toList());

		Map<Long, List<OrderDetailGreenJuiceListDto>> cancelShippingZoneMap = erpGreenJuiceList.stream()
				.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

		List<ErpIfCustOrdInpHeaderRequestDto> cancelHeaderList = new ArrayList<>();

		if(!erpGreenJuiceList.isEmpty()) {
			//cancelShippingZoneMap.entrySet().forEach(entry -> {
//		for(long key : cancelShippingZoneMap.keySet()) {
			//Long key = entry.getKey();

//			OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleListService.getOrderDetailScheduleShippingInfo(Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
			OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleListService.getOrderDetailScheduleShippingInfo(Long.parseLong(erpGreenJuiceList.get(0).getOdOrderDetlId()), erpGreenJuiceList.get(0).getOdShippingZoneId());
//			for (int j = 0; j < entry.getValue().size(); j++) {
			for (int j = 0; j < erpGreenJuiceList.size(); j++) {
//				OrderDetailGreenJuiceListDto item = entry.getValue().get(j);
				OrderDetailGreenJuiceListDto item = erpGreenJuiceList.get(j);
				item.setSeqNo(orderDetailScheduleShippingInfoDto.getSeqNo());
				erpCustCancelLineApiList.add(orderScheduleListService.getScheduleDailyDeliveryOrderLine(item));
			}
			ErpIfCustOrdInpHeaderRequestDto erpIfCustCancelHeaderRequestDto = orderScheduleListService.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto, erpCustCancelLineApiList);
			cancelHeaderList.add(erpIfCustCancelHeaderRequestDto);
//			erpCustCancelLineApiList = new ArrayList<>();
//		}
			//});

			ErpIfCustOrdInpRequestDto erpIfCustCancelRequestDto = ErpIfCustOrdInpRequestDto.builder()
					.totalPage(1)
					.currentPage(1)
					.header(cancelHeaderList)
					.build();

			baseApiResponseVo = orderScheduleListService.addIfDlvFlagByErp(erpIfCustCancelRequestDto);

			if (!baseApiResponseVo.isSuccess()) {
				return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
			}

			// 스케쥴 취소 성공 후 API_CANCEL_SEND_YN 상태 'Y' 업데이트 처리
			orderScheduleListService.putOrderDetlScheduleApiCancelSendYn(erpGreenJuiceList);
		}

		// 2. 스캐줄 주문 내역 입력
		erpGreenJuiceList = orderDetailGreenJuiceList.stream().filter(x -> OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode().equals(x.getOrderSchStatus()) &&
																			OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_Y.getCode().equals(x.getUseYn())).collect(toList());

		Map<Long, List<OrderDetailGreenJuiceListDto>> ShippingZoneMap = erpGreenJuiceList.stream()
				.collect(groupingBy(OrderDetailGreenJuiceListDto::getOdShippingZoneId, LinkedHashMap::new, toList()));

		List<ErpIfCustOrdInpHeaderRequestDto> orderHeaderList = new ArrayList<>();

//		ShippingZoneMap.entrySet().forEach(entry -> {
		for(long key : ShippingZoneMap.keySet()) {
//			Long key = entry.getKey();
//			OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleListService.getOrderDetailScheduleShippingInfo(Long.parseLong(entry.getValue().get(0).getOdOrderDetlId()), key);
			OrderDetailScheduleShippingInfoDto orderDetailScheduleShippingInfoDto = orderScheduleListService.getOrderDetailScheduleShippingInfo(Long.parseLong(ShippingZoneMap.get(key).get(0).getOdOrderDetlId()), key);
//			for (int j = 0; j < entry.getValue().size(); j++) {
			for (int j = 0; j < ShippingZoneMap.get(key).size(); j++) {
//				OrderDetailGreenJuiceListDto item = entry.getValue().get(j);
				OrderDetailGreenJuiceListDto item = ShippingZoneMap.get(key).get(j);
				item.setSeqNo(orderDetailScheduleShippingInfoDto.getSeqNo());
				erpCustordLineApiList.add(orderScheduleListService.getScheduleDailyDeliveryOrderLine(item));
			}
			ErpIfCustOrdInpHeaderRequestDto erpIfCustOrdInpHeaderRequestDto = orderScheduleListService.getScheduleDailyDeliveryOrderHeader(orderDetailScheduleShippingInfoDto, erpCustordLineApiList);
			orderHeaderList.add(erpIfCustOrdInpHeaderRequestDto);
			erpCustordLineApiList = new ArrayList<>();
		}
//		});

		ErpIfCustOrdInpRequestDto erpIfCustOrdInpRequestDto =  ErpIfCustOrdInpRequestDto.builder()
				.totalPage(1)
				.currentPage(1)
				.header(orderHeaderList)
				.build();

		baseApiResponseVo = orderScheduleListService.addIfDlvFlagByErp(erpIfCustOrdInpRequestDto);

		if (!baseApiResponseVo.isSuccess()) {
			return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
		}


		return ApiResult.success();
	}

	/**
	 * 주문 스케줄 배치 여부
	 * @param odOrderDetlId
	 * @return String
	 */
	@Override
	public String getOrderDetailBatchInfo(Long odOrderDetlId) {
		return orderScheduleListService.getOrderDetailBatchInfo(odOrderDetlId);
	}

	/**
	 * 일일상품 주문배송지 변경시 스케쥴 배치 Y인 경우 -> 스케쥴 취소후 다시 생성
	 * @param
	 * @return
	 */
	@Override
	public void addChangeOrderDetailSchedule(List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList, Long odShippingZoneId){

		List<OrderDetlScheduleVo> updateScheduleList = new ArrayList<>();

		// 1. 기존 스케쥴 취소
		for(OrderDetailScheduleListDto cancelOrderDetailScheduleListDto : orderDetailScheduleArrivalDateList) {
			OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();
			orderDetlScheduleVo.setOdOrderDetlDailyId(cancelOrderDetailScheduleListDto.getOdOrderDetlDailyId());
			orderDetlScheduleVo.setOdOrderDetlDailySchSeq(cancelOrderDetailScheduleListDto.getOdOrderDetlDailySchSeq());
			orderDetlScheduleVo.setOdOrderDetlId(cancelOrderDetailScheduleListDto.getOdOrderDetlId());
			orderDetlScheduleVo.setDeliveryDt(cancelOrderDetailScheduleListDto.getDelvDate());
			orderDetlScheduleVo.setOrderCnt(Integer.parseInt(cancelOrderDetailScheduleListDto.getOrderCnt()));
			orderDetlScheduleVo.setOrderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());
			orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_N.getCode());
			orderDetlScheduleVo.setOdShippingZoneId(cancelOrderDetailScheduleListDto.getOdShippingZoneId());

			updateScheduleList.add(orderDetlScheduleVo);

			//orderScheduleListService.addChangeOrderDetailSchedule(orderDetlScheduleVo);
		}
		int result = 0;

		if (!updateScheduleList.isEmpty())
			result = orderScheduleListService.putOrderDetlSchedule(updateScheduleList);

		// 2. 새로 스케쥴 생성
		for(OrderDetailScheduleListDto addOrderDetailScheduleListDto : orderDetailScheduleArrivalDateList){
			OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();
			orderDetlScheduleVo.setOdOrderDetlDailyId(addOrderDetailScheduleListDto.getOdOrderDetlDailyId());
			orderDetlScheduleVo.setDeliveryDt(addOrderDetailScheduleListDto.getDelvDate());
			orderDetlScheduleVo.setOrderCnt(Integer.parseInt(addOrderDetailScheduleListDto.getOrderCnt()));
			orderDetlScheduleVo.setOrderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode());
			orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_Y.getCode());
			orderDetlScheduleVo.setOdOrderDetlId(addOrderDetailScheduleListDto.getOdOrderDetlId());
			orderDetlScheduleVo.setOdShippingZoneId(odShippingZoneId);

			orderScheduleListService.addChangeOrderDetailSchedule(orderDetlScheduleVo);
		}
	}

	/**
	 * 주문정보 ERP API 에서 변경된 모든 ERP 녹즙 정보 조회
	 * @param hdrType
	 * @return
	 */
	public BaseApiResponseVo getErpCustordApiAllList(String hdrType) {
		return orderScheduleListService.getErpCustordApiAllList(hdrType);
	}

	/**
	 * 주문정보 ERP API 에서 변경된 모든 ERP 녹즙 정보 조회
	 * @param erpIfCustordRequestDto
	 * @return
	 */
	public BaseApiResponseVo putErpCustordApiComplete(List<ErpIfCustordRequestDto> erpIfCustordRequestDto) {
		return orderScheduleListService.putIfDlvFlagByErp(erpIfCustordRequestDto);
	}

	/**
	 * 녹즙 동기화 주문 등록 처리
	 * @param insertScheduleList
	 * @return
	 */
	public int addOrderDetlSchedule(List<OrderDetlScheduleVo> insertScheduleList) {
		return orderScheduleListService.addOrderDetlSchedule(insertScheduleList);
	}

	/**
	 * 녹즙 동기화 주문 수정 처리
	 * @param updateScheduleList
	 * @return
	 */
	public int putErpOrderDetlSchedule(List<OrderDetlScheduleVo> updateScheduleList) {
		return orderScheduleListService.putErpOrderDetlSchedule(updateScheduleList);
	}
}