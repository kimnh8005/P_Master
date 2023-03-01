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
 * Mall 일일배송 배송지 변경 내역 목록 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 16.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@ToString
@ApiModel(description = "Mall 일일배송 배송지 변경 내역 목록 조회 Response Dto")
public class MallOrderDailyShippingZoneHistListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "Mall 도착예정일 변경일자 목록")
	private	MallOrderDailyShippingZoneHistListDto shippingZoneInfo;
}