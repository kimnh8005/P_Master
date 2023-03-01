package kr.co.pulmuone.batch.job.application.order.email;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hsqldb.error.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("orderDailyGreenJuiceEndJob")
@Slf4j
@RequiredArgsConstructor
public class OrderDailyGreenJuiceEndJob implements BaseJob {

    @Autowired
    private OrderEmailBiz orderEmailBiz;

    @Autowired
    private OrderEmailSendBiz orderEmailSendBiz;

    @Override
    public void run(String[] params) {

    	log.info("====== 녹즙 일일배송 종료 SMS 발송 -> 마지막 도착예정일 D-4 09:00 ======");

    	try {
    		// 정기배송 만료 SMS 발송 대상 조회
    		List<Long> targetList = orderEmailBiz.getTargetOrderDailyGreenJuiceEnd();

    		if(CollectionUtils.isNotEmpty(targetList)) {
    			for(Long odOrderDetlId : targetList) {
    				try {
    					// SMS 발송
    					OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderDailyGreenJuiceEndInfoForEmail(odOrderDetlId);
    					if(ObjectUtils.isNotEmpty(orderInfoForEmailResultDto)){
							orderEmailSendBiz.orderDailyGreenJuiceEnd(orderInfoForEmailResultDto);
						}

    				} catch (Exception e) {
    					log.error("ERROR ====== 녹즙 일일배송 종료 SMS 발송 오류 odRegularResultId ::", odOrderDetlId);
    					log.error(e.getMessage(), e);
    				}
    			}
    		}

		} catch (Exception e) {
			log.error("ERROR ====== 녹즙 일일배송 종료 SMS 발송 오류 orderEmailBiz.getTargetOrderDailyGreenJuiceEnd ERROR =======");
			log.error(e.getMessage(), e);
		}

    }
}
