package kr.co.pulmuone.v1.order.order.service;


import kr.co.pulmuone.v1.comm.mapper.order.order.OrderDetailStatusMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 상태 관련 Service
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
public class OrderDetailStatusService {

    private final OrderDetailStatusMapper orderDetailStatusMapper;


    /**
     * 주문 상세 상태 값 업데이트
     * @param odOrderDetlId
     * @param orderStatusCd
     * @return
     */
    protected int putOrderDetailStatusChange(long odOrderDetlId, String orderStatusCd) {
        return orderDetailStatusMapper.putOrderDetailStatusChange(odOrderDetlId, orderStatusCd);
    }

}
