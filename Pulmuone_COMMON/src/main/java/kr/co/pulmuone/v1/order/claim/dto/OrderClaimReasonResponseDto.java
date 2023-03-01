package kr.co.pulmuone.v1.order.claim.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
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
public class OrderClaimReasonResponseDto {

//	@ApiModelProperty(value = "MALL 클레임 사유 PK")
//	private int psClaimMallId;
//
//	@ApiModelProperty(value = "사유")
//	private String reasonMsg;
//
//	@ApiModelProperty(value = "사유구분 A: 취소/반품 C: 취소, R: 반품")
//	private String reasonGbn;
//
//	@ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자 ")
//	private String targetGbn;
//
//	@ApiModelProperty(value = "클레임 사유(대)")
//	private int lClaimCtgryId;
//
//	@ApiModelProperty(value = "클레임 사유(중)")
//	private int mClaimCtgryId;
//
//	@ApiModelProperty(value = "귀책처")
//	private int sClaimCtgryId;

	@ApiModelProperty(value = "쇼핑몰 클레임 사유 VO 리스트")
	private List<PolicyClaimMallVo> rows;
}
