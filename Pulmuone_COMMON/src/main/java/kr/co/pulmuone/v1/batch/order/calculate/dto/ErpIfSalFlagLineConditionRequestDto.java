package kr.co.pulmuone.v1.batch.order.calculate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 매출 확정된 내역 조회후 완료 처리 Line Condition Request Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfSalFlagLineConditionRequestDto")
public class ErpIfSalFlagLineConditionRequestDto {

    @ApiModelProperty(value = "Header와 Line의 join key")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값. 온라인 order key값")
    private String oriSysSeq;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "주문상품 순번")
    private String ordNoDtl;

    @Builder
    public ErpIfSalFlagLineConditionRequestDto(String hrdSeq, String oriSysSeq, String ordNum, String ordNoDtl) {
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.ordNum = ordNum;
        this.ordNoDtl = ordNoDtl;
    }

}
