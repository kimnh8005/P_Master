package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.calculate.order.dto.CalDeliveryListDto;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * PG 거래 내역 대사 조회 Response Dto
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
@ApiModel(description = "PG 거래 내역 대사 조회 응답 Response Dto")
public class CalPgListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "PG 거래 내역 대사 리스트")
    private List<CalPgListDto> rows;

    @ApiModelProperty(value = "PG 거래 내역 대사 카운트")
    private	long total;
}
