package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 변경 신청 정보 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 28.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 변경 신청 정보 Request Dto")
public class RegularReqBuyerChangeRequestDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "신청변경구분")
	private String changeTp;

	@ApiModelProperty(value = "신청기간")
	private String goodsCycleTermTp;

	@ApiModelProperty(value = "신청주기")
	private String goodsCycleTp;

	@ApiModelProperty(value = "요일")
	private String weekCd;
}
