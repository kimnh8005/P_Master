package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 현금영수증 발급내역 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 18.            천혜현        최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "현금영수증 발급 Request Dto")
public class CashReceiptIssueRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "PG 유형")
    private String pgService;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문결제 PK")
    private Long odPaymentMasterId;

    @ApiModelProperty(value = "회원 PK")
    private String urUserId;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "현금 영수증 타입")
    private String receiptType;

    @ApiModelProperty(value = "현금영수증 식별번호")
    private String regNumber;

    @ApiModelProperty(value = "결제금액")
    private int paymentPrice;

    @ApiModelProperty(value = "과세금액")
    private int taxablePrice;

}

