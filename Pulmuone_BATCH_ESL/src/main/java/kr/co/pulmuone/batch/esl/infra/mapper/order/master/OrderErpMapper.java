package kr.co.pulmuone.batch.esl.infra.mapper.order.master;

import kr.co.pulmuone.batch.esl.domain.model.order.order.dto.vo.EatsslimOrderListVo;
import kr.co.pulmuone.batch.esl.domain.model.order.order.dto.vo.OrderDetlHistVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Mapper
 * </PRE>
 */

@Mapper
public interface OrderErpMapper {

    /**
     * 잇슬림 일일배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<EatsslimOrderListVo>
     * @throws
     */
    List<EatsslimOrderListVo> selectEatsslimOrderList(@Param("urWarehouseId") String[] urWarehouseId, @Param("orderStatusCd") String orderStatusCd);

    /**
     * 잇슬림 택배배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<EatsslimOrderListVo>
     * @throws
     */
    List<EatsslimOrderListVo> selectEatsslimNormalDeliveryOrderList(@Param("urWarehouseId") String[] urWarehouseId, @Param("orderStatusCd") String orderStatusCd);

    /**
     * 성공 주문 배치완료 업데이트 (매출 O)
     * @param odOrderDetlId
     * @return void
     * @throws
     */
    void putOrderBatchCompleteUpdate(@Param("orderStatusCd") String orderStatusCd, @Param("odOrderDetlId") String odOrderDetlId);

    /**
     * 주문 상태 이력 등록
     * @param orderDetlHistVo
     * @return
     * @throws BaseException
     */
    int addOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo);
}
