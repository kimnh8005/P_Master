package kr.co.pulmuone.v1.policy.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "쇼핑몰 클레임 사유 vo")
public class PolicyClaimMallVo {

	@ApiModelProperty(value = "MALL 클레임 사유 PK")
	private Long psClaimMallId;

	@ApiModelProperty(value = "사유")
	private String reasonMessage;

	@ApiModelProperty(value = "사유구분")
	private String reasonType;

	@ApiModelProperty(value = "첨부파일 필수여부")
	private String attcRequiredYn;

	@ApiModelProperty(value = "BOS 클레임 사유 PK")
	private String psClaimBosId;

	@ApiModelProperty(value = "클레임 사유(대)")
	private String lclaimCtgryId;

	@ApiModelProperty(value = "클레임 사유(중)")
	private String mclaimCtgryId;

	@ApiModelProperty(value = "귀책처")
	private String sclaimCtgryId;

	@ApiModelProperty(value = "귀책유형")
	private String targetType;

	@ApiModelProperty(value = "BOS 클레임사유")
	private String bosClaimCtgry;

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

	@ApiModelProperty(value = "BOS 클레임 미출 사유 코드")
	private String psClaimBosVal;

}
