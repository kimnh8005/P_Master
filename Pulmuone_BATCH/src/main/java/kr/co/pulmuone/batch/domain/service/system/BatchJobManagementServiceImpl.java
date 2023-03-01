package kr.co.pulmuone.batch.domain.service.system;

import kr.co.pulmuone.batch.common.util.BeanUtils;
import kr.co.pulmuone.batch.domain.model.system.BatchJob;
import kr.co.pulmuone.batch.domain.model.system.BatchJobExecution;
import kr.co.pulmuone.batch.domain.service.send.template.BatchSendTemplateService;
import kr.co.pulmuone.batch.domain.service.system.exception.NotExistsBatchNoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class BatchJobManagementServiceImpl implements BatchJobManagementService {

    private final BatchJobService batchJobService;
    private final BatchJobExecutionService batchJobExecutionService;
    private final SlackService slackService;
    private final BatchSendTemplateService batchSendTemplateService;

    @Override
    public void run(long batchNo, String userId) throws Exception {
        run(batchNo, userId, new String[0]);
    }

    @Override
    public void run(long batchNo, String userId, String[] params) throws Exception {
        BatchJob batchJob = batchJobService.getBatchJobByBatchNoAndUsable(batchNo).orElseThrow(
                NotExistsBatchNoException::new);
        BatchJobExecution batchJobExecution = startHandle(batchNo, userId);

        try {
            runBaseJob(batchJob, params);
            completeHandle(batchJobExecution);
            // sendSlackMessage(true, batchJob.getBatchName(), batchNo, null);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            if(e.getMessage() == null) {
                errMsg = e.toString();
            }
            errorHandle(batchJobExecution, errMsg);
            sendSlackMessage(batchJob, batchNo, errMsg);
            e.printStackTrace();
        }
    }

    private void runBaseJob(BatchJob batchJob, String[] params) throws Exception {
        BeanUtils.getBaseJob(batchJob.getJobClassFullPath()).run(params);
    }

    private BatchJobExecution startHandle(long batchNo, String userId) {
        return batchJobExecutionService.startJobExecution(batchNo, userId);
    }

    private BatchJobExecution completeHandle(BatchJobExecution startedBatchJobExecution) {
        return batchJobExecutionService.completeJobExecution(startedBatchJobExecution.getJobExecutionId(), startedBatchJobExecution.getBatchNo());
    }

    private BatchJobExecution errorHandle(BatchJobExecution startedBatchJobExecution, String errorMsg) {
        return batchJobExecutionService.failJobExecution(startedBatchJobExecution.getJobExecutionId(), startedBatchJobExecution.getBatchNo(), errorMsg);
    }

    private void sendSlackMessage(BatchJob batchJob, long batchNo, String errorMsg) {
        try {
            // Send Slack
            StringBuilder sb = new StringBuilder()
                    .append("`[배치 실패]`").append(" ").append("\n")
                    .append("> 배치번호: ").append(batchNo).append("\n")
                    .append("> 배치: ").append(batchJob.getBatchName()).append("\n")
                    .append("> 도메인: ").append(batchJob.getDomain()).append("\n")
                    .append("> 배치명: ").append(batchJob.getDescription()).append("\n")
                    .append("> 중요도: ").append(batchJob.getImportance()).append("\n")
                    .append("> 담당자1: ").append(batchJob.getManager()).append("\n")
                    .append("> 담당자2: ").append(batchJob.getManagerSub()).append("\n");
                sb.append("> 오류내용: ").append(errorMsg);
            slackService.notify(sb.toString());

            // Send SMS
            String smsContents = "`[배치 실패]`" + " " + "\n" +
                    "> 배치번호: " + batchNo + "\n" +
                    "> 배치: " + batchJob.getBatchName() + "\n";
            batchSendTemplateService.sendSmsBatch(batchNo, smsContents);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
