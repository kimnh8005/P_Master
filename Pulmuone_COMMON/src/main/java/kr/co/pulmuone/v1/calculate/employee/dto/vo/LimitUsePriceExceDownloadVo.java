package kr.co.pulmuone.v1.calculate.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 한도 사용액 엑셀다운로드 VO
 * </PRE>
 */
@Builder
@Getter
@ApiModel(description = "LimitUsePriceExceDownloadVo")
public class LimitUsePriceExceDownloadVo {

    @ApiModelProperty(value = "임직원 사번")
    private String urErpEmployeeCd;

    @ApiModelProperty(value = "임직원명")
    private String erpNm;

    @ApiModelProperty(value = "소속법인명")
    private String erpRegalNm;

    @ApiModelProperty(value = "소속조직명")
    private String erpOrganizationNm;

    @ApiModelProperty(value = "임직원 할인금액")
    private int employeeDiscountPrice;

    @ApiModelProperty(value = "브랜드명")
    private String brandNm;

    @ApiModelProperty(value = "결제일자(환불일자)")
    private String settleDay;

}
