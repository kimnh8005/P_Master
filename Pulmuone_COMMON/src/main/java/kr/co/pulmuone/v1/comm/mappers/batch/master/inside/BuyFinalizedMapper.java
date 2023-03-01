package kr.co.pulmuone.v1.comm.mappers.batch.master.inside;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.batch.order.inside.dto.vo.OrderStatusInfoVo;

/**
 * <PRE>
 * Forbiz Korea
 * 구매확정배치 Mapper
 * </PRE>
 */

@Mapper
public interface BuyFinalizedMapper {

	/**
	 * @Desc 자동 구매확정 설정기간 조회
	 * @param buyFinalizedDay
	 * @return int
	 */
	int getBuyFinalizedDay(@Param("buyFinalizedDayKey") String buyFinalizedDayKey);

	/**
	 * @Desc 구매확정대상 리스트 조회
	 * @param orderStatusCd(변경전 주문상태:배송완료), buyFinalizedDay(자동 구매확정 설정기간)
	 * @return List<OrderStatusInfoVo>
	 */
	List<OrderStatusInfoVo> getBuyFinalizedList(@Param("orderStatusCd") String orderStatusCd, @Param("buyFinalizedDay") int buyFinalizedDay);

	/**
	 * @Desc 구매확정 개별저장 (트랜잭션 단위) 배송완료 -> 구매확정
	 * @param orderStatusInfoVo
	 * @return int
	 */
	int putBuyFinalizedInfo(OrderStatusInfoVo orderStatusInfoVo);
}