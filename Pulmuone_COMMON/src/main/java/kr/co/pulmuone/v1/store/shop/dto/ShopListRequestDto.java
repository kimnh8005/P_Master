package kr.co.pulmuone.v1.store.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShopListRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "시/도 명")
    private String areaType;

    @ApiModelProperty(value = "매장명/주소 검색어")
    private String searchText;

}
