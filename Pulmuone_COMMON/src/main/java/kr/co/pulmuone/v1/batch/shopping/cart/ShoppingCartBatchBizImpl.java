package kr.co.pulmuone.v1.batch.shopping.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartBatchBizImpl implements ShoppingCartBatchBiz {

    private final ShoppingCartBatchService shoppingCartBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runShoppingCart() {
        shoppingCartBatchService.runShoppingCart();
    }


}
