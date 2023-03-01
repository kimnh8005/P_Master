package kr.co.pulmuone.v1.policy.origin.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "원산지 등록 RequestDto")
public class AddPolicyOriginRequestDto  extends BaseRequestDto {

	@ApiModelProperty(value = "공통코드ID")
	private String systemCommonCodeId;

	@ApiModelProperty(value = "원산지 코드")
	private String originCode;

	@ApiModelProperty(value = "원산지 구분")
	private String originType;

	@ApiModelProperty(value = "원산지 명에 대한 사전 ID")
	private String gbDictionaryMasterId;


}
