package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SurveyByUserResponseDto {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "D-Day")
    private Long dday;

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

    @ApiModelProperty(value = "임직원 참여 여부")
    private String employeeJoinYn;

    @ApiModelProperty(value = "참여 횟수 유형")
    private String eventJoinType;

    @ApiModelProperty(value = "참여 방식 유형")
    private String eventDrawType;

    @ApiModelProperty(value = "유저 참여 여부")
    private String userJoinYn = "N";

    @ApiModelProperty(value = "참여버튼 백그라운드 컬러 코드")
    private String buttonColorCode ;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "앱전용 이벤트 디바이스 아님")
    private String notDeviceAppOnlyEventYn;

    @ApiModelProperty(value = "룰렛 아이템 정보")
    private List<SurveyQuestionByUserVo> question;

    @ApiModelProperty(value = "이벤트 상품 그룹")
    private List<EventGroupByUserVo> group;

    public SurveyByUserResponseDto(SurveyByUserVo vo) {
        this.title = vo.getTitle();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.winnerInformation = vo.getWinnerInformation();
        this.detailHtml = vo.getDetailHtml();
        this.detailHtml2 = vo.getDetailHtml2();
        this.styleId = vo.getStyleId();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.eventJoinType = vo.getEventJoinType();
        this.eventDrawType = vo.getEventDrawType();
        this.buttonColorCode = vo.getButtonColorCode();
        this.endYn = vo.getEndYn();
        if (vo.getUserJoinCount() > 0) {
            this.userJoinYn = "Y";
        }
    }
}
