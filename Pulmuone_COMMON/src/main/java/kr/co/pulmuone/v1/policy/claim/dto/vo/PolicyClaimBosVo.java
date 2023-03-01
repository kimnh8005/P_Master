package kr.co.pulmuone.v1.policy.claim.dto.vo;

import java.util.List;

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
@ApiModel(description = "BOS 클레임 사유 vo")
public class PolicyClaimBosVo{

	@ApiModelProperty(value = "BOS 클레임 사유 PK")
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

	@ApiModelProperty(value="등록자", required = true)
    private String createId;

    @ApiModelProperty(value="등록일", required = true)
    private String createDate;

    @ApiModelProperty(value="수정자", required = true)
    private String modifyId;

    @ApiModelProperty(value="수정일", required = true)
    private String modifyDate;

    @ApiModelProperty(value = "등록자")
    @UserMaskingUserName
    private String createUserName;

    @ApiModelProperty(value = "수정자")
    @UserMaskingUserName
    private String modifyUserName;

	@ApiModelProperty(value = "공급업체 사유(반품회수) 리스트")
    private List<PolicyClaimBosSupplyVo> claimSupplierList;

	@ApiModelProperty(value = "공급업체 사유(반품 미회수) 리스트")
    private List<PolicyClaimBosSupplyVo> nonClaimSupplierList;

    @ApiModelProperty(value = "쇼핑몰 클레임 사유 목록")
    //private String mallClaimReason;
    private List<PolicyClaimMallVo> mallClaimReasonList;

	@ApiModelProperty(value = "클레임 사유(대) 사유명")
    private String lclaimCtgryName;

	@ApiModelProperty(value = "클레임 사유(중) 사유명")
    private String mclaimCtgryName;

	@ApiModelProperty(value = "귀책처 사유명")
    private String sclaimCtgryName;

	@ApiModelProperty(value = "귀책 유형")
    private String targetType;
}
