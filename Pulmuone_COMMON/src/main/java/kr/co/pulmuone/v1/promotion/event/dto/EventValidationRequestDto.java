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
public class EventValidationRequestDto {

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

    @ApiModelProperty(value = "디바이스 유형")
    private String deviceType;

    @ApiModelProperty(value = "유저 상태")
    private String userStatus;

    @ApiModelProperty(value = "유저 등급 PK")
    private Long urGroupId;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "이벤트 참여 제한 여부")
    private String eventNotAvailableYn;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "이벤트 유형")
    private String eventType;

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "이벤트 당첨 유형")
    private String eventDrawType;

    @ApiModelProperty(value = "이벤트 혜택 유형")
    private String eventBenefitType;

    @ApiModelProperty(value = "잔여 혜택 수")
    private int remainBenefitCount;

    @ApiModelProperty(value = "cicd 참여 수")
    private int cicdCount;

    @ApiModelProperty(value = "신규 회원 확인 유무")
    private String checkNewUserYn;

    @ApiModelProperty(value = "신규 회원 여부")
    private String newUserYn;

    @ApiModelProperty(value = "선택된 쿠폰")
    private String selectCouponId;

    @ApiModelProperty(value = "쿠폰 list")
    private List<EventCouponVo> couponList;

    public EventValidationRequestDto() {
    }

    public EventValidationRequestDto(NormalValidationVo vo, NormalJoinRequestDto dto) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.endYn = vo.getEndYn();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.eventNotAvailableYn = vo.getEventNotAvailableYn();
        this.eventDrawType = vo.getEventDrawType();
        this.eventBenefitType = vo.getEventBenefitType();
        this.eventType = vo.getEventType();
        this.remainBenefitCount = vo.getRemainBenefitCount();
        this.cicdCount = vo.getCicdCount();
        this.checkNewUserYn = vo.getCheckNewUserYn();
        this.newUserYn = vo.getNewUserYn();
        this.couponList = vo.getCoupon();
        this.selectCouponId = dto.getSelectCouponId();
    }

    public EventValidationRequestDto(StampValidationVo vo, StampJoinRequestDto dto) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.endYn = vo.getEndYn();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.eventNotAvailableYn = vo.getEventNotAvailableYn();
        this.cicdCount = vo.getCicdCount();
        this.eventType = vo.getEventType();
    }

    public EventValidationRequestDto(RouletteValidationVo vo, RouletteJoinRequestDto dto) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.endYn = vo.getEndYn();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.eventNotAvailableYn = vo.getEventNotAvailableYn();
        this.eventType = vo.getEventType();
        this.cicdCount = vo.getCicdCount();
    }

    public EventValidationRequestDto(SurveyValidationVo vo, SurveyJoinRequestDto dto) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.endYn = vo.getEndYn();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.eventNotAvailableYn = vo.getEventNotAvailableYn();
    }

    public EventValidationRequestDto(ExperienceValidationVo vo, ExperienceJoinRequestDto dto) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.endYn = vo.getEndYn();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.eventNotAvailableYn = vo.getEventNotAvailableYn();
    }

    public EventValidationRequestDto(EventRequestDto dto, NormalByUserVo vo) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.eventType = vo.getEventType();
        this.eventDrawType = vo.getEventDrawType();
        this.eventBenefitType = vo.getEventBenefitType();
        this.remainBenefitCount = vo.getRemainBenefitCount();
        this.endYn = vo.getEndYn();
        this.evEventId = dto.getEvEventId();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

    public EventValidationRequestDto(EventRequestDto dto, StampByUserVo vo) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

    public EventValidationRequestDto(EventRequestDto dto, RouletteByUserVo vo) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

    public EventValidationRequestDto(EventRequestDto dto, SurveyByUserVo vo) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

    public EventValidationRequestDto(EventRequestDto dto, ExperienceByUserVo vo) {
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.eventJoinType = vo.getEventJoinType();
        this.employeeJoinYn = vo.getEmployeeJoinYn();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.userJoinCount = vo.getUserJoinCount();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

}
