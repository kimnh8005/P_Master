package kr.co.pulmuone.v1.batch.order.ezadmin;

import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;

import java.time.LocalDateTime;

/**
 * EZAdmin 주문 관리
 */
public interface EZAdminOrderBiz {

    /**
     * EZAdmin 주문 생성
     * @param collectionMallDetailIdList
     * @return
     */
    void putEZAdminCreateOrder() throws Exception;

    String createEZAdminOrder(EZAdminOrderDto ezadminOrderItem, LocalDateTime batchStartTime) throws Exception;
}