package kr.co.pulmuone.v1.store.shop.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "픽업가능 매장 리스트 Result")
public class PickUpShopListVo {

    @ApiModelProperty(value = "매장 PK")
    private String urStoreId;

    @ApiModelProperty(value = "매장 명")
    private String name;

}
