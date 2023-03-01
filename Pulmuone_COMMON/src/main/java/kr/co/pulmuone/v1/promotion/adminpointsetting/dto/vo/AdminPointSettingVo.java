package kr.co.pulmuone.v1.promotion.adminpointsetting.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 적립금 한도 설정 정보 List Result")
public class AdminPointSettingVo {


	@ApiModelProperty(value = "적립금 관리자 사용설정 ID")
    private String pmPointAdminSettingId;

    @ApiModelProperty(value = "설정타입")
    private String settingType;

    @ApiModelProperty(value = "적립금 금액")
    private String amount;

    @ApiModelProperty(value = "관리자별 적립금 금액")
    private String amountIndividual;

    @ApiModelProperty(value = "유효기간")
    private String validityDay;

    @ApiModelProperty(value = "역할Id")
    private String stRoleTpId;

    @ApiModelProperty(value = "역할 명")
    private String roleName;

    @ApiModelProperty(value = "등록 정보")
    private String createInfo;

    @ApiModelProperty(value = "수정 정보")
    private String modifyInfo;

    @ApiModelProperty(value = "관리자명")
    @UserMaskingUserName
    private String adminName;

    @ApiModelProperty(value = "로그인 ID")
    private String loginId;

    @ApiModelProperty(value = "역할그룹 ID")
    private String roleGroup;

    @ApiModelProperty(value = "조직코드")
    private String erpOrganizationCd;

	@ApiModelProperty(value = "등록자명")
	@UserMaskingUserName
	private String createName;

	@ApiModelProperty(value = "등록자 ID")
	private String createLoginId;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "수정자 명")
	@UserMaskingUserName
	private String modifyName;

	@ApiModelProperty(value = "수정자 ID")
	private String modifyLoginId;

	@ApiModelProperty(value = "수정일")
	private String modifyDate;

}
