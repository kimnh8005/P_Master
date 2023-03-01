package kr.co.pulmuone.v1.goods.store.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreSearchGoodsRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "키워드")
    private String keyword;

    @ApiModelProperty(value = "매장 PK")
    private String urStoreId;

    @ApiModelProperty(value = "품절제외")
    private Boolean excludeSoldOutGoods;

    @ApiModelProperty(value = "정렬기준")
    private SortCode sortCode = SortCode.NEW;

}
