package kr.co.pulmuone.v1.goods.category.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품검색 ResponseDto")
public class GetSearchGoodsListResponseDto
{

	@ApiModelProperty(value = "상품검색 결과 Dto")
	SearchResultDto data;

}
