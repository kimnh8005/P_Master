package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PutBuyerFromMypageRequestDto")
public class CommonPutBuyerFromMypageRequestDto {

    @ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "이름")
    private String userName;

    @ApiModelProperty(value = "휴대폰번호")
    private String mobile;

    @ApiModelProperty(value = "이메일")
    private String mail;

    @ApiModelProperty(value = "최근본상품 로그 누적여부")
    private String recentlyViewYn;

    @ApiModelProperty(value = "이메일 수신 동의 여부")
    private String mailYn;

    @ApiModelProperty(value = "SMS 수신 동의 여부")
    private String smsYn;

	@ApiModelProperty(value = "마케팅 활용 동의 여부")
	private String marketingYn;
}
