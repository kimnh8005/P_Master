package kr.co.pulmuone.v1.order.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 입력 내역 목록 조회 Request Dto
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
@ApiModel(description = "OrderBulkTrackingNumberListRequestDto")
public class OrderBulkTrackingNumberListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "검색 시작일")
	private String startDate;

	@ApiModelProperty(value = "검색 종료일")
	private String endDate;

	@ApiModelProperty(value = "일괄송장입력PK")
	private Long odBulkTrackingNumberId;
}
