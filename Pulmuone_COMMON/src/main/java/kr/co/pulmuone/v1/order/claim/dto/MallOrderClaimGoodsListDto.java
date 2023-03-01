package kr.co.pulmuone.v1.order.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 요청 정보 상품 리스트 Dto
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
@ApiModel(description = "Mall 주문 클레임 요청 정보 상품 리스트 Dto")
public class MallOrderClaimGoodsListDto {

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "상품이미지")
	private String thumbnailPath;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "상품구매수량")
	private int orderCnt;

	@ApiModelProperty(value = "추가구성리스트")
	private List<MallOrderClaimAddGoodsListDto> addGoodsList;
}
