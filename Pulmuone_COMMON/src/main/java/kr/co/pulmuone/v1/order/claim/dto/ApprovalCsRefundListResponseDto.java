package kr.co.pulmuone.v1.order.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 응답 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 14.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "주문 CS환불 승인 리스트 응답 Response Dto")
public class ApprovalCsRefundListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "주문목록")
	private	List<ApprovalCsRefundListDto> rows;

    @ApiModelProperty(value = "주문상세 카운트")
	private	long total;

}
