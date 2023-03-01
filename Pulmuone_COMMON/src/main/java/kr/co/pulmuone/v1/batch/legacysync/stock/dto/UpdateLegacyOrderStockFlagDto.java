package kr.co.pulmuone.v1.batch.legacysync.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UpdateLegacyOrderStockFlagDto {
    private long seq;
    private String updateFlag;
}
