package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventGroupByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.StampByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.StampDetailByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StampByUserResponseDto {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "D-Day")
    private Long dday;

    @ApiModelProperty(value = "이벤트 유형")
    private String eventType;

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

    @ApiModelProperty(value = "유저 참여 여부")
    private String userJoinYn = "N";

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "스탬프 모두 참여여부(Y : 모두참여)")
    private String joinAllYn;

    @ApiModelProperty(value = "당첨자 안내")
    private String winnerInformation;

    @ApiModelProperty(value = "당첨자 공지")
    private String winnerNotice;

    @ApiModelProperty(value = "스탬프 상세")
    private List<StampDetailByUserVo> stamp;

    @ApiModelProperty(value = "유저 참여 정보")
    private List<Integer> stampCount;

    @ApiModelProperty(value = "앱전용 이벤트 디바이스 아님")
    private String notDeviceAppOnlyEventYn;

    @ApiModelProperty(value = "이벤트 상품 그룹")
    private List<EventGroupByUserVo> group;

    public StampByUserResponseDto(StampByUserVo vo) {
        this.title = vo.getTitle();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventType = vo.getEventType();
        this.detailHtml = vo.getDetailHtml();
        this.detailHtml2 = vo.getDetailHtml2();
        this.styleId = vo.getStyleId();
        this.buttonColorCode = vo.getButtonColorCode();
        this.defaultPath = vo.getDefaultPath();
        this.defaultOriginalName = vo.getDefaultOriginalName();
        this.checkPath = vo.getCheckPath();
        this.checkOriginalName = vo.getCheckOriginalName();
        this.backgroundPath = vo.getBackgroundPath();
        this.backgroundOriginalName = vo.getBackgroundOriginalName();
        this.stampCount1 = vo.getStampCount1();
        this.stampCount2 = vo.getStampCount2();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.eventJoinType = vo.getEventJoinType();
        this.endYn = vo.getEndYn();
        this.winnerInformation = vo.getWinnerInformation();
        this.winnerNotice = vo.getWinnerNotice();
        if (vo.getUserJoinCount() > 0) {
            this.userJoinYn = "Y";
        }
    }
}
