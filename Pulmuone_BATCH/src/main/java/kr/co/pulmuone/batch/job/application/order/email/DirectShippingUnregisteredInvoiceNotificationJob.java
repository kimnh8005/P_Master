package kr.co.pulmuone.batch.job.application.order.email;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("directShippingUnregisteredInvoiceNotificationJob")
@Slf4j
@RequiredArgsConstructor
public class DirectShippingUnregisteredInvoiceNotificationJob implements BaseJob {

    @Autowired
    private OrderEmailSendBiz orderEmailSendBiz;

    @Override
    public void run(String[] params) {
        try {
            orderEmailSendBiz.directShippingUnregisteredInvoiceNotification();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ERROR====== 직접배송 미등록 송장 알림 오류 orderEmailSendBiz.directShippingUnregisteredInvoiceNotification ERROR =======");
            log.error(e.getMessage(), e);
        }
    }
}
