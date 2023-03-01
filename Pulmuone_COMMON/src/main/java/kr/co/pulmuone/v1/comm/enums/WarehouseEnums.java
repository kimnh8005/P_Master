package kr.co.pulmuone.v1.comm.enums;

import java.util.Arrays;

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
 *  1.0    2021. 5. 17.      안치열          최초작성
 * =======================================================================
 * </PRE>
 */
public class WarehouseEnums {

	// 업체명 관리
	@Getter
	@RequiredArgsConstructor
	public enum CompanyNameCheck implements MessageCommEnum
	{
		DUPLICATE_COMPANY_NAME("DUPLICATE_COMPANY_NAME", "중복된 업체명이 존재합니다.");


		private final String code;
		private final String message;
	}

	// 정산 여부
    @Getter
	@RequiredArgsConstructor
	public enum SetlYn implements CodeCommEnum {
    	NOT("X", "정산대상아님"),
    	NO("N", "정산안함"),
    	COMPLETE("Y", "정산완료")
        ;

        private final String code;
        private final String codeName;
    }

    // 배송불가 코드
    @Getter
	@RequiredArgsConstructor
	public enum UndeliverableType implements CodeCommEnum {
    	CJ("UNDELIVERABLE_TP.CJ", "CJ대한통운"),
		LOTTE("UNDELIVERABLE_TP.LOTTE", "롯데택배"),
		ISLAND("UNDELIVERABLE_TP.ISLAND", "도서산간"),
		JEJU("UNDELIVERABLE_TP.JEJU", "제주"),
		DAWN_CJ("UNDELIVERABLE_TP.DAWN_CJ", "CJ택배새벽")
        ;

        private final String code;
        private final String codeName;

		public static UndeliverableType findByCode(String code) {
			return Arrays.stream(UndeliverableType.values())
					.filter(undeliverableAreaType -> undeliverableAreaType.getCode().equals(code)).findAny()
					.orElse(null);

		}
    }
}
