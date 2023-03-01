package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserMaliciousClaimHistoryListRequestDto")
public class GetUserMaliciousClaimHistoryListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private String urUserId;

}
