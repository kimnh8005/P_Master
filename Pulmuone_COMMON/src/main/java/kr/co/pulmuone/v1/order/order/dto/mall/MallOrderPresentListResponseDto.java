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
 * Mall 보낸선물함 리스트 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 07. 19.	홍진영		최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@ToString
@ApiModel(description = "Mall 보낸선물함 리스트 조회 Response Dto")
public class MallOrderPresentListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "주문목록")
	private List<MallOrderPresentListDto> list;

	@ApiModelProperty(value = "주문목록 카운트")
	private long total;
}