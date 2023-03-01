package kr.co.pulmuone.v1.user.buyer.dto.vo;

import kr.co.pulmuone.v1.comm.aop.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class GetBuyerResultVo
{

	// 회원 ID
	private String urUserId;

	// 회원 이름
	@UserMaskingUserName
	private String userName;

	// 회원 로그인 ID
	@UserMaskingLoginId
	private String loginId;

	// 카카오 계정 연동여부
	private String kakaoLoginYn;

	// 네이버 계정 연동여부
	private String naverLoginYn;

	// 구글 계정 연동여부
	private String googleLoginYn;

	// 페이스북 계정 연동여부
	private String facebookLoginYn;

	// 애플 계정 연동여부
	private String appleLoginYn;

	// 생일
	@UserMaskingBirth
	private String bday;

	// 성별
	private String gender;

	// 성별 표시명
	private String genderView;

	// 외국인여부
	private String foreignerYn;

	// 핸드폰
	@UserMaskingMobile
	private String mobile;

	// 이메일
	@UserMaskingEmail
	private String mail;

	// 임직원여부
	private String employeeYn;

	// 회원상태
	private String status;

	// 회원그룹명
	private String groupName;

	// SMS 수신동의 여부
	private String smsYn;

	// 이메일 수신동의 여부
	private String mailYn;

	// 마케팅활용동의여부
	private String marketingYn;

	// SMS 수신동의 일자
	private String smsYnDate;

	// 이메일 수신동의 일자
	private String mailYnDate;

	// 마케팅활용동의여부 설정일
	private String marketingYnDate;

	// PUSH 수신동의 여부
	private String pushYn;

	// PUSH 수신동의 일자
	private String pushYnDate;

	// 계정연동 디바이스 개수
	private String deviceCount;

	// 마케팅 수신동의 실행일자
	private String aexecuteDate;

	// 마케팅 수신동의 일자
	private String aclauseCreateDate;

	// 이용약관 수신동의 실행일자
	private String bexecuteDate;

	// 이용약관 수신동의 일자
	private String bclauseCreateDate;

	// 가입일
	private String createDate;

	// 최근방문일자
	private String lastLoginDate;

	// 내가 추천한 추천인 아이디
	@UserMaskingLoginId
	private String recommendUserId;

	// 내가 추천한 추천인 이름
	@UserMaskingUserName
	private String recommendUserName;

	// 나를 추천한 추천인 수
	private String recommendCount;

	// 블랙리스트 등록 여부
	private String blackList;

	// 악성클레임 수
	private String maliciousClaimCount;

	// 악성클레임 일자
	private String maliciousClaimDate;

	// 이벤트제한여부
	private String eventJoinYn;

	// 회원상태변경로그 PK
	private String urBuyerStatusLogId;

	// 기본배송지
	// @UserMaskingAddress
	private String address;

	// 임직원정보
	private String employeeInfo;

	// 법인정보명
	private String erpRegalName;

	// 직책
	private String erpPositionName;

	// 임직원코드
	private String urErpEmployeeCd;

	// 개인정보 열람권한 유무(Y:보유, N:미보유)
	private String personalInformationAccessYn;

	public String getRecommendUserId() {
		if(!Objects.isNull(recommendUserName)){
			return recommendUserName + '(' + recommendUserId + ')';
		}
		return recommendUserId;
	}
}
