package kr.co.pulmuone.v1.batch.monitoring.dto.vo;

import lombok.Getter;

@Getter
public class YunginNotiSmsSendVo {
    private String ifDt;
    private Long   normalPffOrdCnt;
    private Long   normalPffFrozenCnt;
    private Long   normalPffCnt;
    private Long   dawnPffOrdCnt;
    private Long   dawnPffFrozenCnt;
    private Long   dawnPffCnt;
}
