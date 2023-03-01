package kr.co.pulmuone.v1.shopping.recently.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecentlyViewVo {

    @ApiModelProperty(value = "최근본상품 PK")
    private Long spRecentlyViewId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

}