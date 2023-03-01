package kr.co.pulmuone.batch.job.application.shopping.cart;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.shopping.restock.ShoppingRestockBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("shoppingRestockJob")
@Slf4j
public class ShoppingRestockJob implements BaseJob {

    @Autowired
    private ShoppingRestockBatchBiz shoppingRestockBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {
        shoppingRestockBatchBiz.runRestock();
    }
}
