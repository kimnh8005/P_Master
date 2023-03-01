package kr.co.pulmuone.v1.display.contents.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.AggregationDocumentDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaleGoodsPageInfoResponseDto {

    @ApiModelProperty(value = "대 카테고리")
    private List<AggregationDocumentDto> category;

}
