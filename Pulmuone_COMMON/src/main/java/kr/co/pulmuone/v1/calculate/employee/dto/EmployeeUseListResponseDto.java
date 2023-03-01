package kr.co.pulmuone.v1.calculate.employee.dto;

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
 * 임직원 포인트 사용 현황 조회 Response Dto
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
@ApiModel(description = "임직원 포인트 사용 현황 조회 응답 Response Dto")
public class EmployeeUseListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "임직원 지원금 리스트")
    private List<EmployeeUseListDto> rows;

    @ApiModelProperty(value = "임직원 지원금 카운트")
    private	long total;
}
