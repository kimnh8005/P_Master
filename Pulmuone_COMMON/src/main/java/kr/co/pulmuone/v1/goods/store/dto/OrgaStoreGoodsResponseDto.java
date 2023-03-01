package kr.co.pulmuone.v1.goods.store.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class OrgaStoreGoodsResponseDto {

    @ApiModelProperty(value = "목록 총 갯수")
    private int total;

    @ApiModelProperty(value = "상품 정보")
    private List<GoodsSearchResultDto> goods;

}
