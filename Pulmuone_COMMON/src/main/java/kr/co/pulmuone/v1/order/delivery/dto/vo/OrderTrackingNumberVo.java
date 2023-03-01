package kr.co.pulmuone.v1.order.delivery.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 주문 상세별 송장번호 Vo
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
@Builder
public class OrderTrackingNumberVo {


	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문 배송지 PK")
	private Long odTrackingNumberId;

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "주문상세 SEQ")
	private int odOrderDetlSeq;

	@ApiModelProperty(value = "택배사 PK")
	private Long psShippingCompId;

	@ApiModelProperty(value = "개별송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "순서")
	private int sort;

	@ApiModelProperty(value = "등록자 UR_USER.UR_USER_ID")
	private Long createId;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "성공여부")
	private String successYn;

	@ApiModelProperty(value = "메시지")
	private String msg;
}
