package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 Dto
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
@ApiModel(description = "Mall 주문상세 리스트 Dto")
public class MallOrderDetailListDto {

    private MallOrderDetailGoodsDto goodsInfo;

    @ApiModelProperty(value = "추가상품 목록")
    private List<MallOrderDetailGoodsDto> selectAddGoodsList;

    @ApiModelProperty(value = "묶음상품 주문상세목록")
    private List<MallOrderDetailGoodsDto> packageList;

    @ApiModelProperty(value = "일일상품 주문상세목록")
    private List<MallOrderDetailGoodsDto> dailyList;

}