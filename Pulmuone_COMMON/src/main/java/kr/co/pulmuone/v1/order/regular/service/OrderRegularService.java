package kr.co.pulmuone.v1.order.regular.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.order.regular.dto.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.RegularShippingCycle;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.RegularShippingCycleTerm;
import kr.co.pulmuone.v1.comm.mapper.order.regular.MallOrderRegularMapper;
import kr.co.pulmuone.v1.comm.mapper.order.regular.OrderRegularDetailMapper;
import kr.co.pulmuone.v1.comm.mapper.order.regular.OrderRegularMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularInfoVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularPaymentKeyVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.RegularPaymentKeyVo;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayListResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayListResultVo;
import kr.co.pulmuone.v1.policy.holiday.service.PolicyHolidayBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.ShippingTemplateGroupByVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import lombok.RequiredArgsConstructor;
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
 *  1.0    20200915   	 홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRegularService {

	private final OrderRegularMapper orderRegularMapper;

	private final OrderRegularDetailMapper orderRegularDetailMapper;

	private final MallOrderRegularMapper mallOrderRegularMapper;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
	public PolicyHolidayBiz policyHolidayBiz;

	/**
	 * 사용중인 정기배송 정보 조회
	 *
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	protected OrderRegularInfoVo getActiveRegularInfo(Long urUserId) throws Exception {
		return orderRegularMapper.getActiveRegularInfo(urUserId);
	}

	/**
	 * 정기배송 주기 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	protected List<RegularShippingCycleDto> getRegularShippingCycleList() throws Exception {
		List<RegularShippingCycleDto> list = new ArrayList<RegularShippingCycleDto>();
		for (RegularShippingCycle cycle : OrderEnums.RegularShippingCycle.values()) {
			RegularShippingCycleDto dto = new RegularShippingCycleDto();
			dto.setCycleType(cycle.getCode());
			dto.setCycleTypeName(cycle.getCodeName());
			list.add(dto);
		}
		return list;
	}

	/**
	 * 정기배송 기간 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	protected List<RegularShippingCycleTermDto> getRegularShippingCycleTermList() throws Exception {

		List<RegularShippingCycleTermDto> list = new ArrayList<RegularShippingCycleTermDto>();
		for (RegularShippingCycleTerm cycleTerm : OrderEnums.RegularShippingCycleTerm.values()) {
			RegularShippingCycleTermDto dto = new RegularShippingCycleTermDto();
			dto.setCycleTermType(cycleTerm.getCode());
			dto.setCycleTermTypeName(cycleTerm.getCodeName());
			list.add(dto);
		}
		return list;
	}

	/**
	 * 장바구니용 정기배송 기존 상품 리스트
	 *
	 * @param shippingTemplateData
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	protected List<SpCartVo> getGoodsListByShippingPolicy(ShippingTemplateGroupByVo shippingTemplateData, Long urUserId)
			throws Exception {
		return orderRegularMapper.getGoodsListByShippingPolicy(shippingTemplateData, urUserId);
	}

	/**
	 * 사용중인 정기배송 배송지 조회
	 *
	 * @return
	 * @throws Exception
	 */
	protected GetSessionShippingResponseDto getRegularShippingZone(Long odRegularReqId) throws Exception {
		return orderRegularMapper.getRegularShippingZone(odRegularReqId);
	}

	/**
	 * 정기 배치키 등록
	 *
	 * @param orderRegularPaymentKeyVo
	 * @return
	 * @throws Exception
	 */
	protected int addRegularPaymentKey(OrderRegularPaymentKeyVo orderRegularPaymentKeyVo) throws Exception {
		return orderRegularMapper.addRegularPaymentKey(orderRegularPaymentKeyVo);
	}

	/**
	 * 정기결제 카드정보 조회
	 *
	 * @param	urUserId
	 * @return	RegularPaymentKeyVo
	 * @throws	Exception
	 */
	protected RegularPaymentKeyVo getRegularPaymentKey(Long urUserId) throws Exception {
		return orderRegularMapper.getRegularPaymentKey(urUserId);
	}

	/**
	 * 정기 배치 사용안함 처리
	 *
	 * @param urUserId
	 * @param noPaymentReason
	 * @return
	 * @throws Exception
	 */
	protected int putNoPaymentRegularPaymentKey(Long urUserId, String noPaymentReason) throws Exception {
		return orderRegularMapper.putNoPaymentRegularPaymentKey(urUserId, noPaymentReason);
	}

	/**
	 * 정기배송 신청 번호로 출고처 목록 조회
	 * @param odRegularReqId
	 * @return
	 */
	protected List<Long> getOdRegularUrWarehouseIdsByOdRegularReqId(long odRegularReqId) {
		return orderRegularMapper.getOdRegularUrWarehouseIdsByOdRegularReqId(odRegularReqId);
	}

	/**
	 * 휴무일 조회
	 * 해당 휴무일을 제외해야할 경우 사용 예정
	 * @return
	 */
	private List<LocalDate> getHoliday(List<Long> urWarehouseIds) {
		// 휴일 목록 조회
		GetHolidayListResponseDto holidayListResponse = (GetHolidayListResponseDto) policyHolidayBiz.getHolidayList().getData();
		List<GetHolidayListResultVo> holidayList = holidayListResponse.getRows();

		List<LocalDate> resultHolidayList = new ArrayList<>();

		for(GetHolidayListResultVo holiday : holidayList) {
			resultHolidayList.add(LocalDate.parse(holiday.getHolidayDate()));
		}

		// 출고처 휴일 목록 조회
		if(CollectionUtils.isNotEmpty(urWarehouseIds)) {
			//1개라도 배송이 가능하면 휴일에서 제외  orderRegularMapper.getUrWarehouseHolidayList 쿼리에 포함
			urWarehouseIds = urWarehouseIds.stream().distinct().collect(Collectors.toList());
			List<String> urWarehouseHolidays = orderRegularMapper.getUrWarehouseHolidayList(urWarehouseIds, urWarehouseIds.size());
			if (CollectionUtils.isNotEmpty(urWarehouseHolidays)) {
				for (String urWarehouseHoliday : urWarehouseHolidays) {
					if (!resultHolidayList.contains(LocalDate.parse(urWarehouseHoliday))) {
						resultHolidayList.add(LocalDate.parse(urWarehouseHoliday));
					}
				}
			}
		}

		return resultHolidayList;
	}

	/**
	 * 해당 일자가 일요일일 경우 다음날 얻기
	 * 해당 휴무일을 제외해야할 경우 사용 예정
	 * @return
	 */
	private LocalDate weekEndCheckDate(LocalDate standDate, List<LocalDate> holidayList) {

		LocalDate plusDays = standDate.plusDays(1);

		// 다음일자가 일요일일 경우 그 다음날 얻기
		if(plusDays.getDayOfWeek() == DayOfWeek.SUNDAY || holidayList.contains(plusDays)) {
			return weekEndCheckDate(plusDays, holidayList);
		}
		return plusDays;
	}

	/**
	 * 해당 일자 휴무일 체크
	 * @return
	 */
	protected LocalDate stdDateWeekEndCheckDate(LocalDate standDate, List<Long> urWarehouseIds) {

		LocalDate plusDays = standDate;
		List<LocalDate> holidayList = getHoliday(urWarehouseIds);

		// 다음일자가 일요일일 경우 그 다음날 얻기
		if(plusDays.getDayOfWeek() == DayOfWeek.SUNDAY || holidayList.contains(plusDays)) {
			return weekEndCheckDate(plusDays, holidayList);
		}
		return plusDays;
	}

	/**
	 * 정기배송 스케줄 도착예정일자 리스트
	 * @param StartArrivalScheduledDate
	 * @param regularShippingCycle
	 * @param regularShippingCycleTerm
	 * @return
	 * @throws Exception
	 */
	protected List<LocalDate> getRegularArrivalScheduledDateList(LocalDate StartArrivalScheduledDate,
			RegularShippingCycle regularShippingCycle, RegularShippingCycleTerm regularShippingCycleTerm, List<Long> urWarehouseIds) throws Exception {
		List<LocalDate> list = new ArrayList<LocalDate>();
		List<LocalDate> holidayList = getHoliday(urWarehouseIds);
		LocalDate nowDate = LocalDate.now();
		LocalDate checkLastDate = StartArrivalScheduledDate.plusWeeks((regularShippingCycleTerm.getMonth() * 4) - 1); // 1달을 4주로 계산
        for(LocalDate date = StartArrivalScheduledDate; date.isBefore(checkLastDate) || date.isEqual(checkLastDate); date = date.plusWeeks(regularShippingCycle.getWeeks())) {
        	// 설정 날짜가 현재보다 이전일 경우 다음 날짜 조회
        	if(date.isBefore(nowDate)) continue;
        	if(holidayList.contains(date)) {
        		list.add(weekEndCheckDate(date, holidayList));
        	}
        	else {
        		list.add(date);
			}
        }
		return list;
	}

	/**
	 * 정기배송 스케줄 도착예정일자 리스트 (시작일, 종료일 지정)
	 * @param StartArrivalScheduledDate
	 * @param regularShippingCycle
	 * @param EndArrivalScheduleDate
	 * @param urWarehouseIds
	 * @return
	 * @throws Exception
	 */
	protected List<LocalDate> getRegularArrivalScheduledDateList(LocalDate StartArrivalScheduledDate,
			RegularShippingCycle regularShippingCycle, LocalDate EndArrivalScheduleDate, List<Long> urWarehouseIds) throws Exception {
		List<LocalDate> list = new ArrayList<LocalDate>();
		List<LocalDate> holidayList = getHoliday(urWarehouseIds);
        for(LocalDate date = StartArrivalScheduledDate; (date.isEqual(EndArrivalScheduleDate) || date.isBefore(EndArrivalScheduleDate)); date = date.plusWeeks(regularShippingCycle.getWeeks())) {
        	if(holidayList.contains(date)) {
        		list.add(weekEndCheckDate(date, holidayList));
        	}
        	else {
        		list.add(date);
			}
        }
		return list;
	}

	/**
	 * 정기배송 스케줄 도착예정일자 리스트 (시작일, 시작회차, 종료회차 지정)
	 * @param StartArrivalScheduledDate
	 * @param regularShippingCycle
	 * @param startReqRound
	 * @param endReqRound
	 * @return
	 * @throws Exception
	 */
	protected List<LocalDate> getRegularArrivalScheduledDateListByReqRound(LocalDate StartArrivalScheduledDate,
			RegularShippingCycle regularShippingCycle, int startReqRound, int endReqRound, List<Long> urWarehouseIds) throws Exception {
		List<LocalDate> list = new ArrayList<LocalDate>();
		List<LocalDate> holidayList = getHoliday(urWarehouseIds);
		LocalDate date = StartArrivalScheduledDate;
		for(int i=startReqRound; i<=endReqRound; i++) {
			if(holidayList.contains(date)) {
				list.add(weekEndCheckDate(date, holidayList));
			}
			else {
				list.add(date);
			}
			date = date.plusWeeks(regularShippingCycle.getWeeks());
		}
		return list;
	}

	/**
	 * 배송정책 기준 가장 작은 배송비 얻기
	 * @param ilGoodsShippingTemplateIdList
	 * @param goodsPrice
	 * @param orderCnt
	 * @param recvZipCd
	 * @return
	 * @throws Exception
	 */
	protected RegularShippingPriceInfoDto getShippingPrice(List<Long> ilGoodsShippingTemplateIdList, int goodsPrice, int orderCnt, String recvZipCd) throws Exception {

		RegularShippingPriceInfoDto shippingInfo = new RegularShippingPriceInfoDto();
		ShippingDataResultVo shippingDataResultVo = null;
		ShippingPriceResponseDto shippingPriceDto = null;
		int minShippingPrice = Integer.MAX_VALUE;

		for(long ilGoodsShippingTmplateId : ilGoodsShippingTemplateIdList) {
			shippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(ilGoodsShippingTmplateId);
			shippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(shippingDataResultVo, goodsPrice, orderCnt, recvZipCd);
			// 작은 값으로 배송비 설정
			if(minShippingPrice > shippingPriceDto.getShippingPrice()) {
				shippingInfo.setIlShippingTmplId(ilGoodsShippingTmplateId);
				shippingInfo.setShippingPrice(shippingPriceDto.getShippingPrice());
			}
			minShippingPrice = Math.min(minShippingPrice, shippingPriceDto.getShippingPrice());
		}

		return shippingInfo;
	}

    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

    protected void setOrderRegularReqListSearchParam(RegularReqListRequestDto regularReqListRequestDto) {

    	regularReqListRequestDto.setRegularReqDetailStatusList(getSearchKeyToSearchKeyList(regularReqListRequestDto.getRegularReqDetailStatus(), Constants.ARRAY_SEPARATORS)); // 신청상세상태
    	regularReqListRequestDto.setRegularReqStatusList(getSearchKeyToSearchKeyList(regularReqListRequestDto.getRegularReqStatus(), Constants.ARRAY_SEPARATORS)); // 신청상태
    	regularReqListRequestDto.setRegularTermList(getSearchKeyToSearchKeyList(regularReqListRequestDto.getRegularTerm(), Constants.ARRAY_SEPARATORS)); // 신청기간
    	regularReqListRequestDto.setAgentTypeCodeList(getSearchKeyToSearchKeyList(regularReqListRequestDto.getAgentTypeCode(), Constants.ARRAY_SEPARATORS)); // 유형

    	ArrayList<String> orderCdArray = null;

       	if("singleSection".equals(regularReqListRequestDto.getSelectConditionType())) {

       		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String orderCodeListStr = regularReqListRequestDto.getCodeSearch().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            orderCdArray = StringUtil.getArrayListComma(orderCodeListStr);
            regularReqListRequestDto.setCodeSearchList(orderCdArray);
       	}
    }

	/**
	 * 정기배송 주문 신청 리스트 조회
	 *
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	protected Page<RegularReqListDto> getOrderRegularReqList(RegularReqListRequestDto regularReqListRequestDto) throws Exception {

		setOrderRegularReqListSearchParam(regularReqListRequestDto);

		return orderRegularMapper.getOrderRegularReqList(regularReqListRequestDto);
	}

	/**
	 * 정기배송 주문 신청 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	protected MallRegularReqInfoResponseDto getOrderRegularReqInfo(long urUserId) throws Exception {
		return mallOrderRegularMapper.getOrderRegularReqInfo(urUserId);
	}

	/**
	 * 정기배송 조회 회차 총 수 조회
	 * @param regularReqGoodsListRequestDto
	 * @return
	 * @throws Exception
	 */
	protected int getOrderRegularReqRoundGoodsListTotCnt(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception {
		return mallOrderRegularMapper.getOrderRegularReqRoundGoodsListTotCnt(regularReqGoodsListRequestDto);
	}

	/**
	 * 정기배송 회차별 상품 목록 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	List<RegularResultReqRoundGoodsListDto> getOrderRegularReqRoundGoodsList(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception {

		int startPage = (regularReqGoodsListRequestDto.getPage() - 1) * regularReqGoodsListRequestDto.getLimit();
    	regularReqGoodsListRequestDto.setStartPage((startPage < 1) ? 0 : startPage);
    	regularReqGoodsListRequestDto.setPageSize(regularReqGoodsListRequestDto.getLimit());
    	regularReqGoodsListRequestDto.setEPage(String.valueOf(regularReqGoodsListRequestDto.getLimit()));

		return mallOrderRegularMapper.getOrderRegularReqRoundGoodsList(regularReqGoodsListRequestDto);
	}

	/**
	 * 정기배송 주문 결과 상세 상품 수량 정보 업데이트
	 * @param odRegularReqId
	 * @param stdDate
	 * @param stdDate
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularResultDetailGoodsInfo(long odRegularReqId, LocalDate stdDate) throws Exception {
		return orderRegularMapper.putOrderRegularResultDetailGoodsInfo(odRegularReqId, stdDate);
	}
}
