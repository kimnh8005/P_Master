package kr.co.pulmuone.v1.comm.util.asis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "기존(AS-IS)회원 아이디/비밀번호 체크 API 응답 DTO")
public class UserInfoCheckResponseDto {

	@ApiModelProperty(value = "체크 결과(0-없음, 1-존재, 9-오류)")
    private String resultCode;

	@ApiModelProperty(value = "실패 사유")
    private String resultMessage;

	@ApiModelProperty(value = "고객번호")
    private String customerNumber;

}
