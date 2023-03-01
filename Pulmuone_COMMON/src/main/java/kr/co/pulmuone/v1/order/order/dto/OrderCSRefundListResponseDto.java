package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * CS환불리스트 관련 응답 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "CS환불리스트 응답 Response Dto")
public class OrderCSRefundListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "CS환불리스트목록")
	private	List<OrderCSRefundListDto> rows;

    @ApiModelProperty(value = "CS환불리스트 카운트")
	private	long total;

    @ApiModelProperty(value = "CS환불리스트 총 금액")
	private	long totalPrice;
}
