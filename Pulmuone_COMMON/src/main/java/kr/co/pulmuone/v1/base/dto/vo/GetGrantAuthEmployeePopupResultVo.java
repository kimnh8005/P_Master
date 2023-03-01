package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "거래처 검색 Vo")
public class GetGrantAuthEmployeePopupResultVo {

	@ApiModelProperty(value = "ID")
	private String employeeNumber;

	@ApiModelProperty(value = "직원명")
	private String employeeName;

	@ApiModelProperty(value = "조직정보코드")
	private String organizationCode;

	@ApiModelProperty(value = "조직정보명")
	private String organizationName;

	@ApiModelProperty(value = "법인코드")
	private String regalCode;

	@ApiModelProperty(value = "법인명")
	private String regalName;

	@ApiModelProperty(value = "회원 ID")
	private Long userId;

	@ApiModelProperty(value = "회사 ID")
	private Long companyId;

	@ApiModelProperty(value = "직책명")
	private String positionName;

	@ApiModelProperty(value = "조직장여부")
	private String teamLeaderYn;

	@ApiModelProperty(value = "개인정보 열람권한 유무")
	private String personalInfoAccessYn;
}
