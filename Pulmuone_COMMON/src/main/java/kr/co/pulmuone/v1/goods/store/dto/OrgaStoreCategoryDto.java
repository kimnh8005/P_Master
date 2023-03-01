package kr.co.pulmuone.v1.goods.store.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrgaStoreCategoryDto {

    @ApiModelProperty(value = "카테고리 PK")
    private Long ilCtgryId;

    @ApiModelProperty(value = "카테고리 명")
    private String categoryName;

    @ApiModelProperty(value = "HMR / 손질상품 정보")
    private List<GoodsSearchResultDto> hmrGoods;

}
