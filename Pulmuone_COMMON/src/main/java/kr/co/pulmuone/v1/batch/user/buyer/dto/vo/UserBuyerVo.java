package kr.co.pulmuone.v1.batch.user.buyer.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupBenefitVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserBuyerVo {

    @ApiModelProperty(value = "구매 회원 PK")
    private Long urBuyerId;

    @ApiModelProperty(value = "회원 PK")
    private Long urUserId;

    @ApiModelProperty(value = "회원등급 PK - 변경전")
    private Long beforeUrGroupId;

    @ApiModelProperty(value = "회원등급 PK - 수정용")
    private Long urGroupId;

    @ApiModelProperty(value = "배치 대상 년월 - 배치 히스토리용")
    private String batchYearMonth;

    @ApiModelProperty(value = "배치 대상 회원 유형 - 배치 히스토리용")
    private String batchUserType;

    @ApiModelProperty(value = "등급 혜택")
    private List<UserGroupBenefitVo> benefit;

}
