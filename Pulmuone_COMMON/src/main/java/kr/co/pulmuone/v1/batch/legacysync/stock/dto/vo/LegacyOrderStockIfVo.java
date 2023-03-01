package kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo;

import lombok.Getter;

import java.util.Date;

@Getter
public class LegacyOrderStockIfVo {

    private long seq;
    private long urSupplierId;
    private long urWarehouseId;
    private Date ifSendSchdDt;
    private String supplierCd;
    private String chnnGoodsNo;
    private String orderType;
    private int ordCnt;
    private String itemBarcode;
    private Date delivSchdDt;

}
