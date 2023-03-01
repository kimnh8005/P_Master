package kr.co.pulmuone.batch.job.application.order.email;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.order.email.dto.BosOrderStatusNotiDto;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("bosOrderStatusNotificationJob")
@Slf4j
@RequiredArgsConstructor
public class BosOrderStatusNotificationJob implements BaseJob {

    @Autowired
    private OrderEmailBiz orderEmailBiz;

    @Autowired
    private OrderEmailSendBiz orderEmailSendBiz;

    @Override
    public void run(String[] params) {

    	try {
    		// 메일 발송 대상 거래처 조회
    		List<BosOrderStatusNotiDto> targetClientList = orderEmailBiz.getTargetBosOrderStatusNotification();

    		if(CollectionUtils.isNotEmpty(targetClientList)) {
    			for(BosOrderStatusNotiDto clientDto : targetClientList) {
    				try {

    					// 거래처별 주문 상태별 건수 조회
    					OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getBosOrderStatusNotificationForEmail(clientDto.getUrClientId());
    					if(ObjectUtils.isNotEmpty(orderInfoForEmailResultDto.getBosOrderInfoForEmailVo()) && StringUtils.isNotEmpty(orderInfoForEmailResultDto.getBosOrderInfoForEmailVo().getUrClientId())){
    						orderInfoForEmailResultDto.getBosOrderInfoForEmailVo().setMail(clientDto.getCompMail());
							orderEmailSendBiz.bosOrderStatusNotification(orderInfoForEmailResultDto);
						}

    				} catch (Exception e) {
    					log.error("ERROR====== BOS 주문 상태 알림 자동메일 오류 urClientId::" +  clientDto.getUrClientId());
    					log.error(e.getMessage(), e);
    				}
    			}
    		}

		} catch (Exception e) {
			log.error("ERROR====== BOS 주문 상태 알림 자동메일 오류 orderEmailBiz.getBosOrderStatusNotificationForEmail ERROR =======");
			log.error(e.getMessage(), e);
		}

    }
}
