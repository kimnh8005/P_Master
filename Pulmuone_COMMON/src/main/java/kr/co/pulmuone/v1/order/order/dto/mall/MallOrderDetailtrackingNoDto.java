package kr.co.pulmuone.v1.order.order.dto.mall;

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
 * Mall 송장번호별 주문상세 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 송장번호별 주문상세 Dto")
public class MallOrderDetailtrackingNoDto {

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

	@ApiModelProperty(value = "송장번호별 주문상세 리스트")
    private List<MallOrderDetailGoodsDto> trackingNoOrderDetailList;
}
