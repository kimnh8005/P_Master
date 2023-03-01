package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class LohasBannerResponseDto {

    @ApiModelProperty(value = "갯수")
    private int total;

    @ApiModelProperty(value = "로하스 배너 정보")
    private List<ContentsDetailBannerResponseDto> banner;

}
