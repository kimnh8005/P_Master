package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(description = "PG 대사 업로드 실패 조회Dto")
public class CalPgFailListDto {


    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "거래ID")
    private String tid;

    @ApiModelProperty(value = "승인일자")
    private String approvalDt;

    @ApiModelProperty(value = "결제금액/환불금액")
    private Long transAmt;

    @ApiModelProperty(value = "공제금액")
    private Long deductAmt;

    @ApiModelProperty(value = "정산금액")
    private Long accountAmt;

    @ApiModelProperty(value = "수수료")
    private Long commission;

    @ApiModelProperty(value = "부가세")
    private Long vat;

    @ApiModelProperty(value = "대금지급일")
    private String giveDt;

    @ApiModelProperty(value = "에스크로수수료")
    private Long escrowCommission;

    @ApiModelProperty(value = "에스크로부가세")
    private Long escrowVat;

    @ApiModelProperty(value = "M포인트 금액")
    private Long mPoint;

    @ApiModelProperty(value = "M포인트 수수료")
    private Long mPointCommission;

    @ApiModelProperty(value = "M포인트 부가세")
    private Long mPointVat;

    @ApiModelProperty(value = "마케팅 수수료")
    private Long marketingCommission;

    @ApiModelProperty(value = "마케팅 부가세")
    private Long marketingVat;

    @ApiModelProperty(value = "인증 수수료")
    private Long certificationCommission;

    @ApiModelProperty(value = "인증 부가세")
    private Long certificationVat;

    @ApiModelProperty(value = "무이자 수수료")
    private Long freeCommission;

    @ApiModelProperty(value = "카드명")
    private String cardNm;

    @ApiModelProperty(value = "카드 승인번호")
    private String cardAuthNum;

    @ApiModelProperty(value = "카드 할부개월수")
    private String cardQuota;

    @ApiModelProperty(value = "은행명")
    private String bankNm;

    @ApiModelProperty(value = "계좌번호")
    private String bankAccountNumber;

    @ApiModelProperty(value = "정산일자")
    private String accountDt;

}


