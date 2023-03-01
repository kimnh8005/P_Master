package kr.co.pulmuone.v1.comm.mapper.order.delivery;

import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 배송 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderDeliveryMapper {

    /**
     * 송장번호 등록/업데이트
     * @param orderTrackingNumberVo
     * @return
     */
    int addOrderDetailTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo);

}
