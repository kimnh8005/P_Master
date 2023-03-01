package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 신청 결과 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 08.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Setter
@Getter
@ApiModel(description = "정기배송 신청 결과 Response Dto")
public class ApplyRegularResponseDto {
	@ApiModelProperty(value = "신청 결과")
	private boolean result;

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "신청번호")
	private String reqId;

	@ApiModelProperty(value = "정기배송등록상품목록")
	private List<CartGoodsDto> regularGoodsList;
}
