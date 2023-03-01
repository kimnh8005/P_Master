package kr.co.pulmuone.v1.comm.enums;

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
 *  1.0		20201204		강상국              최초작성
 * =======================================================================
 * </PRE>
 */
public class ErpEnums
{

	// 택배 가맹점 코드
	@Getter
	@RequiredArgsConstructor
	public enum ParcelServiceCustId implements CodeCommEnum
	{
		LOTTE_CUST_CD("121974", "롯데택배 가맹점코드")
		, CJ_CUST_CD("30341064", "CJ택배 백암물류 가맹점코드")
		, CJ_DAWN_CUST_CD("30321611", "CJ택배 용인물류 새벽 가맹점코드")
		, CJ_NORMAL_CUST_CD("30444268", "CJ택배 용인물류 일반 가맹점코드")
		;

		private final String code;
		private final String codeName;
	}

}