package kr.co.pulmuone.v1.order.schedule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 스케줄 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 29.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 스케줄 리스트 Dto")
public class OrderDetailScheduleListDto {

	@ApiModelProperty(value = "I/F 상품 코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "배송일자(요일)")
	private String delvDateWeekDay;

	@ApiModelProperty(value = "배송일자")
	private String delvDate;

	@ApiModelProperty(value = "배송일자")
	private String orgDelvDate;

	@ApiModelProperty(value = "주문수량")
	private String orderCnt;

	@ApiModelProperty(value = "증정타입(잇슬림용)")
	private String stateDetail;

	@ApiModelProperty(value = "증정타입명(잇슬림용)")
	private String stateDetailNm;

	@ApiModelProperty(value = "가맹점코드")
	private String urStoreId;

	@ApiModelProperty(value = "가맹점명")
	private String urStoreNm;

	@ApiModelProperty(value = "수령인 우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "수령인 주소1")
	private String recvAddr1;

	@ApiModelProperty(value = "수령인 주소2(상세주소)")
	private String recvAddr2;

	@ApiModelProperty(value = "구분1(잇슬림용)")
	private String gubun1;

	@ApiModelProperty(value = "구분2(잇슬림용)")
	private String gubun2;

	@ApiModelProperty(value = "배송 스케줄 고유ID(PK)")
	private String id;

	@ApiModelProperty(value = "주문 상세 일일배송 패턴 라인번호")
	private int odOrderDetlDailySchSeq;

	@ApiModelProperty(value = "주문 상세 일일배송 패턴 PK")
	private long odOrderDetlDailyId;

	@ApiModelProperty(value = "배송완료 여부")
	private String deliveryYn;

	@ApiModelProperty(value = "상품이미지")
	private String goodsImgNm;

	@ApiModelProperty(value = "주문 상세 일일배송 스케줄 PK")
	private long odOrderDetlDailySchId;

	@ApiModelProperty(value = "주문배송지 PK")
	private long odShippingZoneId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

}