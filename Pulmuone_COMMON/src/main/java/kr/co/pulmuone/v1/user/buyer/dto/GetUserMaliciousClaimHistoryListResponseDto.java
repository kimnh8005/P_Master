package kr.co.pulmuone.v1.user.buyer.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetUserMaliciousClaimHistoryListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserMaliciousClaimHistoryListResponseDto")
public class GetUserMaliciousClaimHistoryListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetUserMaliciousClaimHistoryListResultVo> rows;
}
