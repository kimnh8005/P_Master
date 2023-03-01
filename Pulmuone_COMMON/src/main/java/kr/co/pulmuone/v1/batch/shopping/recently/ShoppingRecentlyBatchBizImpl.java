package kr.co.pulmuone.v1.batch.shopping.recently;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingRecentlyBatchBizImpl implements ShoppingRecentlyBatchBiz {

    private final ShoppingRecentlyBatchService shoppingRecentlyBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runShoppingRecentlyClear() {
        shoppingRecentlyBatchService.runShoppingRecentlyClear();
    }


}
