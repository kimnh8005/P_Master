package kr.co.pulmuone.v1.comm.mapper.order.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

/**
* <PRE>
* Forbiz Korea
* 주문 스케줄 Mapper
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 02. 26.        석세동          	  최초작성
* =======================================================================
* </PRE>
*/

@Mapper
public interface MallOrderDetailScheduleMapper {

	/**
     * @Desc 주문 스케줄 리스트 Request 정보 조회(주문번호, 주문상세  상품 PK, 일일상품 유형)
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleListRequestDto
     */
	OrderDetailScheduleListRequestDto getOrderScheduleRequestInfo(@Param("odOrderDetlId") Long odOrderDetlId);

	/**
     * @Desc 주문 스케줄 리스트 상단 정보
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleGoodsDto
     */
	OrderDetailScheduleGoodsDto getOrderScheduleGoodsInfo(@Param("odOrderDetlId") Long odOrderDetlId);

	/**
     * @Desc 주문 스케줄 리스트 내맘대로 상단 정보
     * @param odOrderId(주문 PK)
     * @return OrderDetailScheduleGoodsDto
     */
	OrderDetailScheduleGoodsDto getOrderScheduleSelectGoodsInfo(@Param("odOrderId") Long odOrderId);

	/**
     * @Desc 주문 스케줄 리스트 상단 정보(베이비밀, 잇슬림)
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleGoodsDto
     */
	OrderDetailScheduleGoodsDto getOrderScheduleGoodsBaseInfo(@Param("odOrderDetlId") Long odOrderDetlId);

