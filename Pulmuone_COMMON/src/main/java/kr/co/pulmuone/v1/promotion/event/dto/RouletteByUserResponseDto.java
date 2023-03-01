package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventGroupByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.RouletteByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.RouletteItemByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RouletteByUserResponseDto {

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

    @ApiModelProperty(value = "시작버튼 경로")
    private String startButtonPath;

    @ApiModelProperty(value = "시작버튼 원본 명")
    private String startButtonOriginalName;

    @ApiModelProperty(value = "화살표 이미지 경로")
    private String arrowPath;

    @ApiModelProperty(value = "화살표 이미지 원본명")
    private String arrowOriginalName;

    @ApiModelProperty(value = "배경 이미지 경로")
    private String backgroundPath;

    @ApiModelProperty(value = "배경 이미지 원본 명")
    private String backgroundOriginalName;

    @ApiModelProperty(value = "룰렛 이미지 경로")
    private String roulettePath;

    @ApiModelProperty(value = "임직원 참여 여부")
    private String employeeJoinYn;

    @ApiModelProperty(value = "참여 횟수 유형")
    private String eventJoinType;

    @ApiModelProperty(value = "유저 참여 여부")
    private String userJoinYn = "N";

    @ApiModelProperty(value = "룰렛 Count")
    private int rouletteCount;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "룰렛 아이템 정보")
    private List<RouletteItemByUserVo> item;

    @ApiModelProperty(value = "이벤트 상품 그룹")
    private List<EventGroupByUserVo> group;

    public RouletteByUserResponseDto(RouletteByUserVo vo) {
        this.title = vo.getTitle();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.winnerInformation = vo.getWinnerInformation();
        this.winnerNotice = vo.getWinnerNotice();
        this.detailHtml = vo.getDetailHtml();
        this.detailHtml2 = vo.getDetailHtml2();
        this.styleId = vo.getStyleId();
        this.startButtonPath = vo.getStartButtonPath();
        this.startButtonOriginalName = vo.getStartButtonOriginalName();
        this.arrowPath = vo.getArrowPath();
        this.arrowOriginalName = vo.getArrowOriginalName();
        this.backgroundPath = vo.getBackgroundPath();
        this.backgroundOriginalName = vo.getBackgroundOriginalName();
        this.roulettePath = vo.getRoulettePath();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.eventJoinType = vo.getEventJoinType();
        this.rouletteCount = vo.getRouletteCount();
        this.endYn = vo.getEndYn();
        if (vo.getUserJoinCount() > 0) {
            this.userJoinYn = "Y";
        }
    }
}
