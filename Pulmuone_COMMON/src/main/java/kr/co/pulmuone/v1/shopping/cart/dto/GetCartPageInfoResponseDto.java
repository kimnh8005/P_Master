package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 페이지정보 응답 DTO")
public class GetCartPageInfoResponseDto
{
	@ApiModelProperty(value = "배송 출입 정보")
	private List<CodeInfoVo> accessInformationType;

	@ApiModelProperty(value = "장바구니 정보")
	private List<CartTypeSummaryDto> cartTypeSummary;

	@ApiModelProperty(value = "정기배송 정보")
	private RegularShippingConfigDto regularShipping;

    @ApiModelProperty(value = "장바구니 유지 기간")
    private String cartMaintenancePeriod;

	@ApiModelProperty(value = "배송 장소 정보")
	private List<CodeInfoVo> storeDeliveryType;

}
