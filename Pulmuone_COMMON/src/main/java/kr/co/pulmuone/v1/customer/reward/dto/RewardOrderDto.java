package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromRewardVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RewardOrderDto {

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 일자")
    private String orderDate;

    @ApiModelProperty(value = "주문 번호")
    private String odid;

    @ApiModelProperty(value = "도착예정일")
    private String deliveryDate;

    @ApiModelProperty(value = "배송기준 - 배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "배송기준 - 배송비")
    private String odShippingPriceId;

    @ApiModelProperty(value = "신청완료여부")
    private String rewardRequestYn;

    @ApiModelProperty(value = "주문정보")
    private List<OrderInfoFromRewardVo> order;

    public RewardOrderDto(OrderInfoFromRewardVo vo) {
        this.odOrderId = vo.getOdOrderId();
        this.orderDate = vo.getOrderDate();
        this.odid = vo.getOdid();
        this.deliveryDate = vo.getDeliveryDate();
        this.goodsDeliveryType = vo.getGoodsDeliveryType();
        this.odShippingPriceId = vo.getOdShippingPriceId();
        this.rewardRequestYn = vo.getRewardRequestYn();
    }
    
}
