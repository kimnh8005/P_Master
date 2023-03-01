package kr.co.pulmuone.v1.policy.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 사유 카테고리 목록 요청 DTO")
public class PolicyClaimCtgryRequestDto extends BaseRequestPageDto{

    @ApiModelProperty(value = "클레임 사유 카테고리 10: 대, 20: 중, 30: 귀책처")
    private String categoryCode;

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private Long psClaimCtgryId;

    @ApiModelProperty(value = "클래임 사유(대)")
    private Long searchLClaimCtgryId;

    @ApiModelProperty(value = "클레임 사유(중)")
    private Long searchMClaimCtgryId;

    @ApiModelProperty(value = "귀책처")
    private Long searchSClaimCtgryId;

    //공급처에 따른 사유 추가
    @ApiModelProperty(value = "공급업체 PK")
	private Long urSupplierId;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "BOS 클레임 사유 PK")
    private String psClaimBosId;

    @ApiModelProperty(value = "클레임 PK")
    private Long odClaimId;

}
