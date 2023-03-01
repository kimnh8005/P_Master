package kr.co.pulmuone.v1.batch.user.group.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserGroupMasterVo {

    @ApiModelProperty(value = "회원그룹 마스터 PK")
    private Long urGroupMasterId;

    @ApiModelProperty(value = "회원그룹 마스터 명")
    private String groupMasterName;

    @ApiModelProperty(value = "등급산정기간")
    private String calculatePeriod;

    @ApiModelProperty(value = "적용시작일")
    private String startDate;

}
