package kr.co.pulmuone.batch.cj.domain.service.system.exception;

import lombok.Getter;

@Getter
public class BatchJobNotStartedException extends RuntimeException {

    private long batchNo;

    public BatchJobNotStartedException(long batchNo) {
        this.batchNo = batchNo;
    }

    public BatchJobNotStartedException(String message, long batchNo) {
        super(message);
        this.batchNo = batchNo;
    }

    public BatchJobNotStartedException(String message, Throwable cause, long batchNo) {
        super(message, cause);
        this.batchNo = batchNo;
    }

    public BatchJobNotStartedException(Throwable cause, long batchNo) {
        super(cause);
        this.batchNo = batchNo;
    }

    public BatchJobNotStartedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, long batchNo) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.batchNo = batchNo;
    }
}
