package kr.co.pulmuone.v1.goods.store.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrgaStoreGoodsRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "매장 PK")
    private String urStoreId;

    @ApiModelProperty(value = "카테고리 PK")
    private Long ilCtgryId;

}
