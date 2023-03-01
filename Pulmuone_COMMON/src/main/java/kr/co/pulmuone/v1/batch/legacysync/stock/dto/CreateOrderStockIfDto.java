package kr.co.pulmuone.v1.batch.legacysync.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class CreateOrderStockIfDto {
    private Date ifSendSchdDt;
    private String chnnNo;
    private int pdSeq;
    private String chnnGoodsNo;
    private String orderType;
    private int orderCnt;
    private String itemBarcode;
    private Date delivSchdDt;
    private String craimCancelYn;
}