	/**
     * @Desc 주문 스케줄 리스트
     * @param orderDetailScheduleListRequestDto
     * @return OrderDetailScheduleListDto
     */
	List<OrderDetailScheduleListDto> getOrderDetailScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto);

	/**
     * @Desc 주문 스케줄 요일 리스트
     * @param orderDetailScheduleUpdateRequestDto
     * @return OrderDetailScheduleListDto
     */
	List<OrderDetailScheduleDayOfWeekListDto> getOrderDetailScheduleDayOfWeekList(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto);

	/**
     * @Desc 주문 스케줄 요일 리스트
     * @param orderDetailScheduleUpdateRequestDto
     * @return
     */
	int getOrderDetailScheduleOrderCnt(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto);


	/**
     * @Desc 주문 스케줄 등록
     * @param OrderDetlScheduleVo
     * @return
     */
	int addOrderDetailSchedule(List<OrderDetlScheduleVo> insertScheduleList);

	/**
     * @Desc 주문 스케줄 변경
     * @param OrderDetlScheduleVo
     * @return
     */
	int putOrderDetlSchedule(List<OrderDetlScheduleVo> updateScheduleList);

	/**
     * @Desc 주문 스케줄 ERP 변경
     * @param OrderDetlScheduleVo
     * @return
     */
	int putErpOrderDetlSchedule(List<OrderDetlScheduleVo> updateScheduleList);

	/**
     * @Desc 주문 스케줄 휴무일 확인
     * @param nowDate(날짜)
     * @return
     */
	String getOrderDetlScheduleHolidayYn(@Param("nowDate") String nowDate);

	/**
     * @Desc 주문 스케줄 배송 요일 변경
     * @param OrderDetlScheduleVo
     * @return
     */
	int addChangeOrderDetailSchedule(OrderDetlScheduleVo orderDetlScheduleVo);

	/**
     * @Desc 주문 녹즙 일정 건너뛰기 주문/취소 등록
     * @param OrderDetlScheduleVo
     * @return
     */
	int addSkipOrderDetailSchedule(OrderDetlScheduleVo orderDetlScheduleVo);

	/**
     * @Desc 주문 녹즙 일정 건너뛰기 수정
     * @param OrderDetlScheduleVo
     * @return
     */
	int putSkipOrderDetailSchedule(List<OrderDetailScheduleListDto> updateScheduleList);

	/**
     * @Desc 주문 스케줄 리스트
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleListDto
     */
	List<OrderDetailGreenJuiceListDto> getOrderDetailGreenJuiceList(@Param("odOrderDetlId") Long odOrderDetlId);

    /**
     * 주문 스케줄 배치 여부
     * @param odOrderDetlId
     * @return String
     */
	String getOrderDetailBatchInfo(@Param("odOrderDetlId") Long odOrderDetlId);

    /**
     * 주문 스케줄  요일,수량 변경 조회
     * @param odOrderDetlId
     * @return String
     */
	OrderDetailScheduleListDto getOrderDetailScheduleArrivalInfo(@Param("odOrderDetlId") Long odOrderDetlId, @Param("changeDate") String changeDate, @Param("odOrderDetlDailySchId") Long odOrderDetlDailySchId);

    /**
     * 주문 스케줄 seq
     * @param odOrderDetlId
     * @return int
     */
	int getOrderDetailDailySchSeq(@Param("odOrderDetlId") Long odOrderDetlId);

	/**
     * @Desc 주문 녹즙 ERP 전송 스캐줄 리스트 조회
     * @param odOrderDetlId, odOrderDetlDailySchSeq, orderSchStatus
     * @return OrderDetailScheduleListDto
     */
	List<OrderDetailGreenJuiceListDto> getErpOrderDetailScheduleList(@Param("odOrderDetlId") Long odOrderDetlId, @Param("odOrderDetlDailySchSeq") int odOrderDetlDailySchSeq, @Param("odOrderDetlDailySchId") Long odOrderDetlDailySchId);

    /**
     * 주문 스케줄 삭제
     * @param odOrderDetlId, odOrderDetlDailySchSeq
     * @return int
     */
	int delOdOrderDetlDailySch(OrderDetlScheduleVo orderDetlScheduleVo);

	/**
     * 주문 스케줄  정보 조회
     * @param odOrderDetlId
     * @return String
     */
	OrderDetailScheduleInfoDto getOrderDetailScheduleInfo(@Param("odOrderDetlId") Long odOrderDetlId, @Param("odOrderDetlDailySchId") Long odOrderDetlDailySchId);

    /**
     * 주문 상세 pk
     * @param odOrderDetlDailySchId
     * @return long
     */
	long getOdOrderDetlId(@Param("odOrderDetlDailySchId") Long odOrderDetlDailySchId);

    /**
     * 주문 상세 스캐줄 배송요일 확인
     * @param odOrderDetlDailySchId, changeDate
     * @return String
     */
	List<String> getOrderDetailScheduleDayOfWeekInfo(@Param("odOrderDetlId") Long odOrderDetlId, @Param("changeDate") String changeDate);

	/**
     * @Desc 주문 녹즙 스캐줄 요일 패턴 수정
     * @param odOrderDetlId, deliveryDayOfWeekList
     * @return
     */
	int putOrderDetlSchedulePattern(@Param("odOrderDetlId") Long odOrderDetlId, @Param("deliveryDayOfWeekList") String deliveryDayOfWeekList);

	/**
	 * @Desc 주문 녹즙 스캐줄 주문배송지 PK 수정
	 * @param odShippingZoneId
	 * @param odOrderDetlDailyId
	 * @param changeDate
	 * @return
	 */
	int putOrderDetlScheduleOdShippingZoneId(@Param("odShippingZoneId") Long odShippingZoneId, @Param("odOrderDetlDailyId") Long odOrderDetlDailyId, @Param("changeDate") String changeDate);

	/**
	 * 녹즙스케쥴 API 취소 전송 여부 업데이트
	 * @param cancelGreenJuiceList
	 * @return
	 */
	int putOrderDetlScheduleApiCancelSendYn(@Param("cancelGreenJuiceList") List<OrderDetailGreenJuiceListDto> cancelGreenJuiceList);

	/**
     * @Desc 주문 녹즙 스캐줄 시작/마지막 배송일자 확인
     * @param odOrderDetlId
     * @return
     */
	List<OrderDetailScheduleDateInfoDto> getOrderDetailScheduleDeliveryDt(@Param("odOrderDetlId") Long odOrderDetlId);

	/**
     * @Desc 주문 녹즙 스캐줄 배송 정보 확인
     * @param odOrderDetlId, odShippingZoneId
     * @return
     */
	OrderDetailScheduleShippingInfoDto getOrderDetailScheduleShippingInfo(@Param("odOrderDetlId") Long odOrderDetlId, @Param("odShippingZoneId") Long odShippingZoneId);

}