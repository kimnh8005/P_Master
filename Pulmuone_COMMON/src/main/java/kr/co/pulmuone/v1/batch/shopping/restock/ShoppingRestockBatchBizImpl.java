package kr.co.pulmuone.v1.batch.shopping.restock;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.shopping.restock.service.ShoppingRestockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingRestockBatchBizImpl implements ShoppingRestockBatchBiz {

    private final ShoppingRestockService shoppingRestockService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runRestock() throws BaseException {
        shoppingRestockService.runRestock();
    }
}
