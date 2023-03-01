package kr.co.pulmuone.batch.erp.common.enums;

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
	// 회원기본정보 - 회원구분
	@Getter
	@RequiredArgsConstructor
	public enum EmployeeStatus {
		NORMAL("EMPLOYEE_STATUS.NORMAL", "정상"),
		STOP("EMPLOYEE_STATUS.STOP", "정지"),
		RESIGN("EMPLOYEE_STATUS.RESIGN", "퇴사"),
		TEMPORARY_STOP("EMPLOYEE_STATUS.TEMPORARY_STOP", "일시정지"),
		ADMINISTRATIVE_LEAVE("EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE", "휴직");

		private final String code;
		private final String codeName;
	}

	// ERP 회원상태(ERP 에서 상태값이 변경될 가망성 존재)
	@Getter
	@RequiredArgsConstructor
	public enum ErpEmployeeStatus {
		NORMAL1("EMPLOYEE_STATUS.NORMAL", "Active Assignment"),
		NORMAL2("EMPLOYEE_STATUS.NORMAL", "현재 발령"),
		RESIGN1("EMPLOYEE_STATUS.RESIGN", "발령 보류"),
		RESIGN2("EMPLOYEE_STATUS.RESIGN", "발령 종료"),
		RESIGN3("EMPLOYEE_STATUS.RESIGN", "종료"),
		RESIGN4("EMPLOYEE_STATUS.RESIGN", "Terminate Assignment"),
		ADMINISTRATIVE_LEAVE1("EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE", "무급휴직"),
		ADMINISTRATIVE_LEAVE2("EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE", "유급휴직");

		private final String code;
		private final String codeName;
	}
}