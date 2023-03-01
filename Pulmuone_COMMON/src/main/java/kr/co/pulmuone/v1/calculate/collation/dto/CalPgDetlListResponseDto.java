package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * PG 대사 상세내역 조회 Response Dto
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
@ApiModel(description = "PG 대사 상세내역 조회 응답 Response Dto")
public class CalPgDetlListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "PG 대사 상세내역 리스트")
    private List<CalPgDetlListDto> rows;

    @ApiModelProperty(value = "PG 대사 상세내역 카운트")
    private	long total;

    @ApiModelProperty(value = "PG 대사 상세내역 합계")
    private	long emSumPrice;

}
