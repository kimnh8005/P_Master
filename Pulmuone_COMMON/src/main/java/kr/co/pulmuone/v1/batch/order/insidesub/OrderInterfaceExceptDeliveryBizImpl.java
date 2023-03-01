package kr.co.pulmuone.v1.batch.order.insidesub;

import kr.co.pulmuone.v1.batch.order.insidesub.dto.vo.InterfaceExceptOrderVo;
import kr.co.pulmuone.v1.batch.order.order.OrderErpService;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 I/F 외 배송준비중 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderInterfaceExceptDeliveryBizImpl implements OrderInterfaceExceptDeliveryBiz {

	private final OrderInterfaceExceptDeliveryService orderInterfaceExceptDeliveryService;

	private final OrderErpService orderErpService;

	/**
	 * 주문 배송준비중 업데이트
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void orderDeliveryPreparingUpdate() throws BaseException {

		String[] orderChangeTp = { ErpApiEnums.OrderChangeTp.ORDER_CHANGE.getCode() }; // 주문 변경여부 유형: 배송준비중 변경
		String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

		// I/F 외 주문 조회
		List<InterfaceExceptOrderVo> listVo = orderInterfaceExceptDeliveryService.selectInterfaceExceptOrder(orderChangeTp, orderStatusCd);

		for(InterfaceExceptOrderVo vo : listVo) {
			String odOrderDetlId = vo.getOdOrderDetlId();

			log.info("===========================odOrderDetlId: "+odOrderDetlId);

			// 배송준비중 업데이트
			orderErpService.putOrderBatchCompleteUpdateNotSales(odOrderDetlId, "");
		}

	}

}