package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@ApiModel(description = "UserGroupInfoDto")
public class UserGroupInfoDto {
    @ApiModelProperty(value = "회원그룹아이디")
    private long urGroupId;

    @ApiModelProperty(value = "회원그룹명")
    private String urGroupNm;
}
