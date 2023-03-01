package kr.co.pulmuone.v1.goods.search.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsSearchAdditionalVo {

    @ApiModelProperty(value = "상품 PK")
    private Long goodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "썸네일 Path")
    private String thumbnailPath;

}