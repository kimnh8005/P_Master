package kr.co.pulmuone.v1.batch.legacysync.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class SearchOrderStockIfDto {
    private Date ifSendSchdDt;
    private String chnnNo;
    private int pdSeq;
    private String chnnGoodsNo;
    private String orderType;
}
