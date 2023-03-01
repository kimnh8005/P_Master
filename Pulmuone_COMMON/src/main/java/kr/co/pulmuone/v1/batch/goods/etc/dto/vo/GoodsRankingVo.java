package kr.co.pulmuone.v1.batch.goods.etc.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GoodsRankingVo {

    @ApiModelProperty(value = "몰 구분")
    private String mallDiv;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "대 카테고리 PK")
    private Long ilCtgryId;

    @ApiModelProperty(value = "랭킹")
    private int ranking;

    @ApiModelProperty(value = "베스트 여부")
    private String bestYn;

}