package kr.co.pulmuone.v1.user.dormancy.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정상회원 휴면전환 결과 Vo")
public class UserDormancyResultVo {

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "로그인 아이디")
	private String loginId;

	@ApiModelProperty(value="전환 일자")
	private String createDate;

	@ApiModelProperty(value="이메일")
	private String mail;

	@ApiModelProperty(value="모바일 전화번호")
	private String mobile;

}
