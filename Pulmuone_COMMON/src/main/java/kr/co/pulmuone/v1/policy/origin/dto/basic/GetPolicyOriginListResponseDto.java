package kr.co.pulmuone.v1.policy.origin.dto.basic;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "원산지 목록 조회 ResponseDto")
public class GetPolicyOriginListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "레코드 목록")
	private	List<GetPolicyOriginListResultVo> rows;

	@ApiModelProperty(value = "레코드 수")
	private long total;
}
