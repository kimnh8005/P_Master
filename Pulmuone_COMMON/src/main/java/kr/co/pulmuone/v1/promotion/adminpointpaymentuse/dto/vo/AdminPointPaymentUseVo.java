package kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 적립금 설정 지급/차감 내역 List Result")
public class AdminPointPaymentUseVo {

	@ApiModelProperty(value = "적립금 상세구분 명")
    private String pointDetailType;

	@ApiModelProperty(value = "적립금 상세구분")
    private String pointDetailTypeName;

    @ApiModelProperty(value = "관리자 조직명")
    private String erpOrganizationName;

    @ApiModelProperty(value = "적립금 지급차감 관리자 명")
    @UserMaskingUserName
    private String adminUserName;

    @ApiModelProperty(value = "적립금 지급차감 관리자 ID")
    @UserMaskingLoginId
    private String adminUserId;

    @ApiModelProperty(value = "적립금 사용 회원 ID")
    @UserMaskingLoginId
    private String urUserId;

    @ApiModelProperty(value = "적립금 사용 회원 명")
    @UserMaskingUserName
    private String urUserName;

    @ApiModelProperty(value = "적립금 구분 타입")
    private String paymentType;

    @ApiModelProperty(value = "적립급 구분 타입명")
    private String paymentTypeName;

    @ApiModelProperty(value = "적립금 지급차감 액")
    private String issueValue;

    @ApiModelProperty(value = "적립금 지급차감 유효일")
    private String validityDay;

    @ApiModelProperty(value = "적립금 지급차감 일자")
    private String pointUseDate;

    @ApiModelProperty(value = "적립금 지급차감 사유")
    private String pointUsedMsg;

    @ApiModelProperty(value = "적립금 설정 ID")
    private String pmPointId;

    @ApiModelProperty(value = "Excel No")
    private String noExcel;

    @ApiModelProperty(value = "순번")
    private String rowNumber;
}
