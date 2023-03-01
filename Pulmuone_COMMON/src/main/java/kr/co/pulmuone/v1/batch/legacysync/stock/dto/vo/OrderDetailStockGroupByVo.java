package kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo;

import lombok.Getter;

import java.util.Date;

@Getter
public class OrderDetailStockGroupByVo {
    private Date shippingDt;
    private String supplierCd;
    private int warehouseId;
    private Long ilGoodsId;
    private int orderCnt;
    private int cancelCnt;
    private int craimCnt;
    private String ilItemCd;
    private String itemBarcode;
    private Date deliveryDt;
}
