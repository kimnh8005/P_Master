package kr.co.pulmuone.v1.outmall.order.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CollectionMallInterfaceFailVo {

    @ApiModelProperty(value = "실패사유")
    private String errorMessage;

    @ApiModelProperty(value = "합포번호")
    private String pack;

    @ApiModelProperty(value = "관리번호")
    private String seq;

    @ApiModelProperty(value = "외부몰코드")
    private String shopId;

    @ApiModelProperty(value = "상품코드")
    private String shopProductId;

    @ApiModelProperty(value = "품목 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "상품명")
    private String productName;

    @ApiModelProperty(value = "수량")
    private String qty;

    @ApiModelProperty(value = "상품총금액")
    private String amount;

    @ApiModelProperty(value = "주문자명")
    private String orderName;

    @ApiModelProperty(value = "주문자연락처")
    private String orderTel;

    @ApiModelProperty(value = "주문자휴대번호")
    private String orderMobile;

    @ApiModelProperty(value = "수취인명")
    private String receiverName;

    @ApiModelProperty(value = "수취인연락처")
    private String receiverTel;

    @ApiModelProperty(value = "수취인휴대번호")
    private String receiverMobile;

    @ApiModelProperty(value = "수취인우편번호")
    private String receiverZip;

    @ApiModelProperty(value = "수취인주소1")
    private String receiverAddress1;

    @ApiModelProperty(value = "수취인주소2")
    private String receiverAddress2;

    @ApiModelProperty(value = "배송비")
    private String shippingPrice;

    @ApiModelProperty(value = "메세지")
    private String memo;

    @ApiModelProperty(value = "택배사")
    private String logisticsCd;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "외부몰주문번호")
    private String orderId;

    @ApiModelProperty(value = "외부몰주문상세번호1")
    private String orderIdSeq;

    @ApiModelProperty(value = "외부몰주문상세번호2")
    private String orderIdSeq2;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMessage;

    @ApiModelProperty(value = "외부몰주문일")
    private String outmallOrderDt;

    @ApiModelProperty(value = "외부몰상품명")
    private String outmallGoodsNm;

    @ApiModelProperty(value = "외부몰옵션명")
    private String outmallOptNm;

}
