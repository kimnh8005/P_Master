package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <PRE>
 * Forbiz Korea
 * 외부몰 주문 대사 조회 Upload Dto
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "외부몰 주문 대사 조회 Upload Dto")
public class CalOutmallUploadDto {

    @ApiModelProperty(value = "A")
    private String a;

    @ApiModelProperty(value = "B")
    private String b;

    @ApiModelProperty(value = "외부몰주문번호")
    private String outmallId;

    @ApiModelProperty(value = "판매처 PK")
    private String omSellersId;

    @ApiModelProperty(value = "판매처명")
    private String sellersNm;

    @ApiModelProperty(value = "매출금액")
    private String orderAmt;

    @ApiModelProperty(value = "판매수량")
    private String orderCnt;

    @ApiModelProperty(value = "결제일자")
    private String icDt;

    @ApiModelProperty(value = "주문일자")
    private String orderIfDt;

    @ApiModelProperty(value = "매출확정일자")
    private String settleDt;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "에누리금액")
    private String discountPrice;

    @ApiModelProperty(value = "쿠촘금액")
    private String couponPrice;

    @ApiModelProperty(value = "기타공제금액")
    private String etcDiscountPrice;

    @ApiModelProperty(value = "최종매출금액")
    private String settlePrice;

    @ApiModelProperty(value = "최종판매수량")
    private String settleItemCnt;

    @ApiModelProperty(value = "PK")
    private Long odOutMallCompareUploadInfoId;

    @ApiModelProperty(value = "등록자 ID")
    private Long createId;

    @ApiModelProperty(value = "성공여부")
    private boolean success;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "업로드성공여부")
    private String successYn;



}
