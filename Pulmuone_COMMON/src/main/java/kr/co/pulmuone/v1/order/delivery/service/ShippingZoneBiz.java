package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;

public interface ShippingZoneBiz {
    /**
     * 배송지 정보 수정
     * @param orderShippingZoneVo
     * @return
     */
    ApiResult<?> putShippingZone(OrderShippingZoneVo orderShippingZoneVo) throws Exception;

    /**
     * 주문 배송지 조회
     * @param odShippingZoneId
     * @return OrderShippingZoneVo
     */
	OrderShippingZoneVo getOrderShippingZone(Long odShippingZoneId) throws Exception;
}
