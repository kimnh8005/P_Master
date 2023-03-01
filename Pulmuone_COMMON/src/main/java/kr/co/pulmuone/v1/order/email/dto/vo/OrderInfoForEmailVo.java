package kr.co.pulmuone.v1.order.email.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 자동메일 발송위한 주문 정보 vo")
public class OrderInfoForEmailVo {

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "이메일")
    private String mail;

    @ApiModelProperty(value = "휴대폰")
    private String mobile;

    @ApiModelProperty(value = "주문자명")
    private String buyerName;

    @ApiModelProperty(value = "주문일자")
    private String createDate;

    @ApiModelProperty(value = "주문일시")
    private String createDateTime;

    @ApiModelProperty(value = "결제일자")
    private String icDate;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "결제타입")
    private String payType;

    @ApiModelProperty(value = "결제타입명")
    private String payTypeName;

    @ApiModelProperty(value = "결제방법")
    private String payInfo;

    @ApiModelProperty(value = "은행명")
    private String bankName;

    @ApiModelProperty(value = "가상계좌 번호")
    private String virtualAccountNumber;

    @ApiModelProperty(value = "입금자명")
    private String paidHolder;

    @ApiModelProperty(value = "결제정보")
    private String info;

    @ApiModelProperty(value = "카드 할부기간")
    private String cardQuota;

    @ApiModelProperty(value = "입금기한")
    private String paidDueDate;

    @ApiModelProperty(value = "결제예정금액")
    private int paymentPrice;

    @ApiModelProperty(value = "주문금액(배송비 포함) OD_PAYMENT_MASTER.SALE_PRICE")
    private int salePrice;

    @ApiModelProperty(value = "총 할인금액")
    private int discountPrice;

    @ApiModelProperty(value = "적립금금액")
    private int pointPrice;

    @ApiModelProperty(value = "주문금액 (정가 + 배송비)")
    private int totalSalePrice;

	@ApiModelProperty(value = "수령인명")
    private String recvNm;

	@ApiModelProperty(value = "수령인 주소")
    private String recvAddr;

    @ApiModelProperty(value = "수령인핸드폰")
    private String recvHp;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

	/* 상품발송 */
    @ApiModelProperty(value = "택배사명")
    private String shippingCompName;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "배송추척 URL")
    private String trackingUrl;

    @ApiModelProperty(value = "발송일자")
    private String diDate;

    @ApiModelProperty(value = "주문 상세 PK 리스트")
    private List<Long> orderDetailGoodsList;

    /* 클레임 정보 */
    @ApiModelProperty(value = "클레임 PK")
    private String odClaimId;

    @ApiModelProperty(value = "취소,반품 사유")
    private String claimReasonMsg;

    @ApiModelProperty(value = "환불방법")
    private String refundType;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    @ApiModelProperty(value = "환불 적립금 금액")
    private int refundPointPrice;

    @ApiModelProperty(value = "주문금액 - 총 할인금액")
    private int paymentMinusDiscountPrice;

    @ApiModelProperty(value = "환불금액 정보")
    private String refundPriceInfo;

    @ApiModelProperty(value = "렌탈상품 count")
    private int rentalCount;

    @ApiModelProperty(value = "올가 상품 count")
    private int orgaGoodsCount;

    @ApiModelProperty(value = "쿠폰 사용여부")
    private String couponUse;

    @ApiModelProperty(value = "적립금 사용여부")
    private String pointUse;

    @ApiModelProperty(value = "주문유형")
    private String agentTypeCd;

    @ApiModelProperty(value = "매장 ID")
    private String urStoreId;

    @ApiModelProperty(value = "매장명")
    private String urStoreNm;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송시작시간")
    private String storeStartTime;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송종료시간")
    private String storeEndTime;

}
