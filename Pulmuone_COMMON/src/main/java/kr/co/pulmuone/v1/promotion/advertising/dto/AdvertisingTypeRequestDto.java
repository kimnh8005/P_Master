package kr.co.pulmuone.v1.promotion.advertising.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdvertisingTypeRequestDto {

    @ApiModelProperty(value = "외부광고코드 유형")
    private String searchType;

    @ApiModelProperty(value = "매체")
    private String source;

    @ApiModelProperty(value = "구좌")
    private String medium;

    @ApiModelProperty(value = "캠페인")
    private String campaign;

}
