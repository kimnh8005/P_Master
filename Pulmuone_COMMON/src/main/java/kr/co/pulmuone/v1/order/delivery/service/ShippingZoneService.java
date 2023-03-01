package kr.co.pulmuone.v1.order.delivery.service;


import kr.co.pulmuone.v1.comm.mapper.order.delivery.OrderDeliveryMapper;
import kr.co.pulmuone.v1.comm.mapper.order.delivery.ShippingZoneMapper;
import kr.co.pulmuone.v1.comm.mapper.order.registration.OrderRegistrationMapper;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Service
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
@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingZoneService {

    private final ShippingZoneMapper shippingZoneMapper;

    private final OrderRegistrationMapper orderRegistrationMapper;


    /**
     * 주문 배송지 수정
     * OD_SHIPPING_ZONE
     * @param orderShippingZoneVo
     * @return
     */
    protected int putShippingZone(OrderShippingZoneVo orderShippingZoneVo) {
        return shippingZoneMapper.putShippingZone(orderShippingZoneVo);
    }

    /**
     * 주문 배송지 이력 등록
     * OD_SHIPPING_ZONE_HIST
     * @param orderShippingZoneHistVo
     * @return
     */
    protected int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo) {
        return orderRegistrationMapper.addShippingZoneHist(orderShippingZoneHistVo);
    }

    /**
     * 주문 배송지 조회
     * OD_SHIPPING_ZONE
     * @param odShippingZoneId
     * @return OrderShippingZoneVo
     */
    protected OrderShippingZoneVo getOrderShippingZone(Long odShippingZoneId) {
        return shippingZoneMapper.getOrderShippingZone(odShippingZoneId);
    }

}
