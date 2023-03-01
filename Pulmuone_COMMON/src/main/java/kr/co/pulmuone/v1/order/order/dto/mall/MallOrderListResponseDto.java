package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 리스트 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@ToString
@ApiModel(description = "Mall 주문 리스트 Response Dto")
public class MallOrderListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "주문목록")
	private	List<MallOrderListDto> list;

    @ApiModelProperty(value = "주문목록 카운트")
	private	long total;
}