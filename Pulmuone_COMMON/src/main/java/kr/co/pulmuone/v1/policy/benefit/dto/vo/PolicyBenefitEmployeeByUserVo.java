package kr.co.pulmuone.v1.policy.benefit.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원 혜택 정보 조회 by user ")
public class PolicyBenefitEmployeeByUserVo {

    @ApiModelProperty(value = "임직원 할인 그룹 코드 PK")
    private Long psEmplDiscMasterId;

    @ApiModelProperty(value = "임직원 할인 그룹 명")
    private String masterName;

    @ApiModelProperty(value = "PK")
    private Long psEmplDiscGrpId;

    @ApiModelProperty(value = "지원 금액")
    private Integer limitAmount;

    @ApiModelProperty(value = "사용 금액")
    private Integer useAmount;

    @ApiModelProperty(value = "잔여 금액")
    private Integer remainAmount;

    @ApiModelProperty(value = "기간")
    private String employeeDiscountLimitCycleTypeName;

    @ApiModelProperty(value = "대상일자 - 시작")
    private String startDate;

    @ApiModelProperty(value = "대상일자 - 종료")
    private String endDate;

    @ApiModelProperty(value = "임직원 할인 브랜드 정보")
    private List<PolicyBenefitEmployeeBrandGroupByUserVo> list;

}
