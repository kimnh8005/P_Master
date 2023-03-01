package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "MenuUrlResultVo")
public class MenuUrlResultVo {

	@ApiModelProperty(value = "SEQ")
	private String systemMenuUrlId;

	@ApiModelProperty(value = "메뉴명")
	private String menuName;

	@ApiModelProperty(value = "URL명(시스템명)")
	private String urlName;

	@ApiModelProperty(value = "URL")
	private String url;

	@ApiModelProperty(value = "개인정보 처리 로그 유무(Y:로그 처리)")
	private String privacyLogYn;

	@ApiModelProperty(value = "CRUD 유형(CRUD_TP : C(CREATE), R(READ), U(UPDATE), D(DELETE), DL(DOWNLOAD), UNDEFINED(미정의))")
	private String crudType;



}
