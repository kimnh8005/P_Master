package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class GoodsRankingRequestDto {

    @ApiModelProperty(value = "몰구분")
    private String mallDiv;

    @ApiModelProperty(value = "카테고리")
    private Long categoryIdDepth1;

    @ApiModelProperty(value = "조회 수")
    private Integer total;

    @ApiModelProperty(value = "베스트 상품 조회여부")
    private String bestYn;

    @ApiModelProperty(value = "전시 브랜드 ID 리스트")
    private List<String> dpBrandIdList;

    @ApiModelProperty(value = "매장전용상품 제외여부")
    private boolean exceptShopOnly;

}
