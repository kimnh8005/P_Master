package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.EventEnums;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackTargetListByUserResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventListFromMyPageVo {

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "배너 이미지 경로")
    private String bannerImagePath;

    @ApiModelProperty(value = "배너 이미지 원본 명")
    private String bannerImageOriginalName;

    @ApiModelProperty(value = "이벤트 유형")
    private String eventType;

    @ApiModelProperty(value = "이벤트 제목")
    private String title;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "당첨여부")
    private String userWinnerYn;

    @ApiModelProperty(value = "스탬프 참여 갯수")
    private String stampCount;

    @ApiModelProperty(value = "스탬프 최대 참여 갯수")
    private String stampMaxCount;

    @ApiModelProperty(value = "상태")
    private String status;

    @ApiModelProperty(value = "당첨자 유무 구분자")
    private String winnerExistYn;

    @ApiModelProperty(value = "당첨자 유무 구분자")
    private String winnerNotice;

    @ApiModelProperty(value = "당첨자 유무 구분자")
    private String winnerInfor;

    @ApiModelProperty(value = "당첨일자")
    private String winnerInforDate;

    @ApiModelProperty(value = "체험단 상품 상태")
    private String experienceOrderCode = EventEnums.ExperienceOrderCode.ORDER_BEFORE.getCode();

    @ApiModelProperty(value = "체험단 상품 PK")
    private Long experienceIlGoodsId;

    @ApiModelProperty(value = "후기 작성 여부")
    private String feedbackYn;

    @ApiModelProperty(value = "후기")
    private FeedbackTargetListByUserResultVo feedback;

}