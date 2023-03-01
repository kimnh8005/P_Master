package kr.co.pulmuone.v1.comm.mapper.order.delivery;

import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
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
public interface ShippingZoneMapper {

    /**
     * 배송지정보 수정
     * @param orderShippingZoneVo
     * @return
     */
    int putShippingZone(OrderShippingZoneVo orderShippingZoneVo);

    /**
     * 배송지정보 조회
     * @param odShippingZoneId
     * @return
     */
    OrderShippingZoneVo getOrderShippingZone(Long odShippingZoneId);
}
