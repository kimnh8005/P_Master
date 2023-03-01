package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GreenJuicePageRequestDto {

    @ApiModelProperty(value = "주소 ZIP 코드")
    private String zipCode;

    @ApiModelProperty(value = "빌딩코드")
    private String buildingCode;

}
