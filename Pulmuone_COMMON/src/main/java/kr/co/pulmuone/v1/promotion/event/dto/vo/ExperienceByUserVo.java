package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExperienceByUserVo {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "체험단 시작일자")
    private String startDate;

    @ApiModelProperty(value = "체험단 종료일자")
    private String endDate;

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

    @ApiModelProperty(value = "유저 참여 Count")
    private int userJoinCount;

    @ApiModelProperty(value = "참여버튼 백그라운드 컬러 코드")
    private String buttonColorCode ;

    @ApiModelProperty(value = "추첨 유형")
    private String eventDrawType;

    @ApiModelProperty(value = "모집 종료 여부")
    private String recruitCloseYn;

    @ApiModelProperty(value = "당첨자 공지사항")
    private String winnerNotice;

    @ApiModelProperty(value = "댓글 구분 사용 여부")
    private String commentCodeYn;

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

    @ApiModelProperty(value = "접근권한 설정 유형")
    private List<EventUserGroupByUserVo> userGroupList;

    @ApiModelProperty(value = "체험단 상품 PK")
    private Long ilGoodsId;

}
