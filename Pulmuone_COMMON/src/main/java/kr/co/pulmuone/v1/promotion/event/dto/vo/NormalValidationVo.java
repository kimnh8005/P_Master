package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NormalValidationVo {

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "시작일시")
    private String startDateTime;

    @ApiModelProperty(value = "종료일시")
    private String endDateTime;

    @ApiModelProperty(value = "참여횟수 설정유형")
    private String eventJoinType;

    @ApiModelProperty(value = "임직원 참여여부")
    private String employeeJoinYn;

    @ApiModelProperty(value = "일반이벤트 참여방법")
    private String normalEventType;

    @ApiModelProperty(value = "댓글분류 사용여부")
    private String commentCodeYn;

    @ApiModelProperty(value = "WEB PC 전시여부")
    private String displayWebPcYn;

    @ApiModelProperty(value = "WEB Mobile 전시여부")
    private String displayWebMobileYn;

    @ApiModelProperty(value = "APP 전시여부")
    private String displayAppYn;

    @ApiModelProperty(value = "임직원 전용 유형")
    private String evEmployeeType;

    @ApiModelProperty(value = "이벤트 참여 제한 여부")
    private String eventNotAvailableYn;

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

    @ApiModelProperty(value = "당첨 확률")
    private Double awardRate;

    @ApiModelProperty(value = "예상 참여자 수")
    private Integer expectJoinUserCount;

    @ApiModelProperty(value = "참여 조건")
    private String joinCondition;

    @ApiModelProperty(value = "주문 구분")
    private String eventStampOrderType;

    @ApiModelProperty(value = "주문 건수")
    private Integer orderCount;

    @ApiModelProperty(value = "주문 금액")
    private Integer orderPrice;

    @ApiModelProperty(value = "주문 주문배송유형")
    private String goodsDeliveryTp;

    @ApiModelProperty(value = "쿠폰 list pk")
    private List<EventCouponVo> coupon;

    @ApiModelProperty(value = "이벤트 유형")
    private String eventType;

    @ApiModelProperty(value = "잔여 혜택 수")
    private int remainBenefitCount;

    @ApiModelProperty(value = "cicd 참여 수")
    private int cicdCount;

    @ApiModelProperty(value = "신규 회원 확인 유무")
    private String checkNewUserYn;

    @ApiModelProperty(value = "신규 회원 유무")
    private String newUserYn;
}
