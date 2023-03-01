package kr.co.pulmuone.v1.batch.order.inside.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상품에서 배송중 상품만 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 18.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상품에서 배송중 상품만 조회 결과 Dto")
public class OrderDeliveryInfoDto {

	@ApiModelProperty(value = "주문상세 pk")
	private long odOrderDetlId;

	@ApiModelProperty(value = "택배사 설정 pk")
	private long psShippingCompId;

	@ApiModelProperty(value = "개별송장번호")
	private String trackingNo;
}
