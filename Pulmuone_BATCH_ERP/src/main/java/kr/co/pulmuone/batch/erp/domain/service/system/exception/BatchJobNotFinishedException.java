package kr.co.pulmuone.batch.erp.domain.service.system.exception;

import lombok.Getter;

@Getter
public class BatchJobNotFinishedException extends RuntimeException {

    private Long batchNo;

    public BatchJobNotFinishedException(Long batchNo) {
        this.batchNo = batchNo;
    }

    public BatchJobNotFinishedException(String message, Long batchNo) {
        super(message);
        this.batchNo = batchNo;
    }

    public BatchJobNotFinishedException(String message, Throwable cause, Long batchNo) {
        super(message, cause);
        this.batchNo = batchNo;
    }

    public BatchJobNotFinishedException(Throwable cause, Long batchNo) {
        super(cause);
        this.batchNo = batchNo;
    }

    public BatchJobNotFinishedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Long batchNo) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.batchNo = batchNo;
    }
}
