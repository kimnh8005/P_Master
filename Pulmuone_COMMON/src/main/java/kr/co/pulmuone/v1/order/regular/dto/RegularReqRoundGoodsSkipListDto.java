package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 회차별 상품 정보 건너뛰기   Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 10.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 회차별 상품 정보 건너뛰기 Dto")
public class RegularReqRoundGoodsSkipListDto {

	@ApiModelProperty(value = "정기배송상품결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "정기배송상품상세결과PK")
	private long odRegularResultDetlId;
}
