package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * PG 대사 상세내역 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Setter
@Getter
@ToString
@ApiModel(description = "PG 대사 상세내역 조회 Request Dto")
public class CalPgDetlListDto {

    @ApiModelProperty(value = "업로드 PG구분")
    private String uploadPgServiceNm;

    @ApiModelProperty(value = "업로드 구분")
    private String uploadTypeNm;

    @ApiModelProperty(value = "업로드 주문번호")
    private String uploadOdid;

    @ApiModelProperty(value = "업로드 TID")
    private String uploadTid;

    @ApiModelProperty(value = "결제일자/환불일자")
    private String uploadApprovalDt;

    @ApiModelProperty(value = "결제금액/환불금액")
    private Long transAmt;

    @ApiModelProperty(value = "공제금액")
    private Long deductAmt;

    @ApiModelProperty(value = "정산금액")
    private Long accountAmt;

    @ApiModelProperty(value = "PG구분")
    private String pgServiceNm;

    @ApiModelProperty(value = "구분")
    private String typeNm;

    @ApiModelProperty(value = "결제 수단")
    private String payTpNm;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문일자")
    private String createDt;

    @ApiModelProperty(value = "승인일자")
    private String approvalDt;

    @ApiModelProperty(value = "수량")
    private Integer cnt;

    @ApiModelProperty(value = "주문금액")
    private Long salePrice;

    @ApiModelProperty(value = "배송비 금액")
    private Long shippingPrice;

    @ApiModelProperty(value = "쿠폰할인금액")
    private Long couponPrice;

    @ApiModelProperty(value = "적립금")
    private Long pointPrice;

    @ApiModelProperty(value = "결제금액")
    private Long paymentPrice;

}
