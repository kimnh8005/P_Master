package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StampDetailByUserVo {

    @ApiModelProperty(value = "스탬프번호")
    private String stampCount;

    @ApiModelProperty(value = "기본이미지경로")
    private String defaultPath;

    @ApiModelProperty(value = "기본이미지원본파일명")
    private String defaultOriginalName;

    @ApiModelProperty(value = "체크이미지경로")
    private String checkPath;

    @ApiModelProperty(value = "체크이미지원본파일명")
    private String checkOriginalName;

    @ApiModelProperty(value = "아이콘이미지경로")
    private String iconPath;

    @ApiModelProperty(value = "아이콘이미지원본파일명")
    private String iconOriginalName;

    @ApiModelProperty(value = "스탬프노출 URL")
    private String stampUrl;

}
