package kr.co.pulmuone.batch.job.application.legacy.stock;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.legacysync.stock.LegacySyncStockBatchBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("OrderStockLegacyIfJob")
@RequiredArgsConstructor
public class OrderStockLegacyIfJob implements BaseJob {
    /**
     * 통합몰 주문 데이터 > Legacy 재고 정보 I/F
     * batchNo: 79
     */
    @Autowired
    private LegacySyncStockBatchBiz legacySyncStockBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        legacySyncStockBatchBiz.orderStockSyncBatch();
    }
}
