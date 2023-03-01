package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "SNS 로그인 계정 연동 끊기 RequestDto")
public class UnlinkAccountRequestDto {

	@ApiModelProperty(value = "회원번호")
	private String user_id;

	@ApiModelProperty(value = "연결 끊기 요청 경로(카카오)")
	private String referrer_type;

	@ApiModelProperty(value = "SNS 제공사")
	private String provider;

	@ApiModelProperty(value = "연결 끊기 응답값(페이스북)")
	private String signed_request;

}
