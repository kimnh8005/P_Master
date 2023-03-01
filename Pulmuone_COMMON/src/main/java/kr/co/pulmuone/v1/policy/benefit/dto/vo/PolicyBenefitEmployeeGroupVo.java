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
@ApiModel(description = "임직원 혜택 그룹 VO")
public class PolicyBenefitEmployeeGroupVo extends BaseRequestDto{

	@ApiModelProperty(value = "임직원 혜택 그룹 브랜드 그룹 목록 리스트")
	private	List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList;

	@ApiModelProperty(value = "임직원 혜택 그룹.SEQ")
	private String psEmplDiscGrpId;

	@ApiModelProperty(value = "임직원 혜택 관리 MASTER.SEQ")
	private String psEmplDiscMasterId;

	@ApiModelProperty(value = "임직원 혜택 그룹.한도액")
	private String limitAmt;

	@ApiModelProperty(value = "임직원 혜택 그룹.임직원 할인 한도액 주기 유형 공통코드(EMPL_DISC_LIMIT_CYCLE_TP)")
	private String emplDiscLimitCycleTp;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
