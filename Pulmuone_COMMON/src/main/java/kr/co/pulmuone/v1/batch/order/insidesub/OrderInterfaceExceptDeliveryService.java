package kr.co.pulmuone.v1.batch.order.insidesub;

import kr.co.pulmuone.v1.batch.order.insidesub.dto.LimitIsOrderCountSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.insidesub.dto.vo.InterfaceExceptOrderVo;
import kr.co.pulmuone.v1.batch.order.insidesub.dto.vo.OrderLimitInfoVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.insidesub.OrderInterfaceExceptDeliveryMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.insidesub.OrderIsLimitAfterCheckMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 I/F 외 배송준비중 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderInterfaceExceptDeliveryService {

    private final OrderInterfaceExceptDeliveryMapper orderInterfaceExceptDeliveryMapper;

    /**
     * I/F 외 주문 조회
     * @param orderChangeTp
     * @param orderStatusCd
     * @return List<InterfaceExceptOrderVo>
     * @throws BaseException
     */
    protected List<InterfaceExceptOrderVo> selectInterfaceExceptOrder(String[] orderChangeTp, String orderStatusCd) throws BaseException {
        return orderInterfaceExceptDeliveryMapper.selectInterfaceExceptOrder(orderChangeTp, orderStatusCd);
    }

}
