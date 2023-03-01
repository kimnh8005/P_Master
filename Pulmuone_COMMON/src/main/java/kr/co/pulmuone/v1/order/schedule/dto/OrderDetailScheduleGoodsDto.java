package kr.co.pulmuone.v1.order.schedule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 스케줄 상품정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 02.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 스케줄 상품정보 Dto")
public class OrderDetailScheduleGoodsDto {

	@ApiModelProperty(value = "I/F 상품 코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "I/F 주문번호")
	private String orderNum;

	@ApiModelProperty(value = "I/F 서브주문번호")
	private String oSubNum;

	@ApiModelProperty(value = "배송요일")
	private String goodsDailyCycleTermDays;

	@ApiModelProperty(value = "배송시작일자")
	private String firstDeliveryDate;

	@ApiModelProperty(value = "최종배송예정일자")
	private String lastDeliveryDate;

	@ApiModelProperty(value = "결제수단 (10:신용카드, 20:계좌이체, 30:무통장 , 40:쿠폰)")
	private String orderPaymentType;

	@ApiModelProperty(value = "배송회차(남은회차)")
	private int saleSeq;

	@ApiModelProperty(value = "주문상세번호")
	private long odOrderDetlId;

	@ApiModelProperty(value = "알러지 식단여부")
	private String allergyYn;

	@ApiModelProperty(value = "월요일 수량")
	private int monCnt;

	@ApiModelProperty(value = "화요일 수량")
	private int tueCnt;

	@ApiModelProperty(value = "수요일 수량")
	private int wedCnt;

	@ApiModelProperty(value = "목요일 수량")
	private int thuCnt;

	@ApiModelProperty(value = "금요일 수량")
	private int friCnt;

	@ApiModelProperty(value = "스케줄 I/F 여부")
	private String scheduleIfYn;

	@ApiModelProperty(value = "배송요일 숫자")
	private String goodsDailyCycleTermDaysNum;

    @ApiModelProperty(value = "일일상품 유형(GOODS_DAILY_TP : GREENJUICE/BABYMEAL/EATSSLIM )")
    private String goodsDailyTp;

	@ApiModelProperty(value = "상품이미지")
	private String goodsImgNm;

	@ApiModelProperty(value = "내맘대로 여부")
	private String promotionYn;

	@ApiModelProperty(value = "배송 권역")
	private String scheduleDeliveryInterval;

    @ApiModelProperty(value = "수령인 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

	@ApiModelProperty(value = "출고처 PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "최초 주문 수량")
	private int orderCnt;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문번호")
	private long odid;

	@ApiModelProperty(value = "주문상태")
	private String orderStatusCd;

    @ApiModelProperty(value = "변경된 배송 패턴")
    private String dailyPattern;
}