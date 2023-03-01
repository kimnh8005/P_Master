package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 리스트 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 리스트 Request Dto")
public class MallOrderListRequestDto extends MallBaseRequestPageDto {

	@ApiModelProperty(value = "조회시작일(yyyy-MM-dd)")
    private String startDate;

    @ApiModelProperty(value = "조회종료일(yyyy-MM-dd)")
    private String endDate;

    @ApiModelProperty(value = "주문자ID", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "비회원 CI", hidden = true)
    private String guestCi;
}