package kr.co.pulmuone.batch.job.application.legacy.purchase;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.legacysync.purchase.LegacySyncPurchaseBatchBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("PurchaseOrderLegacyIf1845Job")
@RequiredArgsConstructor
public class PurchaseOrderLegacyIf1845Job implements BaseJob {
    /**
     * 통합몰 식품 발주 정보 > 풀무원샵 식품 발주 정보 I/F
     * batchNo: 90
     */

    @Autowired
    private LegacySyncPurchaseBatchBiz legacySyncPurchaseBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        List<String> erpPoTypeList = new ArrayList<>();
        erpPoTypeList.add("ERP_PO_TP.PF");

        legacySyncPurchaseBatchBiz.purchaseOrderSyncBatch(erpPoTypeList);
    }
}
