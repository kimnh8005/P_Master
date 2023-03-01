package kr.co.pulmuone.v1.calculate.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 지원금 정산 엑셀다운로드 VO
 * </PRE>
 */
@Builder
@Getter
@ApiModel(description = "SupportPriceExceDownloadlVo")
public class SupportPriceExceDownloadlVo {

    @ApiModelProperty(value = "법인 구분")
    private String legCd;

    @ApiModelProperty(value = "전표 차변 Line 번호")
    private String linNo;

    @ApiModelProperty(value = "GL일자")
    private String glDat;

    @ApiModelProperty(value = "송장일자")
    private String invDat;

    @ApiModelProperty(value = "송장 적요")
    private String accDes;

    @ApiModelProperty(value = "전표 발행 총금액")
    private String invAmt;

    @ApiModelProperty(value = "공급자사업자번호")
    private String supNo;

    @ApiModelProperty(value = "공급자명")
    private String supNam;

    @ApiModelProperty(value = "지급방법")
    private String payMet;

    @ApiModelProperty(value = "결제조건")
    private String payTer;

    @ApiModelProperty(value = "지급그룹")
    private String payGrp;

    @ApiModelProperty(value = "대변 법인")
    private String creLegCd;

    @ApiModelProperty(value = "대변 부서코드")
    private String creDepCd;

    @ApiModelProperty(value = "대변 계정코드")
    private String creAccCd;

    @ApiModelProperty(value = "대변 사업부")
    private String creOrg;

    @ApiModelProperty(value = "차변 순번")
    private String debLinNo;

    @ApiModelProperty(value = "차변 공급가액")
    private String debLinAmt;

    @ApiModelProperty(value = "차변법인")
    private String debLegCd;

    @ApiModelProperty(value = "차변부서")
    private String debDepCd;

    @ApiModelProperty(value = "차변계정코드")
    private String debAccCd;

    @ApiModelProperty(value = "차변사업부")
    private String debOrg;

}
