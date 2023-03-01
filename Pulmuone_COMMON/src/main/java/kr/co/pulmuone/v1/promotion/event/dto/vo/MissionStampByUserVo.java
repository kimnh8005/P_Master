package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MissionStampByUserVo {

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "스탬프 번호")
    private String stampCount;

    @ApiModelProperty(value = "임직원 참여 여부")
    private String employeeJoinYn;

    @ApiModelProperty(value = "임직원 전용 유형")
    private String evEmployeeType;

    @ApiModelProperty(value = "총 스탬프 갯수")
    private int totalStampCount;

    @ApiModelProperty(value = "유저 참여 총 갯수")
    private int joinStampCount;

    @ApiModelProperty(value = "아이콘 경로")
    private String iconPath;

    @ApiModelProperty(value = "스탬프 URL")
    private String stampUrl;

    @ApiModelProperty(value = "이벤트 참여 유형")
    private String eventJoinType;

    @ApiModelProperty(value = "이벤트 당일 참여여부")
    private int userJoinTodayCount;

    @ApiModelProperty(value = "이벤트 아이템 유저 참여여부")
    private int userJoinItemCount;

}
