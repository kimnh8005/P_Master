package kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "법인 정보")
public class ErpRegalBatchVo {
	@ApiModelProperty(value = "법인코드")
	private String erpRegalCode;

	@ApiModelProperty(value = "법인명")
	private String erpRegalName;

    @ApiModelProperty(value = "임직원 혜택적용")
    private String employeeDiscountYn;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "등록자 ID")
    private Long createId;
}
