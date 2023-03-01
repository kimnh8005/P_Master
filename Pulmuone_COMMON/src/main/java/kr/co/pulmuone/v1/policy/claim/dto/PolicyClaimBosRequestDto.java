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
@ApiModel(description = "BOS 클레임 사유 요청 DTO")
public class PolicyClaimBosRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private Long psClaimBosId;

    @ApiModelProperty(value = "클래임 사유(대)")
    private Long searchLClaimCtgryId;

    @ApiModelProperty(value = "클레임 사유(중)")
    private Long searchMClaimCtgryId;

    @ApiModelProperty(value = "귀책처")
    private Long searchSClaimCtgryId;

    @ApiModelProperty(value = "귀책 유형")
    private String searchTargetType;

    @ApiModelProperty(value = "검색조건")
    private String searchCondition;

    @ApiModelProperty(value = "검색어")
    private String searchKeyword;
}
