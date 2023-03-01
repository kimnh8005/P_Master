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
 *  1.0		2021. 03. 15.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "임직원 지원금 엑셀다운로드 조회 Request Dto")
public class EmployeeExcelListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "부문 구분")
    private String ouId;

    @ApiModelProperty(value = "SESSION_ID")
    private String sessionId;

    @ApiModelProperty(value = "정산월")
    private String settleMonth;

    @ApiModelProperty(value = "확정여부")
    private String confirmYn;
}
