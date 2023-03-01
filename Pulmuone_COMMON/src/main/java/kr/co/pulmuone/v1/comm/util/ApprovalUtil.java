package kr.co.pulmuone.v1.comm.util;

import kr.co.pulmuone.v1.comm.enums.UserEnums;

public class ApprovalUtil {

	/**
     * 업무별 승인 상태 코드 값을 코드 값만 반환한다.
     * 사용하지 않음으로 변경됨. 모든 업무 승인상태코드를 동일하게 사용으로 변경됨 (2021.02.04)
     * @param String
     * @return String
     */
	public static String replaceStatusCode(String str) {
    	if(str == null || "".equals(str)) {
    		return "";
    	}
    	str = str.replaceFirst("COUPON_APPR_STAT.", "")
				.replaceFirst("POINT_APPR_STAT.", "");
//				.replaceFirst("ITEM_APPR_STAT.", "")
//				.replaceFirst("COUPON_APPR_STAT.", "")
//				.replaceFirst("COUPON_APPR_STAT.", "")

    	return str;
    }

	/**
     * 담당자의 상태가 승인 가능 상태인지를 확인한다.
     * @param String
     * @return boolean
     */
	public static boolean isAbleEmployeeStatus(String status) {
		if(status == null || "".equals(status)) {
			return false;
		}
		if(UserEnums.EmployeeStatus.NORMAL.getCode().equals(status)
			|| UserEnums.EmployeeStatus.TEMPORARY_STOP.getCode().equals(status)
			) return true;
		else return false;
	}
}
