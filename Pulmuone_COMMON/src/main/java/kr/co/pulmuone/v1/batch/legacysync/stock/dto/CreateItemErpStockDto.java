package kr.co.pulmuone.v1.batch.legacysync.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class CreateItemErpStockDto {
    private long ilItemErpStockId;
    private long ilItemWarehouseId;
    private Date baseDt;
    private String stockTp;
    private int stockQty;
    private Date scheduleDt;
}
