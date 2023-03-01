package kr.co.pulmuone.v1.batch.order.calculate.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * 임직원정산 마스터 vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "CaSettleEmployeeMasterVo")
public class CaSettleEmployeeMasterVo {

    @ApiModelProperty(value = "임직원정산 마스터 PK")
    private Long caSettleEmployeeMonthId;

    @ApiModelProperty(value = "정산월(YYYYMM)")
    private String settleMonth;

    @ApiModelProperty(value = "정산시작일자")
    private String startDt;

    @ApiModelProperty(value = "정산종료일자")
    private String endDt;

    @ApiModelProperty(value = "OU 아이디")
    private String ouId;

    @ApiModelProperty(value = "OU 명")
    private String ouNm;

    @ApiModelProperty(value = "할인금액")
    private Long salePrice;

    @ApiModelProperty(value = "확정여부(N:대기,Y:확정)")
    private String confirmYn;

    @ApiModelProperty(value = "확정일자")
    private String confirmDt;

    @ApiModelProperty(value = "확정자")
    private String confirmId;

    @ApiModelProperty(value = "세션아이디")
    private Long sessionId;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

}
