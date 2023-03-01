package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GroupMasterCommonRequestDto {

    @ApiModelProperty(value = "회원그룹 마스터명")
    private Long urGroupMasterId;

    @ApiModelProperty(value = "산정기간(months)")
    private Boolean searchActiveMaster;

}