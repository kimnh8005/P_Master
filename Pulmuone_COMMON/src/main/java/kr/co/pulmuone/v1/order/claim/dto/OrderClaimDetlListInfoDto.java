package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 파일첨부 정보 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 02.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 파일첨부 정보 조회 결과 Dto")
public class OrderClaimDetlListInfoDto {

	@ApiModelProperty(value = "주문클레임상세PK")
	private long odClaimDetlId;

	@ApiModelProperty(value = "주문클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "클레임수량")
	private int claimCnt;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "장바구니쿠폰금액")
	private int cartCouponPrice;

	@ApiModelProperty(value = "상품쿠폰금액")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "즉시할인금액")
	private int directPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "BOS클레임사유공급업체PK")
	private long psClaimBosSupplyId;

	@ApiModelProperty(value = "BOS클레임사유PK")
	private long psClaimBosId;

	@ApiModelProperty(value = "BOS클레임대분류ID")
	private long bosClaimLargeId;

	@ApiModelProperty(value = "BOS클레임중분류ID")
	private long bosClaimMiddleId;

	@ApiModelProperty(value = "BOS클레임소분류ID")
	private long bosClaimSmallId;

	@ApiModelProperty(value = "주문상태코드")
	private String orderStatusCd;

	@ApiModelProperty(value = "클레임상태코드")
	private String claimStatusCd;

	@ApiModelProperty(value = "재배송구분")
	private String redeliveryType;

	@ApiModelProperty(value = "환불상태코드")
	private String refundStatusCd;

	@ApiModelProperty(value = "반품매출연동여부")
	private String returnSalesExecFl;

	@ApiModelProperty(value = "반품매출연동일자")
	private String returnSalesExecDt;

	@ApiModelProperty(value = "배송비정책PK")
	private long ilGoodsShippingTemplateId;

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "추가배송비")
	private String addPaymentShippingPrice;

	@ApiModelProperty(value = "취소요청 등록자")
	private long caId;

	@ApiModelProperty(value = "취소요청 일자")
	private LocalDateTime caDt;

	@ApiModelProperty(value = "취소철회 등록자")
	private long cwId;

	@ApiModelProperty(value = "취소철회 일자")
	private LocalDateTime cwDt;

	@ApiModelProperty(value = "취소완료 등록자")
	private long ccId;

	@ApiModelProperty(value = "취소완료 일자")
	private LocalDateTime ccDt;

	@ApiModelProperty(value = "반품요청 등록자")
	private long raId;

	@ApiModelProperty(value = "반품요청 일자")
	private LocalDateTime raDt;

	@ApiModelProperty(value = "반품철회 등록자")
	private long rwId;

	@ApiModelProperty(value = "반품철회 일자")
	private LocalDateTime rwDt;

	@ApiModelProperty(value = "반품승인 등록자")
	private long riId;

	@ApiModelProperty(value = "반품승인 일자")
	private LocalDateTime riDt;

	@ApiModelProperty(value = "반품보류 등록자")
	private long rfId;

	@ApiModelProperty(value = "반품보류 일자")
	private LocalDateTime rfDt;

	@ApiModelProperty(value = "반품완료 등록자")
	private long rcId;

	@ApiModelProperty(value = "반품완료 일자")
	private LocalDateTime rcDt;

	@ApiModelProperty(value = "재배송 등록자")
	private long ecId;

	@ApiModelProperty(value = "재배송 일자")
	private LocalDateTime ecDt;

	@ApiModelProperty(value = "CS환불 등록자")
	private long csId;

	@ApiModelProperty(value = "CS환불 일자")
	private LocalDateTime csDt;

	@ApiModelProperty(value = "환불요청 등록자")
	private long faId;

	@ApiModelProperty(value = "환불요청 일자")
	private LocalDateTime faDt;

	@ApiModelProperty(value = "환불완료 등록자")
	private long fcId;

	@ApiModelProperty(value = "환불완료 일자")
	private LocalDateTime fcDt;

	@ApiModelProperty(value = "클레임요청 등록자")
	private long crId;

	@ApiModelProperty(value = "클레임요청 일자")
	private LocalDateTime crDt;

	@ApiModelProperty(value = "클레임승인 등록자")
	private long ceId;

	@ApiModelProperty(value = "클레임승인 일자")
	private LocalDateTime ceDt;

	@ApiModelProperty(value = "주문 I/F 등록자")
	private long orderIfId;

	@ApiModelProperty(value = "주문 I/F 일자")
	private LocalDateTime orderIfDt;

	@ApiModelProperty(value = "출고예정일 등록자")
	private long shippingId;

	@ApiModelProperty(value = "출고예정일 일자")
	private LocalDateTime shippingDt;

	@ApiModelProperty(value = "도착예정일 등록자")
	private long deliveryId;

	@ApiModelProperty(value = "도착예정일 일자")
	private LocalDateTime deliveryDt;
}
