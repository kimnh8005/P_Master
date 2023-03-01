package kr.co.pulmuone.v1.policy.benefit.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원 혜택 그룹 및 법인 관계 VO")
public class PolicyBenefitEmployeeLegalVo extends BaseRequestDto{

	@ApiModelProperty(value = "임직원 혜택 그룹 및 법인 관계.SEQ")
	private String psEmplDiscMasterLegalId;

	@ApiModelProperty(value = "임직원 혜택 관리 MASTER.SEQ")
	private String psEmplDiscMasterId;

	@ApiModelProperty(value = "임직원 혜택 그룹 및 법인 관계.UR_ERP_REGAL PK(법인코드)")
	private String erpRegalCode;

	@ApiModelProperty(value = "임직원 혜택 그룹 및 법인 관계.법인명")
	private String erpRegalName;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
