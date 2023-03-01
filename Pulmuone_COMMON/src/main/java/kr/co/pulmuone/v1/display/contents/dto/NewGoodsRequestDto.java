package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewGoodsRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "몰인몰구분")
    private String mallDiv = GoodsEnums.MallDiv.PULMUONE.getCode();
    
    @ApiModelProperty(value = "상품 정렬 코드")
    private String goodsSortCode;

}
