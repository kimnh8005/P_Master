package kr.co.pulmuone.batch.job.application.legacy.purchase;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.legacysync.purchase.LegacySyncPurchaseBatchBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("PurchaseOrderLegacyIf1445Job")
@RequiredArgsConstructor
public class PurchaseOrderLegacyIf1445Job implements BaseJob {
    /**
     * 통합몰 푸드머스 발주 정보 > 풀무원샵 푸드머스 발주 정보 I/F
     * 월요일부터 목요일까지 발주요청일은 PD1, 양지는 baseDt +2일, PD2는 baseDt +3일
     * 금요일 발주요청일은 푸드머스 상품 모두 baseDt +4일
     * batchNo: 89
     */

    @Autowired
    private LegacySyncPurchaseBatchBiz legacySyncPurchaseBatchBiz;

    @Override
    public void run(String[] params) throws Exception {

        List<String> erpPoTypeList = new ArrayList<>();
        erpPoTypeList.add("ERP_PO_TP.YJ");
        erpPoTypeList.add("ERP_PO_TP.PD1");
        erpPoTypeList.add("ERP_PO_TP.PD2");

        legacySyncPurchaseBatchBiz.purchaseOrderSyncBatch(erpPoTypeList);
    }
}
