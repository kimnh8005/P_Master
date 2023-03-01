package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventInfoFromMetaVo {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "배너 이미지 경로")
    private String bannerImagePath;

    @ApiModelProperty(value = "진행기간")
    private String dateInfo;

}
