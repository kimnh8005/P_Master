package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송  주문생성 신청 정보 목록  조회 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 22.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문생성 신청 정보 목록 조회 Dto")
public class RegularReqCreateOrderListDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "정기배송결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "주문자명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자핸드폰")
	private String buyerHp;

	@ApiModelProperty(value = "주문자이메일")
	private String buyerMail;
}
