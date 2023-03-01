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
 * 주문 클레임 할인 정보 목록 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 09. 03.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 할인 정보 목록 Dto")
public class OrderClaimDetlDiscountListInfoDto {

	@ApiModelProperty(value = "클레임상세할인PK")
	private long odClaimDetlDiscountId;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "클레임상세PK")
	private long odClaimDetlId;

	@ApiModelProperty(value = "할인타입")
	private String discountTp;

	@ApiModelProperty(value = "쿠폰발급PK")
	private long pmCouponIssueId;

	@ApiModelProperty(value = "쿠폰PK")
	private long pmCouponId;

	@ApiModelProperty(value = "쿠폰명")
	private String pmCouponNm;

	@ApiModelProperty(value = "할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "임직원 할인그룹")
	private long psEmplDiscGrpId;

	@ApiModelProperty(value = "임직원 혜택 표준브랜드")
	private long urBrandId;
}
