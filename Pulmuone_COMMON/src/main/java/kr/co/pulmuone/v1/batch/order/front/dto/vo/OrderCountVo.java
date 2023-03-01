package kr.co.pulmuone.v1.batch.order.front.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderCountVo {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private Long orderCountSum;

    @ApiModelProperty(value = "풀무원 대카테고리 PK")
    private Long pulmuoneCategoryId;

    @ApiModelProperty(value = "올가 대카테고리 PK")
    private Long orgaCategoryId;

    @ApiModelProperty(value = "잇슬림 대카테고리 PK")
    private Long eatslimCategoryId;

    @ApiModelProperty(value = "베이비밀 대카테고리 PK")
    private Long babymealCategoryId;

}
