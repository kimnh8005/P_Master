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
 * 임직원 포인트 사용 현황 조회 Dto
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
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "임직원 포인트 사용 현황 조회 Dto")
public class EmployeeUseListDto {

    @ApiModelProperty(value = "OU 명")
    private String ouNm;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "사번")
    private String urEmployeeCd;

    @ApiModelProperty(value = "소속")
    private String erpRegalNm;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세번호")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "상품코드")
    private long ilGoodsId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문상태")
    private String orderStatus;

    @ApiModelProperty(value = "수량")
    private int orderCnt;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "회사지원액")
    private int supportPrice;

    @ApiModelProperty(value = "주문일자")
    private String orderDt;

    @ApiModelProperty(value = "결제일자(환불이자)")
    private String paymentDt;

    @ApiModelProperty(value = "임직원 할인그룹")
    private String masterNm;

    @ApiModelProperty(value = "회사지원액 합계")
    private Long totalTaxablePrice;

}
