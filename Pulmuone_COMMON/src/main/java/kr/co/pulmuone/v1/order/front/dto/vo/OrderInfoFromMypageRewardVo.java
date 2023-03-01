package kr.co.pulmuone.v1.order.front.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderInfoFromMypageRewardVo {

    @ApiModelProperty(value = "주문일자")
    private String orderDate;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "배송예정일")
    private String deliveryDate;

    @ApiModelProperty(value = "기획전 PK")
    private String evExhibitId;

    @ApiModelProperty(value = "상품유형")
    private String packType;

    @ApiModelProperty(value = "타이틀")
    private String packTitle;

    @ApiModelProperty(value = "이미지경로")
    private String thumbnailPath;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

}
