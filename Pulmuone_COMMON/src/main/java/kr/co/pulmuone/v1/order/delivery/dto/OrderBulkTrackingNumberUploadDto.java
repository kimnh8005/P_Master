package kr.co.pulmuone.v1.order.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
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
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "OrderBulkTrackingNumberUploadDto")
public class OrderBulkTrackingNumberUploadDto {
	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세 SEQ")
	private int odOrderDetlSeq;

	@ApiModelProperty(value = "택배사")
	private String shippingCompNm;

	@ApiModelProperty(value = "송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "택배사 PK")
	private long psShippingCompId;

	@ApiModelProperty(value = "등록자")
	private long createId;

	@ApiModelProperty(value = "성공여부")
	private boolean success;

	@ApiModelProperty(value = "실패사유")
	private String failMessage;


}
