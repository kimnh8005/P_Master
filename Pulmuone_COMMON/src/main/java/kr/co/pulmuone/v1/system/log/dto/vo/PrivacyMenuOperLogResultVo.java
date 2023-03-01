package kr.co.pulmuone.v1.system.log.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "개인정보 처리 로그 Result Vo")
public class PrivacyMenuOperLogResultVo {

	@ApiModelProperty(value = "아이디")
	private String loginId;

	@ApiModelProperty(value = "관리자명")
	private String loginName;

	@ApiModelProperty(value = "param")
	private String paramValue;

	@ApiModelProperty(value = "URL")
	private String url;

	@ApiModelProperty(value = "메뉴명")
	private String urlName;

	@ApiModelProperty(value = "CRUD 유형(CRUD_TP : C(CREATE), R(READ), U(UPDATE), D(DELETE), DL(DOWNLOAD), UNDEFINED(미정의))")
	private String crudType;

	@ApiModelProperty(value = "IP")
	private String ip;

	@ApiModelProperty(value = "등록일자")
	private String createDate;


}
