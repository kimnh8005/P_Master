package kr.co.pulmuone.batch.job.application.shopping.cart;

import kr.co.pulmuone.v1.batch.shopping.cart.ShoppingCartBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("shoppingCartJob")
@Slf4j
public class ShoppingCartJob implements BaseJob {

    @Autowired
    private ShoppingCartBatchBiz shoppingCartBatchBiz;

    @Override
    public void run(String[] params) {
        shoppingCartBatchBiz.runShoppingCart();
    }
}
