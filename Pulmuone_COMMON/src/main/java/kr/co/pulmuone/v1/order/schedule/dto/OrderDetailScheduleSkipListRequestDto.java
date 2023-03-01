package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련  Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 3. 18.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 건너뛰기 스케줄 리스트 Request Dto")
public class OrderDetailScheduleSkipListRequestDto {

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

    @ApiModelProperty(value = "변경 적용일(YYYY-MM-DD)")
    private String changeDate;

    @ApiModelProperty(value = "일정 시작(YYYY-MM-DD)")
    private String firstDate;

    @ApiModelProperty(value = "일정 마지막(YYYY-MM-DD)")
    private String lastDate;
}