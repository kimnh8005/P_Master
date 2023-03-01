package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LohasBannerRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "인벤토리 코드", hidden = true)
    private String inventoryCd;

    @ApiModelProperty(value = "디바이스 타입", hidden = true)
    private String deviceType;

}
