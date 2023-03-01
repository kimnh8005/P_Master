package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.PutOrderPaymentCompleteResDto;

public interface OrderProcessBiz {

	PutOrderPaymentCompleteResDto putOrderPaymentComplete(PgApprovalOrderDataDto orderData, PgApprovalOrderPaymentDataDto paymentData) throws Exception;
}
