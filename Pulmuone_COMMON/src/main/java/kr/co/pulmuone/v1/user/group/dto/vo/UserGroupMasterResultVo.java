package kr.co.pulmuone.v1.user.group.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원그룹 Master Result Vo")
public class UserGroupMasterResultVo {

    @ApiModelProperty(value = "회원그룹 마스터 PK")
    private Long urGroupMasterId;

    @ApiModelProperty(value = "회원그룹 마스터명")
    private String groupMasterName;

    @ApiModelProperty(value = "산정기간(months)")
    private int calculatePeriod;

    @ApiModelProperty(value = "적용시작일")
    private String startDate;

    @ApiModelProperty(value = "적용종료일")
    private String endDate;

    @ApiModelProperty(value = "상태")
    private String state;

    @ApiModelProperty(value = "회원 등급 수")
    private int groupLevelCount;

    @ApiModelProperty(value = "적용시작 년도")
    private String startDateYear;

    @ApiModelProperty(value = "적용시작 월")
    private String startDateMonth;
}
