package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 출고처 기준 주문 정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 27.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 클레임 출고처 기준 주문 정보 Dto")
public class OrderDetlWarehouseShippingDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "상품판매가")
	private int salePrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "할인금액")
	private int couponPrice;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "상품타입")
	private String goodsTpCd;

	@ApiModelProperty(value = "출고처 PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "배송템플릿 PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "배송템플릿명")
	private String ilGoodsShippingTemplateNm;
}