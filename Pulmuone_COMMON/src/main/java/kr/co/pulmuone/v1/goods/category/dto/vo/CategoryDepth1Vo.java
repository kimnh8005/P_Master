package kr.co.pulmuone.v1.goods.category.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDepth1Vo {

    @ApiModelProperty(value = "카테고리 PK")
    private Long ilCtgryId;

    @ApiModelProperty(value = "카테고리 명")
    private String categoryName;

    @ApiModelProperty(value = "상품 존재 여부")
    private String existGoodsYn;

}