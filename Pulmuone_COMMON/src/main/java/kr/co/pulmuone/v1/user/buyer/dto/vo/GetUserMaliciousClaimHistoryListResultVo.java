package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserMaliciousClaimHistoryListResultVo")
public class GetUserMaliciousClaimHistoryListResultVo {

	@ApiModelProperty(value = "")
	private int rowNumber;

	@ApiModelProperty(value = "")
	private String stClassificationName;

	@ApiModelProperty(value = "")
	private String maliciousClaimReason;

	@ApiModelProperty(value = "")
	private String maliciousClaimCreateDate;

}
