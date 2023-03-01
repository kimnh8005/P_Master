package kr.co.pulmuone.v1.policy.benefit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PolicyBenefitEmployeeBrandGroupByUserVo {

    @ApiModelProperty(value = "임직원 할인율 브랜드 그룹 PK")
    private Long psEmplDiscBrandGrpId;

    @ApiModelProperty(value = "그룹 명")
    private String groupName;

    @ApiModelProperty(value = "할인율")
    private int discountRatio;

    @ApiModelProperty(value = "브랜드 List")
    private List<PolicyBenefitEmployeeBrandByUserVo> brand;

}
