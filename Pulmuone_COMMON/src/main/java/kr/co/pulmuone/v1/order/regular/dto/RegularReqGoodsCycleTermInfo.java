package kr.co.pulmuone.v1.order.regular.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 기간 정보 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 24.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 기간 정보 결과 Dto")
public class RegularReqGoodsCycleTermInfo {

	@ApiModelProperty(value = "기준일자")
	private LocalDate standardDate;

	@ApiModelProperty(value = "신청종료일자")
	private LocalDate lastArriveDate;

	@ApiModelProperty(value = "기간연장횟수")
	private int termExtensionCnt;

	@ApiModelProperty(value = "기간종료일자")
	private LocalDate endDate;

	@ApiModelProperty(value = "에러코드")
	private String regularErrorCd;
}
