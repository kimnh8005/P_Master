package kr.co.pulmuone.v1.batch.order.escrow.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EscrowRegistDeliveryDto {

	@ApiModelProperty(value = "주문결제 마스터 PK")
	private Long odPaymentMasterId;

	@ApiModelProperty(value = "PG종류 공통코드(PG_ACCOUNT_TYPE)")
	private String pgService;

	@ApiModelProperty(value = "주문 ID")
	private String odid;

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

    @ApiModelProperty(value = "거래번호")
	private String tid;

    @ApiModelProperty(value = "결제금액")
	private int paymentPrice;

    @ApiModelProperty(value = "송장번호")
	private String trackingNo;

    @ApiModelProperty(value = "배송등록자")
	private String registTrakingNoUserName;

    @ApiModelProperty(value = "택배사명")
	private String shippingCompanyName;

    @ApiModelProperty(value = "이니시스 택배사 코드")
	private String inicisSshippingCompanyCode;

    @ApiModelProperty(value = "배송등록일자")
	private LocalDateTime registTrackingNoDate;

    @ApiModelProperty(value = "수신자 이름")
	private String receiverName;

    @ApiModelProperty(value = "수신자 전화번호")
	private String receiverMobile;

    @ApiModelProperty(value = "수신자 우편번호")
	private String receiverZipCode;

    @ApiModelProperty(value = "수신자 주소 1")
	private String receiverAddress1;

}
