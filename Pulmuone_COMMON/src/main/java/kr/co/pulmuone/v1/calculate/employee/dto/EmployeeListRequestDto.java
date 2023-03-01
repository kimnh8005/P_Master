package kr.co.pulmuone.v1.calculate.employee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleEmployeeMasterVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 지원금 정산 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "임직원 지원금 정산 조회 Request Dto")
public class EmployeeListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "부문 구분")
    private String findOuId;

    @ApiModelProperty(value = "확정 여부")
    private String findConfirmYn;

    @ApiModelProperty(value = "조회시작 년(yyyy)")
    private String findStartYear;

    @ApiModelProperty(value = "조회시작 월(MM)")
    private String findStartMonth;

    @ApiModelProperty(value = "조회종료 년(yyyy)")
    private String findEndYear;

    @ApiModelProperty(value = "조회종료 월(MM)")
    private String findEndMonth;

    @ApiModelProperty(value = "조회종료 월(MM)")
    private List<String> confirmList;

}
