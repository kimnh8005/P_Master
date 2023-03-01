package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 결과 주문생성 상품 목록 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 21.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 결과 주문생성 상품 목록 Dto")
public class RegularResultCreateOrderGoodsListDto {

	@ApiModelProperty(value = "정기배송 결과 PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "정기배송 추가할인 기준 회차")
	private int addDiscountStdReqRound;

	@ApiModelProperty(value = "정기배송 상품 목록")
	private Map<String, List<RegularResultCreateOrderListDto>> goodsList;

	@ApiModelProperty(value = "추가상품 목록")
	private Map<Long, List<RegularResultCreateOrderListDto>> childGoodsList;
}
