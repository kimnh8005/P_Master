package kr.co.pulmuone.v1.batch.user.group.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserGroupVo {

    @ApiModelProperty(value = "회원그룹 PK")
    private Long urGroupId;

    @ApiModelProperty(value = "기본등급여부")
    private String defaultYn;

    @ApiModelProperty(value = "매출액조건 FROM")
    private int purchaseAmountFrom;

    @ApiModelProperty(value = "매출액조건 TO")
    private int purchaseAmountTo;

    @ApiModelProperty(value = "구매건수 조건 FROM")
    private int purchaseCountFrom;

    @ApiModelProperty(value = "구매건수 조건 To")
    private int purchaseCountTo;

    @ApiModelProperty(value = "등급 혜택")
    private List<UserGroupBenefitVo> benefit;

}
