package kr.co.pulmuone.batch.esl.domain.model.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 잇슬림 주문 header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "EatsslimOrderHeaderDto")
public class EatsslimOrderHeaderDto {

    @ApiModelProperty(value = "풀샵 주문번호")
    private String outOrderNum;

    @ApiModelProperty(value = "주문경로")
    private String ordChannelCd;

    @ApiModelProperty(value = "주문자명")
    private String orderName;

    @ApiModelProperty(value = "받는사람명")
    private String recvName;

    @ApiModelProperty(value = "받는사람우편번호")
    private String recvZipcode;

    @ApiModelProperty(value = "받는사람 주소1")
    private String recvAddr1;

    @ApiModelProperty(value = "받는사람 주소2")
    private String recvAddr2;

    @ApiModelProperty(value = "받는사람 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "받는사람 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "배송메세지")
    private String recvReqMsg;
    
    @ApiModelProperty(value = "판매가격")
    private String goodsPrice;

    @ApiModelProperty(value = "배송비")
    private String devlPrice;

    @ApiModelProperty(value = "쿠폰비")
    private String couponPrice;

    @ApiModelProperty(value = "실 결제비용")
    private String payPrice;

    @ApiModelProperty(value = "주문일")
    private String orderDate;

    @ApiModelProperty(value = "채널상품코드")
    private String groupCode;

    @ApiModelProperty(value = "배송방법")
    private String delvType;

    @ApiModelProperty(value = "가맹점코드")
    private String jisaCd;

    @ApiModelProperty(value = "주문수량")
    private String orderQty;

    @ApiModelProperty(value = "첫배송일")
    private String delvFirstDate;

    @ApiModelProperty(value = "배송기간 일")
    private String devlDay;

    @ApiModelProperty(value = "배송기간 주")
    private String devlWeek;

    @ApiModelProperty(value = "보낭가방 여부")
    private String buyBagYn;

    @ApiModelProperty(value = "증정채널상품코드")
    private String giftGroupCode;

    @ApiModelProperty(value = "증정 수량")
    private String giftOrderQty;

    @ApiModelProperty(value = "증정 배송일")
    private String giftDevlDay;

    @ApiModelProperty(value = "체험단여부")
    private String experienceGroup;
    
    @ApiModelProperty(value = "공동현관 비밀번호")
    private String doorMsg;
    
    @ApiModelProperty(value = "공동현관 출입방법")
    private String doorMsgDtl;

    @Builder
    public EatsslimOrderHeaderDto(String outOrderNum, String ordChannelCd, String orderName, String recvName, String recvZipcode,
                                  String recvAddr1, String recvAddr2, String recvHp, String recvTel, String recvReqMsg,
                                  String goodsPrice, String payPrice, String orderDate, String groupCode, String delvType,
                                  String jisaCd, String orderQty, String delvFirstDate, String devlDay, String devlWeek,
                                  String couponPrice, String doorMsg, String doorMsgDtl
    ) {
        this.outOrderNum = outOrderNum;
        this.ordChannelCd = ordChannelCd;
        this.orderName = orderName;
        this.recvName = recvName;
        this.recvZipcode = recvZipcode;
        this.recvAddr1 = recvAddr1;
        this.recvAddr2 = recvAddr2;
        this.recvHp = recvHp;
        this.recvTel = recvTel;
        this.recvReqMsg = recvReqMsg;
        this.goodsPrice = goodsPrice;
        this.devlPrice = "0";
        this.couponPrice = couponPrice;
        this.payPrice = payPrice;
        this.orderDate = orderDate;
        this.groupCode = groupCode;
        this.delvType = delvType;
        this.jisaCd = jisaCd;
        this.orderQty = orderQty;
        this.delvFirstDate = delvFirstDate;
        this.devlDay = devlDay;
        this.devlWeek = devlWeek;
        this.buyBagYn = "N";
        this.giftGroupCode = "0";
        this.giftOrderQty = "0";
        this.giftDevlDay = "0";
        this.experienceGroup = "N";
        this.doorMsg = doorMsg;
        this.doorMsgDtl = doorMsgDtl;
    }

}
