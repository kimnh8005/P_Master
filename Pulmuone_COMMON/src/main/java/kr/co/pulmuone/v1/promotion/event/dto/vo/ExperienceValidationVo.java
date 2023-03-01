package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExperienceValidationVo {

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

    @ApiModelProperty(value = "모집종료여부")
    private String recruitCloseYn;

    @ApiModelProperty(value = "댓글분류 사용 여부")
    private String commentCodeYn;

    @ApiModelProperty(value = "선착순 당첨 인원")
    private int firstComeCount;
    
    @ApiModelProperty(value = "쿠폰 PK")
    private Long pmCouponId;

    @ApiModelProperty(value = "이벤트 참여 제한 여부")
    private String eventNotAvailableYn;

    @ApiModelProperty(value = "유저 참여 수")
    private int userCount;

    @ApiModelProperty(value = "쿠폰 list pk")
    private List<EventCouponVo> coupon;

}