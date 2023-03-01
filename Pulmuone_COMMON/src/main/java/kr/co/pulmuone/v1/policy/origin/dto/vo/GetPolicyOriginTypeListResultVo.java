package kr.co.pulmuone.v1.policy.origin.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "원산지 구분 목록 조회 ResultVo")
public class GetPolicyOriginTypeListResultVo {

	@ApiModelProperty(value = "")
	private String originTypeCode;

	@ApiModelProperty(value = "")
	private String originTypeName;

}
