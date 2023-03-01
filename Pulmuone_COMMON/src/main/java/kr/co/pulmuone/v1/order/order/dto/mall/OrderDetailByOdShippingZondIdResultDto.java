package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 결제정보 sql 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 23.	천혜현		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "Mall 주문배송지PK로 주문상세 조회 결과 Dto")
public class OrderDetailByOdShippingZondIdResultDto {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

	@ApiModelProperty(value = "스토어 PK")
    private Long urStoreId;

	@ApiModelProperty(value = "배송방식(매일, 격일(월, 수, 금))")
    private String storeDeliveryIntervalTp;

}