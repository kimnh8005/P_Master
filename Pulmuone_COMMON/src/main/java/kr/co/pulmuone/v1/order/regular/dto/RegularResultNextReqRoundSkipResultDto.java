package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 결과 다음회차 상품 스킵 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 18.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 결과 다음회차 상품 스킵 결과 Dto")
public class RegularResultNextReqRoundSkipResultDto {

	@ApiModelProperty(value = "정기배송신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "다음회차 상품 스킵 수")
	private int nextSkipCnt;
}
