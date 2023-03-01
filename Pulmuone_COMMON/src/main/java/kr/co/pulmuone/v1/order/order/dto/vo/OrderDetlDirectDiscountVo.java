package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 결제상세정보 > 즉시할인내역 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 08.            	최윤지         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "주문 상세 결제상세정보 > 즉시할인내역 VO")
public class OrderDetlDirectDiscountVo {

    @ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

	@ApiModelProperty(value = "상품코드")
    private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
    private String goodsNm;

	@ApiModelProperty(value="정상가")
	private long recommendedPrice;

	@ApiModelProperty(value="판매가")
	private long salePrice;

    @ApiModelProperty(value = "상품할인 유형코드")
    private String discountTp;

    @ApiModelProperty(value = "할인금액")
    private long discountPrice;

}
