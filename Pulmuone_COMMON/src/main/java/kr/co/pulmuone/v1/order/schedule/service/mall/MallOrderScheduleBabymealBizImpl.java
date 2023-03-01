package kr.co.pulmuone.v1.order.schedule.service.mall;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderInfoDto;
import kr.co.pulmuone.v1.api.babymeal.service.BabymealBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.DeliveryEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDelvDateDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleGoodsDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문관련 베이비밀 I/F Impl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                	:  	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 1. 25.		석세동	최초작성
 *  1.1    2021. 1. 28.   	이규한	베이비밀 스케줄 리스트 조회 I/F 추가
 *  1.2	   2021. 2. 10.		이규한	베이비밀 스케줄 도착일 변경 I/F, 베이비밀 스케줄 건너뛰기 I/F 추가
 *  1.3    2021. 2. 15.		이명수    요일 Enums수
 * =======================================================================
 * </PRE>
 */

@Service("mallOrderScheduleBabymealBizImpl")
public class MallOrderScheduleBabymealBizImpl implements MallOrderScheduleBindBiz {

	// 베이비밀 I/F Biz
	@Autowired
	private BabymealBiz babymealBiz;

	// 주문 녹즙/잇슬림/베이비밀 스캐줄  관련 Service
	@Autowired
	private MallOrderScheduleService orderScheduleService;

	// 스토어 배송권역 Biz
	@Autowired
	private StoreDeliveryBiz storeDeliveryBiz;

