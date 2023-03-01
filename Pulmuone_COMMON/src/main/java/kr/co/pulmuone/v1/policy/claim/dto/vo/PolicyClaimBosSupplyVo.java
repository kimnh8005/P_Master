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
@ApiModel(description = "BOS 클레임 사유 공급업체 vo")
public class PolicyClaimBosSupplyVo extends BaseRequestPageDto{

	@ApiModelProperty(value = "BOS 클레임 사유 공급업체 PK")
	private Long psClaimBosSupplyId;

	@ApiModelProperty(value = "BOS 클레임 사유 PK")
	private Long psClaimBosId;

	@ApiModelProperty(value = "반품회수-공급업체 코드")
	private String supplierCode;

	@ApiModelProperty(value = "반품회수-클레임 사유 코드")
	private String claimCode;

	@ApiModelProperty(value = "반품미회수-공급업체 코드")
	private String nonSupplierCode;

	@ApiModelProperty(value = "반품미회수-클레임 사유 코드")
	private String nonClaimCode;

	@ApiModelProperty(value = "공급업체명")
	private String supplierName;

	@ApiModelProperty(value = "반품회수 클레임사유")
	private String claimName;

	@ApiModelProperty(value = "반품미회수 클레임사유")
	private String nonClaimName;
}
