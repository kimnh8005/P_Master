package kr.co.pulmuone.v1.policy.origin.dto.basic;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginTypeListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "원산지 구분 목록 조회 ResponseDto")
public class GetPolicyOriginTypeListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "레코드 목록")
	private	List<GetPolicyOriginTypeListResultVo> rows;
}
