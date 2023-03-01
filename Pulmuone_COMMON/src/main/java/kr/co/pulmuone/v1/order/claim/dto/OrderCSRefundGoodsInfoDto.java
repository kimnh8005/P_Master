package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * CS 환불 상품 정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "CS 환불 상품 정보 Dto")
public class OrderCSRefundGoodsInfoDto {

	@ApiModelProperty(value = "CS환불상세 PK")
	private long odCsDetlId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "상품별 CS환불 금액")
	private int csRefundPrice;

	@ApiModelProperty(value = "BOS클레임사유PK")
	private long psClaimBosId;

	@ApiModelProperty(value = "BOS클레임대분류ID")
	private long bosClaimLargeId;

	@ApiModelProperty(value = "BOS클레임중분류ID")
	private long bosClaimMiddleId;

	@ApiModelProperty(value = "BOS클레임소분류ID")
	private long bosClaimSmallId;
}

