package kr.co.pulmuone.v1.system.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 역활그룹의 권한 목록 리스트 DTO")
public class RoleMenuAuthListExcelDto{

	@ApiModelProperty(value = "매뉴 그룹 명")
	private String menuGroupName;

	@ApiModelProperty(value = "중매뉴명")
	private String parentsMenuName;

	@ApiModelProperty(value = "매뉴명")
	private String menuName;

	@ApiModelProperty(value = "프로그램 권한 코드")
	private String programAuthCode;

	@ApiModelProperty(value = "프로그램 권한 명")
	private String programAuthCodeName;
}
