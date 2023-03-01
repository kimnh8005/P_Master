package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 CS환불 정보 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 10.   			  김명진        최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 CS환불 정보 조회 Request Dto")
public class OrderClaimCSRefundRequestDto {

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "클레임구분 CLAIM_STATUS_TP.CANCEL : 취소, CLAIM_STATUS_TP.RETURN : 반품, CLAIM_STATUS_TP.CS_REFUND : CS환불")
	private String claimStatusTp;

	@ApiModelProperty(value = "CS환불승인코드리스트")
	private List<String> csRefundAproveCdList;
}
