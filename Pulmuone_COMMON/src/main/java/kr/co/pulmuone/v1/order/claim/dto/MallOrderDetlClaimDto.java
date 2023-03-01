package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 요청 주문 정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 22.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "Mall 주문 클레임 요청 주문 정보 Dto")
public class MallOrderDetlClaimDto {

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "배송정책템플릿ID")
	private long ilGoodsShippingTemplateId;

	@ApiModelProperty(value = "배송정책명")
	private String ilGoodsShippingTemplateNm;

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "상품타입")
	private String goodsTp;

	@ApiModelProperty(value = "상품PK")
	private long goodsId;

	@ApiModelProperty(value = "상품패키지이미지타입")
	private String goodsPackageImgTp;

	@ApiModelProperty(value = "상품품목PK")
	private String ilItemCd;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;
}
