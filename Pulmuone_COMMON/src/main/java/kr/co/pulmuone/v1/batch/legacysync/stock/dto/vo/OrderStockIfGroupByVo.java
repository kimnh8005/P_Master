package kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo;

import lombok.Getter;

import java.util.Date;

@Getter
public class OrderStockIfGroupByVo {
    private Date ifSendSchdDt;
    private String chnnNo;
    private int pdSeq;
    private String chnnGoodsNo;
    private String orderType;
    private int orderCnt;
    private String itemBarcode;
    private Date deliveryDt;
}
