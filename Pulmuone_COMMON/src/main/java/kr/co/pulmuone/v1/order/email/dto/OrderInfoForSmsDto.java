package kr.co.pulmuone.v1.order.email.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 06.	천혜현		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "SMS 발송위한  주문 Dto")
public class OrderInfoForSmsDto {

    @ApiModelProperty(value = "이름")
    private String buyerName;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    /* 결제대기 */
    @ApiModelProperty(value = "계좌정보")
    private String virtualAccountInfo;

    @ApiModelProperty(value = "입금액")
    private int paymentPrice;

    @ApiModelProperty(value = "입금기한")
    private String paidDueDate;

    /* 결제완료 */
    @ApiModelProperty(value = "결제타입")
    private String payType;

    @ApiModelProperty(value = "결제정보 OD_PAYMENT_MASTER.INFO")
    private String info;

    @ApiModelProperty(value = "카드 할부기간")
    private String cardQuota;

    @ApiModelProperty(value = "결제방법")
    private String payInfo;

    /* 상품발송 */
	@ApiModelProperty(value = "수령인 주소")
    private String recvAddr;

    @ApiModelProperty(value = "택배사명")
    private String shippingCompName;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    /* 주문취소,반품 */
    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    /* 정기배송 */
    @ApiModelProperty(value = "배송주기")
    private String goodsCycle;

    @ApiModelProperty(value = "정기배송 결제금액")
    private int paymentPriceByRegular;

    /* 정기배송 결제실패*/
	@ApiModelProperty(value = "결제실패일자")
	private String paymentFailDate;

	@ApiModelProperty(value = "실패사유")
	private String paymentFailResponseMsg;

	@ApiModelProperty(value = "요청회차")
	private int reqRound;

	@ApiModelProperty(value = "전체회차")
	private int totalRound;

	/* 정기배송 건너뛰기 완료*/
	@ApiModelProperty(value = "결제일자")
	private String createDate;

	@ApiModelProperty(value = "도착예정일")
	private String arriveDt;

	/* 정기배송 상품금액 변동 */
	@ApiModelProperty(value = "도착예정일_(MM)")
	private String arriveDtMonth;

	@ApiModelProperty(value = "도착예정일_(DD)")
	private String arriveDtDay;

	@ApiModelProperty(value = "변경 전 결제금액")
	private int beforeSalePrice;

	@ApiModelProperty(value = "변경 후 결제금액")
	private int afterSalePrice;

	/* 매장픽업 상품 준비 */
    @ApiModelProperty(value = "매장명")
    private String urStoreNm;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송시작시간")
    private String storeStartTime;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송종료시간")
    private String storeEndTime;


}