package kr.co.pulmuone.v1.promotion.advertising.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdvertisingExternalRequestDto {

    @ApiModelProperty(value = "외부광고코드 PK")
    private String pmAdExternalCd;

    @ApiModelProperty(value = "외부광고코드 PK List")
    private List<String> pmAdExternalCdList;

}
