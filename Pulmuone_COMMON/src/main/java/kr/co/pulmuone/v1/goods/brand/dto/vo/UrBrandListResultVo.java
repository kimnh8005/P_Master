package kr.co.pulmuone.v1.goods.brand.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UrBrandListResultVo {

    @ApiModelProperty(value = "브랜드 PK")
    private int urBrandId;

    @ApiModelProperty(value = "브랜드명")
    private String brandName;

}
