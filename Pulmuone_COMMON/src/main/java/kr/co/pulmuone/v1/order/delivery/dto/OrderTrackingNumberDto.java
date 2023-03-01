package kr.co.pulmuone.v1.order.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 송장번호 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 상세 송장번호 Dto")
public class OrderTrackingNumberDto {
    List<OrderTrackingNumberVo> orderTrackingNumberList;
}
