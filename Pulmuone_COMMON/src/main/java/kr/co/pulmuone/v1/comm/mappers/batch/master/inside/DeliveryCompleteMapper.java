package kr.co.pulmuone.v1.comm.mappers.batch.master.inside;

import java.util.List;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.batch.order.inside.dto.vo.OrderStatusInfoVo;
import kr.co.pulmuone.v1.batch.order.inside.dto.vo.TrackingShippingCompVo;

/**
 * <PRE>
 * Forbiz Korea
 * 배송완료배치 Mapper
 * </PRE>
 */

@Mapper
public interface DeliveryCompleteMapper {

	/**
	 * @Desc 자동 배송완료 설정기간 조회
	 * @param deliveryCompleteDay
	 * @return int
	 */
	int getDeliveryCompleteDay(@Param("deliveryCompleteDayKey") String deliveryCompleteDayKey);

	/**
	 * @Desc 배송완료 대상 리스트 조회
	 * @param orderStatusCd(변경전 주문상태:배송중), deliveryCompleteDay(자동 배송완료 설정기간)
	 * @return List<OrderStatusInfoVo>
	 */
	List<OrderStatusInfoVo> getDeliveryCompleteList(@Param("orderStatusCd") String orderStatusCd, @Param("deliveryCompleteDay") Integer deliveryCompleteDay);

	/**
	 * 매장배송 배송완료 대상조회
	 * @param inOrderStatusCd
	 * @return
	 * @throws BaseException
	 */
	List<OrderStatusInfoVo> gutStoreDeliveryCompleteList(@Param("inOrderStatusCd") String inOrderStatusCd) throws BaseException;

	/**
	 * @Desc 배송완료 트래킹 대상 리스트(CJ/롯데) 조회
	 * @param orderStatusCd(변경전 주문상태:배송중)
	 * @return List<OrderStatusInfoVo>
	 */
	List<OrderStatusInfoVo> getDeliveryCompleteTrackingList(@Param("orderStatusCd") String orderStatusCd);

	/**
	 * @Desc 트래킹 택배사(CJ/롯데) 정보 조회
	 * @param
	 * @return List<TrackingShippingCompVo>
	 */
	List<TrackingShippingCompVo> getTrackingListShippingCompList();

	/**
	 * @Desc 배송완료 개별저장 (트랜잭션 단위) 배송중 -> 배송완료
	 * @param orderStatusInfoVo
	 * @return int
	 */
	int putDeliveryCompleteInfo(OrderStatusInfoVo orderStatusInfoVo);

	/**
	 * 하이톡(녹즙) 스케줄 배송완료 저장
	 * @return int
	 * @throws BaseException
	 */
	int putHitokScheduleDeliveryComplete() throws BaseException;

	/**
	 * 잇슬림 일일배송 배송중 대상조회
	 * @param putOrderStatusCd
	 * @param createId
	 * @param urWarehouseId
	 * @param inOrderStatusCd
	 * @return
	 * @throws BaseException
	 */
	List<OrderStatusInfoVo> getEatsslimDailyDeliveryIngList(@Param("urWarehouseId") String urWarehouseId, @Param("inOrderStatusCd") String inOrderStatusCd) throws BaseException;

	/**
	 * 잇슬림 일일배송 배송중 배치
	 * @param orderStatus
	 * @param batchCreateUserId
	 * @return
	 * @throws BaseException
	 */
	int putEatsslimDailyDeliveryIng(OrderStatusInfoVo orderStatusInfoVo) throws BaseException;
}