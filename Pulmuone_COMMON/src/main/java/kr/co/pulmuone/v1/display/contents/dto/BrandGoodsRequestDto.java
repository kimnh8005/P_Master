package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BrandGoodsRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "몰인몰 구분")
    private String mallDiv;

    @ApiModelProperty(value = "전시 브랜드 PK")
    private String dpBrandId;

    @ApiModelProperty(value = "상품 정렬 코드")
    private String goodsSortCode;

}
