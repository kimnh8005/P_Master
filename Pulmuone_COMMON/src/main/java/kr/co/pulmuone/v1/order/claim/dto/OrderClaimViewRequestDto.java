package kr.co.pulmuone.v1.order.claim.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 정보 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 정보 조회 Request Dto")
public class OrderClaimViewRequestDto {

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문상태코드")
	private String orderStatusCd;

	@ApiModelProperty(value = "클레임구분 CLAIM_STATUS_TP.CANCEL : 취소, CLAIM_STATUS_TP.RETURN : 반품")
	private String claimStatusTp;

	@ApiModelProperty(value = "클레임주문상태코드")
	private String claimStatusCd;

	@ApiModelProperty(value = "변경상태코드")
	private String putOrderStatusCd;

	@ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자")
	private String targetTp;

	@ApiModelProperty(value = "회수여부")
	private String returnsYn;

	@ApiModelProperty(value = "쿠폰종류 상품(판매가지정도 포함) : G, 장바구니 :C ")
	private String couponTp;

	@ApiModelProperty(value = "조회구분 전체 : 0, 상품갯수변경 : 1")
	private int goodsChange;

	@ApiModelProperty(value = "프론트 구분 (0 : Bos, 1:Front)")
	private int frontTp;

	@ApiModelProperty(value = "주문상품 갯수 변경 데이터 ")
	private String goodSearch;
	
	@ApiModelProperty(value = "보내는사람 우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "주문상품 갯수 변경 dto ")
	List<OrderClaimSearchGoodsDto> goodSearchList;

	@ApiModelProperty(value = "상품스케쥴정보 리스트")
	List<OrderClaimGoodsScheduleInfoDto> goodSchList;

	@ApiModelProperty(value = "주문클레임상세PK dto ")
	List<Long> odClaimDetlIdList;

	@ApiModelProperty(value = "MALL 주문 상태 코드")
	private String status;

	@ApiModelProperty(value = "DB 상태 체크 여부")
	private String dbStatusCheckYn;

	@ApiModelProperty(value = "회원 PK")
	private String urUserId;

	@ApiModelProperty(value = "비회원CI")
	private String guestCi;

	@ApiModelProperty(value = "추가결제 건 수")
	private int addPaymentCnt;

	/** 녹즙 클레임 관련 변수 추가 */
	@ApiModelProperty(value = "녹즙 클레임구분")
	private String claimType;

	@ApiModelProperty(value = "미출 클레임 처리여부")
	private String undeliveredClaimYn;

	@ApiModelProperty(value = "녹즙 배송완료여부")
	private String deliveryYn;

	@ApiModelProperty(value = "주문 상세 일일배송 스케쥴 라인번호")
	private String odOrderDetlDailySchSeq;

	@ApiModelProperty(value = "배송비재계산여부")
	private String reAccountShippingPriceYn;

	@ApiModelProperty(value = "환불배송비")
	private int refundReqShippingPrice;

	@ApiModelProperty(value = "환불주문시배송비")
	private int refundReqOrderShippingPrice;
}
