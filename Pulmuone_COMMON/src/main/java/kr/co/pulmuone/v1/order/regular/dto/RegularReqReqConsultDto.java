package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송  신청 상담 정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 23.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 신청 상담 정보 Dto")
public class RegularReqReqConsultDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqMemoId;

	@ApiModelProperty(value = "메모내용")
	private String memo;
}
