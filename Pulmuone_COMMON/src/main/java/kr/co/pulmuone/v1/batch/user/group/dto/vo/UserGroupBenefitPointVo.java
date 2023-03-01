package kr.co.pulmuone.v1.batch.user.group.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupBenefitPointVo {

    @ApiModelProperty(value = "회원 그룹 PK")
    private Long urGroupId;

    @ApiModelProperty(value = "혜택 PK")
    private Long benefitRelationId;

}
