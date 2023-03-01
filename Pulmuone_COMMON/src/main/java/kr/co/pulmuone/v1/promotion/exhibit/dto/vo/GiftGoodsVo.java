package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GiftGoodsVo {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품 명")
    private String goodsName;

    @ApiModelProperty(value = "상품 이미지")
    private String thumbnailPath;

    @ApiModelProperty(value = "증정 기본 수량")
    private int giftCount = 0;

    @ApiModelProperty(value = "재고 수량")
    private int stock = 0 ;

    @ApiModelProperty(value = "출고처")
	private Long urWareHouseId;

    @ApiModelProperty(value = "합배송여부")
	private String bundleYn;
}