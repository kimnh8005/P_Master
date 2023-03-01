package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StampByUserVo {

    @ApiModelProperty(value = "스탬프 이벤트 PK")
    private Long evEventStampId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "이벤트 유형")
    private String eventType;

    @ApiModelProperty(value = "당첨자 안내")
    private String winnerInformation;

    @ApiModelProperty(value = "당첨자 공지")
    private String winnerNotice;

    @ApiModelProperty(value = "상세 HTML")
    private String detailHtml;

    @ApiModelProperty(value = "상세 HTML2")
    private String detailHtml2;

    @ApiModelProperty(value = "스타일 아이디")
    private String styleId;

    @ApiModelProperty(value = "참여버튼 BG Color Code")
    private String buttonColorCode;

    @ApiModelProperty(value = "기본이미지 경로")
    private String defaultPath;

    @ApiModelProperty(value = "기본이미지 원본명")
    private String defaultOriginalName;

    @ApiModelProperty(value = "체크이미지 경로")
    private String checkPath;

    @ApiModelProperty(value = "체크이미지 원본명")
    private String checkOriginalName;

    @ApiModelProperty(value = "배경이미지 경로")
    private String backgroundPath;

    @ApiModelProperty(value = "배경이미지 원본명")
    private String backgroundOriginalName;

    @ApiModelProperty(value = "스탬프개수1")
    private String stampCount1;

    @ApiModelProperty(value = "스탬프개수2")
    private String stampCount2;

    @ApiModelProperty(value = "임직원 참여 여부")
    private String employeeJoinYn;

    @ApiModelProperty(value = "참여 횟수 유형")
    private String eventJoinType;

    @ApiModelProperty(value = "유저 참여 Count")
    private int userJoinCount;

    @ApiModelProperty(value = "WEB PC 전시여부")
    private String displayWebPcYn;

    @ApiModelProperty(value = "WEB Mobile 전시여부")
    private String displayWebMobileYn;

    @ApiModelProperty(value = "APP 전시여부")
    private String displayAppYn;

    @ApiModelProperty(value = "임직원 전용 유형")
    private String evEmployeeType;

    @ApiModelProperty(value = "접근권한 설정 유형")
    private List<EventUserGroupByUserVo> userGroupList;

    @ApiModelProperty(value = "스탬프(구매) 지급조건 금액")
    private int orderPrice;

    @ApiModelProperty(value = "시작 일시")
    private String startDateTime;

    @ApiModelProperty(value = "종료 일시")
    private String endDateTime;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "유저 참여 횟수")
    private String joinCount;

    @ApiModelProperty(value = "앱전용 이벤트 여부")
    private String appOnlyEventYn;

}
