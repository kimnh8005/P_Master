package kr.co.pulmuone.v1.batch.order.calculate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 매출 확정된 내역 조회후 완료 처리 Line Request Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfSalFlagLineRequestDto")
public class ErpIfSalFlagLineRequestDto {

    @ApiModelProperty(value = "매출 확정된 내역 조회후 완료 처리 Line condition Request Dto")
    private Object condition;

    @Builder
    public ErpIfSalFlagLineRequestDto(Object condition) {
        this.condition = condition;
    }

}
