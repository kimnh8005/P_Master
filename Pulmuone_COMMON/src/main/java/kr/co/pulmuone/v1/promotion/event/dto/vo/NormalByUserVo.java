package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NormalByUserVo {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

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
    
    @ApiModelProperty(value = "일반이벤트 참여 방법")
    private String normalEventType;

    @ApiModelProperty(value = "댓글 코드 사용 여부")
    private String commentCodeYn;

    @ApiModelProperty(value = "임직원 참여 여부")
    private String employeeJoinYn;

    @ApiModelProperty(value = "참여 횟수 유형")
    private String eventJoinType;

    @ApiModelProperty(value = "추첨 방식 유형")
    private String eventDrawType;

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

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "이벤트 유형")
    private String eventType;

    @ApiModelProperty(value = "이벤트 혜택 구분")
    private String eventBenefitType;

    @ApiModelProperty(value = "잔여 혜택 수")
    private int remainBenefitCount;

    @ApiModelProperty(value = "앱전용 이벤트 여부")
    private String appOnlyEventYn;

    @ApiModelProperty(value = "접근권한 설정 유형")
    private List<EventUserGroupByUserVo> userGroupList;

}
