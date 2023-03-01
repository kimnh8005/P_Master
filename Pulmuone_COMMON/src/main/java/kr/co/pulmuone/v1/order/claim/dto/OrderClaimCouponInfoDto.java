package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 환불정보에서 상품금액 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 환불정보에서 쿠폰정보 조회 결과 Dto")
public class OrderClaimCouponInfoDto {

	@ApiModelProperty(value = "주문상세 할인 pk")
	private long odOrderDetlDiscountId;

	@ApiModelProperty(value = "주문상세 pk")
	private long odOrderDetlId;

	@ApiModelProperty(value = "상품할인 유형 공통코드(GOODS_DISCOUNT_TP - PRIORITY:우선할인, ERP_EVENT:올가할인, IMMEDIATE:즉시할인)")
	private String discountTp;

	@ApiModelProperty(value = "쿠폰발급고유값")
	private long pmCouponIssueId;

	@ApiModelProperty(value = "쿠폰명")
	private String pmCouponNm;

	@ApiModelProperty(value = "쿠폰혜택정보")
	private String pmCouponBenefit;

	@ApiModelProperty(value = "할인상세")
	private String discountDetl;

	@ApiModelProperty(value = "할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "임직원 혜택그룹 : PS_EMPL_DISC_MASTER.PS_EMPL_DISC_MASTER_ID")
	private long psEmplDiscMasterId;

	@ApiModelProperty(value = "임직원 혜택 표준 브랜드 : UR_BRAND.UR_BRAND_ID")
	private long urBrandId;

	@ApiModelProperty(value = "주문클레임 상세 할인 PK")
	private long odClaimDetlDiscountId;

	@ApiModelProperty(value = "주문 pk")
	private long odOrderId;

	@ApiModelProperty(value = "주문 클레임 pk")
	private long odClaimId;

	@ApiModelProperty(value = "주문클레임 상세 pk")
	private long odClaimDetlId;

}
