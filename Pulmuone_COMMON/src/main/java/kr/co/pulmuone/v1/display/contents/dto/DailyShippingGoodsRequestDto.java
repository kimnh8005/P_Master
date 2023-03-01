package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DailyShippingGoodsRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "일일배송 조회 구분")
    private String goodsDailyType;

    @ApiModelProperty(value = "상품 정렬 코드")
    private String goodsSortCode;

}
