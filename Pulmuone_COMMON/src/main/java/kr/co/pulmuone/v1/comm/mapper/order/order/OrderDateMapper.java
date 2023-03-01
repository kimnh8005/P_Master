package kr.co.pulmuone.v1.comm.mapper.order.order;


import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 날짜 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderDateMapper {

    /**
     * 주문 상태 일자 업데이트
     * @param orderDtVo
     * @return
     */
    int putOrderDt(OrderDtVo orderDtVo);

    /**
     * 주문 상세 상태 일자 업데이트
     * @param orderDetlVo
     * @return
     */
    int putOrderDetailDt(OrderDetlVo orderDetlVo);

    /**
     * 클레임 상세 상태 일자 업데이트
     * @param claimDetlDtVo
     * @return
     */
    int putClaimDetailDt(ClaimDetlDtVo claimDetlDtVo);
}
