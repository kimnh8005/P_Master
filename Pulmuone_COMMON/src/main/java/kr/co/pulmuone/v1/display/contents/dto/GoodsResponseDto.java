package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GoodsResponseDto {

    @ApiModelProperty(value = "total")
    private long total;

    @ApiModelProperty(value = "상품 검색 정보")
    private List<GoodsSearchResultDto> goods;

}
