package kr.co.pulmuone.v1.calculate.employee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 포인트 사용 현황 조회 Request Dto
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "임직원 포인트 사용 현황 조회 Request Dto")
public class EmployeeUseListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "부문 구분")
    private String findOuId;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;

    @ApiModelProperty(value = "검색조건")
    private String searchMultiType;

    @ApiModelProperty(value = "검색어")
    private String findKeyword;

    @ApiModelProperty(value = "엑셀여부")
    private String excelYn;
}
