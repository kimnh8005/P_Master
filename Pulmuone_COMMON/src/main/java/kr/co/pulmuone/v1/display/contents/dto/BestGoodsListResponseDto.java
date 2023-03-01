package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BestGoodsListResponseDto {

    @ApiModelProperty(value = "카테고리 PK")
    private Long ilCtgryId;

    @ApiModelProperty(value = "카테고리 PK")
    private String categoryName;

    @ApiModelProperty(value = "상품수량")
    private int total;

    @ApiModelProperty(value = "카테고리 상품 정보")
    private List<GoodsSearchResultDto> goods;

    public BestGoodsListResponseDto(GetCategoryListResultVo vo) {
        this.ilCtgryId = (long) vo.getIlCategoryId();
        this.categoryName = vo.getCategoryName();
    }

    public BestGoodsListResponseDto(Long ilCtgryId, String categoryName) {
        this.ilCtgryId = ilCtgryId;
        this.categoryName = categoryName;
    }
}
