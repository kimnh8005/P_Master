package kr.co.pulmuone.v1.user.certification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KmcCertRequestDataDto {
	// 회원사 아이디 (필수)
	private String cpId;

	// URL코드 (필수)
	private String urlCode;

	// 요청번호 (필수)
	private String certNum;

	// 요청일시 (필수)
	private String date;

	// 본인인증방법 (필수) (A:인증방법선택 화면, M:휴대폰인증 화면, C:신용카드인증 화면, P:공인인증서 화면)
	private String certMet = "M";

	// 이용자성명
	private String name = "";

	// 휴대폰번호 (01012345678)
	private String phoneNo = "";

	// 이통사 (SKT:SKT,KTF: KT, LGT:LG U+, SKM:SKTmvno, KTM:KTmvno, LGM:LGU+mvno)
	private String phoneCorp = "";

	// 생년월일 (YYYYMMDD)
	private String birthDay = "";

	// 이용자성별 (0:남, 1:여)
	private String gender = "";

	// 내외국인 (0:내국인, 1:외국인)
	private String nation = "";

	// 추가DATA정보
	private String plusInfo = "";

	// 확장변수
	private String extedVar = "0000000000000000";

}
