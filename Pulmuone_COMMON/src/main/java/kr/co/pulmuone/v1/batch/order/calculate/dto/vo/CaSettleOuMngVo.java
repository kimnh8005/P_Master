package kr.co.pulmuone.v1.batch.order.calculate.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 정산용 OU 관리 vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "CaSettleOuMngVo")
public class CaSettleOuMngVo {

    @ApiModelProperty(value = "OU 아이디")
    private String ouId;

    @ApiModelProperty(value = "OU 명")
    private String ouNm;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "등록일")
    private String createDt;

}
