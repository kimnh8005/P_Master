package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetOrganizationPopupListResultVo")
public class OrganizationPopupListResultVo {

	@ApiModelProperty(value = "no")
	private int no;

	@ApiModelProperty(value = "법인코드")
	private String erpRegalCode;

	@ApiModelProperty(value = "법인명")
	private String erpRegalName;

	@ApiModelProperty(value = "조직코드")
	private String erpOrganizationCode;

	@ApiModelProperty(value = "조직명")
	private String erpOrganizationName;

	@ApiModelProperty(value = "적립금 제한액")
	private String amount;

	@ApiModelProperty(value = "역할그룹 ID")
	private String stRoleTpId;

	@ApiModelProperty(value = "적립금 유효일")
	private String validityDay;

	@ApiModelProperty(value = "조직 회계 코드")
	private String finOrganizationCode;

}
