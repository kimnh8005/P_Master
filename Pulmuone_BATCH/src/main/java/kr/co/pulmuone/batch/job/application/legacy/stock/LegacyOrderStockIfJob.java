package kr.co.pulmuone.batch.job.application.legacy.stock;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.legacysync.stock.LegacySyncStockBatchBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("LegacyOrderStockIfJob")
@RequiredArgsConstructor
public class LegacyOrderStockIfJob implements BaseJob {
    /**
     * Legacy 주문 재고 정보 > 통합몰 재고 계산
     * batchNo: 78
     */

    @Autowired
    private LegacySyncStockBatchBiz legacySyncStockBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        legacySyncStockBatchBiz.legacyOrderStockCalculateBatch();
    }

}
