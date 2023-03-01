package kr.co.pulmuone.v1.batch.user.group.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupBenefitVo {

    @ApiModelProperty(value = "회원그룹 혜택 PK")
    private Long urGroupBenefitId;

    @ApiModelProperty(value = "혜택 유형")
    private String urGroupBenefitType;

    @ApiModelProperty(value = "혜택 PK")
    private Long benefitRelationId;

}
