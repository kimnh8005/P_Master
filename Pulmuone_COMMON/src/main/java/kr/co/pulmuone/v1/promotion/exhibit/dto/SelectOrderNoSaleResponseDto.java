package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SelectOrderNoSaleResponseDto {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

}
