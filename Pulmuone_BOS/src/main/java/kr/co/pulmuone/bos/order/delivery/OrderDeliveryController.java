package kr.co.pulmuone.bos.order.delivery;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.delivery.dto.OrderTrackingNumberDto;
import kr.co.pulmuone.v1.order.delivery.service.OrderDeliveryBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 배송 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.    세        이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderDeliveryController {

    @Autowired
    private OrderDeliveryBiz orderDeliveryBiz;

    /**
     * 주문 상세 조회
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "주문 상세 송장번호 등록")
    @PostMapping(value = "/admin/order/delivery/addOrderDetailTrackingNumber")
    public ApiResult<?> addOrderDetailTrackingNumber(OrderTrackingNumberDto orderTrackingNumberDto) throws Exception {
        return orderDeliveryBiz.addOrderDetailTrackingNumber(orderTrackingNumberDto);
    }
}
