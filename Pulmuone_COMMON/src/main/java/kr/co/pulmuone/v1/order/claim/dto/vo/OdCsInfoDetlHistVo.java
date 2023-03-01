package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * CS 환불 정보 상세 이력 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 13.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "CS 환불 정보 상세 이력 VO")
public class OdCsInfoDetlHistVo {

    @ApiModelProperty(value = "CS환불정보상세이력PK")
    private long odCsDetlHistId;

	@ApiModelProperty(value = "CS환불정보PK")
	private long odCsId;

    @ApiModelProperty(value = "CS환불정보상세PK")
    private long odCsDetlId;

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "환불금액(CS환불상세 REFUND_PRICE 합계)")
	private int refundPrice;

	@ApiModelProperty(value = "BOS 클레임 사유 PK")
	private long psClaimBosId;

	@ApiModelProperty(value = "BOS 클레임 대분류 ID")
	private long bosClaimLargeId;

	@ApiModelProperty(value = "BOS 클레임 중분류 ID")
	private long bosClaimMiddleId;

	@ApiModelProperty(value = "BOS 클레임 소분류 ID")
	private long bosClaimSmallId;

	@ApiModelProperty(value = "처리이력내용")
	private String histMsg;

	@ApiModelProperty(value = "등록자")
	private long createId;

	@ApiModelProperty(value ="등록일시")
	private LocalDateTime createDt;
}
