package kr.co.pulmuone.v1.order.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄 송장 입력 내역 상세 목록 조회 Dto
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
@ApiModel(description = "OrderBulkTrackingNumberDetlDto")
public class OrderBulkTrackingNumberDetlDto {

	@ApiModelProperty(value = "No")
	private int rowNum;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "주문상세 SEQ")
	private Long odOrderDetlSeq;

	@ApiModelProperty(value = "택배사")
	private String shippingCompNm;

	@ApiModelProperty(value = "송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "택배사 PK")
	private Long psShippingCompId;

	@ApiModelProperty(value = "등록일자")
	private String createDt;
}
