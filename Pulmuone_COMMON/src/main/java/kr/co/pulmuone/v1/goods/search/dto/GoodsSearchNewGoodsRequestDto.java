package kr.co.pulmuone.v1.goods.search.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class GoodsSearchNewGoodsRequestDto {

    @ApiModelProperty(value = "몰 구분")
    private String mallDiv;

    @ApiModelProperty(value = "브랜드 리스트")
    private List<String> dpBrandIdList;

    @ApiModelProperty(value = "분류")
    private String ctgryIdDepth1;

    @ApiModelProperty(value = "신상품 조회 월")
    private Integer monthSub;

    @ApiModelProperty(value = "매장전용상품 제외여부")
    private boolean exceptShopOnly;

}
