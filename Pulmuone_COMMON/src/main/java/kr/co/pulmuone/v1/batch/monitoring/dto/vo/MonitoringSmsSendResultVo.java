package kr.co.pulmuone.v1.batch.monitoring.dto.vo;

import lombok.Getter;

@Getter
public class MonitoringSmsSendResultVo {
    private String execYmd;
    private String execTime;
    private Long   totUserCnt;
    private Long   totOrderCnt;
    private Long   totOrderCntOutmall;
    private Long   totPaymentPrice;
    private Long   totPaymentPriceOutmall;
}
