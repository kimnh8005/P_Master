package kr.co.pulmuone.v1.order.delivery.dto.vo;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 실패내역 Vo
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 24.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/

@Getter
@Setter
@ToString
public class OrderBulkTrackingNumberFailVo {

	@ApiModelProperty(value = "일괄송장입력 성공내역 PK")
	private Long odBulkTrackingNumberFailDetlId;

	@ApiModelProperty(value = "일괄송장입력 PK")
	private Long odBulkTrackingNumberId;

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "택배사 PK")
	private Long psShippingCompId;

	@ApiModelProperty(value = "송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "실패사유")
	private String failReason;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세 SEQ")
	private int odOrderDetlSeq;
}
