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

@Component("systemMonitoringBosLoginJob")
@Slf4j
public class SystemMonitoringBosLoginJob implements BaseJob {
    /*
    배치명 : 모니터링 - BOS 로그인
    배치번호 : 132
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

        Boolean detect = systemMonitoringBiz.getBosLogin(SystemMonitoringRequestDto.builder()
                .minute(monitoringMinute)
                .count(monitoringCount)
                .build());

        if (detect) {
            // Send Slack
            Long batchNo = SystemEnums.MonitoringMessage.BOS_LOGIN.getBatchNo();
            String content = SystemEnums.MonitoringMessage.BOS_LOGIN.getMessage();
            slackService.notify(content);

            // Send SMS
            batchSendTemplateService.sendSmsBatch(batchNo, content);
        }
    }

}