package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상품조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ApiModel(description = "주문 클레임 상품조회 결과 Dto")
public class OrderClaimGoodsListDto {

	@ApiModelProperty(value = "배송비")
	private String shippingPrice;

	@ApiModelProperty(value = "배송정책명")
	private String ilShippingTmplNm;

	@ApiModelProperty(value = "배송정책 PK")
	private String ilShippingTmplId;

	@ApiModelProperty(value = "상품 리스트")
	List<OrderClaimGoodsInfoDto> goodsList;


}
