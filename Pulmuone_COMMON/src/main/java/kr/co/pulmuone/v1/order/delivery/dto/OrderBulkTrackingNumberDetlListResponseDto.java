package kr.co.pulmuone.v1.order.delivery.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄 송장 입력 내역 상세 목록 조회 Response Dto
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 29.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/

@Getter
@Setter
@ToString
@ApiModel(description = "OrderBulkTrackingNumberDetlListResponseDto")
public class OrderBulkTrackingNumberDetlListResponseDto {

	@ApiModelProperty(value = "일괄 송장 입력 내역 상세 목록")
	private List<OrderBulkTrackingNumberDetlDto> rows;

	@ApiModelProperty(value = "일괄 송장 입력 내역 상세 목록 카운트")
	private long total;
}
