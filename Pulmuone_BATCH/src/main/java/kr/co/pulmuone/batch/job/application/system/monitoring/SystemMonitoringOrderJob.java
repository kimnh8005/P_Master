package kr.co.pulmuone.batch.job.application.system.monitoring;

import kr.co.pulmuone.batch.domain.service.send.template.BatchSendTemplateService;
import kr.co.pulmuone.batch.domain.service.system.SlackService;
import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.system.monitoring.dto.SystemMonitoringRequestDto;
import kr.co.pulmuone.v1.system.monitoring.service.SystemMonitoringBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("systemMonitoringOrderJob")
@Slf4j
public class SystemMonitoringOrderJob implements BaseJob {
    /*
    배치명 : 모니터링 - 주문 발생 여부
    배치번호 : 127
     */

    @Autowired
    private SystemMonitoringBiz systemMonitoringBiz;

    @Autowired
    private SlackService slackService;

    @Autowired
    private BatchSendTemplateService batchSendTemplateService;

    @Override
    public void run(String[] params) throws BaseException {
        if (params.length < 4) throw new BaseException(SystemEnums.MonitoringError.PARAM_NOT_ENOUGH);
        String monitoringMinute = params[2];
        String monitoringCount = params[3];

        Boolean detect = systemMonitoringBiz.getOrder(SystemMonitoringRequestDto.builder()
                .minute(monitoringMinute)
                .count(monitoringCount)
                .build());

        if (detect) {
            // Send Slack
            Long batchNo = SystemEnums.MonitoringMessage.ORDER.getBatchNo();
            String content = SystemEnums.MonitoringMessage.ORDER.getMessage();
            slackService.notify(content);

            // Send SMS
            batchSendTemplateService.sendSmsBatch(batchNo, content);
        }
    }

}