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
 * 주문 상세 결제상세정보 > 증정품 내역 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 19.    천혜현         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "주문 상세 결제상세정보 > 증정품 VO")
public class OrderGiftVo {

	@ApiModelProperty(value = "상품코드")
    private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "기획전명")
    private String title;

    @ApiModelProperty(value = "증정조건")
    private String exhibitInfo;

}
