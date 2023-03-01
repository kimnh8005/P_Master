package kr.co.pulmuone.v1.order.front.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderInfoFromRewardVo {

    @ApiModelProperty(value = "주문 번호")
    private String odid;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "주문 일자")
    private String orderDate;

    @ApiModelProperty(value = "도착 예정 일자")
    private String deliveryDate;

    @ApiModelProperty(value = "기 신청 여부")
    private String rewardRequestYn;

    @ApiModelProperty(value = "합 유형")
    private String packType;

    @ApiModelProperty(value = "타이틀")
    private String packTitle;

    @ApiModelProperty(value = "묶음상품 상품 PK")
    private String packGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "상품이미지")
    private String goodsImagePath;

    @ApiModelProperty(value = "배송기준 - 배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "배송기준 - 배송비")
    private String odShippingPriceId;

    @ApiModelProperty(value = "배송기준")
    private String deliveryKey;

}
