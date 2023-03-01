package kr.co.pulmuone.v1.batch.system.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SystemLogBatchBizImpl implements SystemLogBatchBiz {

    private final SystemLogBatchService service;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDetectIllegalUserJoin() {
        service.runDetectIllegalUserJoin();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDetectIllegalLoginFail() {
        service.runDetectIllegalLoginFail();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDetectIllegalOrderCount() {
        service.runDetectIllegalOrderCount();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDetectIllegalOrderPrice() {
        service.runDetectIllegalOrderPrice();
    }
}
