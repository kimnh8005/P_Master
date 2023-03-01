package kr.co.pulmuone.v1.batch.denormalization;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenormalizationBatchBizImpl implements DenormalizationBatchBiz {

    @Autowired
    private DenormalizationBatchService denormalizationBatchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public void displayCategoryDenormalization() {
        denormalizationBatchService.displayCategoryDenormalization();
    }
}
