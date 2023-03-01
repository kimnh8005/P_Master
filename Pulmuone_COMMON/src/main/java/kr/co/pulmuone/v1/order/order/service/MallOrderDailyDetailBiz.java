package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneHistListDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송주문상세 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 16.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

public interface MallOrderDailyDetailBiz {

	/** 일일배송 배송지 변경 내역 조회 */
	public ApiResult<?> getOrderDailyShippingZoneHist(long odOrderId, long odShippingZoneId, long odOrderDetlId);

	/** 일일배송 배송지 정보 수정 */
	public ApiResult<?> putOrderDailyShippingZone(MallOrderDailyShippingZoneRequestDto mallOrderDailyShippingZoneRequestDto) throws Exception;

	/** 주문상세PK로 일일상품 정보 조회 */
	MallOrderDetailGoodsDto getOrderDailyDetailByOdOrderDetlId(Long odOrderDetlId);

	/** 일일배송 배송지 리스트 조회 */
	List<MallOrderDailyShippingZoneHistListDto> getOrderDailyShippingZoneList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto);

	/** 주문배송지 seq 조회 */
	long getOrderShippingZoneSeq();

	/** 일일상품 배송지 변경이력 count 조회 */
	int getOrderDailyShippingZoneChangeCount(String promotionType, Long odOrderDetlId);

	/** 일일상품 배송지 변경시 스케쥴 수정 */
	ApiResult<?> putOrderDailyGreenjuiceShippingZone(MallOrderDetailGoodsDto orderDetailGoodsDto, List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList, Long odShippingZoneId, String deliveryDt);

	/** 주문PK로 같은 일일상품유형의 주문상세PK 조회 */
	List<MallOrderDetailGoodsDto> getOrderDailyDetailByOdOrderId(Long odOrderId, String goodsDailyTp);

	/** 주문상세PK로 주문건내 같은 일일상품유형의 상품PK 조회 */
	List<Long> getOrderGoodsIdListByOdOrderDetlId(@Param("odOrderDetlId")Long odOrderDetlId);

	/** 일일배송상품 도착일 정보 리스트 조회 */
	List<OrderDetailScheduleListDto> getOrderDetailScheduleArrivalDateList(String deliveryDt, MallOrderDetailGoodsDto orderDetailGoodsDto) throws Exception;
}