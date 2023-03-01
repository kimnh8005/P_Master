package kr.co.pulmuone.v1.comm.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
 *  1.0    2020. 10. 07.    천혜현          최초작성
 * =======================================================================
 * </PRE>
 */
public class PayEnums {


	// 카드 할부기간
	@Getter
	@RequiredArgsConstructor
	public enum InstallmentPeriod implements CodeCommEnum {
		ONE_PAYMENT("0", "일시불"),
		MONTH_2("2", "2개월"),
		MONTH_3("3", "3개월"),
		MONTH_4("4", "4개월"),
		MONTH_5("5", "5개월"),
		MONTH_6("6", "6개월"),
		MONTH_7("7", "7개월"),
		MONTH_8("8", "8개월"),
		MONTH_9("9", "9개월"),
		MONTH_10("10", "10개월"),
		MONTH_11("11", "11개월"),
		MONTH_12("12", "12개월");

		private final String code;
		private final String codeName;
	}


	//결제수단 정보(결제수단별 채널리스트 포함)
    @Getter
	@RequiredArgsConstructor
	public enum PsPay implements CodeCommEnum {
    	BANK("PAY_TP.BANK", "실시간계좌이체", 			Arrays.asList("REAL_TIME_ACCOUNT_WIRE_PC","REAL_TIME_ACCOUNT_WIRE_MOBILE","REAL_TIME_ACCOUNT_WIRE_APP")),
    	CARD("PAY_TP.CARD", "신용카드", 				Arrays.asList("CREDIT_CARD_PAYMENT_PC","CREDIT_CARD_PAYMENT_MOBILE","CREDIT_CARD_PAYMENT_APP")),
    	VIRTUAL_BANK("PAY_TP.VIRTUAL_BANK", "가상계좌", Arrays.asList("VIRTUAL_ACCOUNT_WIRE_PC","VIRTUAL_ACCOUNT_WIRE_MOBILE","VIRTUAL_ACCOUNT_WIRE_APP")),
		KAKAOPAY("PAY_TP.KAKAOPAY", "카카오페이", 		Arrays.asList("KAKAOPAY_WIRE_PC","KAKAOPAY_WIRE_MOBILE","KAKAOPAY_WIRE_APP")),
    	PAYCO("PAY_TP.PAYCO", "페이코", 				Arrays.asList("PAYCO_WIRE_PC","PAYCO_WIRE_MOBILE","PAYCO_WIRE_APP")),
    	NAVERPAY("PAY_TP.NAVERPAY", "네이버페이", 		Arrays.asList("NAVERPAY_WIRE_PC","NAVERPAY_WIRE_MOBILE","NAVERPAY_WIRE_APP")),
    	SSPAY("PAY_TP.SSPAY", "삼성페이", 		Arrays.asList("SSPAY_WIRE_PC","SSPAY_WIRE_MOBILE","SSPAY_WIRE_APP"))
        ;

        private final String code;
        private final String codeName;
        private final List<String> psConfig;

        public static PsPay findByCode(String code) {
			return Arrays.stream(PsPay.values())
		            .filter(psPayCode -> psPayCode.getCode().equals(code))
		            .findAny()
		            .orElse(null);
        }

        public static String findPsConfigByDeviceType(String code, String deviceType) {
        	PsPay psPay = findByCode(code);
			if (psPay == null) {
				return null;
			} else {
				List<String> psConfigList = psPay.getPsConfig();
	        	return psConfigList.stream()
	    				.filter(f -> f.contains(deviceType))
	    				.findAny()
	    				.orElse(null);
			}
        }

    }
}