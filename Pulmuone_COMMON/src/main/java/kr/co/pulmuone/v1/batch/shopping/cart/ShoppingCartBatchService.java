package kr.co.pulmuone.v1.batch.shopping.cart;

import kr.co.pulmuone.v1.batch.policy.config.PolicyConfigBatchBiz;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.shopping.ShoppingCartBatchMapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShoppingCartBatchService {

    @Autowired
    @Qualifier("masterSqlSessionTemplateBatch")
    private SqlSessionTemplate masterSqlSession;

    @Autowired
    private PolicyConfigBatchBiz policyConfigBatchBiz;

    private ShoppingCartBatchMapper shoppingCartBatchMapper;

    /**
     * 장바구니 정리
     */
    public void runShoppingCart() {
        shoppingCartBatchMapper = masterSqlSession.getMapper(ShoppingCartBatchMapper.class);

        // 삭제기간 검색
        String cartMaintenancePeriod = policyConfigBatchBiz.getConfigValue(PolicyEnums.PolicyKey.OD_CART_MAINTENANCE_PERIOD.getCode());

        List<Long> cartIds = getMaintenanceCart(cartMaintenancePeriod);
        for (Long cartId : cartIds) {
            delCartPickGoodsBySpCartId(cartId); // 장바구니 기획전 구성상품 삭제
            delCartAddGoodsBySpCartId(cartId);  // 장바구니 추가상품 삭제
            delCart(cartId);                    // 장바구니 삭제
        }
    }

    /**
     * 장바구니 삭제
     *
     * @param spCartId Long
     */
    protected void delCart(Long spCartId) {
        shoppingCartBatchMapper.delCart(spCartId);
    }

    /**
     * 장바구니 PK로 장바구니 추가 구성상품 삭제
     *
     * @param spCartId Long
     */
    protected void delCartAddGoodsBySpCartId(Long spCartId) {
        shoppingCartBatchMapper.delCartAddGoodsBySpCartId(spCartId);
    }

    /**
     * 장바구니 PK로 장바구니 기획전 구성상품 삭제
     *
     * @param spCartId Long
     */
    protected void delCartPickGoodsBySpCartId(Long spCartId) {
        shoppingCartBatchMapper.delCartPickGoodsBySpCartId(spCartId);
    }

    /**
     * 장바구니 정리대상 조회
     *
     * @param cartMaintenancePeriod String
     * @return List<Long>
     */
    protected List<Long> getMaintenanceCart(String cartMaintenancePeriod) {
        return shoppingCartBatchMapper.getMaintenanceCart(cartMaintenancePeriod);
    }

}
