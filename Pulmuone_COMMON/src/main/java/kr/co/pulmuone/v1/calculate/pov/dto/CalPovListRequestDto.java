package kr.co.pulmuone.v1.calculate.pov.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * POV I/F 조회 Response Dto
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
@Setter
@Getter
@ToString
@ApiModel(description = "POV I/F 조회 응답 Request Dto")
public class CalPovListRequestDto  {

	@ApiModelProperty(value = "년도")
	private String findYear;

	@ApiModelProperty(value = "월")
	private String findMonth;
}
