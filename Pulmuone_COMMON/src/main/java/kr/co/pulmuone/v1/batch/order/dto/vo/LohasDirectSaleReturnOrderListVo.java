package kr.co.pulmuone.v1.batch.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 풀무원건강생활(LDS) 반품 주문 List vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "LohasDirectSaleReturnOrderListVo")
public class LohasDirectSaleReturnOrderListVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "주문 클레임 PK")
    private String odClaimId;

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private String odClaimDetlId;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 전화번호")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 휴대폰번호")
    private String buyerHp;

    @ApiModelProperty(value = "수령자명")
    private String recvNm;

    @ApiModelProperty(value = "수령자 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String recvAddr;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String recvAddr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String recvAddr2;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "배송비")
    private String shippingPrice;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

    @ApiModelProperty(value = "클레임수량")
    private Integer claimCnt;

    @ApiModelProperty(value = "결제일시")
    private String payOutDt;

    @ApiModelProperty(value = "판매금액")
    private double salePrice;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문자 유형 : 공통코드(BUYER_TYPE)")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문 유형 : 공통코드(AGENT_TYPE)")
    private String agentTypeCd;

    @ApiModelProperty(value = "시퀀스번호")
    private String seqNo;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

}
