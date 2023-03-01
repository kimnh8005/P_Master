package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddItemBenefitDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "회원그룹 혜택 PK")
    private long urGroupBenefitId;

    @ApiModelProperty(value = "그룹 PK")
    private long urGroupId;

    @ApiModelProperty(value = "혜택 유형 PK")
    private String benefitRelationId;

    @ApiModelProperty(value = "회원그룹 혜택 유형 코드")
    private String userGroupBenefitType;

    @ApiModelProperty(value = "쿠폰 명")
    private String displayCouponName;

    @ApiModelProperty(value = "사용 기간")
    private String validityDate;

    @ApiModelProperty(value = "적립금 명")
    private String pointName;

    @ApiModelProperty(value = "적립금 상세 유형")
    private String pointDetailTypeName;

    @ApiModelProperty(value = "적립금 값")
    private String issueValue;
    
}
