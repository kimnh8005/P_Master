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
@ApiModel(description = "임직원 혜택 관리 MASTER VO")
public class PolicyBenefitEmployeeVo extends BaseRequestDto{

	@ApiModelProperty(value = "임직원 혜택 그룹 목록 리스트")
	private	List<PolicyBenefitEmployeeGroupVo> groupList;

	@ApiModelProperty(value = "임직원 혜택 법인 그룹 리스트")
	private	List<PolicyBenefitEmployeeLegalVo> companyList;

	@ApiModelProperty(value = "임직원 혜택 관리 MASTER.SEQ")
	private String psEmplDiscMasterId;

	@ApiModelProperty(value = "임직원 혜택 관리.그룹명")
	private String masterName;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
