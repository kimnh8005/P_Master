package kr.co.pulmuone.v1.order.registration.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 배송비 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 21.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 생성 배송비 Dto")
public class OrderBindShippingPriceDto {

    @ApiModelProperty(value = "순번")
    private int shippingPriceSeq;

    @ApiModelProperty(value = "주문 배송비 VO")
    private OrderShippingPriceVo orderShippingPriceVo;

    @ApiModelProperty(value = "주문 상세 정보 VO")
    List<OrderBindOrderDetlDto> orderDetlList;
}
