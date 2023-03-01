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
 * Mall 일일배송 주문 리스트 Dto
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
@ApiModel(description = "Mall 일일배송 주문 리스트 Dto")
public class MallOrderDailyListDto {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문상세 뎁스")
	private long odOrderDetlDepthId;

	@ApiModelProperty(value = "주문상세 부모값")
	private long odOrderDetlParentId;

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

	@ApiModelProperty(value = "월요일수량")
	private int monCnt;

	@ApiModelProperty(value = "화요일수량")
	private int tueCnt;

	@ApiModelProperty(value = "수요일수량")
	private int wedCnt;

	@ApiModelProperty(value = "목요일수량")
	private int thuCnt;

	@ApiModelProperty(value = "금요일수량")
	private int friCnt;

	@ApiModelProperty(value = "토요일수량")
	private int setCnt;

	@ApiModelProperty(value = "알러지식단여부")
	private String allergyYn;

	@ApiModelProperty(value = "일괄배송여부")
	private String dailyBulkYn;

	@ApiModelProperty(value = "스케쥴생성여부 - 베이비밀은 I/F 여부")
	private String scheduleYn;

	@ApiModelProperty(value = "I/F연동여부")
	private String ifYn;

	@ApiModelProperty(value = "주문I/F일자")
	private String orderIfDt;

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

	@ApiModelProperty(value = "첫도착예정일자")
	private String firstDeliveryDt;

	@ApiModelProperty(value = "배송지변경이력여부")
	private String recvChgHistYn;

	@ApiModelProperty(value = "요일명")
	private String weekDayNm;

	@ApiModelProperty(value = "골라담기 구분 C:균일가 골라담기 G:녹즙골라담기 이외 일반")
	private String packageGbn;

	@ApiModelProperty(value = "프로모션 타입(균일가 골라담기, 녹즙골라담기)")
	private String promotionType;

	@ApiModelProperty(value = "배송장소 코드")
	private String storeDeliveryTp;


	@ApiModelProperty(value = "녹즙 골라담기 월요일")
	private List<MallOrderDailyListDto> pickMonList;

	@ApiModelProperty(value = "녹즙 골라담기 화요일")
	private List<MallOrderDailyListDto> pickTueList;

	@ApiModelProperty(value = "녹즙 골라담기 수요일")
	private List<MallOrderDailyListDto> pickWedList;

	@ApiModelProperty(value = "녹즙 골라담기 목요일")
	private List<MallOrderDailyListDto> pickThuList;

	@ApiModelProperty(value = "녹즙 골라담기 금요일")
	private List<MallOrderDailyListDto> pickFriList;

	@ApiModelProperty(value = "후기 작성 갯수")
	private int feedbackWriteCnt;

	@ApiModelProperty(value = "배송중 이후 날짜")
	private int feedbackWriteUseDay;

	@ApiModelProperty(value = "주문타입(order:정상주문, claim:클레임주문)")
	private String orderType;

	@ApiModelProperty(value = "건강기능식품 여부")
	private String healthGoodsYn;

}