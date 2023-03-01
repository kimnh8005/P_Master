package kr.co.pulmuone.v1.goods.search.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsSearchHmrVo {

    @ApiModelProperty(value = "카테고리 PK")
    private Long ilCtgryId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

}
