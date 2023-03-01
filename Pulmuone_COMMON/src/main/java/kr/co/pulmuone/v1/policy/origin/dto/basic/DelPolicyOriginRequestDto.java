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
@ApiModel(description = "원산지 삭제 RequestDto")
public class DelPolicyOriginRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "공통코드ID")
	private String systemCommonCodeId;

}
