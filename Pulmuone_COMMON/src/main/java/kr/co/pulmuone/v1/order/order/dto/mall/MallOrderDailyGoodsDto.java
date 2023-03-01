package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송 주문 상품 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 29.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 일일배송 주문 상품 리스트 Dto")
public class MallOrderDailyGoodsDto {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "기획전PK")
	private long evExhibitCd;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품이미지")
	private String thumbnailPath;

	@ApiModelProperty(value = "일일상품유형")
	private String goodsDailyTp;

	@ApiModelProperty(value = "주문상태코드")
	private String orderStatusCd;

	@ApiModelProperty(value = "FRONT JSON")
	private String frontJson;

	@ApiModelProperty(value = "ACTION JSON")
	private String actionJson;

	@ApiModelProperty(value = "일일배송주문상세PK")
	private String odOrderDetlDailyId;

	@ApiModelProperty(value = "배송주기코드")
	private String goodsCycleTp;

	@ApiModelProperty(value = "배송주기코드명")
	private String goodsCycleTpNm;

	@ApiModelProperty(value = "배송기간코드")
	private String goodsCycleTermTp;

	@ApiModelProperty(value = "배송기간코드명")
	private String goodsCycleTermTpNm;

	@ApiModelProperty(value = "알러지식단여부")
	private String allergyYn;

	@ApiModelProperty(value = "주문배송지PK")
	private long odShippingZoneId;

	@ApiModelProperty(value = "수령인명")
	private String recvNm;

	@ApiModelProperty(value = "수령인 우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "수령인주소1")
	private String recvAddr1;

	@ApiModelProperty(value = "수령인주소2")
	private String recvAddr2;

	@ApiModelProperty(value = "수령인 빌딩번호")
	private String recvBldNo;

	@ApiModelProperty(value = "도착예정일자")
	private String deliveryDt;

	@ApiModelProperty(value = "배송지변경이력여부")
	private String recvChgHistYn;

	@ApiModelProperty(value = "요일명")
	private String weekDayNm;

	@ApiModelProperty(value = "변경가능 첫 배송일자")
	private String firstDeliveryDt;

	@ApiModelProperty(value = "요일목록")
	private List<MallOrderDailyDaysDto> daysList;
}