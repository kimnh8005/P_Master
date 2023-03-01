package kr.co.pulmuone.v1.order.email.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 배송정책별 주문상세 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	천혜현		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 자동메일 배송정책별 주문상세 Dto")
public class OrderDetailShippingTypeDto {

    @ApiModelProperty(value = "배송정책별 주문상세목록")
	private List<OrderDetailGoodsDto> orderDetailGoodsDto;

    @ApiModelProperty(value = "배송정책별 결제 예정금액")
    private int paymentPrice;

    @ApiModelProperty(value = "배송정책별 배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "배송정책별 결제 예정금액")
    private int regularPaymentPrice;

    @ApiModelProperty(value = "배송정책별 배송비")
    private int regularShippingPrice;
}