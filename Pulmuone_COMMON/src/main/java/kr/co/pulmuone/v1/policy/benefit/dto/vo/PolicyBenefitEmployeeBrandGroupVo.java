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
@ApiModel(description = "임직원 할인율 브랜드 그룹 관리 VO")
public class PolicyBenefitEmployeeBrandGroupVo extends BaseRequestDto{

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리 그룹별 브랜드 목록 리스트")
	private	List<PolicyBenefitEmployeeBrandGroupBrandVo> brandList;

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리.임직원 할인 브랜드 그룹.SEQ")
	private String psEmplDiscBrandGrpId;

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리.그룹명")
	private String groupName;

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리.할인율")
	private String discountRatio;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
