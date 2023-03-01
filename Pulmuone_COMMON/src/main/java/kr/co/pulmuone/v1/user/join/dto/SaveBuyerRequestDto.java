package kr.co.pulmuone.v1.user.join.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetClauseArrayRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveBuyerRequestDto")
public class SaveBuyerRequestDto extends BaseRequestDto
{

	@ApiModelProperty(value = "로그인아이디", required = true)
	private String loginId;

	@ApiModelProperty(value = "비밀번호", required = true)
	private String password;

	@ApiModelProperty(value = "이메일", required = true)
	private String mail;

	@ApiModelProperty(value = "우편번호", required = true)
	private String zipCode;

	@ApiModelProperty(value = "기본주소", required = true)
	private String address1;

	@ApiModelProperty(value = "상세주소", required = true)
	private String address2;

	@ApiModelProperty(value = "건물관리번호", required = true)
	private String buildingCode;

	@ApiModelProperty(value = "추천인아이디", required = true)
	private String recommendLoginId;

	@ApiModelProperty(value = "SMS동의여부", required = true)
	private String smsYn;

	@ApiModelProperty(value = "이메일동의", required = true)
	private String mailYn;

	@ApiModelProperty(value = "마케팅 활용 동의 여부", required = true)
	private String marketingYn;

	@ApiModelProperty(value = "약관동의리스트")
	private GetClauseArrayRequestDto[] clause;

	@ApiModelProperty(value = "유저아이디")
	private String urUserId;

	@ApiModelProperty(value = "성별")
	private String gender;

	@ApiModelProperty(value = "생년월일")
	private String birthday;

	@ApiModelProperty(value = "약관코드", required = true)
	private String psClauseGrpCd;

	@ApiModelProperty(value = "약관 적용 시작일", required = true)
	private String executeDate;

	@ApiModelProperty(value = "회원이름", required = true)
	private String userName;

	@ApiModelProperty(value = "사용자타입", required = true)
	private String userType;

	@ApiModelProperty(value = "핸드폰번호", required = true)
	private String mobile;

	@ApiModelProperty(value = "회원상태", required = true)
	private String userStatus;

	@ApiModelProperty(value = "상태")
	private String status;

	@ApiModelProperty(value = "사용자그룹아이디")
	private Long urGroupId;

	@ApiModelProperty(value = "CICD")
	private String ciCd;

	@ApiModelProperty(value = "추천인아이디")
	private String recommUserId;

	@ApiModelProperty(value = "이벤트초청여부")
	private String eventJoinYn;

	@ApiModelProperty(value = "사번")
	private String urErpEmployeeCd;

	@ApiModelProperty(value = "외국인여부")
	private String foreignerYn;

	@ApiModelProperty(value = "SMS동의일자")
	private String smsYnDate;

	@ApiModelProperty(value = "이메일동의일자")
	private String mailYnDate;

	@ApiModelProperty(value = "승급날짜")
	private String levelupDate;

	// UR_CERTIFICATION

	@ApiModelProperty(value = "승인PK")
	private String urCertificationId;

	@ApiModelProperty(value = "비밀번호변경코드")
	private String pwdChangeCd;

	@ApiModelProperty(value = "자동로드인키")
	private String autoLoginKey;

	@ApiModelProperty(value = "실패건수")
	private String failCnt;
}
