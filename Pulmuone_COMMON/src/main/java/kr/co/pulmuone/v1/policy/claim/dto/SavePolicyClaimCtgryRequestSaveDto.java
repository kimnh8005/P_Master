package kr.co.pulmuone.v1.policy.claim.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 사유 카테고리 저장 DTO")
public class SavePolicyClaimCtgryRequestSaveDto extends BaseRequestDto {

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private Long psClaimCtgryId;

	@ApiModelProperty(value = "클레임 사유명")
	private String claimName;

	@ApiModelProperty(value = "클레임 카테고리 코드")
	private String categoryCode;

	@ApiModelProperty(value = "귀책처 코드")
	private String targetTypeCode;

}
