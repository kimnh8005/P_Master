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
@ApiModel(description = "임직원 할인 브랜드 그룹 및 브랜드 관계	VO")
public class PolicyBenefitEmployeeBrandGroupBrandVo extends BaseRequestDto{

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리.임직원 할인 브랜드 그룹 및 브랜드 관계.SEQ")
	private String psEmplDiscBrandGrpBrandId;

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리.임직원 할인 브랜드 그룹.SEQ")
	private String psEmplDiscBrandGrpId;

	@ApiModelProperty(value = "임직원 할인율")
	private int discountRatio;

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리.브랜드 PK")
	private String urBrandId;

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리.브랜드 명")
	private String brandName;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
