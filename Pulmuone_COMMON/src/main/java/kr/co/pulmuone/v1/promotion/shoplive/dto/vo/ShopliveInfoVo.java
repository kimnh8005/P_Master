package kr.co.pulmuone.v1.promotion.shoplive.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel(description = "샵라이브 기본정보")
public class ShopliveInfoVo {

    @ApiModelProperty(value = "샵라이브PK")
    private Long evShopliveId;

    @ApiModelProperty(value = "방송제목")
    private String title;

    @ApiModelProperty(value = "연동코드")
    private String campaignKey;

    @ApiModelProperty(value = "시작일자(일단위)")
    private String startDate;

    @ApiModelProperty(value = "종료일자(일단위)")
    private String endDate;

    @ApiModelProperty(value = "시작일자(시분초포함)")
    private String startDt;

    @ApiModelProperty(value = "종료일자(시분초포함)")
    private String endDt;

    @ApiModelProperty(value = "방송 상태")
    private String campaignStatus;

    @ApiModelProperty(value = "작성자ID")
    private String createId;

    @ApiModelProperty(value = "작성일자")
    private String createDt;

    @ApiModelProperty(value = "작성자명")
    private String createNm;

    @ApiModelProperty(value = "수정자ID")
    private String modifyId;

    @ApiModelProperty(value = "수정일자")
    private String modifyDt;

    @ApiModelProperty(value = "수정자명")
    private String modifyNm;
}
