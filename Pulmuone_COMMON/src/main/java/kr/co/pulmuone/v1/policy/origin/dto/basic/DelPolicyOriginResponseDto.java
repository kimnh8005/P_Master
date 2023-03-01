package kr.co.pulmuone.v1.policy.origin.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "원산지 삭제 ResponseDto")
public class DelPolicyOriginResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "레코드")
	private	GetPolicyOriginResultVo rows;

}
