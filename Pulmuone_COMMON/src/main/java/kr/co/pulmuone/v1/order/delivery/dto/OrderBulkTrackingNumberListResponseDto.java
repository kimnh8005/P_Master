package kr.co.pulmuone.v1.order.delivery.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 입력 내역 목록 조회 Response Dto
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 28.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/

@Getter
@Setter
@ToString
@ApiModel(description = "OrderBulkTrackingNumberListResponseDto")
public class OrderBulkTrackingNumberListResponseDto {

	@ApiModelProperty(value = "일괄송장 입력 내역 목록")
	private List<OrderBulkTrackingNumberVo> rows;

	@ApiModelProperty(value = "일괄송장 입력 내역 목록 카운트")
	private long total;
}
