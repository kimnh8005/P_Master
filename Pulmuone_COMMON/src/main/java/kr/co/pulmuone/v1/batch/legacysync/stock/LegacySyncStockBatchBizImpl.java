package kr.co.pulmuone.v1.batch.legacysync.stock;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LegacySyncStockBatchBizImpl implements LegacySyncStockBatchBiz {
    @Autowired
    private LegacySyncStockBatchService legacySyncStockBatchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public int orderStockSyncBatch() {
        return legacySyncStockBatchService.orderStockInterface();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public int legacyOrderStockCalculateBatch() {
        return legacySyncStockBatchService.legacyOrderStockCalculated();
    }
}
