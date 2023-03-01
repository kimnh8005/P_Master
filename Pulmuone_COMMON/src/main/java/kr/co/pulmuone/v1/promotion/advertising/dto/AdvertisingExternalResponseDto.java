package kr.co.pulmuone.v1.promotion.advertising.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.advertising.dto.vo.AdvertisingExternalVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class AdvertisingExternalResponseDto {

    @ApiModelProperty(value = "외부광고코드 List")
    private List<AdvertisingExternalVo> advertisingExternalList;

}
