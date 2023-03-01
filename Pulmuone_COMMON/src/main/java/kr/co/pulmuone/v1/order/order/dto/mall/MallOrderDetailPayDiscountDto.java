package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 결제정보 할인정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 23.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "Mall 주문상세 결제정보 할인정보 Dto")
public class MallOrderDetailPayDiscountDto {

    @ApiModelProperty(value = "할인타입")
    private String discountType;

    @ApiModelProperty(value = "할인타입명")
    private String discountTypeName;

    @ApiModelProperty(value = "할인금액")
    private int discountPrice;

    @ApiModelProperty(value = "할인쿠폰타입")
    private String discountCouponType;
}