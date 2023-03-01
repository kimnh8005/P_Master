package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 도착예정일 변경일자 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 18.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문상세 리스트 Request Dto")
public class MallArriveDateRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "출고처ID(출고처PK)")
	private Long urWarehouseId;

	@ApiModelProperty(value = "상품ID(상품PK)")
	private Long ilGoodsId;

	@ApiModelProperty(value = "새벽배송여부")
	private Boolean isDawnDelivery;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;

	@ApiModelProperty(value = "일일 배송요일코드")
	private String weekCode;
}