package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 카트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 05. 26.		이명수	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 상품추가 Dto")
public class OrderCreateAddGoodsResponseDto {

    @ApiModelProperty(value = "상품 추가 데이터")
    ApiResult<?> orderCreateCartList;

    @ApiModelProperty(value = "상품 추가 실패 메세지")
    private List<String> failMessageList;
}