package kr.co.pulmuone.v1.batch.order.outmall;

/**
 * outmall 주문 관리
 */
public interface OutmallExcelOrderBiz {

    /**
     * outmall 주문 생성
     * @param
     * @return
     */
    void putOutmallCreateOrder(int maxThreadCnt, String targetStatusList) throws Exception;
}