package kr.co.pulmuone.v1.batch.legacysync.purchase.dto.vo;

import lombok.Getter;

import java.util.Date;

@Getter
public class PurchaseOrderVo {
    private Date baseDt;
    private Date reqDt;
    private Date stockScheduledDt;
    private int poIfQty;
    private String itemCd;
    private long supplierId;
    private String itemName;
    private String warehouseCd;
    private String erpPoType;
    private String poTypeName;
    private String poType;
    private String poTime;
    private String foodmerceResponseDate;
}

