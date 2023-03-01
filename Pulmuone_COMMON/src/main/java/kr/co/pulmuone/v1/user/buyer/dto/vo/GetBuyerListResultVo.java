package kr.co.pulmuone.v1.user.buyer.dto.vo;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetBuyerListResultVo
{

	// 회원 PK
	private String urUserId;

	// 임직원 여부
	private String employeeYn;

	// 회원명
	@UserMaskingUserName
	private String userName;

	// 회원 로그인 ID
	@UserMaskingLoginId
	private String loginId;

	// 휴대폰
	@UserMaskingMobile
	private String mobile;

	// 이메일
	@UserMaskingEmail
	private String mail;

	// 가입일
	private String createDate;

	// 최근 로그인 일자
	private String lastLoginDate;

	// SMS 수신여부
	private String smsYn;

	// 이메일 수신여부
	private String mailYn;

	// 마케팅활용동의여부
	private String marketingYn;

	// PUSH 수신여부
	private String pushYn;

	// 회원등급명
	private String groupName;

	// 회원상태
	private String status;

	// rownum
	private String rnum;

	// 성별
	private String genderView;

	// 블랙리스트
	private String accumulateCount;

	// 임직원코드
	private String urErpEmployeeCd;

	// 생년월일
	private String bday;

	// 생년월일
	private String bdayView;

	// 나이
	private String age;
	
}
