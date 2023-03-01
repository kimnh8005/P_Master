package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
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
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 환불정보에서 쿠폰정보 조회 결과 Dto")
public class OrderClaimStatusInfoDto {

	@ApiModelProperty(value = "클레임진행상태")
	private String claimStatusCd;

	@ApiModelProperty(value = "클레임진행상태명")
	private String claimStatusNm;

}
