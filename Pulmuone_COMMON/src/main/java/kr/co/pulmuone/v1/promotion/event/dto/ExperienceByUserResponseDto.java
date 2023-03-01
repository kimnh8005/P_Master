package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchExperienceVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.CommentCodeByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventGroupByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.ExperienceByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExperienceByUserResponseDto {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "체험단 시작일자")
    private String startDate;

    @ApiModelProperty(value = "체험단 종료일자")
    private String endDate;

    @ApiModelProperty(value = "D-Day")
    private Long dday;

    @ApiModelProperty(value = "모집기간 시작일자")
    private String recruitStartDate;

    @ApiModelProperty(value = "모집기간 종료일자")
    private String recruitEndDate;

    @ApiModelProperty(value = "당첨자 선정 시작일자")
    private String selectStartDate;

    @ApiModelProperty(value = "당첨자 선정 종료일자")
    private String selectEndDate;

    @ApiModelProperty(value = "후기 시작일자")
    private String reviewStartDate;

    @ApiModelProperty(value = "후기 종료일자")
    private String reviewEndDate;

    @ApiModelProperty(value = "당첨자 안내")
    private String winnerInformation;

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

    @ApiModelProperty(value = "유저 참여 여부")
    private String userJoinYn = "N";

    @ApiModelProperty(value = "참여버튼 백그라운드 컬러 코드")
    private String buttonColorCode;

    @ApiModelProperty(value = "추첨 유형")
    private String eventDrawType;

    @ApiModelProperty(value = "모집 종료 여부")
    private String recruitCloseYn;

    @ApiModelProperty(value = "당첨자 공지사항")
    private String winnerNotice;

    @ApiModelProperty(value = "댓글 코드 사용 여부")
    private String commentCodeYn;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "댓글 코드")
    private List<CommentCodeByUserVo> commentCode;

    @ApiModelProperty(value = "체험단 상품 정보")
    private GoodsSearchExperienceVo goods;

    @ApiModelProperty(value = "이벤트 상품 그룹")
    private List<EventGroupByUserVo> group;

    public ExperienceByUserResponseDto(ExperienceByUserVo vo) {
        this.title = vo.getTitle();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.recruitStartDate = vo.getRecruitStartDate();
        this.recruitEndDate = vo.getRecruitEndDate();
        this.selectStartDate = vo.getSelectStartDate();
        this.selectEndDate = vo.getSelectEndDate();
        this.reviewStartDate = vo.getReviewStartDate();
        this.reviewEndDate = vo.getReviewEndDate();
        this.winnerInformation = vo.getWinnerInformation();
        this.detailHtml = vo.getDetailHtml();
        this.detailHtml2 = vo.getDetailHtml2();
        this.styleId = vo.getStyleId();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.eventJoinType = vo.getEventJoinType();
        this.buttonColorCode = vo.getButtonColorCode();
        this.eventDrawType = vo.getEventDrawType();
        this.recruitCloseYn = vo.getRecruitCloseYn();
        this.winnerNotice = vo.getWinnerNotice();
        this.commentCodeYn = vo.getCommentCodeYn();
        this.endYn = vo.getEndYn();
        if (vo.getUserJoinCount() > 0) {
            this.userJoinYn = "Y";
        }
    }
}
