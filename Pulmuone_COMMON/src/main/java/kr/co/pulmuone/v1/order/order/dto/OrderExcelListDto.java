package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 엑셀 다운로드 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 08.    김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "OrderExcelListDto")
public class OrderExcelListDto {

    @ApiModelProperty(value = "주문등록일")
    private String orderCreateDt;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자연락처")
    private String buyerHp;

    @ApiModelProperty(value = "판매처명")
    private String sellersNm;

    @ApiModelProperty(value = "수취인명")
    private String recvNm;

    @ApiModelProperty(value = "수취인연락처")
    private String recvHp;

    @ApiModelProperty(value = "주소1")
    private String recvAddr1;

    @ApiModelProperty(value = "주소2")
    private String recvAddr2;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문금액")
    private String orderPrice;

    @ApiModelProperty(value = "배송비합계")
    private String shippingPrice;

    @ApiModelProperty(value = "쿠폰할인합계")
    private String couponPrice;

    @ApiModelProperty(value = "결제금액")
    private String paidPrice;

    @ApiModelProperty(value = "결제수단명")
    private String orderPaymentType;

    @ApiModelProperty(value = "주문유형")
    private String agentType;

    @ApiModelProperty(value = "주문상태")
    private String statusNm;

    @ApiModelProperty(value = "클레임상태")
    private String claimStatusNm;

    @ApiModelProperty(value = "수집몰 주문번호")
    private String collectionMallId;
}
