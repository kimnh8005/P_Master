package kr.co.pulmuone.v1.batch.system.log;

public interface SystemLogBatchBiz {

    void runDetectIllegalUserJoin();

    void runDetectIllegalLoginFail();

    void runDetectIllegalOrderCount();

    void runDetectIllegalOrderPrice();

}
