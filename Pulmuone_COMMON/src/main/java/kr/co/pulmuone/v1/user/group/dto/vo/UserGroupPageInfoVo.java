package kr.co.pulmuone.v1.user.group.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원그룹 Result Vo")
public class UserGroupPageInfoVo {

    // List 상단
    @ApiModelProperty(value = "회원그룹 마스터명")
    private String groupMasterName;

    @ApiModelProperty(value = "산정기간(months)")
    private int calculatePeriod;

    @ApiModelProperty(value = "적용시작일")
    private String startDate;

    @ApiModelProperty(value = "적용종료일")
    private String endDate;

}