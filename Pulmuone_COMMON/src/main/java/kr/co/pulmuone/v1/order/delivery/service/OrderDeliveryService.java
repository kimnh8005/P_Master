package kr.co.pulmuone.v1.order.delivery.service;


import kr.co.pulmuone.v1.comm.mapper.order.delivery.OrderDeliveryMapper;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
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
public class OrderDeliveryService {

    private final OrderDeliveryMapper orderDeliveryMapper;

    /**
     * 송장번호 등록/업데이트
     * @param orderTrackingNumberVo
     * @return
     */
    protected int addOrderDetailTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) {
        return orderDeliveryMapper.addOrderDetailTrackingNumber(orderTrackingNumberVo);
    }




}
