package kr.co.pulmuone.v1.policy.benefit.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원 혜택 브랜드 정보 조회 by user ")
public class PolicyBenefitEmployeeBrandByUserVo {

    @ApiModelProperty(value = "표준 브랜드 PK")
    private Long urBrandId;

    @ApiModelProperty(value = "브랜드 명")
    private String brandName;

}
