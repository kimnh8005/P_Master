package kr.co.pulmuone.batch.esl.domain.model.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 잇슬림 주문 List vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "EatsslimOrderListVo")
public class EatsslimOrderListVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "수령자명")
    private String recvNm;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String recvAddr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String recvAddr2;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "수령자 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;
    
    @ApiModelProperty(value = "출입정보")
    private String doorMsgNm;
    
    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;
    
    @ApiModelProperty(value = "정상판매금액")
    private int recommendedPrice;
    
    @ApiModelProperty(value = "판매가")
    private int salePrice;
    
    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "장바구니쿠폰 할인금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품쿠폰금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "즉시할인금액")
    private int directPrice;

    @ApiModelProperty(value = "결제금액")
    private int paidPrice;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "PDM 그룹코드")
    private String pdmGroupCd;

    @ApiModelProperty(value = "매장/가맹점 코드")
    private String urStoreId;

    @ApiModelProperty(value = "주문수량-주문취소수량")
    private int orderCancelCnt;

    @ApiModelProperty(value = "도착예정일자")
    private String deliveryDt;

    @ApiModelProperty(value = "주문자 유형")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문 유형")
    private String agentTypeCd;

    @ApiModelProperty(value = "배송주기")
    private String goodsCycleTp;

    @ApiModelProperty(value = "배송기간")
    private String goodsCycleTermTp;

}
