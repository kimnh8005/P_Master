package kr.co.pulmuone.v1.policy.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 공급업체별 사유 카테고리 vo")
public class PolicyClaimSupplyCtgryVo extends BaseRequestPageDto{

	@ApiModelProperty(value = "BOS 클레임 공급업체별 사유 카테고리 PK")
	private Long psClaimSupplyCtgryId;

	@ApiModelProperty(value = "공급업체 코드")
	private String supplierCode;

	@ApiModelProperty(value = "클레임사유 코드")
	private String claimCode;

	@ApiModelProperty(value = "클레임사유 명")
	private String claimName;

	@ApiModelProperty(value = "삭제여부")
	private String delYn;

}
