package kr.co.pulmuone.v1.user.certification.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "임직원 회원 인증 결과 Vo")
public class EmployeeCertificationResultVo {
	@ApiModelProperty(value = "회원PK")
	private String urUserId;

	@ApiModelProperty(value = "임직원 사번")
	private String urErpEmployeeCd;

	@ApiModelProperty(value = "이메일")
	private String mail;

	@ApiModelProperty(value = "모바일 전화번호")
	private String mobile;

	@ApiModelProperty(value = "임시발급번호 첫번째")
	private String tempCertiNoFirst;

	@ApiModelProperty(value = "임시발급번호 두번째")
	private String tempCertiNoSecond;

	@ApiModelProperty(value = "임시발급번호 세번째")
	private String tempCertiNoThird;

	@ApiModelProperty(value = "임시발급번호 네번째")
	private String tempCertiNoFourth;

	@ApiModelProperty(value = "임시발급번호 다섯번째")
	private String tempCertiNoFifth;

	@ApiModelProperty(value = "임시발급번호 여섯번째")
	private String tempCertiNoSixth;

	@ApiModelProperty(value = "임시발급번호-SMS용")
	private String tempCertiNo;

}
