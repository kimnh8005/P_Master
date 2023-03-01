package kr.co.pulmuone.v1.calculate.employee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 지원금 정산 header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "EmployeeCalculateHeaderDto")
public class EmployeeCalculateHeaderDto {

    @ApiModelProperty(value = "입력시스템 코드값 ESHOP : 온라인몰", required = true)
    private String srcSvr;

    @ApiModelProperty(value = "AP 전표 생성용 세션값")
    private long sesId;

    @ApiModelProperty(value = "ERP의 OU ID")
    private String ouId;

    @ApiModelProperty(value = "생성월(YYYYMM)")
    private String perId;

    @ApiModelProperty(value = "생성일자(sysdate)")
    private String creDat;

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

    @Builder
    public EmployeeCalculateHeaderDto(String srcSvr, long sesId, String ouId, String perId, String creDat, String legCd, String linNo,
                                      String glDat, String invDat, String accDes, String invAmt, String supNo, String supNam,
                                      String payMet, String payTer, String payGrp, String creLegCd, String creDepCd, String creAccCd,
                                      String creOrg, String debLinNo, String debLinAmt, String debLegCd, String debDepCd,
                                      String debAccCd, String debOrg) {
        this.srcSvr = srcSvr;
        this.sesId = sesId;
        this.ouId = ouId;
        this.perId = perId;
        this.creDat = creDat;
        this.legCd = legCd;
        this.linNo = linNo;
        this.glDat = glDat;
        this.invDat = invDat;
        this.accDes = accDes;
        this.invAmt = invAmt;
        this.supNo = supNo;
        this.supNam = supNam;
        this.payMet = payMet;
        this.payTer = payTer;
        this.payGrp = payGrp;
        this.creLegCd = creLegCd;
        this.creDepCd = creDepCd;
        this.creAccCd = creAccCd;
        this.creOrg = creOrg;
        this.debLinNo = debLinNo;
        this.debLinAmt = debLinAmt;
        this.debLegCd = debLegCd;
        this.debDepCd = debDepCd;
        this.debAccCd = debAccCd;
        this.debOrg = debOrg;
    }

}
