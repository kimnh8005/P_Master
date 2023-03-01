package kr.co.pulmuone.v1.order.delivery.dto;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄 송장 입력 내역 상세 목록 조회 Request Dto
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
@ApiModel(description = "OrderBulkTrackingNumberDetlListRequestDto")
public class OrderBulkTrackingNumberDetlListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "일괄송장입력PK")
	private String odBulkTrackingNumberId;

	@ApiModelProperty(value = "조회구분 값")
	private String searchType;

	@ApiModelProperty(value = "검색코드")
	private String codeSearch;

	@ApiModelProperty(value = "검색코드Array", required = false)
    private ArrayList<String> codeArray;
}
