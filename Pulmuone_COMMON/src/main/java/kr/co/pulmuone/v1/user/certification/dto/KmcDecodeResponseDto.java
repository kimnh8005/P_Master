package kr.co.pulmuone.v1.user.certification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KmcDecodeResponseDto {
	// 요청번호
	private String certNum;
	// 요청일시
	private String date;
	// 연계정보(CI)
	private String CI;
	// 중복가입확인정보(DI)
	private String DI;
	// 휴대폰번호 (01012345678)
	private String phoneNo;
	// 이통사 (SKT:SKT,KTF: KT, LGT:LG U+, SKM:SKTmvno, KTM:KTmvno, LGM:LGU+mvno)
	private String phoneCorp;
	// 생년월일 (YYYYMMDD)
	private String birthDay;
	// 이용자성별 (0:남, 1:여)
	private String gender;
	// 내외국인 (0:내국인, 1:외국인)
	private String nation;
	// 성명
	private String name;
	// 미성년자 성명
	private String M_name;
	// 미성년자 생년월일
	private String M_birthDay;
	// 미성년자 성별
	private String M_Gender;
	// 미성년자 내외국인
	private String M_nation;
	// 본인인증방법 (필수) (A:인증방법선택 화면, M:휴대폰인증 화면, C:신용카드인증 화면, P:공인인증서 화면)
	private String certMet;
	// 추가DATA정보
	private String plusInfo;
	// 결과값 (Y : 성공, N : 실패, F : 오류, X : 데이터 유효성 검증 실패)
	private String result;
	// 결과 메세지
	private String resultMessage;
}
