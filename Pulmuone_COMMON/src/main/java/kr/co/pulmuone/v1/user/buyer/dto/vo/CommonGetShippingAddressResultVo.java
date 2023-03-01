package kr.co.pulmuone.v1.user.buyer.dto.vo;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddress;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddressDetail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingTelePhone;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonGetShippingAddressResultVo
{

	// 배송지정보 PK
	private String urShippingAddrId;

	// 기본배송지 여부
	private String defaultYn;

	// 배송지명
	private String shippingName;

	// 받는사람
	@UserMaskingUserName
	private String receiverName;

	// 휴대폰
	@UserMaskingMobile
	private String receiverMobile;

	// 전화번호
	@UserMaskingTelePhone
	private String receiverTelephone;

	// 우편번호
	private String receiverZipCode;

	// 주소
	@UserMaskingAddress
	private String receiverAddress1;

	// 상세주소
	@UserMaskingAddressDetail
	private String receiverAddress2;

	// 건물관리번호
	private String buildingCode;

	// 배송요청사항
	private String shippingComment;

	// 출입정보 공통코드
	private String accessInformationType;

	// 출입정보 공통코드명
	private String accessInformationName;

	// 공동현관 비밀번호
	private String accessInformationPassword;

}
