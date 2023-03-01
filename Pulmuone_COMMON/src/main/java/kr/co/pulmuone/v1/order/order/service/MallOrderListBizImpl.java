package kr.co.pulmuone.v1.order.order.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDeliveryType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.WeekCodeByGreenJuice;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문리스트 관련 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class MallOrderListBizImpl implements MallOrderListBiz {

    @Autowired
    private MallOrderListService mallOrderListService;

	// 주문 녹즙/잇슬림/베이비밀 스캐줄  관련 Service
	@Autowired
	private MallOrderScheduleBiz mallOrderScheduleBiz;

	@Autowired
	private MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

	@Autowired
	private OrderDetailBiz orderDetailBiz;

    /**
     * @Desc 주문/배송 리스트 조회
     * @param mallOrderListRequestDto
     * @return ApiResult<?>
     * @throws
     */
	@Override
	public ApiResult<?> getOrderList(MallOrderListRequestDto mallOrderListRequestDto) {

		Page<MallOrderListDto> orderList = mallOrderListService.getOrderList(mallOrderListRequestDto);
		MallOrderListResponseDto mallOrderListResponseDto = MallOrderListResponseDto.builder()
				.total(orderList.getTotal())
				.list(orderList.getResult())
				.build();

		return ApiResult.success(mallOrderListResponseDto);
	}

    /**
     * @Desc 취소/반품 리스트 조회
     * @param mallOrderListRequestDto
     * @return ApiResult<?>
     * @throws
     */
	@Override
	public ApiResult<?> getOrderClaimList(MallOrderListRequestDto mallOrderListRequestDto) {

		Page<MallOrderListDto> orderClaimList = mallOrderListService.getOrderClaimList(mallOrderListRequestDto);
		MallOrderListResponseDto mallOrderListResponseDto = MallOrderListResponseDto.builder()
				.total(orderClaimList.getTotal())
				.list(orderClaimList.getResult())
				.build();

		return ApiResult.success(mallOrderListResponseDto);
	}

    /**
     * @Desc 일일배송 리스트 조회
     * @param mallOrderListRequestDto
     * @return ApiResult<?>
     * @throws
     */
	@Override
	public ApiResult<?> getOrderDetailDailyList(MallOrderDailyListRequestDto mallOrderDailyListRequestDto) {

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        mallOrderDailyListRequestDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
		mallOrderDailyListRequestDto.setGoodsDeliveryType(GoodsDeliveryType.DAILY.getCode());

		long totalCnt = mallOrderListService.getOrderDailyListCount(mallOrderDailyListRequestDto);

		//PageMethod.startPage(mallOrderDailyListRequestDto.getPage(), mallOrderDailyListRequestDto.getLimit());




		int page = mallOrderDailyListRequestDto.getPage();
		int limit = mallOrderDailyListRequestDto.getLimit();
		int sPage = 0;
		int ePage = limit;
		if(StringUtil.nvlInt(page) > 1) {
			sPage = ( StringUtil.nvlInt(page) - 1 ) * StringUtil.nvlInt(limit);
		}


		mallOrderDailyListRequestDto.setStartPage(sPage);
		mallOrderDailyListRequestDto.setPageSize(ePage);


		List<MallOrderDailyListDto> mallOrderDailyList = mallOrderListService.getOrderDailyList(mallOrderDailyListRequestDto);
		List<MallOrderDailyListDto> responseGoodsList = new ArrayList<> ();

		// 일일배송 리스트가 존재할 경우
		if(!Objects.isNull(mallOrderDailyList)) {



			// 1 뎁스 리스트 조회
			List<MallOrderDailyListDto> mainList = mallOrderDailyList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_FIRST.getDepthNum())
					.collect(toList());

			// 2 뎁스 리스트 조회
			List<MallOrderDailyListDto> subList = mallOrderDailyList.stream()
					.filter(obj -> obj.getOdOrderDetlDepthId() == OrderEnums.OrderGoodsDepth.ORDER_GOODS_SECOND.getDepthNum())
					.collect(toList());

			String weekDayNm = "";
			for(MallOrderDailyListDto mallOrderDailyItem: mainList){

				weekDayNm = "";
				if (mallOrderDailyItem.getMonCnt() > 0){
					weekDayNm = WeekCodeByGreenJuice.MON.getCodeName();
				}
				if (mallOrderDailyItem.getTueCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.TUE.getCodeName();
				}
				if (mallOrderDailyItem.getWedCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.WED.getCodeName();
				}
				if (mallOrderDailyItem.getThuCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.THU.getCodeName();
				}
				if (mallOrderDailyItem.getFriCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.FRI.getCodeName();
				}
				mallOrderDailyItem.setWeekDayNm(weekDayNm);


				System.out.println("mallOrderDailyItem.getOdOrderDetlParentId() : " + mallOrderDailyItem.getOdOrderDetlParentId());


				subList.stream()
						.filter(obj -> obj.getMonCnt() > 0 && obj.getOdOrderDetlParentId() == mallOrderDailyItem.getOdOrderDetlId())
						.collect(toList());


				mallOrderDailyItem.setPickMonList(subList.stream()
														.filter(obj -> obj.getMonCnt() > 0 && obj.getOdOrderDetlParentId() == mallOrderDailyItem.getOdOrderDetlId())
														.collect(toList()));
				mallOrderDailyItem.setPickTueList(subList.stream()
														.filter(obj -> obj.getTueCnt() > 0 && obj.getOdOrderDetlParentId() == mallOrderDailyItem.getOdOrderDetlId())
														.collect(toList()));
				mallOrderDailyItem.setPickWedList(subList.stream()
														.filter(obj -> obj.getWedCnt() > 0 && obj.getOdOrderDetlParentId() == mallOrderDailyItem.getOdOrderDetlId())
														.collect(toList()));
				mallOrderDailyItem.setPickThuList(subList.stream()
														.filter(obj -> obj.getThuCnt() > 0 && obj.getOdOrderDetlParentId() == mallOrderDailyItem.getOdOrderDetlId())
														.collect(toList()));
				mallOrderDailyItem.setPickFriList(subList.stream()
														.filter(obj -> obj.getFriCnt() > 0 && obj.getOdOrderDetlParentId() == mallOrderDailyItem.getOdOrderDetlId())
														.collect(toList()));

				// 녹즙 내맘대로 주문인 경우 ORDER_CNT 수정, 후기 업데이트
				List<MallOrderDailyListDto> pickTotalList =  subList.stream()
																.filter(obj -> obj.getOdOrderDetlParentId() == mallOrderDailyItem.getOdOrderDetlId())
																.collect(toList());
				if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(mallOrderDailyItem.getPromotionType())
						&& CollectionUtils.isNotEmpty(pickTotalList)){
					// order_cnt 수정
					mallOrderDailyItem.setOrderCnt(pickTotalList.stream().mapToInt(m-> m.getOrderCnt()).sum());

					// 껍데기상품에 후기작성여부 업데이트
					// ilGoodsId 기준으로 grouping
					Map<Long, List<MallOrderDailyListDto>> goodsIdMap = pickTotalList.stream()
							.collect(groupingBy(MallOrderDailyListDto::getIlGoodsId, LinkedHashMap::new,toList()));

					AtomicLong pickGoodsFeedbackCnt = new AtomicLong(0);
					long totalOrderGoodsCnt = goodsIdMap.size();

					goodsIdMap.entrySet().forEach(idEntry -> {
						boolean isFeedbackWrite = false;
						for(MallOrderDailyListDto goodsDto : idEntry.getValue()){
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
						mallOrderDailyItem.setFeedbackWriteCnt(1);
					}else{
						mallOrderDailyItem.setFeedbackWriteCnt(0);
						mallOrderDailyItem.setFeedbackWriteUseDay(minFeedbackWriteUseDay);
					}

				}

				// 녹즙 - 배송지 변경가능한 가장 빠른 도착예정일 세팅(배송지변경 팝업에서 사용)
				// 1. 일일배송 스케쥴 정보 조회
				OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();
				List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList = new ArrayList<>();

				// 1.1 - 주문상세PK로 일일상품 정보 조회
				MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(mallOrderDailyItem.getOdOrderDetlId());

				if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailGoodsDto.getGoodsTpCd())
						&& ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){
					// 1.2 - 녹즙-내맘대로 인 경우 -> OD_ORDER_ID 세팅
					orderDetailScheduleListRequestDto.setOdOrderId(String.valueOf(orderDetailGoodsDto.getOdOrderId()));
					orderDetailScheduleListRequestDto.setPromotionYn("Y");

					orderDetailScheduleArrivalDateList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
				}else if(GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(orderDetailGoodsDto.getGoodsDailyTp())){

					// 1.3 - 녹즙인 경우 - > 같은 주문건에서 녹즙주문건 모두 조회
					List<MallOrderDetailGoodsDto> orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(mallOrderDailyItem.getOdOrderId(), mallOrderDailyItem.getGoodsDailyTp());

					for(MallOrderDetailGoodsDto dto : orderDetailGoodsList){
						// OD_ORDER_DETL_ID 세팅
						orderDetailScheduleListRequestDto.setOdOrderDetlId(dto.getOdOrderDetlId());
						List<OrderDetailScheduleListDto>  orderDetailList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
						orderDetailScheduleArrivalDateList.addAll(orderDetailList);
					}
				}

				// 2. 스케쥴 리스트에서 변경 가능한 가장 빠른 도착예정일 찾기
				if(CollectionUtils.isNotEmpty(orderDetailScheduleArrivalDateList)){

					//리스트 정렬
					List<OrderDetailScheduleListDto>  sortedOrderDetailScheduleArrivalDateList = orderDetailScheduleArrivalDateList.stream()
							.sorted(Comparator.comparing(OrderDetailScheduleListDto::getDelvDate))
							.collect(toList());

					try{
						for(OrderDetailScheduleListDto schDto : sortedOrderDetailScheduleArrivalDateList){
							String delvDate = schDto.getDelvDate().replaceAll("-", ""); // 도착예정일
							String delvDateWeekDay = schDto.getDelvDateWeekDay(); // 도착예정일의 요일
							int addDate = OrderScheduleEnums.ScheduleChangeDate.findByCode(delvDateWeekDay).getAddDate();

							String possibleChangeDate = DateUtil.addDays(delvDate, addDate, "yyyyMMdd");// 해당 도착예정일의 스케쥴 변경 가능일자
							String currentDate = DateUtil.getCurrentDate();

							if (Integer.parseInt(currentDate) <= Integer.parseInt(possibleChangeDate)){
								mallOrderDailyItem.setFirstDeliveryDt(schDto.getDelvDate());
								break;
							}
						}
					}catch(Exception e){
					}
				}

				// 현재 배송지 정보 수정
				MallOrderDailyShippingZoneHistListResponseDto mallDailyShippingZoneHistResponse
						= (MallOrderDailyShippingZoneHistListResponseDto)mallOrderDailyDetailBiz.getOrderDailyShippingZoneHist(mallOrderDailyItem.getOdOrderId(),mallOrderDailyItem.getOdShippingZoneId(),mallOrderDailyItem.getOdOrderDetlId()).getData();
				if(StringUtils.isNotEmpty(mallDailyShippingZoneHistResponse.getShippingZoneInfo().getRecvNm())){
					mallOrderDailyItem.setRecvAddr1(mallDailyShippingZoneHistResponse.getShippingZoneInfo().getRecvAddr1());
					mallOrderDailyItem.setRecvAddr2(mallDailyShippingZoneHistResponse.getShippingZoneInfo().getRecvAddr2());
					mallOrderDailyItem.setRecvBldNo(mallDailyShippingZoneHistResponse.getShippingZoneInfo().getRecvBldNo());
					mallOrderDailyItem.setRecvNm(mallDailyShippingZoneHistResponse.getShippingZoneInfo().getRecvNm());
					mallOrderDailyItem.setRecvZipCd(mallDailyShippingZoneHistResponse.getShippingZoneInfo().getRecvZipCd());
				}


				// 주문배송지 변경이력 수정 -> 녹즙만 해당
				if(GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(mallOrderDailyItem.getGoodsDailyTp())){
					long odOrderDetlId = mallOrderDailyItem.getOdOrderDetlId();

					// 주문배송지 변경이력 count 조회
					String recvChgHistYn = mallOrderDailyDetailBiz.getOrderDailyShippingZoneChangeCount(mallOrderDailyItem.getPromotionType(), odOrderDetlId) > 1 ? "Y" : "N";
					mallOrderDailyItem.setRecvChgHistYn(recvChgHistYn);

				}


				responseGoodsList.add(mallOrderDailyItem);
			}

		}


		/*
		if(!Objects.isNull(mallOrderDailyList)) {




			String weekDayNm = "";
			// 이미 저장한 녹즙 정보를 위한 Map
			Map<Long, Integer> greenJuiceMap = new HashMap<> ();
			for(int i=0; i<mallOrderDailyList.size(); i++) {
				// sql 응답 Item 얻기
				MallOrderDailyListDto mallOrderDailyItem = mallOrderDailyList.get(i);
				// 응답 Dto에 넣을 상품 객체 생성
				MallOrderDailyGoodsDto mallOrderDailyGoods = new MallOrderDailyGoodsDto();
				mallOrderDailyGoods.setOdOrderId(mallOrderDailyItem.getOdOrderId());					// 주문PK
				mallOrderDailyGoods.setOdOrderDetlId(mallOrderDailyItem.getOdOrderDetlId());			// 주문상세PK
				mallOrderDailyGoods.setIlGoodsId(mallOrderDailyItem.getIlGoodsId());
				mallOrderDailyGoods.setIlItemCd(mallOrderDailyItem.getIlItemCd());
				mallOrderDailyGoods.setEvExhibitCd(mallOrderDailyItem.getEvExhibitCd());
				mallOrderDailyGoods.setGoodsNm(mallOrderDailyItem.getGoodsNm());						// 상품명
				mallOrderDailyGoods.setThumbnailPath(mallOrderDailyItem.getThumbnailPath());			// 상품이미지
				mallOrderDailyGoods.setOrderCnt(mallOrderDailyItem.getOrderCnt());						// 주문수량
				mallOrderDailyGoods.setPaidPrice(mallOrderDailyItem.getPaidPrice());					// 결제금액
				mallOrderDailyGoods.setOrderStatusCd(mallOrderDailyItem.getOrderStatusCd());			// 주문상태코드
				mallOrderDailyGoods.setGoodsDailyTp(mallOrderDailyItem.getGoodsDailyTp());				// 일일상품유형코드
				mallOrderDailyGoods.setAllergyYn(mallOrderDailyItem.getAllergyYn());					// 알러지식단여부
				mallOrderDailyGoods.setGoodsCycleTp(mallOrderDailyItem.getGoodsCycleTp());				// 배송주기코드
				mallOrderDailyGoods.setGoodsCycleTpNm(mallOrderDailyItem.getGoodsCycleTpNm());			// 배송주기코드명
				mallOrderDailyGoods.setGoodsCycleTermTp(mallOrderDailyItem.getGoodsCycleTermTp());		// 배송기간코드
				mallOrderDailyGoods.setGoodsCycleTermTpNm(mallOrderDailyItem.getGoodsCycleTermTpNm());	// 배송기간코드명
				mallOrderDailyGoods.setOdShippingZoneId(mallOrderDailyItem.getOdShippingZoneId());		// 주문배송지PK
				mallOrderDailyGoods.setRecvNm(mallOrderDailyItem.getRecvNm());							// 수령인명
				mallOrderDailyGoods.setRecvZipCd(mallOrderDailyItem.getRecvZipCd());					// 수령인 우편번호
				mallOrderDailyGoods.setRecvAddr1(mallOrderDailyItem.getRecvAddr1());					// 수령인주소1
				mallOrderDailyGoods.setRecvAddr2(mallOrderDailyItem.getRecvAddr2());					// 수령인주소2
				mallOrderDailyGoods.setRecvBldNo(mallOrderDailyItem.getRecvBldNo());					// 수령인빌딩번호
				mallOrderDailyGoods.setRecvChgHistYn(mallOrderDailyItem.getRecvChgHistYn());			// 배송지변경내역여부
				mallOrderDailyGoods.setFrontJson(mallOrderDailyItem.getFrontJson());					// FRONT JSON
				mallOrderDailyGoods.setActionJson(mallOrderDailyItem.getActionJson());					// ACTION JSON
				// TODO 배송 가능 첫번째 날짜 정보 얻어야함
				mallOrderDailyGoods.setFirstDeliveryDt("YYYY년MM월DD일");									// 배송 가능 첫번째 날짜

				weekDayNm = "";
				if (mallOrderDailyItem.getMonCnt() > 0){
					weekDayNm = WeekCodeByGreenJuice.MON.getCodeName();
				}
				if (mallOrderDailyItem.getTueCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.TUE.getCodeName();
				}
				if (mallOrderDailyItem.getWedCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.WED.getCodeName();
				}
				if (mallOrderDailyItem.getThuCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.THU.getCodeName();
				}
				if (mallOrderDailyItem.getFriCnt() > 0){
					if (!"".equals(weekDayNm)) {weekDayNm += "/";}
					weekDayNm += WeekCodeByGreenJuice.FRI.getCodeName();
				}
				mallOrderDailyGoods.setWeekDayNm(weekDayNm);

				// 일일상품 유형이 녹즙이고 기존에 담은 정보가 없을 경우
				if(GoodsDailyType.GREENJUICE.getCode().equals(mallOrderDailyItem.getGoodsDailyTp())) {

					// 이미 처리한 녹즙 정보가 있을 경우 Pass
					if(greenJuiceMap.containsKey(mallOrderDailyItem.getOdOrderDetlId())) {
						continue;
					}

					// 현재 주문상세번호
					long nowOdOrderDetlId = mallOrderDailyItem.getOdOrderDetlId();
					// 요일목록
					List<MallOrderDailyDaysDto> daysList = new ArrayList<> ();
					Map<String, List<MallOrderDailyDaysGoodsDto>> daysMap = new LinkedHashMap<>();

					// WeekCodeByGreenJuice Enum 클래스의 코드명으로 Key 값 Set - 월 ~ 금 순차적으로 저장하기 위해
					for(WeekCodeByGreenJuice daysCode : WeekCodeByGreenJuice.values()) {
						daysMap.put(daysCode.getCodeName(), null);
					}

					// 녹즙 정보 Set
					for(int j=i; j<mallOrderDailyList.size(); j++) {
						MallOrderDailyListDto greenJuiceItem = mallOrderDailyList.get(j);
						// nowOdOrderDetlId 와 조회 하고자 하는 주문 상세번호가 다를 경우 다른 주문건이므로 break 처리
						if(nowOdOrderDetlId != greenJuiceItem.getOdOrderDetlId()) { break; }
						setOrderDailyDaysGoodsList(daysMap, greenJuiceItem);
					}

					// Map 에 저장된 Key 값 얻어서 List Set
					Set<String> keys = daysMap.keySet();
					for(String key : keys) {
						// 요일 정보가 있는 것만 리스트에 추가 해준다
						if(!Objects.isNull(daysMap.get(key))) {
							MallOrderDailyDaysDto daysInfo = new MallOrderDailyDaysDto();
							daysInfo.setDays(key);							// 요일명
							daysInfo.setDaysGoodsList(daysMap.get(key));	// 요일 상품 리스트
							daysList.add(daysInfo);
						}
					}

					// 요일별 상품정보 Set
					mallOrderDailyGoods.setDaysList(daysList);

					// 주문상세PK 추가
					greenJuiceMap.put(mallOrderDailyItem.getOdOrderDetlId(), 1);
				}
				responseGoodsList.add(mallOrderDailyGoods);
			}


		}
 */
		//하이톡 스위치
		boolean hitokSwitch = (Boolean)orderDetailBiz.getHitokSwitch().getData() == null ? false : (Boolean)orderDetailBiz.getHitokSwitch().getData();

		MallOrderDailyListResponseDto mallOrderDailyListResponseDto = MallOrderDailyListResponseDto.builder()
																									.total(totalCnt)
																									.list(responseGoodsList)
																									.isHitokSwitch(hitokSwitch)
																									.build();

		return ApiResult.success(mallOrderDailyListResponseDto);
	}

	/**
	 * 일일배송 주문 요일 별 상품정보 Set
	 * @param daysInf
	 * @param greenJuiceItem
	 */
	private void setOrderDailyDaysGoodsList(Map<String, List<MallOrderDailyDaysGoodsDto>> daysInf, MallOrderDailyListDto greenJuiceItem) {

		long ilGoodsId = greenJuiceItem.getIlGoodsId();
		String goodsNm = greenJuiceItem.getGoodsNm();

		MallOrderDailyDaysGoodsDto goodsItem = null;
		List<MallOrderDailyDaysGoodsDto> goodsList = null;
		// 월요일 수량이 있을 경우
		if(greenJuiceItem.getMonCnt() > 0) {
			goodsItem = new MallOrderDailyDaysGoodsDto();
			goodsItem.setIlGoodsId(ilGoodsId);
			goodsItem.setGoodsNm(goodsNm);
			goodsItem.setOrderCnt(greenJuiceItem.getMonCnt());
			goodsList = new ArrayList<>();
			if(!Objects.isNull(daysInf.get(WeekCodeByGreenJuice.MON.getCodeName()))) {
				goodsList = getOrderDailyGoodsList(daysInf.get(WeekCodeByGreenJuice.MON.getCodeName()), goodsItem);
			}
			else {
				goodsList.add(goodsItem);
			}
			daysInf.put(WeekCodeByGreenJuice.MON.getCodeName(), goodsList);
		}
		// 화요일 수량이 있을 경우
		if(greenJuiceItem.getTueCnt() > 0) {
			goodsItem = new MallOrderDailyDaysGoodsDto();
			goodsItem.setIlGoodsId(ilGoodsId);
			goodsItem.setGoodsNm(goodsNm);
			goodsItem.setOrderCnt(greenJuiceItem.getTueCnt());
			goodsList = new ArrayList<>();
			if(!Objects.isNull(daysInf.get(WeekCodeByGreenJuice.TUE.getCodeName()))) {
				goodsList = getOrderDailyGoodsList(daysInf.get(WeekCodeByGreenJuice.TUE.getCodeName()), goodsItem);
			}
			else {
				goodsList.add(goodsItem);
			}
			daysInf.put(WeekCodeByGreenJuice.TUE.getCodeName(), goodsList);
		}
		// 수요일 수량이 있을 경우
		if(greenJuiceItem.getWedCnt() > 0) {
			goodsItem = new MallOrderDailyDaysGoodsDto();
			goodsItem.setIlGoodsId(ilGoodsId);
			goodsItem.setGoodsNm(goodsNm);
			goodsItem.setOrderCnt(greenJuiceItem.getWedCnt());
			goodsList = new ArrayList<>();
			if(!Objects.isNull(daysInf.get(WeekCodeByGreenJuice.WED.getCodeName()))) {
				goodsList = getOrderDailyGoodsList(daysInf.get(WeekCodeByGreenJuice.WED.getCodeName()), goodsItem);
			}
			else {
				goodsList.add(goodsItem);
			}
			daysInf.put(WeekCodeByGreenJuice.WED.getCodeName(), goodsList);
		}
		// 목요일 수량이 있을 경우
		if(greenJuiceItem.getThuCnt() > 0) {
			goodsItem = new MallOrderDailyDaysGoodsDto();
			goodsItem.setIlGoodsId(ilGoodsId);
			goodsItem.setGoodsNm(goodsNm);
			goodsItem.setOrderCnt(greenJuiceItem.getThuCnt());
			goodsList = new ArrayList<>();
			if(!Objects.isNull(daysInf.get(WeekCodeByGreenJuice.THU.getCodeName()))) {
				goodsList = getOrderDailyGoodsList(daysInf.get(WeekCodeByGreenJuice.THU.getCodeName()), goodsItem);
			}
			else {
				goodsList.add(goodsItem);
			}
			daysInf.put(WeekCodeByGreenJuice.THU.getCodeName(), goodsList);
		}
		// 금요일 수량이 있을 경우
		if(greenJuiceItem.getFriCnt() > 0) {
			goodsItem = new MallOrderDailyDaysGoodsDto();
			goodsItem.setIlGoodsId(ilGoodsId);
			goodsItem.setGoodsNm(goodsNm);
			goodsItem.setOrderCnt(greenJuiceItem.getFriCnt());
			goodsList = new ArrayList<>();
			if(!Objects.isNull(daysInf.get(WeekCodeByGreenJuice.FRI.getCodeName()))) {
				goodsList = getOrderDailyGoodsList(daysInf.get(WeekCodeByGreenJuice.FRI.getCodeName()), goodsItem);
			}
			else {
				goodsList.add(goodsItem);
			}
			daysInf.put(WeekCodeByGreenJuice.FRI.getCodeName(), goodsList);
		}
	}

	/**
	 * 상품 코드 동일한 정보 찾아서 수량 합산
	 * @param goodsList
	 * @param ilGoodsId
	 * @param orderCnt
	 * @return
	 */
	private List<MallOrderDailyDaysGoodsDto> getOrderDailyGoodsList(List<MallOrderDailyDaysGoodsDto> goodsList, MallOrderDailyDaysGoodsDto goodsItem) {
		boolean dupFlag = false;
		for(MallOrderDailyDaysGoodsDto dailyDaysGoods : goodsList) {
			// 이미 담겨 있는 상품일 경우 주문 수량을 더해준다
			if(goodsItem.getIlGoodsId() == dailyDaysGoods.getIlGoodsId()) {
				dailyDaysGoods.setOrderCnt(dailyDaysGoods.getOrderCnt() + goodsItem.getOrderCnt());
				dupFlag = true;
			}
		}
		// 중복되는 상품 번호가 없을 경우 리스트에 추가
		if(!dupFlag) {
			goodsList.add(goodsItem);
		}
		return goodsList;
	}

	@Override
	public ApiResult<?> getOrderPresentList(MallOrderListRequestDto mallOrderListRequestDto) {
		Page<MallOrderPresentListDto> orderList = mallOrderListService.getOrderPresentList(mallOrderListRequestDto);
		MallOrderPresentListResponseDto mallOrderListResponseDto = MallOrderPresentListResponseDto.builder()
				.total(orderList.getTotal())
				.list(orderList.getResult())
				.build();

		return ApiResult.success(mallOrderListResponseDto);
	}
}