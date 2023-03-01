package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BestGoodsRequestDto {
    @ApiModelProperty(value = "조회 수량")
    private int total;

    @ApiModelProperty(value = "몰구분")
    private String mallDiv;

    @ApiModelProperty(value = "카테고리 PK")
    private Long ilCtgryId;

    @ApiModelProperty(value = "디바이스 유형")
    private String deviceType;

    @ApiModelProperty(value = "유저 유형")
    private String userType;
    
}
