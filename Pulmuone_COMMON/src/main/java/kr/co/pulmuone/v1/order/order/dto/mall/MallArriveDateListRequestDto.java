package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 도착예정일 변경일자 목록 조회 Request Dto
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

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 도착예정일 변경일자 목록 조회 Request Dto")
public class MallArriveDateListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "Mall 도착예정일 조회 Request 목록")
    private List<MallArriveDateRequestDto> mallArriveDateList;
}