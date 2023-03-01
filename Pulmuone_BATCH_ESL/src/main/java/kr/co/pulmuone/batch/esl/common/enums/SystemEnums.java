package kr.co.pulmuone.batch.esl.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 시스템 Enums
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 9. 14.                손진구          최초작성
 * =======================================================================
 * </PRE>
 */
public class SystemEnums
{

	// 회원기본정보 - 구매자구분
	@Getter
	@RequiredArgsConstructor
	public enum AgentType {
		ADMIN("AGENT_TYPE.A", "관리자 주문"),
		OUTMALL("AGENT_TYPE.O", "외부몰 주문"),
		APP("AGENT_TYPE.APP", "APP"),
		PC("AGENT_TYPE.W", "PC"),
		MOBILE("AGENT_TYPE.M", "MOBILE");

		private final String code;
		private final String codeName;
	}
}