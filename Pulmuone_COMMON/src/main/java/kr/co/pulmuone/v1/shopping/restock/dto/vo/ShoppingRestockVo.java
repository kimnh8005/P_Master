package kr.co.pulmuone.v1.shopping.restock.dto.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ShoppingRestockVo {

    @ApiModelProperty(value = "재입고 알림 PK")
    private Long spRestockId;

    @ApiModelProperty(value = "사용자 ID")
    private String urUserId;

    @ApiModelProperty(value = "Mobile Number")
    private String mobile;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "상품 ID")
    private String ilGoodsId;

    @ApiModelProperty(value = "재입고 알림 Array", required = false)
    List<Long> spRestockIdArray;

}
