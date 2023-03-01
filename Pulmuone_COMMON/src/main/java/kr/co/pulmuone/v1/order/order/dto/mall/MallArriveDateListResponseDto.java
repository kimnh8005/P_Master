package kr.co.pulmuone.v1.order.order.dto.mall;

import java.time.LocalDate;
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
 * Mall 도착예정일 변경일자 목록 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 18.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@ToString
@ApiModel(description = "Mall 도착예정일 변경일자 목록 조회 Response Dto")
public class MallArriveDateListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "Mall 도착예정일 변경일자 목록")
	private	List<LocalDate> list;
}