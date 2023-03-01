package kr.co.pulmuone.v1.order.claim.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 결제 마스터 OD_PAYMENT_MASTER VO")
public class OrderClaimPaymentMasterDto {
    @ApiModelProperty(value = "주문 결제 마스터 PK")
    private long odPaymentMasterId;

    @ApiModelProperty(value = "결제타입 (G : 결제, F : 환불 , A : 추가)")
    private String type;

    @ApiModelProperty(value = "결제상태(IR:입금예정,IC:입금완료)")
    private String status;

    @ApiModelProperty(value = "결제방법 공통코드(PAY_TP)")
    private String payTp;

    @ApiModelProperty(value = "PG 종류 공통코드(PG_SERVICE)")
    private String pgService;

    @ApiModelProperty(value = "거래 ID")
    private String tid;

    @ApiModelProperty(value = "승인코드")
    private String authCd;

    @ApiModelProperty(value = "카드번호")
    private String cardNumber;

    @ApiModelProperty(value = "카드 무이자 구분 (Y: 무이자, N: 일반)")
    private String cardQuotaInterest;

    @ApiModelProperty(value = "카드 할부기간")
    private String cardQuota;

    @ApiModelProperty(value = "가상계좌번호")
    private String virtualAccountNumber;

    @ApiModelProperty(value = "입금은행")
    private String bankNm;

    @ApiModelProperty(value = "결제 정보")
    private String info;

    @ApiModelProperty(value = "입금기한")
    private String paidDueDt;

    @ApiModelProperty(value = "입금자명")
    private String paidHolder;

    @ApiModelProperty(value = "부분취소 가능여부")
    private String partCancelYn;

    @ApiModelProperty(value = "에스크로결여부")
    private String escrowYn;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "장바구니 쿠폰 할인금액 합")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품 쿠폰 할인금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "즉시 할인금액 합")
    private int directPrice;

    @ApiModelProperty(value = "결제금액금액 합")
    private int paidPrice;

    @ApiModelProperty(value = "배송비합")
    private int shippingPrice;

    @ApiModelProperty(value = "과세결제금액")
    private int taxablePrice;

    @ApiModelProperty(value = "비과세결제금액")
    private int nonTaxablePrice;

    @ApiModelProperty(value = "결제금액")
    private int paymentPrice;

    @ApiModelProperty(value = "사용적립금금액")
    private int pointPrice;

    @ApiModelProperty(value = "정산번호")
    private String setlNo;

    @ApiModelProperty(value = "응답데이터")
    private String responseData;

    @ApiModelProperty(value = "승인 일자")
    private String approvalDt;

    @ApiModelProperty(value = "생성일자")
    private String createDt;

}
