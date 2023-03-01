package kr.co.pulmuone.v1.promotion.shoplive.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ShopliveRequestDto {

    @ApiModelProperty(value = "샵라이브PK")
    private Long evShopliveId;

    @ApiModelProperty(value = "방송제목")
    private String title;

    @ApiModelProperty(value = "연동코드")
    private String campaignKey;

    @ApiModelProperty(value = "시작일자(시분초포함)")
    private String startDt;

    @ApiModelProperty(value = "종료일자(시분초포함)")
    private String endDt;

    @ApiModelProperty(value = "작성자ID")
    private String createId;

    @ApiModelProperty(value = "수정자ID")
    private String modifyId;
}
