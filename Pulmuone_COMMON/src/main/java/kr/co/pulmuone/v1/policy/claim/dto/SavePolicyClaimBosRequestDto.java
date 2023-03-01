package kr.co.pulmuone.v1.policy.claim.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosSupplyVo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 사유 등록,수정 요청 DTO")
public class SavePolicyClaimBosRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private Long psClaimBosId;

	@ApiModelProperty(value = "클레임 사유(대)")
	private Long lClaimCtgryId;

	@ApiModelProperty(value = "클레임 사유(중)")
	private Long mClaimCtgryId;

	@ApiModelProperty(value = "귀책처")
	private Long sClaimCtgryId;

	@ApiModelProperty(value = "삭제여부")
	private String delYn;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "공급업체(반품회수) 사유")
    private List<PolicyClaimBosSupplyVo> claimSupplierList;

	@ApiModelProperty(value = "공급업체(반품미회수) 사유")
    private List<PolicyClaimBosSupplyVo> nonClaimSupplierList;


}
