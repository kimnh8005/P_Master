package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 철회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 12.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 철회 Dto")
public class OrderClaimRestoreDto {

	@ApiModelProperty(value = "주문 상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "클레임 상세 PK")
	private long odClaimDetlId;

	@ApiModelProperty(value = "주문 수량")
	private int orderCnt;

	@ApiModelProperty(value = "취소 수량")
	private int cancelCnt;

	@ApiModelProperty(value = "클레임 수량")
	private int claimCnt;

	@ApiModelProperty(value = "계산 결과 수량")
	private int resultCnt;

	@ApiModelProperty(value = "주문상태")
	private String orderStatusCd;

	@ApiModelProperty(value = "클레임상태")
	private String claimStatusCd;

}
