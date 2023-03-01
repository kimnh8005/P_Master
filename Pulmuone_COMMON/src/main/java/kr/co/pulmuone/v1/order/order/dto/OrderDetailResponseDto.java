package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 응답 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 상세 응답 Response Dto")
public class OrderDetailResponseDto extends BaseResponseDto {

    private OrderDto order;

}
