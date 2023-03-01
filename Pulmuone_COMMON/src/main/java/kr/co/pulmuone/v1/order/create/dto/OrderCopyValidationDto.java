package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문복사 유효성 체크 DTO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 08. 24.					천혜현			최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문복사 유효성 체크 DTO")
public class OrderCopyValidationDto{

	@ApiModelProperty(value = "주문상세 PK")
    private Long odOrderDetlId;

	@ApiModelProperty(value = "주문배송지 PK")
	private Long odShippingZoneId;

	@ApiModelProperty(value = "출고처 PK")
	private Long urWarehouseId;

	@ApiModelProperty(value = "우편번호")
    private String zipCode;

	@ApiModelProperty(value = "새벽배송여부")
    private boolean isDawnDelivery;

	@ApiModelProperty(value = "상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "주문 매출 연동 방법(ORDER_IF_TYPE.ORDER_IF : 주문I/F, ORDER_IF_TYPE.SAL_IF: 매출만 연동)")
	private String orderIfType;
}
