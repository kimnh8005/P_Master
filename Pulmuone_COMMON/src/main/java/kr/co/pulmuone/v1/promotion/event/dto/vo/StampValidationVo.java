package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StampValidationVo {

    @ApiModelProperty(value = "스탬프 이벤트 PK")
    private Long evEventStampId;

    @ApiModelProperty(value = "이벤트유형")
    private String eventType;
    
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

    @ApiModelProperty(value = "스탬프 지급조건 금액")
    private int orderPrice;

    @ApiModelProperty(value = "시작일시")
    private String startDateTime;

    @ApiModelProperty(value = "종료일시")
    private String endDateTime;
    
    @ApiModelProperty(value = "유저 참여 정보")
    private int userJoinCount;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

    @ApiModelProperty(value = "이벤트 참여 제한 여부")
    private String eventNotAvailableYn;

    @ApiModelProperty(value = "스탬프개수2")
    private int stampCount2;

    @ApiModelProperty(value = "CICD 참여 정보")
    private int cicdCount;

    @ApiModelProperty(value = "스탬프 이벤트 상세")
    private List<StampDetailValidation> stamp;

    @ApiModelProperty(value = "스탬프 이벤트 참여정보")
    private List<StampJoinByUserVo> join;
}
