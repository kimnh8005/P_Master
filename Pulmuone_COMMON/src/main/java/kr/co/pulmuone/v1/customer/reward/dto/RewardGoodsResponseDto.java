package kr.co.pulmuone.v1.customer.reward.dto;

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
public class RewardGoodsResponseDto {

    @ApiModelProperty(value = "total")
    private int total;

    @ApiModelProperty(value = "보상제 대상 상품 정보")
    private List<GoodsSearchResultDto> goods;

}
