package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송 주문 리스트 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 29.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "Mall 일일배송 주문 리스트 Response Dto")
public class MallOrderDailyListResponseDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "일일배송 주문목록")
	private	List<MallOrderDailyListDto> list;

    @ApiModelProperty(value = "일일배송 주문목록 카운트")
	private	long total;
    
    @ApiModelProperty(value = "하이톡_FD-PHI 스위치")
	private boolean isHitokSwitch; //하이톡 스위치 여부
}