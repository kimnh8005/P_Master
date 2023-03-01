package kr.co.pulmuone.v1.order.present.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailDeliveryTypeDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailShippingZoneDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 인증 응답 DTO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 07. 15.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 선물하기 인증 결과 DTO")
public class OrderPresentAuthResponseDto {

	@ApiModelProperty(value = "선물하기 DTO")
	private OrderPresentDto orderPresent;

	@ApiModelProperty(value = "주문상세 목록")
	private	List<MallOrderDetailDeliveryTypeDto> orderDetailList;

    @ApiModelProperty(value = "증정품 목록")
    private	List<MallOrderDetailGoodsDto> giftGoodsList;

	@ApiModelProperty(value = "주문배송지정보")
    private MallOrderDetailShippingZoneDto shippingAddress;

	@ApiModelProperty(value = "선택 가능 일자")
	private List<OrderPresentChoiceDateDto> choiceDate;
}