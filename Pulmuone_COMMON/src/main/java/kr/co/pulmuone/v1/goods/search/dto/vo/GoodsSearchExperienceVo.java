package kr.co.pulmuone.v1.goods.search.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsSearchExperienceVo {

    @ApiModelProperty(value = "상품 PK")
    private Long goodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "상품 설명")
    private String goodsDesc;

    @ApiModelProperty(value = "썸네일 Path")
    private String thumbnailPath;

    @ApiModelProperty(value = "신상품 여부")
    private Boolean isNewGoods;

    @ApiModelProperty(value = "MD 추천 상품 여부")
    private Boolean isRecommendedGoods;

    @ApiModelProperty(value = "베스트 상품 여부")
    private Boolean isBestGoods;

}
