package kr.co.pulmuone.v1.policy.origin.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "원산지 상세 조회 ResultVo")
public class GetPolicyOriginResultVo {

	@ApiModelProperty(value = "공통코드ID")
	private String systemCommonCodeId;

	@ApiModelProperty(value = "원산지 코드")
	private String originCode;

	@ApiModelProperty(value = "원산지 구분")
	private String originType;

	@ApiModelProperty(value = "원산지 명")
	private String originName;

	@ApiModelProperty(value = "원산지 명에 대한 사전 ID")
	private String gbDictionaryMasterId;


}
