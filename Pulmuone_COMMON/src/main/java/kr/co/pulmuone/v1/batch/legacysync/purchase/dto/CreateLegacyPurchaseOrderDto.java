package kr.co.pulmuone.v1.batch.legacysync.purchase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class CreateLegacyPurchaseOrderDto {
    private Date poDt;
    private int pdSeq;
    private String itemCd;
    private String goodsNm;
    private int orderDt;
    private int addOrderDt;
    private Date requestDate;
    private String poTime;
    private String foodmerceResponseDate;
}
