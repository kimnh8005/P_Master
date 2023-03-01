package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AsisUserPointRequestDto {

    @ApiModelProperty(value = "ASIS 로그인 아이디")
    private String loginId;

    @ApiModelProperty(value = "ASIS 로그인 패스워드")
    private String password;

    @ApiModelProperty(value = "ASIS 풀무원샵 적립금")
    private int pulmuonePoint = 0;

    @ApiModelProperty(value = "ASIS 올가 적립금")
    private int orgaPoint = 0;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "(구) 풀무원샵 고객번호", hidden = true)
    private String customerNumber;

    @ApiModelProperty(value = "(구) 풀무원샵 적립금", hidden = true)
    private int asIsPulmuonePoint;

    @ApiModelProperty(value = "(구) 올가 적립금", hidden = true)
    private int asIsOrgaPoint;

}