	private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    /**
     * 베이비밀 스케줄 리스트 조회
     * @param orderDetailScheduleListRequestDto
     * @return List<OrderDetailScheduleListDto>
     */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception {

		ApiResult<?> apiResult = babymealBiz.getOrderScheduleList(orderDetailScheduleListRequestDto);
		if (Integer.parseInt(apiResult.getCode()) < 0) return apiResult;

		BabymealOrderInfoDto babymealOrderInfoDto = (BabymealOrderInfoDto)((Map<String, Object>)apiResult.getData()).get("orderInfo");
		List<BabymealOrderDeliveryListDto> babymealOrderDeliveryListDtoList = (List<BabymealOrderDeliveryListDto>)((Map<String, Object>)apiResult.getData()).get("orderScheduleList");

		OrderDetailScheduleGoodsDto orderDetailScheduleGoodsDto = orderScheduleService.getOrderScheduleGoodsBaseInfo(orderDetailScheduleListRequestDto.getOdOrderDetlId());
 		String goodsDailyCycleTermDays = (orderDetailScheduleGoodsDto.getMonCnt() > 0 ? DeliveryEnums.WeekType.MON.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getTueCnt() > 0 ? DeliveryEnums.WeekType.TUE.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getWedCnt() > 0 ? DeliveryEnums.WeekType.WED.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getThuCnt() > 0 ? DeliveryEnums.WeekType.THU.getCodeName() + "/" : "") +
 				(orderDetailScheduleGoodsDto.getFriCnt() > 0 ? DeliveryEnums.WeekType.FRI.getCodeName(): "");
 		goodsDailyCycleTermDays = goodsDailyCycleTermDays.endsWith("/") ? goodsDailyCycleTermDays.substring(0, goodsDailyCycleTermDays.length()-1) : goodsDailyCycleTermDays;

		/*
		 * orderDetailScheduleGoodsDto.setIlGoodsId(babymealOrderInfoDto.getIlGoodsId())
		 * ; // I/F 상품 코드
		 */
 		orderDetailScheduleGoodsDto.setGoodsNm(orderDetailScheduleGoodsDto.getGoodsNm() +
 				("Y".equals(orderDetailScheduleGoodsDto.getAllergyYn()) ? ApiEnums.BabymealGetOrderInfoDateType.SEARCH_BM_ORDER_DELIVERY_LIST.getAllergyTypeNm() : ""));	// 상품명
 		orderDetailScheduleGoodsDto.setOrderNum(babymealOrderInfoDto.getOrderNum());						// I/F 주문번호
 		orderDetailScheduleGoodsDto.setFirstDeliveryDate(babymealOrderDeliveryListDtoList.get(0).getDelvDate());						// 배송시작일자
 		orderDetailScheduleGoodsDto.setLastDeliveryDate(babymealOrderDeliveryListDtoList.get(babymealOrderDeliveryListDtoList.size()-1).getDelvDate());						// 최종배송예정일자
 		orderDetailScheduleGoodsDto.setGoodsDailyCycleTermDays(goodsDailyCycleTermDays);					// 배송요일
 		orderDetailScheduleGoodsDto.setSaleSeq(babymealOrderInfoDto.getSaleSeq());							// 배송회차(남은회차)
 		orderDetailScheduleGoodsDto.setOdOrderDetlId(orderDetailScheduleListRequestDto.getOdOrderDetlId());	// 주문상세번호

 		// 배송가능한 배송권역정보 조회
 		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(
						Long.parseLong(orderDetailScheduleGoodsDto.getIlGoodsId()),	// 주문상세 상품ID
						orderDetailScheduleGoodsDto.getRecvZipCd(),						// 수령인 우편번호
						orderDetailScheduleGoodsDto.getRecvBldNo());						// 건물번호

		orderDetailScheduleGoodsDto.setScheduleDeliveryInterval(shippingPossibilityStoreDeliveryAreaDto.getStoreDeliveryIntervalType());

 		// 스케줄 목록 배송예정일 기준으로 그룹
		Map<String, List<BabymealOrderDeliveryListDto>> delvDateMap = babymealOrderDeliveryListDtoList.stream()
				.collect(groupingBy(BabymealOrderDeliveryListDto::getDelvDate, LinkedHashMap::new, toList()));

		List<OrderDetailScheduleDelvDateDto> scheduleDelvDateList = new ArrayList<OrderDetailScheduleDelvDateDto>();

		delvDateMap.entrySet().forEach(entry -> {
			String key = entry.getKey();

			OrderDetailScheduleDelvDateDto orderDetailScheduleDelvDateDto = new OrderDetailScheduleDelvDateDto();

			orderDetailScheduleDelvDateDto.setDelvDate(key);
			orderDetailScheduleDelvDateDto.setDelvDateWeekDay(entry.getValue().get(0).getDelvDateWeekDay());

			List<OrderDetailScheduleListDto> rows = new ArrayList<OrderDetailScheduleListDto>();
			entry.getValue().forEach(babymealOrderDeliveryListDto -> {
				// 배송요일 추출 셋팅
				babymealOrderDeliveryListDto.setDelvDateWeekDay(
						babymealOrderDeliveryListDto.getDelvDateWeekDay().indexOf("(") > 0
										? babymealOrderDeliveryListDto.getDelvDateWeekDay().substring(
												babymealOrderDeliveryListDto.getDelvDateWeekDay().indexOf("(") + 1,
												babymealOrderDeliveryListDto.getDelvDateWeekDay().indexOf("(") + 2)
										: "");
				babymealOrderDeliveryListDto.setGoodsImgNm(orderDetailScheduleGoodsDto.getGoodsImgNm());
				rows.add(OBJECT_MAPPER.convertValue(babymealOrderDeliveryListDto, OrderDetailScheduleListDto.class));
			});
			orderDetailScheduleDelvDateDto.setRows(rows);
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

		// 스케줄 리스트 존재여부로 스케줄I/F 여부 셋팅
		orderDetailScheduleListResponseDto.getGoodsInfo().setScheduleIfYn(babymealOrderDeliveryListDtoList.size() > 0 ? "Y" : "N");
		return ApiResult.success(orderDetailScheduleListResponseDto);
	}

	/**
     * 베이비밀 스케줄 배송일자 변경
     * @param orderDetailScheduleListRequestDto
     * @return ApiResult<?>
     */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {

		// 해당 주문상세의 기본 배송 스케줄 리스트 조회
		OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = orderScheduleService.getOrderScheduleRequestInfo(orderDetailScheduleUpdateRequestDto.getOdOrderDetlId());
		ApiResult<?> apiResult = babymealBiz.getOrderScheduleList(orderDetailScheduleListRequestDto);
		if (Integer.parseInt(apiResult.getCode()) < 0) return apiResult;

		// 주문정보, 배송 스케줄 정보
		BabymealOrderInfoDto babymealOrderInfoDto = (BabymealOrderInfoDto)((Map<String, Object>)apiResult.getData()).get("orderInfo");
		List<BabymealOrderDeliveryListDto> babymealOrderDeliveryListDtoList = (List<BabymealOrderDeliveryListDto>)((Map<String, Object>)apiResult.getData()).get("orderScheduleList");
		// 배송 스케줄 정보 존재여부 체크
		if (babymealOrderDeliveryListDtoList.size() < 0) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.VALUE_EMPTY);

		// 변경할 배송일자 Dto 리스트
		List<OrderDetailScheduleUpdateDto> scheduleUpdateList = orderDetailScheduleUpdateRequestDto.getScheduleUpdateList();
		for (OrderDetailScheduleUpdateDto orderDetailScheduleUpdateDto : scheduleUpdateList) {
			BabymealOrderDeliveryListDto paramDto = babymealOrderDeliveryListDtoList.stream()
					.filter(babymealOrderDeliveryListDto -> babymealOrderDeliveryListDto.getId().equals(orderDetailScheduleUpdateDto.getId()))
					.findFirst().orElse(null);

			// 베이비밀 스케줄 변경 정합성 체크
			ApiResult<?> validResult = putScheduleValidation(paramDto, orderDetailScheduleListRequestDto, orderDetailScheduleUpdateDto.getDelvDate());
			if (Integer.parseInt(validResult.getCode()) < 0) return validResult;
		}

		List<String> idList 		= scheduleUpdateList.stream().map(OrderDetailScheduleUpdateDto::getId).collect(Collectors.toList());
		List<String> delvDateList 	= scheduleUpdateList.stream().map(OrderDetailScheduleUpdateDto::getDelvDate).collect(Collectors.toList());

		babymealOrderDeliveryListDtoList.forEach(babymealOrderDeliveryListDto -> {
			int targetIdx = idList.indexOf(babymealOrderDeliveryListDto.getId());

			if (targetIdx >= 0) {
				babymealOrderDeliveryListDto.setActiveFlg(ApiEnums.BabymealPutOrderInfoActiveFlag.UPDATE.getCode());
				babymealOrderDeliveryListDto.setDeliveryDate(delvDateList.get(targetIdx));
			} else {
				babymealOrderDeliveryListDto.setActiveFlg(ApiEnums.BabymealPutOrderInfoActiveFlag.SAVE.getCode());
				babymealOrderDeliveryListDto.setDeliveryDate(babymealOrderDeliveryListDto.getDelvDate());
			}
		});

		// 베이비밀 스케줄 배송일자 변경 I/F 호출
		return babymealBiz.putScheduleArrivalDate(babymealOrderInfoDto, babymealOrderDeliveryListDtoList);
	}

	/**
     * 베이비밀 스케줄 배송요일 변경
     * @param orderDetailScheduleListRequestDto
     * @return ApiResult<?>
     */
	@Override
	public ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.REQUIRED_GOODS_DAILY_TP);
	}

	/**
     * 베이비밀 스케줄 변경 Validation
     * @param BabymealOrderDeliveryListDto(베이비밀 배송 스케줄정보), OrderDetailScheduleListRequestDto(주문 상세 스케줄 정보), delvDate(변경할 배송예정일)
     * @return ApiResult<?>
	 * @throws Exception
     */
	private ApiResult<?> putScheduleValidation(BabymealOrderDeliveryListDto babymealOrderDeliveryListDto, OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto, String delvDate) throws Exception {

		// 주문정보 유효성 체크
		if (babymealOrderDeliveryListDto == null || babymealOrderDeliveryListDto.getId().isEmpty()) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.VALUE_EMPTY);

		// 해당 주문의 배송일자 변경 가능여부 체크(From)
		String fromDelvDate = babymealOrderDeliveryListDto.getDelvDate().replaceAll("-", "");
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
						Long.parseLong(orderDetailScheduleListRequestDto.getOrgIlGoodsId()),	// 주문상세 상품ID
						orderDetailScheduleListRequestDto.getRecvZipCd(),						// 수령인 우편번호
						orderDetailScheduleListRequestDto.getRecvBldNo());						// 건물번호

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
}