package kr.co.pulmuone.v1.shopping.restock.service;

public interface ShoppingRestockBiz {

    /**
     * 재입고 알림 요청
     *
     */
    int putRetockInfo(Long ilGoodsId, String urUserId) throws Exception;

}
