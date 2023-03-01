package kr.co.pulmuone.v1.order.schedule.service.mall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.mapper.order.schedule.OrderDetailScheduleMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.order.schedule.MallOrderDetailScheduleMapper;
import kr.co.pulmuone.v1.comm.util.PhoneUtil;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustOrdInpHeaderRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustOrdInpLineRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustOrdInpRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailGreenJuiceListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDateInfoDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDayOfWeekListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleGoodsDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleInfoDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleShippingInfoDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.vo.OrderDetlScheduleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@RequiredArgsConstructor
public class MallOrderScheduleService {

	// 주문 스케줄 Mapper
    private final MallOrderDetailScheduleMapper mallOrderDetailScheduleMapper;

	// 주문 스케줄 Mapper
    private final OrderDetailScheduleMapper orderDetailScheduleMapper;

    @Autowired
    private final ErpApiExchangeService erpApiExchangeService;

    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아닌 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();
        if(StringUtils.isNotEmpty(searchKey)) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }

        return searchKeyList;
    }


    /**
     * 주문 스케줄 리스트 Request 정보 조회(주문번호, 주문상세  상품 PK, 일일상품 유형)
     * @param odOrderDetlId
     * @return OrderDetailScheduleListRequestDto
     */
	public OrderDetailScheduleListRequestDto getOrderScheduleRequestInfo(Long odOrderDetlId) {
		return mallOrderDetailScheduleMapper.getOrderScheduleRequestInfo(odOrderDetlId);
	}

    /**
     * 주문 스케줄 리스트 상단정보 내맘대로
     * @param odOrderId
     * @return OrderDetailScheduleListRequestDto
     */
	public OrderDetailScheduleGoodsDto getOrderScheduleSelectGoodsInfo(Long odOrderId) {
		return mallOrderDetailScheduleMapper.getOrderScheduleSelectGoodsInfo(odOrderId);
	}

    /**
     * 주문 스케줄 리스트 상단정보
     * @param odOrderDetlId
     * @return OrderDetailScheduleListRequestDto
     */
	public OrderDetailScheduleGoodsDto getOrderScheduleGoodsInfo(Long odOrderDetlId) {
		return mallOrderDetailScheduleMapper.getOrderScheduleGoodsInfo(odOrderDetlId);
	}

	/**
     * 주문 스케줄 리스트 상단정보(베이비밀, 잇슬림)
     * @param odOrderDetlId
     * @return OrderDetailScheduleListRequestDto
     */
	public OrderDetailScheduleGoodsDto getOrderScheduleGoodsBaseInfo(Long odOrderDetlId) {
		return mallOrderDetailScheduleMapper.getOrderScheduleGoodsBaseInfo(odOrderDetlId);
	}

    /**
     * 주문 녹즙 스캐줄 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailScheduleListDto> getOrderDetailScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) {
    	return mallOrderDetailScheduleMapper.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
    }

    /**
     * 주문 녹즙 스캐줄 요일 리스트 조회
     * @param orderDetailScheduleUpdateRequestDto
     * @return
     */
    protected List<OrderDetailScheduleDayOfWeekListDto> getOrderDetailScheduleDayOfWeekList(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) {
    	return mallOrderDetailScheduleMapper.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto);
    }

    /**
     * 주문 녹즙 스캐줄 요일 주문 수량
     * @param orderDetailScheduleUpdateRequestDto
     * @return
     */
    protected int getOrderDetailScheduleOrderCnt(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) {
    	return mallOrderDetailScheduleMapper.getOrderDetailScheduleOrderCnt(orderDetailScheduleUpdateRequestDto);
    }

    /**
     * 주문 녹즙 스캐줄 등록
     * @param OrderDetlScheduleVo
     * @return
     */
    protected int addOrderDetlSchedule(List<OrderDetlScheduleVo> insertScheduleList) {
        return mallOrderDetailScheduleMapper.addOrderDetailSchedule(insertScheduleList);
    }

    /**
     * 주문 녹즙 스캐줄 수정
     * @param OrderDetlScheduleVo
     * @return
     */
    protected int putOrderDetlSchedule(List<OrderDetlScheduleVo> updateScheduleList) {
        return mallOrderDetailScheduleMapper.putOrderDetlSchedule(updateScheduleList);
    }

    /**
     * 주문 녹즙 스캐줄 ERP 수정
     * @param OrderDetlScheduleVo
     * @return
     */
    protected int putErpOrderDetlSchedule(List<OrderDetlScheduleVo> updateScheduleList) {
        return mallOrderDetailScheduleMapper.putErpOrderDetlSchedule(updateScheduleList);
    }

    /**
     * 주문 녹즙 스캐줄 휴무일 확인
     * @param OrderDetlScheduleVo
     * @return
     */
    protected String getOrderDetlScheduleHolidayYn(String nowDate) {
        return mallOrderDetailScheduleMapper.getOrderDetlScheduleHolidayYn(nowDate);
    }

    /**
     * 주문 녹즙 배송 요일 변경
     * @param OrderDetlScheduleVo
     * @return
     */
    protected int addChangeOrderDetailSchedule(OrderDetlScheduleVo orderDetlScheduleVo) {
        return mallOrderDetailScheduleMapper.addChangeOrderDetailSchedule(orderDetlScheduleVo);
    }

    /**
     * 주문 녹즙 일정 건너뛰기  주문/취소 등록
     * @param OrderDetlScheduleVo
     * @return
     */
    protected int addSkipOrderDetailSchedule(OrderDetlScheduleVo orderDetlScheduleVo) {
        return mallOrderDetailScheduleMapper.addSkipOrderDetailSchedule(orderDetlScheduleVo);
    }

    /**
     * 주문 녹즙 일정 건너뛰기 수정
     * @param OrderDetlScheduleVo
     * @return
     */
    protected int putSkipOrderDetailSchedule(List<OrderDetailScheduleListDto> updateScheduleList) {
        return mallOrderDetailScheduleMapper.putSkipOrderDetailSchedule(updateScheduleList);
    }

    /**
     * 녹즙 일일배송 리스트 Request
     * @param odOrderDetlId
     * @return OrderDetailScheduleListRequestDto
     */
    protected List<OrderDetailGreenJuiceListDto> getOrderDetailGreenJuiceList(Long odOrderDetlId) {
		return mallOrderDetailScheduleMapper.getOrderDetailGreenJuiceList(odOrderDetlId);
	}

    /**
     * 주문 스케줄 배치 여부
     * @param odOrderDetlId
     * @return String
     */
    protected String getOrderDetailBatchInfo(Long odOrderDetlId) {
		return mallOrderDetailScheduleMapper.getOrderDetailBatchInfo(odOrderDetlId);
	}

    /**
     * 주문 스케줄 수량,요일 변경 정보 조호
     * @param odOrderDetlId
     * @return String
     */
    protected OrderDetailScheduleListDto getOrderDetailScheduleArrivalInfo(Long odOrderDetlId, String changeDate, Long odOrderDetlDailySchId) {
		return mallOrderDetailScheduleMapper.getOrderDetailScheduleArrivalInfo(odOrderDetlId, changeDate, odOrderDetlDailySchId);
	}

    /**
     * 주문 스케줄 seq
     * @param odOrderDetlId
     * @return int
     */
    protected int getOrderDetailDailySchSeq(Long odOrderDetlId) {
		return mallOrderDetailScheduleMapper.getOrderDetailDailySchSeq(odOrderDetlId);
	}

    /**
     * 주문 상세 PK
     * @param odOrderId, odOrderDetlDailySchId
     * @return long
     */
    protected long getOdOrderDetlId(Long odOrderDetlDailySchId) {
		return mallOrderDetailScheduleMapper.getOdOrderDetlId(odOrderDetlDailySchId);
	}

    /**
     * 주문 녹즙 ERP 전송 스캐줄 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailGreenJuiceListDto> getErpOrderDetailScheduleList(Long odOrderDetlId, int odOrderDetlDailySchSeq, Long odOrderDetlDailySchId) {
    	return mallOrderDetailScheduleMapper.getErpOrderDetailScheduleList(odOrderDetlId, odOrderDetlDailySchSeq, odOrderDetlDailySchId);
    }

    /**
     * 주문 스케줄 삭제
     * @param odOrderDetlId, odOrderDetlDailySchSeq
     * @return int
     */
    protected int delOdOrderDetlDailySch(OrderDetlScheduleVo orderDetlScheduleVo) {
		return mallOrderDetailScheduleMapper.delOdOrderDetlDailySch(orderDetlScheduleVo);
	}

    /**
     * 주문 스케줄 정보
     * @param odOrderDetlId, odOrderDetlId
     * @return int
     */
    protected OrderDetailScheduleInfoDto getOrderDetailScheduleInfo(Long odOrderDetlId, Long odOrderDetlDailySchId) {
		return mallOrderDetailScheduleMapper.getOrderDetailScheduleInfo(odOrderDetlId, odOrderDetlDailySchId);
	}

    /**
     * 주문 스케줄 배송요일 정보
     * @param odOrderDetlId, changeDate
     * @return String
     */
    protected List<String> getOrderDetailScheduleDayOfWeekInfo(Long odOrderDetlId, String changeDate) {
		return mallOrderDetailScheduleMapper.getOrderDetailScheduleDayOfWeekInfo(odOrderDetlId, changeDate);
	}

    /**
     * 주문 녹즙 스캐줄 요일 패턴 수정
     * @param odOrderDetlId, deliveryDayOfWeekList
     * @return
     */
    protected int putOrderDetlSchedulePattern(Long odOrderDetlId, String deliveryDayOfWeekList) {
        return mallOrderDetailScheduleMapper.putOrderDetlSchedulePattern(odOrderDetlId, deliveryDayOfWeekList);
    }

    /**
     * 주문 녹즙 스캐줄 시작/마지막 배송일자 확인
     * @param odOrderDetlId
     * @return
     */
    protected List<OrderDetailScheduleDateInfoDto> getOrderDetailScheduleDeliveryDt(Long odOrderDetlId) {
        return mallOrderDetailScheduleMapper.getOrderDetailScheduleDeliveryDt(odOrderDetlId);
    }

    /**
     * 주문 녹즙 스캐줄 배송 정보 확인
     * @param odOrderDetlId, deliveryDayOfWeekList
     * @return
     */
    protected OrderDetailScheduleShippingInfoDto getOrderDetailScheduleShippingInfo(Long odOrderDetlId, Long odShippingZoneId) {
        return mallOrderDetailScheduleMapper.getOrderDetailScheduleShippingInfo(odOrderDetlId, odShippingZoneId);
    }

	/**
	 * 하이톡 일일배송 주문 배달주기
	 * @param lineItem
	 * @return String
	 * @throws
	 */
    protected static String getGoodsCycleTp(OrderDetailGreenJuiceListDto lineItem) {

//		String drnkPtrn = "";
//		if(lineItem.getMonCnt() > 0) drnkPtrn += GoodsEnums.WeekCodeByGreenJuice.MON.getCodeName(); // 월
//		if(lineItem.getTueCnt() > 0) drnkPtrn += ("".equals(drnkPtrn) ? "" : "_") + GoodsEnums.WeekCodeByGreenJuice.TUE.getCodeName(); // 화
//		if(lineItem.getWedCnt() > 0) drnkPtrn += ("".equals(drnkPtrn) ? "" : "_") + GoodsEnums.WeekCodeByGreenJuice.WED.getCodeName(); // 수
//		if(lineItem.getThuCnt() > 0) drnkPtrn += ("".equals(drnkPtrn) ? "" : "_") + GoodsEnums.WeekCodeByGreenJuice.THU.getCodeName(); // 목
//		if(lineItem.getFriCnt() > 0) drnkPtrn += ("".equals(drnkPtrn) ? "" : "_") + GoodsEnums.WeekCodeByGreenJuice.FRI.getCodeName(); // 금
//		drnkPtrn += "/" + lineItem.getGoodsCycleTermTpNm(); // 배달기간

        //return drnkPtrn;

        // 디폴트 배달 고정
        String goodsCycleTp = ErpApiEnums.ErpGoodsCycleTp.FIXING.getCode();
        // 1일,4일 배달 비고정
        if(GoodsEnums.GoodsCycleTypeByGreenJuice.DAY1_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())
                || GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS4_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp()))
            goodsCycleTp = ErpApiEnums.ErpGoodsCycleTp.NON_FIXING.getCode();

        // 배달요일이면 1 아니면 0
        String mon = lineItem.getMonCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String tue = lineItem.getTueCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String wed = lineItem.getWedCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String thu = lineItem.getThuCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String fri = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        if(lineItem.getFriCnt() > 0)
            // 주6일이면 금요일 2 이외 1
            fri = GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS6_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())
                    ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.SATURDAY_DELIVERY.getCode()
                    : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode();
        // 토요일/일요일은 무조건 0
        String sun = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String sat = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();

        return lineItem.getOrderCnt()+"|"+sun+mon+tue+wed+thu+fri+sat+"|"+goodsCycleTp;
   }

    /**
     * ERP 녹즙 스캐줄 line 생성
     * @param lineItem
     * @return ErpIfCustOrdInpLineRequestDto
     * @throws
     */
    protected ErpIfCustOrdInpLineRequestDto getScheduleDailyDeliveryOrderLine(OrderDetailGreenJuiceListDto lineItem) {
        double ordAmt = 0;
        // 증정품이 아니고, 증정 마케팅식품이 아닐 경우
        if(!GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) &&
            !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd())) {
            ordAmt = lineItem.getSalePrice();
        }
        return ErpIfCustOrdInpLineRequestDto.builder()
                                    .crpCd(SourceServerTypes.HITOK.getCode()) // 녹즙: HITOK
                                    .hrdSeq(lineItem.getOrderSchStatus()+lineItem.getOdOrderId()+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(lineItem.getOrderSchStatus()+lineItem.getOdOrderId()+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목 Code
                                    .ordCnt(lineItem.getOrderCnt()) // 1회배달 수량
                                    .dlvGrp(ErpApiEnums.ErpHitokDlvGrp.DAILY_DELIVERY.getCode()) // 0 : 일배
                                    .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                    .ordAmt(ordAmt) // 판매가격
                                    .totOrdCnt(lineItem.getOrderCancelCnt()) // 총수량
                                    .shiToOrgId(lineItem.getSupplierCd()) // 납품처 ID, 상세사항은 이성권님과 박종성님 협의
                                    .schLinNo(lineItem.getOdOrderDetlDailySchSeq()) // 스케줄 라인
                                    .stoCd(lineItem.getUrStoreId()) // 가맹점 코드
                                    .drnkPtrn(getGoodsCycleTp(lineItem)) // 배달주기
                                    .orderSchStatus(lineItem.getOrderSchStatus()) // 주문 - 1, 취소 - 2
                                    .taxYn(lineItem.getTaxYn()) // 과세 여부
                                    .build();
    }

    /**
     * ERP 녹즙 스캐줄 header 생성
     * @param headerItem
     * @return HitokDeliveryOrderHeaderDto
     * @throws
     */
    protected ErpIfCustOrdInpHeaderRequestDto getScheduleDailyDeliveryOrderHeader(OrderDetailScheduleShippingInfoDto headerItem, List<ErpIfCustOrdInpLineRequestDto> lineBindList) {
        return ErpIfCustOrdInpHeaderRequestDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(lineBindList.get(0).getOrderSchStatus()+headerItem.getOdOrderId()+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(lineBindList.get(0).getOrderSchStatus()+headerItem.getOdOrderId()+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(lineBindList.get(0).getOrderSchStatus()) // 주문 - 1, 취소 - 2
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd(headerItem.getOrderHpnCd()) // 주문경로
                                    .ordDat(headerItem.getCreateDt()) // 주문일시(YYYYMMDDHHMISS)
                                    .ordNam(headerItem.getBuyerNm()) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 핸드폰번호
                                    .ordEml(headerItem.getBuyerMail()) // 주문자 이메일
                                    .dlvNam(headerItem.getRecvNm()) // 수령자명
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvAdr(headerItem.getRecvAddr()) // 수령자 주소 전체
                                    .dlvAdr1(headerItem.getRecvAddr1()) // 수령자 주소 앞
                                    .dlvAdr2(headerItem.getRecvAddr2()) // 수령자 주소 뒤
                                    .bldNo(headerItem.getRecvBldNo()) // 건물번호
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                    .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * @Desc ( ERP 주문|취소 조회 API ) 주문정보 ERP API 에서 주문 코드로 ERP 녹즙 정보 조회
     *
     * @param odOrderId
     *
     * @return List<ErpIfCustordSearchResponseDto> : ERP 연동 API 를 통해 조회된 녹즙 주문 목록
     */
    protected BaseApiResponseVo getErpCustordApiList(String odid, String hdrType) {

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("ordNum", odid);
        parameterMap.put("hdrTyp", hdrType);
        parameterMap.put("srcSvr", SourceServerTypes.HITOK.getCode());

        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, ErpApiEnums.ErpInterfaceId.CUST_ORDER_SEARCH_INTERFACE_ID.getCode());

        return baseApiResponseVo;

    }

    /**
     * @Desc ( ERP 주문|취소 조회 API ) 주문정보 ERP API 에서 주문 코드로 ERP 녹즙 정보 조회 완료
     *
     * @param erpIfCustordConditionRequestDto
     *
     * @return baseApiResponseVo
     */
	protected BaseApiResponseVo putIfDlvFlagByErp(List<ErpIfCustordRequestDto> erpIfCustordRequestDto){

	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(erpIfCustordRequestDto, ErpApiEnums.ErpInterfaceId.CUST_ORDER_FLAG_INTERFACE_ID.getCode());

        return baseApiResponseVo;
	}

    /**
     * @Desc ( ERP 주문|취소 조회 API ) 주문정보 ERP API 에서 주문 코드로 ERP 녹즙 정보 입력
     *
     * @param erpIfCustordConditionRequestDto
     *
     * @return baseApiResponseVo
     */
	protected BaseApiResponseVo addIfDlvFlagByErp(ErpIfCustOrdInpRequestDto erpIfCustOrdInpRequestDto){

	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.post(erpIfCustOrdInpRequestDto, ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode());

        return baseApiResponseVo;
	}

    /**
     * @Desc 주문 녹즙 스캐줄 주문배송지 PK 수정
     * @param odShippingZoneId
     * @param odOrderDetlDailyId
     * @param changeDate
     * @return
     */
    protected int putOrderDetlScheduleOdShippingZoneId(Long odShippingZoneId, Long odOrderDetlDailyId, String changeDate) {
        return mallOrderDetailScheduleMapper.putOrderDetlScheduleOdShippingZoneId(odShippingZoneId, odOrderDetlDailyId, changeDate);
    }

    /**
     * 녹즙스케쥴 API 취소 전송 여부 업데이트
     * @param cancelGreenJuiceList
     * @return
     */
    protected int putOrderDetlScheduleApiCancelSendYn(List<OrderDetailGreenJuiceListDto> cancelGreenJuiceList) {
        return mallOrderDetailScheduleMapper.putOrderDetlScheduleApiCancelSendYn(cancelGreenJuiceList);
    }

    /**
     * @Desc 일일배송 도착일 변경 가능여부
     * @param orderDetailScheduleListRequestDto
     * @param delvDate
     * @return
     */
    protected boolean isChangeDeliveryDate(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto, String delvDate){
        boolean isChangeDeliveryDate = false;

        try {
            // 도착일변경 가능일
            String deliverableDate = "";

            // 마감시간전
            if("N".equals(orderDetailScheduleListRequestDto.getCutoffTimeYn())){
                deliverableDate = DateUtil.addDays(DateUtil.getCurrentDate(),
                        OrderScheduleEnums.ScheduleChangePossibleDate.findByCode(orderDetailScheduleListRequestDto.getGoodsDailyTp()).getChangePossibleAddDate(), "yyyy-MM-dd");

                // 마감시간 후
            }else{
                deliverableDate = DateUtil.addDays(DateUtil.getCurrentDate(),
                        (OrderScheduleEnums.ScheduleChangePossibleDate.findByCode(orderDetailScheduleListRequestDto.getGoodsDailyTp()).getChangePossibleAddDate()+1), "yyyy-MM-dd");
            }

            isChangeDeliveryDate = delvDate.compareTo(deliverableDate) >= 0 ? true : false;

        }catch(Exception e){
            e.printStackTrace();
        }

        return isChangeDeliveryDate;
    }

    /**
     * 주문 녹즙 ERP 전송 스캐줄 리스트 조회 By 취소 수정 리스트
     * @param odOrderDetlId
     * @param updateScheduleList
     * @return
     */
    protected List<OrderDetailGreenJuiceListDto> getErpOrderDetailScheduleListByCancelUpdateList(long odOrderDetlId, List<OrderDetlScheduleVo> updateScheduleList) {
        return orderDetailScheduleMapper.getErpOrderDetailScheduleListByCancelUpdateList(odOrderDetlId, updateScheduleList);
    }

    /**
     * 주문 녹즙 ERP 전송 스캐줄 리스트 조회 By 등록 리스트
     * @param odOrderDetlId
     * @param insertScheduleList
     * @return
     */protected List<OrderDetailGreenJuiceListDto> getErpOrderDetailScheduleListByInsertList(long odOrderDetlId, List<OrderDetailScheduleListDto> insertScheduleList) {
        return orderDetailScheduleMapper.getErpOrderDetailScheduleListByInsertList(odOrderDetlId, insertScheduleList);
    }
}
