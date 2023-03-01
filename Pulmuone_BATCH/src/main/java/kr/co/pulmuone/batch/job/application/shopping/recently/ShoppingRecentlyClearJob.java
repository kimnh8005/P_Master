package kr.co.pulmuone.batch.job.application.shopping.recently;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.shopping.recently.ShoppingRecentlyBatchBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("shoppingRecentlyClearJob")
@Slf4j
public class ShoppingRecentlyClearJob implements BaseJob {
    /*
    배치명 : 최근본상품 정리 작업
    배치번호 : 106
    배치주기 : 매일 새벽 1회
     */

    @Autowired
    private ShoppingRecentlyBatchBiz shoppingRecentlyBatchBiz;

    @Override
    public void run(String[] params) {
        shoppingRecentlyBatchBiz.runShoppingRecentlyClear();
    }
}
