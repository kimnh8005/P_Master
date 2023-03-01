package kr.co.pulmuone.v1.batch.legacysync.purchase;

import java.util.List;

public interface LegacySyncPurchaseBatchBiz {
    int purchaseOrderSyncBatch(List<String> erpPoTypeList);
}
