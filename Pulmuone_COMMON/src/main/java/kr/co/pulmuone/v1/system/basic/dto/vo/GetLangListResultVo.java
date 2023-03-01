package kr.co.pulmuone.v1.system.basic.dto.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetLangListResultVo")
public class GetLangListResultVo implements Serializable {

	private static final long serialVersionUID = 1054098732658562455L;

	@ApiModelProperty(value = "프로그램PK")
	private String stProgramId;

	@ApiModelProperty(value = "언어 아이디")
	private String gbLangId;

	@ApiModelProperty(value = "사전 마스터 아이디")
	private String gbDicMstId;

	@ApiModelProperty(value = "사전명")
	private String dicName;

	@ApiModelProperty(value = "정책아이디")
	private String pgId;
}
