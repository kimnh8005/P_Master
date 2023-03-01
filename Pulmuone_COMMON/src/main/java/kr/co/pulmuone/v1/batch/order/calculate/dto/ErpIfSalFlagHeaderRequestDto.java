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
 * 매출 확정된 내역 조회후 완료 처리 Header Request Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfSalFlagHeaderRequestDto")
public class ErpIfSalFlagHeaderRequestDto {

    @ApiModelProperty(value = "매출 확정된 내역 조회후 완료 처리 Header condition Request Dto")
    private ErpIfSalFlagHeaderConditionRequestDto condition;

    @ApiModelProperty(value = "매출 확정된 내역 조회후 완료 처리 line Request Dto 리스트")
    private List<ErpIfSalFlagLineRequestDto> line;

    @Builder
    public ErpIfSalFlagHeaderRequestDto(ErpIfSalFlagHeaderConditionRequestDto condition, List<ErpIfSalFlagLineRequestDto> line) {
        this.condition = condition;
        this.line = line;
    }

}
