package kr.co.pulmuone.v1.calculate.order.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 상품 정산 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "상품 정산 조회 응답 Response Dto")
public class CalOrderListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "임직원 지원금 리스트")
    private List<CalOrderListDto> rows;

    @ApiModelProperty(value = "임직원 지원금 카운트")
    private	long total;
}
