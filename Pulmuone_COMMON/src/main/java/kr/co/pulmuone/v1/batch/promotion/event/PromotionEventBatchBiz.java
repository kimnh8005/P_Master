package kr.co.pulmuone.v1.batch.promotion.event;

public interface PromotionEventBatchBiz {

    void runEventTimeOver();

    void runStampPurchaseEvent();

}
