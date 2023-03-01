package kr.co.pulmuone.v1.policy.claim.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "쇼핑몰 클레임 사유 등록,수정 요청 DTO")
public class SavePolicyClaimMallRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private Long psClaimCtgryId;

	@ApiModelProperty(value = "MALL 클레임 사유 PK")
	private Long psClaimMallId;

	@ApiModelProperty(value = "BOS 클레임 사유 PK")
	private Long psClaimBosId;

	@ApiModelProperty(value = "사유")
	private String reasonMessage;

	@ApiModelProperty(value = "사유구분")
	private String reasonType;

	@ApiModelProperty(value = "첨부파일 필수여부")
	private String attcRequiredYn;

	@ApiModelProperty(value = "사용여부")
	private String useYn;
}
