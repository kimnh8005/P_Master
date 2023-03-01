package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세내역 복사 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세내역 복사 Dto")
public class OrderCopyDto {

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "주문상세 뎁스")
	private int odOrderDetlDepthId;

	@ApiModelProperty(value = "주문 I/F 일자")
	private LocalDate orderIfDt;

	@ApiModelProperty(value = "출고예정일일자")
	private LocalDate shippingDt;


	@ApiModelProperty(value = "도착예정일일자")
	private LocalDate deliveryDt;

}