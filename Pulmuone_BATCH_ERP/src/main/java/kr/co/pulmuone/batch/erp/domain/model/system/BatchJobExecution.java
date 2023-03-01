package kr.co.pulmuone.batch.erp.domain.model.system;

import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchJobExecution {

    private Long jobExecutionId;

    private Long batchNo;

    private String userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    private String errorMsg;

    @Builder
    public BatchJobExecution(Long batchNo, String userId, Date startTime, Date endTime, BatchStatus status, String errorMsg) {
        this.batchNo = batchNo;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public static BatchJobExecution start(long batchNo, String userId) {
        return BatchJobExecution.builder()
                .batchNo(batchNo)
                .userId(userId)
                .status(BatchStatus.STARTED)
                .startTime(new Date())
                .build();
    }

    public BatchJobExecution complete() {
        this.status = BatchStatus.COMPLETED;
        this.endTime = new Date();
        return this;
    }

    public BatchJobExecution fail(String errorMsg) {
        this.status = BatchStatus.FAILED;
        this.endTime = new Date();
        this.errorMsg = errorMsg;
        return this;
    }
}
