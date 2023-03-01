package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 결과 상세 상품 정보 Dto
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
@ApiModel(description = "정기배송 주문 결과 상세 상품 정보 Dto")
public class RegularResultGoodsDetailInfoDto {

	@ApiModelProperty(value = "정기배송신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "정기배송신청결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "회차")
	private int reqRound;

	@ApiModelProperty(value = "회차완료여부")
	private String reqRoundYn;

	@ApiModelProperty(value = "추가할인기준회차")
	private int addDiscountStdReqRound;
}
