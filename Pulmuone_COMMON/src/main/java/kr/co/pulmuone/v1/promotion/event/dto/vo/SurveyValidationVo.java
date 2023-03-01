package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SurveyValidationVo {

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "참여횟수 설정유형")
    private String eventJoinType;

    @ApiModelProperty(value = "임직원 참여여부")
    private String employeeJoinYn;

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

    @ApiModelProperty(value = "유저 참여 정보")
    private int userJoinCount;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "당첨자 설정 유형")
    private String eventDrawType;

    @ApiModelProperty(value = "당첨자 혜택 유형")
    private String eventBenefitType;

    @ApiModelProperty(value = "적립금 PK")
    private Long pmPointId;

    @ApiModelProperty(value = "당첨자 혜택")
    private String benefitName;

    @ApiModelProperty(value = "이벤트 참여 제한 여부")
    private String eventNotAvailableYn;

    @ApiModelProperty(value = "쿠폰 list pk")
    private List<EventCouponVo> coupon;
}