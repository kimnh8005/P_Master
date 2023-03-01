package kr.co.pulmuone.v1.order.schedule.service;

import java.time.LocalDate;

import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.vo.OrderDetlScheduleVo;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;

import java.util.HashMap;
import java.util.List;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문 스캐줄 관련 I/F
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 1. 20.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

public interface OrderScheduleBiz {
	/** 주문 녹즙/잇슬림/베이비밀 스캐줄 리스트 조회 */
	ApiResult<?> getOrderScheduleList(Long odOrderDetlId) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 배송일자,수량 변경 */
	ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경 */
	ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 건너뛰기 */
	ApiResult<?> putScheduleSkip(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회 */
	ApiResult<?> getOrderDeliverableScheduleList(Long parseLong) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 > 주문I/F일자, 출고예정일자, 도착예정일자 업데이트 */
	ApiResult<?> putOrderArrivalScheduledDate(long odOrderDetlId, long ilGoodsId, LocalDate deliveryDt) throws Exception;
	/** 주문 배송일자 조회 */
	ApiResult<?> getOrderDetailScheduleDayOfWeekList(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 배송일자 조회 */
	HashMap<String,List<String>> getScheduleDelvDateList(ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaDto, Long urWarehouseId) throws Exception;
	/** 주문 스케쥴 변경*/
	void changeOrderDetlDaily(OrderDetlVo orderDetlVo, Long odOrderDetlId) throws Exception;
	/** 주문상세 일일배송 배송지 정보 수정*/
	int putOrderDetlDailyZone(Long odOrderDetlId,Long odOrderId);
	/** 주문상세 일일배송 배송지 정보 등록*/
	int addOrderDetlDailyZoneByOdOrderDetlId(Long odOrderDetlId, String promotionTp, Long odOrderId);
	/** 주문상세 일일배송 배송지 정보 등록 및 수정*/
	void saveOrderDetlDailyZone(Long odOrderDetlId, String promotionTp, Long odOrderId);
	/** 주문상세 일일배송 스토어정보 수정*/
	void putOrderDetlDailyUrStoreId(long odOrderId, long odOrderDetlId, long odShippingZoneId);
	/** 주문 스케줄 배치 여부*/
	String getOrderDetailBatchInfo(Long odOrderDetlId);
	/** 일일상품 주문배송지 변경시 스케쥴 배치 Y인 경우 -> 스케쥴 취소후 다시 생성*/
	void addChangeOrderDetailSchedule(List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList, Long odShippingZoneId);
	/** 녹즙 주문 스케쥴 seq 조회*/
	int getOrderDetailDailySchSeq(Long odOrderDetlId);
	/** ( ERP 주문|취소 조회 API ) 주문정보 ERP API 에서 주문 코드로 ERP 녹즙 정보 조회*/
	BaseApiResponseVo getErpCustordApiList(String odid, String hdrType);
	/** 일일배송 배송지 수정시 ERP 스케쥴 수정*/
	//ApiResult<?> putIfDlvFlagByErp(Long odOrderDetlId, int orderDetailDailySchSeq);
	ApiResult<?> putIfDlvFlagByErp(Long odOrderDetlId, String deliveryDt);

	/**
	 * 주문정보 ERP API 에서 변경된 모든 ERP 녹즙 정보 조회
	 * @param hdrType
	 * @return
	 */
	BaseApiResponseVo getErpCustordApiAllList(String hdrType);

	/**
	 * 주문정보 ERP API 에서 변경된 모든 ERP 녹즙 정보 완료 처리
	 * @param erpIfCustordRequestDto
	 * @return
	 */
	BaseApiResponseVo putErpCustordApiComplete(List<ErpIfCustordRequestDto> erpIfCustordRequestDto);

	/**
	 * 녹즙 동기화 주문 등록 처리
	 * @param insertScheduleList
	 * @return
	 */
	int addOrderDetlSchedule(List<OrderDetlScheduleVo> insertScheduleList);

	/**
	 * 녹즙 동기화 주문 수정 처리
	 * @param updateScheduleList
	 * @return
	 */
	int putErpOrderDetlSchedule(List<OrderDetlScheduleVo> updateScheduleList);
}

