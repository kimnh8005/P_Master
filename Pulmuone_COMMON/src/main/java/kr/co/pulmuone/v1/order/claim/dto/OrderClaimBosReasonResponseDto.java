package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 클레임정보 사유 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 21.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 클레임정보 사유 조회 결과 Dto")
public class OrderClaimBosReasonResponseDto {

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private int psClaimCtgryId;

	@ApiModelProperty(value = "클레임 사유 카테고리 10: 대, 20: 중, 30: 귀책처")
	private String cateCd;

	@ApiModelProperty(value = "클레임사유 코드")
	private String claimCd;

	@ApiModelProperty(value = "클레임사유명")
	private String claimNm;
}
