package kr.co.pulmuone.batch.job.application.order.email;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("orderRegularExpiredJob")
@Slf4j
@RequiredArgsConstructor
public class OrderRegularExpiredJob implements BaseJob {

    @Autowired
    private OrderEmailBiz orderEmailBiz;

    @Autowired
    private OrderEmailSendBiz orderEmailSendBiz;

    @Override
    public void run(String[] params) {

    	log.info("======정기배송 만료 SMS 발송 -> 정기배송 마지막 회차 배송완료 직후 09:00 ======");

    	try {
    		// 정기배송 만료 SMS 발송 대상 조회
    		List<Long> targetList = orderEmailBiz.getTargetOrderRegularExpired();

    		if(CollectionUtils.isNotEmpty(targetList)) {
    			for(Long odRegularResultId : targetList) {
    				try {
    					// SMS 발송
    					OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderRegularInfoForEmail(odRegularResultId);
    					orderEmailSendBiz.orderRegularExpired(orderInfoForEmailResultDto);

    					// OD_ORDER_RESULT의 SMS_SEND_YN -> 'Y'로 업데이트
    					orderEmailBiz.putSmsSendStatusByOrderRegularExpired(odRegularResultId);

    				} catch (Exception e) {
    					log.error("ERROR ====== 정기배송 만료 SMS 발송 오류 odRegularResultId ::", odRegularResultId);
    					log.error(e.getMessage(), e);
    				}
    			}
    		}

		} catch (Exception e) {
			log.error("ERROR ====== 정기배송 만료 SMS 발송 오류 orderEmailBiz.getTargetOrderRegularExpired ERROR");
			log.error(e.getMessage(), e);
		}

    }
}
