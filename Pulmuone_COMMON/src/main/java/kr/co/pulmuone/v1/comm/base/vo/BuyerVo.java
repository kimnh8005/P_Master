package kr.co.pulmuone.v1.comm.base.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
/**
 * 로그인 Session 객체
 *
 * @author 김경민
 */
public class BuyerVo implements Serializable {
	// 회원.회원 PK
	private String urUserId;

	// 회원.회원명
	private String userName;

	// 회원.로그인 Id
	private String loginId;

	// 회원.모바일
	private String userMobile;

	// 회원.이메일
	private String userEmail;

	// 회원.로그인 connectLogId
	private String urConnectLogId;

	// 회원.임직원 사번 PK
	private String urErpEmployeeCode;

	// 회원.임직원인증 임시 임직원 사번 PK
	private String tempUrErpEmployeeCode;

	// 회원.임직원인증 임시발급번호
	private String tempCertiNo;

	// 회원.임직원인증 실패건수
	private String failCnt;

	// 회원.기존회원 인증 실패건수
	private int asisUserFailCnt;

	// 회원.기존회원 고객번호
	private String asisCustomerNumber;

	// 회원.기존회원 로그인id
	private String asisLoginId;

	// 비회원.CI
	private String nonMemberCiCd;

	// 비회원.회원명
	private String nonMemberUserName;

	// 비회원.모바일
	private String nonMemberMobile;

	// 본인인증.이름
	private String personalCertificationUserName;

	// 본인인증.모바일
	private String personalCertificationMobile;

	// 본인인증.성별
	private String personalCertificationGender;

	// 본인인증.CI
	private String personalCertificationCiCd;

	// 본인인증.생년월일 (YYYYMMDD)
	private String personalCertificationBirthday;

	// 본인인증.기존 탈퇴 회원 여부(Y)
	private String personalCertificationBeforeUserDropYn;

	// sns.인증 state
	private String snsAuthorizationState;

	// sns.제공사
	private String snsProvider;

	// sns.소셜아이디
	private String snsSocialId;

	// 장바구니. 배송지 수령인명
	private String receiverName;

	// 장바구니. 배송지 우편번호
	private String receiverZipCode;

	// 장바구니. 배송지 주소
	private String receiverAddress1;

	// 장바구니. 배송지 상세주소
	private String receiverAddress2;

	// 장바구니. 배송지 건물 관리 번호
	private String buildingCode;

	// 장바구니. 배송지 모바일
	private String receiverMobile;

	// 장바구니. 출입정보타입
	private String accessInformationType;

	// 장바구니. 출입정보 비밀번호(암호화)
	private String accessInformationPassword;

	// 장바구니. 배송 요청 사항
	private String shippingComment;

	// 장바구니. 기본 배송지 여부
	private String selectBasicYn;

	// 장바구니. 배송지 PK
	private Long shippingAddressId;

	// 이용권 등록 recaptcha 실패건수
	private int promotionRecaptchaFailCount;

	// 회원 등급 PK
	private Long urGroupId;

}
