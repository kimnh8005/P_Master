package kr.co.pulmuone.v1.batch.legacysync.stock;

public interface LegacySyncStockBatchBiz {
    int orderStockSyncBatch();
    int legacyOrderStockCalculateBatch();
}
