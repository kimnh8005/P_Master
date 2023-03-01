package kr.co.pulmuone.v1.order.registration.dto;


import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderAccountVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPresentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 생성 관련 Request Dto")
public class OrderBindDto {

    @ApiModelProperty(value = "주문 VO")
    OrderVo order;

    @ApiModelProperty(value = "주문 일자 VO")
    OrderDtVo orderDt;

    @ApiModelProperty(value = "배송지 리스트")
    List<OrderBindShippingZoneDto> orderShippingZoneList;

    @ApiModelProperty(value = "주문 결제 VO")
    OrderPaymentVo orderPaymentVo;

    @ApiModelProperty(value = "메모 리스트")
    List<OrderOutmallMemoDto> memoList;

    @ApiModelProperty(value = "환불계좌 VO")
    OrderAccountVo orderAccount;

    @ApiModelProperty(value = "주문생성 실패리스트")
    List<Map<String, Object>> collectionMalllFailList;

    @ApiModelProperty(value = "선물하기 VO")
    OrderPresentVo orderPresentVo;
}
