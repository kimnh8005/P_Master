package kr.co.pulmuone.v1.system.code.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeMasterListResultVo")
public class GetCodeListResultVo {

	@ApiModelProperty(value = "공통 코드 시퀀스 아이디")
	private String stCommonCodeId;

	@ApiModelProperty(value = "공통 코드 마스터 아이디")
	private String stCommonCodeMasterId;

	@ApiModelProperty(value = "공통 사전 마스터 아이디")
	private String gbDictionaryMasterId;

	@ApiModelProperty(value = "공통코드")
	private String commonCode;

	@ApiModelProperty(value = "순번")
	private String sort;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "임시값1")
	private String attribute1;

	@ApiModelProperty(value = "임시값2")
	private String attribute2;

	@ApiModelProperty(value = "임시값3")
	private String attribute3;

	@ApiModelProperty(value = "설명")
	private String comment;

	@ApiModelProperty(value = "사전마스터명")
	private String dictionaryMasterName;

	@ApiModelProperty(value = "공통 마스터 코드")
	private String commonMasterCode;
}
