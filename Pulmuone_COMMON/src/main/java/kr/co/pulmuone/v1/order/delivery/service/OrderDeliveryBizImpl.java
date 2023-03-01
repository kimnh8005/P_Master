package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.delivery.dto.OrderTrackingNumberDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 배송 관련 Implements
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
@Slf4j
@Service
public class OrderDeliveryBizImpl implements OrderDeliveryBiz {
    @Autowired
    private OrderDeliveryService orderDeliveryService;

    /**
     * 송장번호 등록/업데이트
     * @param orderTrackingNumberDto
     * @return
     */
    @Override
    public ApiResult<?> addOrderDetailTrackingNumber(OrderTrackingNumberDto orderTrackingNumberDto) {
        int cnt = 0;
        for(OrderTrackingNumberVo orderTrackingNumberVo:orderTrackingNumberDto.getOrderTrackingNumberList()){
            cnt = orderDeliveryService.addOrderDetailTrackingNumber(orderTrackingNumberVo);
        }

        return ApiResult.success();
    }
}
