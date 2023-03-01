package kr.co.pulmuone.v1.calculate.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 지원금 정산 확정 대상 VO
 * </PRE>
 */
@Builder
@Getter
@ApiModel(description = "CalculateConfirmProcVo")
public class CalculateConfirmProcVo {

    @ApiModelProperty(value = "조직코드")
    private String erpOrganizationCd;

    @ApiModelProperty(value = "회계법인코드")
    private String finRegalCd;

    @ApiModelProperty(value = "회계조직코드")
    private String finOrganizationCd;

    @ApiModelProperty(value = "계정과목코드")
    private String finAccountCd;

    @ApiModelProperty(value = "ERP장부ID")
    private String sob;

    @ApiModelProperty(value = "일반회원 총 판매금액")
    private int totalSalePrice;

    @ApiModelProperty(value = "임직원 총 할인금액")
    private int totalEmployeeDiscountPrice;

    @ApiModelProperty(value = "임직원 총 판매금액")
    private int totalEmployeePrice;

    @ApiModelProperty(value = "정산월")
    private String settleMonth;

    @ApiModelProperty(value = "OU코드")
    private String ouId;

    @ApiModelProperty(value = "생성일")
    private String createDt;

    @ApiModelProperty(value = "전표발행 총금액")
    private long debitSidePrice;

    @ApiModelProperty(value = "차변번호")
    private int debitSideNo;

    @ApiModelProperty(value = "임직원 회계법인코드 Temp")
    private int finRegalCdTemp;

    @ApiModelProperty(value = "정산종료일자")
    private String endDt;

    @ApiModelProperty(value = "대변 부서코드")
    private String creDepCd;

}
