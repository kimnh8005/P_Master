package kr.co.pulmuone.v1.order.front.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class OrderInfoFromMypageRewardRequestDto {

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "도착예정일자")
    private String deliveryDate;

    @ApiModelProperty(value = "배송유형 코드")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "주문 배송비")
    private String odShippingPriceId;

    @ApiModelProperty(value = "상품 PK")
    private List<Long> goodsIdList;

}
