package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventListByUserVo {

    @ApiModelProperty(value = "이벤트 PK")
    private String evEventId;

    @ApiModelProperty(value = "이벤트 제목")
    private String title;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "이벤트 몰인몰 유형")
    private String mallDiv;

    @ApiModelProperty(value = "이벤트 유형")
    private String eventType;

    @ApiModelProperty(value = "배너 이미지 경로")
    private String bannerImagePath;

    @ApiModelProperty(value = "배너 이미지 원본 명")
    private String bannerImageOriginalName;

    @ApiModelProperty(value = "당첨자 안내")
    private String winnerInformation;

    @ApiModelProperty(value = "당첨자 공지")
    private String winnerNotice;
    
    @ApiModelProperty(value = "종료여부")
    private String endYn;

    @ApiModelProperty(value = "이벤트 추첨 유형")
    private String eventDrawType;

    @ApiModelProperty(value = "이벤트 혜택 유형")
    private String eventBenefitType;

    @ApiModelProperty(value = "잔여 혜택 수")
    private Integer remainBenefitCount;
    
}