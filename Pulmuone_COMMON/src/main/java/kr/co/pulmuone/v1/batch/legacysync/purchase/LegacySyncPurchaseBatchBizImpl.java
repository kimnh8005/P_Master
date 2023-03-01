package kr.co.pulmuone.v1.batch.legacysync.purchase;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LegacySyncPurchaseBatchBizImpl implements LegacySyncPurchaseBatchBiz {

    @Autowired
    private LegacySyncPurchaseBatchService legacySyncPurchaseBatchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public int purchaseOrderSyncBatch(List<String> erpPoTypeList) {
        return legacySyncPurchaseBatchService.purchaseOrderInterface(erpPoTypeList);
    }
}
