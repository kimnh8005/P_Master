package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "OrderDto")
public class OrderDto {


    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "판매처 PK")
    private long omSellersId;

    @ApiModelProperty(value = "외부몰주문번호")
    private String outmallId;

    @ApiModelProperty(value = "회원그룹아이디")
    private long urGroupId;

    @ApiModelProperty(value = "회원그룹명")
    private String urGroupNm;

    @ApiModelProperty(value = "회원아이디")
    private long urUserId;

    @ApiModelProperty(value = "임직원사번")
    private String urEmployeeCd;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "주문자 명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 핸드폰")
    private String buyerHp;

    @ApiModelProperty(value = "주문자 연락처")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 이메일")
    private String buyerMail;

    @ApiModelProperty(value = "주문 상품명")
    private String goodsNm;

    @ApiModelProperty(value = "판매처그룹")
    private String sellersGroupCd;

    @ApiModelProperty(value = "결제수단")
    private String orderPaymentType;

    @ApiModelProperty(value = "주문자 유형 : 공통코드(BUYER_TYPE)")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문 유형 : 공통코드(AGENT_TYPE)")
    private String agentTypeCd;

    @ApiModelProperty(value = "주문경로 ERP I/F 주문경로 필요")
    private String orderHpnCd;

    @ApiModelProperty(value = "일반주문: N, 선물하기주문: Y")
    private String giftYn;

    @ApiModelProperty(value = "사용자환경정보")
    private String urPcidCd;

    @ApiModelProperty(value = "주문금액")
    private String orderPrice;

    @ApiModelProperty(value = "배송비합계")
    private String shippingPrice;

    @ApiModelProperty(value = "쿠폰할인합계")
    private String couponPrice;

    @ApiModelProperty(value = "결제금액")
    private String paidPrice;


}
