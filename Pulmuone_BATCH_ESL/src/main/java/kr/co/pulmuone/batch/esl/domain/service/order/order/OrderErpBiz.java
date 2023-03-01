package kr.co.pulmuone.batch.esl.domain.service.order.order;

import kr.co.pulmuone.batch.esl.common.enums.ErpApiEnums;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 ERP API 배치 Biz
 * </PRE>
 */

public interface OrderErpBiz {

	/**
	 * 잇슬림 주문 입력 ERP API 송신
	 * @param erpServiceType
	 * @return void
	 * @throws Exception
	 */
	void addEatsslimOrderIfInpByErp(ErpApiEnums.ErpServiceType erpServiceType) throws Exception;

}
