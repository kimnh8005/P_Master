package kr.co.pulmuone.v1.batch.legacysync.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SearchItemWarehouseIdDto {
    private String itemCd;
    private long supplierId;
    private long warehouseId;
}
