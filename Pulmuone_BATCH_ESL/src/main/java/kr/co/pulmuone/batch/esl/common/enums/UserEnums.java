package kr.co.pulmuone.batch.esl.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Java 에서 코드성 값을 사용해야 할때 여기에 추가해서 사용
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 7. 15.                jg          최초작성
 * =======================================================================
 * </PRE>
 */
public class UserEnums
{
	// 회원기본정보 - 구매자구분
	@Getter
	@RequiredArgsConstructor
	public enum BuyerType {
		USER("BUYER_TYPE.USER", "회원"),
		EMPLOYEE("BUYER_TYPE.EMPLOYEE", "임직원"),
		EMPLOYEE_BASIC("BUYER_TYPE.EMPLOYEE_BASIC", "임직원(회원가)"),
		GUEST("BUYER_TYPE.GUEST", "비회원");

		private final String code;
		private final String codeName;
	}
}