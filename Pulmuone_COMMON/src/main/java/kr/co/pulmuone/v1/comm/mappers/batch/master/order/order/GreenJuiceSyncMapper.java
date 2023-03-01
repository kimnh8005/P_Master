package kr.co.pulmuone.v1.comm.mappers.batch.master.order.order;

import kr.co.pulmuone.v1.order.schedule.dto.vo.OrderDetlScheduleVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Mapper
 * </PRE>
 */

@Mapper
public interface GreenJuiceSyncMapper {

	/**
	 * 주문 상세 번호 조회
	 * @param orderDetlScheduleVo
	 * @return
	 */
	long getOdOrderDetlByOrderDetlScheduleVo(OrderDetlScheduleVo orderDetlScheduleVo);
}
