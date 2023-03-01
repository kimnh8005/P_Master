package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 결제정보(결제) 리스트 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 14.            석세동         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 결제정보(결제) 리스트 관련 Dto")
public class OrderDetailPayListDto {

	@ApiModelProperty(value = "결제타입 G:결제 F:환불 A:추가")
	private String type;

    @ApiModelProperty(value = "연동PG")
    private String pgService;

    @ApiModelProperty(value = "결제방법")
    private String payType;

    @ApiModelProperty(value = "결제수단")
    private String payInfo;

    @ApiModelProperty(value = "주문상태")
    private String orderStatus;

    @ApiModelProperty(value = "주문일")
    private String createDt;

    @ApiModelProperty(value = "결제일")
    private String approvalDt;

    @ApiModelProperty(value = "결제금액")
    private int paidPrice;

    @ApiModelProperty(value = "PG결제금액")
    private int paymentPrice;
}
