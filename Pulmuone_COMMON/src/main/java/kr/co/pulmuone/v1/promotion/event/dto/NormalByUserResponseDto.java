package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.CommentCodeByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.NormalByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventGroupByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NormalByUserResponseDto {

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

    @ApiModelProperty(value = "일반이벤트 참여방법")
    private String normalEventType;

    @ApiModelProperty(value = "댓글 코드 사용 여부")
    private String commentCodeYn;

    @ApiModelProperty(value = "댓글 코드")
    private List<CommentCodeByUserVo> commentCode;

    @ApiModelProperty(value = "임직원 참여 여부")
    private String employeeJoinYn;

    @ApiModelProperty(value = "참여 횟수 유형")
    private String eventJoinType;

    @ApiModelProperty(value = "추첨 방식 유형")
    private String eventDrawType;

    @ApiModelProperty(value = "유저 참여 여부")
    private String userJoinYn = "N";

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "앱전용 이벤트 디바이스 아님")
    private String notDeviceAppOnlyEventYn;

    @ApiModelProperty(value = "앱전용 이벤트 디바이스 아님")
    private int remainBenefitCount;

    @ApiModelProperty(value = "이벤트 상품 그룹")
    private List<EventGroupByUserVo> group;

    public NormalByUserResponseDto(NormalByUserVo vo) {
        this.title = vo.getTitle();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.winnerInformation = vo.getWinnerInformation();
        this.winnerNotice = vo.getWinnerNotice();
        this.detailHtml = vo.getDetailHtml();
        this.detailHtml2 = vo.getDetailHtml2();
        this.styleId = vo.getStyleId();
        this.normalEventType = vo.getNormalEventType();
        this.commentCodeYn = vo.getCommentCodeYn();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.eventJoinType = vo.getEventJoinType();
        this.eventDrawType = vo.getEventDrawType();
        this.endYn = vo.getEndYn();
        if (vo.getUserJoinCount() > 0) {
            this.userJoinYn = "Y";
        }
        this.remainBenefitCount = vo.getRemainBenefitCount();
    }
}
