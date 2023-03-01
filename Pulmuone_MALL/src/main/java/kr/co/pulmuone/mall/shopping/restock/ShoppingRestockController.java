package kr.co.pulmuone.mall.shopping.restock;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.mall.shopping.restock.service.ShoppingRestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingRestockController {

    @Autowired
    public ShoppingRestockService shoppingRestockService;

    /**
     * 재입고 알림 요청
     *
     * @param ilGoodsId
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/shopping/restock/applyRestockNotice")
    @ApiOperation(value = "재입고 알림 요청", httpMethod = "POST")
    public ApiResult<?> applyRestockNotice(Long ilGoodsId) throws Exception {
        return shoppingRestockService.applyRestockNotice(ilGoodsId);
    }
}
