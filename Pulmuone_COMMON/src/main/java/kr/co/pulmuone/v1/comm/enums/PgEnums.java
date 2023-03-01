package kr.co.pulmuone.v1.comm.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import kr.co.pulmuone.v1.comm.enums.SystemEnums.IllegalDetailType;
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
 *  1.0    2020. 9. 23.                홍진영          최초작성
 * =======================================================================
 * </PRE>
 */
public class PgEnums {

	// 결제 모듈 서비스 종류
	@Getter
	@RequiredArgsConstructor
	public enum PgServiceType implements CodeCommEnum {
		KCP("PG_SERVICE.KCP", "kcp", "KcpPgService"),
		INICIS("PG_SERVICE.INICIS", "이니시스", "InicisPgService");
		private final String code;
		private final String codeName;
		private final String serviceName;
		public static PgServiceType getProbabilityChoiceService(int kcpRate, int inicisRate) {
			List<Integer> inputs = new ArrayList<Integer>(Arrays.asList(kcpRate, inicisRate));
			int max = 100;
			inputs.add(0, 0);
			int rand = new Random().nextInt(100) + 1;
			int result = 0;
			int start = 0;
			int end = 0;
			for (int i = 0; i < inputs.size() - 1; i++) {
				start += inputs.get(i);
				end += inputs.get(i + 1);
				if (rand >= start && rand <= end) {
					result = i;
					break;
				}
			}
			return PgServiceType.values()[result];
		}
		public static PgServiceType findByCode(String code) {
			return Arrays.stream(PgServiceType.values())
		            .filter(pgServiceType -> pgServiceType.getCode().equals(code))
		            .findAny()
		            .orElse(null);
        }
	}

	// PG 실행 스크립트 타입
	@Getter
	@RequiredArgsConstructor
	public enum ExeScriptType implements CodeCommEnum {
		PC_KCP_BASIC("PC_KCP_BASIC", "pc kcp 카드,실시간이체,PG 포함된 간편결제"),
		MOBILE_KCP_BASIC("MOBILE_KCP_BASIC", "mobile kcp 카드,실시간이체,PG 포함된 간편결제"),
		PC_KCP_BATCH_KEY("PC_KCP_BATCH_KEY", "PC 배치키 발급"),
		MOBILE_KCP_BATCH_KEY("MOBILE_KCP_BATCH_KEY", "mobile 배치키 발급"),
		PC_INICIS_BASIC("PC_INICIS_BASIC", "pc inicis 카드,실시간이체,PG 포함된 간편결제"),
		MOBILE_INICIS_BASIC("MOBILE_INICIS_BASIC", "mobile inicis 카드,실시간이체,PG 포함된 간편결제");

		private final String code;
		private final String codeName;
	}

	// PG 승인 결과 코드
	@Getter
	@RequiredArgsConstructor
	public enum PgErrorType implements CodeCommEnum {
		SUCCESS("SUCCESS", "검증 성공"),
		FAIL_NULL_ORDER_DATA("FAIL_NULL_ORDER_DATA", "주문 데이터 조회 실패"),
		FAIL_APPROVAL_VALIDATION_COUPON("FAIL_APPROVAL_VALIDATION_COUPON", "PG 승인전 검증 - 쿠폰 실패"),
		FAIL_APPROVAL_VALIDATION_POINT("FAIL_APPROVAL_VALIDATION_POINT", "PG 승인전 검증 - 적립금 실패"),
		FAIL_APPROVAL_VALIDATION_STOCK("FAIL_APPROVAL_VALIDATION_STOCK", "PG 승인전 검증 - 재고 없음"),
		FAIL_NOT_PAYMENT_PRICE("FAIL_NOT_PAYMENT_PRICE", "금액 검증 실패"),
		FAIL_UPDATE_STOCK("FAIL_UPDATE_STOCK", "재고처리 차감 실패"),
		FAIL_UPDATE_ERROR("FAIL_UPDATE_ERROR", "주문 데이터 업데이트 에러"),
		FAIL_STORE_SCHEDULE("FAIL_STORE_SCHEDULE", "매장회차 주문 불가");

		private final String code;
		private final String codeName;
	}

	// PG 승인 결과 코드
	@Getter
	@RequiredArgsConstructor
	public enum PgAccountType implements CodeCommEnum {
		KCP_BASIC("PG_ACCOUNT_TYPE.KCP_BASIC", "KCP 일반결제 + 가상계좌채번", "PG_SERVICE.KCP"),
		KCP_SIMPLE("PG_ACCOUNT_TYPE.KCP_SIMPLE", "KCP 간편결제", "PG_SERVICE.KCP"),
		KCP_REGULAR("PG_ACCOUNT_TYPE.KCP_REGULAR", "KCP 정기결제", "PG_SERVICE.KCP"),
		INICIS_BASIC("PG_ACCOUNT_TYPE.INICIS_BASIC", "INICIS 일반결제 + 가상계좌채번", "PG_SERVICE.INICIS"),
		INICIS_ADMIN("PG_ACCOUNT_TYPE.INICIS_ADMIN", "INICIS 관리자 주문(신용카드 비인증)", "PG_SERVICE.INICIS");
		private final String code;
		private final String codeName;
		private final String pgServiceType;
		public static PgAccountType findByCode(String code) {
			return Arrays.stream(PgAccountType.values())
		            .filter(pgAccountType -> pgAccountType.getCode().equals(code))
		            .findAny()
		            .orElse(null);
        }
	}

	// PG 은행 코드
	@Getter
	@RequiredArgsConstructor
	public enum InicisBankCode implements CodeCommEnum {
		INICIS("", ""),
		INICIS_02("02", "한국산업은행"),
		INICIS_03("03", "기업은행"),
		INICIS_04("04", "국민은행"),
		INICIS_05("05", "하나은행"),
		INICIS_06("06", "국민은행"),
		INICIS_07("07", "수협중앙회"),
		INICIS_11("11", "농협중앙회"),
		INICIS_12("12", "단위농협"),
		INICIS_16("16", "축협중앙회"),
		INICIS_20("20", "우리은행"),
		INICIS_21("21", "구)조흥은행"),
		INICIS_22("22", "상업은행"),
		INICIS_23("23", "SC제일은행"),
		INICIS_24("24", "한일은행"),
		INICIS_25("25", "서울은행"),
		INICIS_26("26", "구)신한은행"),
		INICIS_27("27", "한국씨티은행"),
		INICIS_31("31", "대구은행"),
		INICIS_32("32", "부산은행"),
		INICIS_34("34", "광주은행"),
		INICIS_35("35", "제주은행"),
		INICIS_37("37", "전북은행"),
		INICIS_38("38", "강원은행"),
		INICIS_39("39", "경남은행"),
		INICIS_41("41", "비씨카드"),
		INICIS_48("48", "신용협동조합중앙회"),
		INICIS_50("50", "상호저축은행"),
		INICIS_53("53", "한국씨티은행"),
		INICIS_54("54", "홍콩상하이은행"),
		INICIS_55("55", "도이치은행"),
		INICIS_56("56", "ABN암로"),
		INICIS_57("57", "JP모건"),
		INICIS_59("59", "미쓰비시도쿄은행"),
		INICIS_60("60", "BOA(Bank of America)"),
		INICIS_64("64", "산림조합"),
		INICIS_70("70", "신안상호저축은행"),
		INICIS_71("71", "우체국"),
		INICIS_81("81", "하나은행"),
		INICIS_83("83", "평화은행"),
		INICIS_87("87", "신세계"),
		INICIS_88("88", "신한(통합)은행"),
		INICIS_89("89", "케이뱅크"),
		INICIS_90("90", "카카오뱅크"),
		INICIS_91("91", "네이버포인트"),
		INICIS_93("93", "토스머니"),
		INICIS_94("94", "SSG머니"),
		INICIS_96("96", "엘포인트"),
		INICIS_97("97", "카카오머니"),
		INICIS_98("98", "페이코")
		;
		private final String code;
		private final String codeName;

		public static InicisBankCode findByCode(String code) {
			return Arrays.stream(InicisBankCode.values())
					.filter(inicisBankCode -> inicisBankCode.getCode().equals(code))
					.findAny()
					.orElse(InicisBankCode.INICIS);
		}
	}

	// PG 은행 코드
	@Getter
	@RequiredArgsConstructor
	public enum InicisCardCode implements CodeCommEnum {
		INICIS("", ""),
		INICIS_01("01", "외환카드"),
		INICIS_03("03", "롯데카드"),
		INICIS_04("04", "현대카드"),
		INICIS_06("06", "국민카드"),
		INICIS_11("11", "비씨카드"),
		INICIS_12("12", "삼성카드"),
		INICIS_14("14", "신한카드"),
		INICIS_15("15", "한미카드"),
		INICIS_16("16", "NH농협카드"),
		INICIS_17("17", "하나SK카드"),
		INICIS_21("21", "해외비자"),
		INICIS_22("22", "해외마스터"),
		INICIS_23("23", "해외JCB"),
		INICIS_24("24", "해외아멕스"),
		INICIS_25("25", "해외다이너스"),
		INICIS_93("93", "토스머니"),
		INICIS_94("94", "SSG머니"),
		INICIS_96("96", "엘포인트"),
		INICIS_97("97", "카카오머니"),
		INICIS_98("98", "페이코"),
		;
		private final String code;
		private final String codeName;

		public static InicisCardCode findByCode(String code) {
			return Arrays.stream(InicisCardCode.values())
					.filter(inicisCardCode -> inicisCardCode.getCode().equals(code))
					.findAny()
					.orElse(InicisCardCode.INICIS);
		}
	}

	// PG 이니시스 코드
	@Getter
	@RequiredArgsConstructor
	public enum InicisCode implements CodeCommEnum {
		SUCCESS_PC("0000", "PC 성공"),
		SUCCESS_MOBILE("00", "모바일/API 성공"),
		USER_CANCEL("01", "사용자가 결제를 취소"),
		DEPOSIT_NOTI("02", "입금통보"),
		DEPOSIT_NOTI_SUCCESS("OK", "입금통보 결과 성공"),
		DEPOSIT_NOTI_FAIL("FAIL", "입금통보 결과 실패"),
		VIRTUAL_BANK("VBANK", "가상계좌"),
		CASH_RECEIPT_SUCCESS_PC("220000", "현금영수증 성공(PC)"),
		CASH_RECEIPT_SUCCESS_MO("0000", "현금영수증 성공(MO)")

		;
		private final String code;
		private final String codeName;
	}

	// PG KCP 코드
	@Getter
	@RequiredArgsConstructor
	public enum KcpCode implements CodeCommEnum {
		SUCCESS("0000", "성공"),
		USER_CANCEL("3001", "사용자가 결제를 취소(공통)"),
		USER_CANCEL_BANK("AC45", "사용자가 결제를 취소(계좌이체)"),
		DEPOSIT_NOTI_PC("TX00", "PC 입금통보"),
		DEPOSIT_NOTI_MOBILE("TX08", "모바일 입금통보"),
		DEPOSIT_NOTI_SUCCESS("0000", "입금통보 결과 성공"),
		DEPOSIT_NOTI_FAIL("9999", "입금통보 결과 실패"),

		;
		private final String code;
		private final String codeName;
	}

	// PG 이니시스 PC 부정거래 응답코드
	@Getter
	@RequiredArgsConstructor
	public enum InicisPcIllegalCode implements CodeCommEnum {
		CODE120026("120026", "신용카드-도난, 분실카드", IllegalDetailType.STOLEN_LOST_CARD),
		CODE170026("170026", "ISP-도난/분실:위:변조카드", IllegalDetailType.STOLEN_LOST_CARD),
		CODE1226("1226", "빌링(카드)-도난, 분실카드", IllegalDetailType.STOLEN_LOST_CARD),
		CODE160262("160262", "실시간계좌이체(K계좌)-분실,도난 통장(사용불가신청)", IllegalDetailType.STOLEN_LOST_CARD),
		CODE120027("120027", "신용카드-거래 불가 카드", IllegalDetailType.TRANSACTION_NOT_CARD),
		CODE1227("1227", "빌링(카드)-거래 불가 카드", IllegalDetailType.TRANSACTION_NOT_CARD),
		CODE16E063("16E063", "실시간계좌이체(I계좌)-거래불가/거래타입에러", IllegalDetailType.TRANSACTION_NOT_CARD),
		CODE160850("160850", "실시간계좌이체(I계좌)-기타 거래 불가입니다.", IllegalDetailType.TRANSACTION_NOT_CARD),
		;
		private final String code;
		private final String codeName;
		private final IllegalDetailType illegalDetailType;


		public static boolean isStolenLostCard(String code) {
			return Arrays.stream(InicisPcIllegalCode.values())
					.filter(illegal -> illegal.getCode().equals(code)
							&& illegal.getIllegalDetailType().equals(IllegalDetailType.STOLEN_LOST_CARD))
					.findAny().orElse(null) != null;
		}

		public static boolean isTransactionNotCard(String code) {
			return Arrays.stream(InicisPcIllegalCode.values())
					.filter(illegal -> illegal.getCode().equals(code)
							&& illegal.getIllegalDetailType().equals(IllegalDetailType.TRANSACTION_NOT_CARD))
					.findAny().orElse(null) != null;
		}
	}

	// PG 이니시스 Mobile 부정거래 응답코드
	@Getter
	@RequiredArgsConstructor
	public enum InicisMobileIllegalCode implements CodeCommEnum {
		NULL("없음", "문서전달받을예정", IllegalDetailType.STOLEN_LOST_CARD),
		;
		private final String code;
		private final String codeName;
		private final IllegalDetailType illegalDetailType;

		public static boolean isStolenLostCard(String code) {
			return Arrays.stream(InicisMobileIllegalCode.values())
					.filter(illegal -> illegal.getCode().equals(code)
							&& illegal.getIllegalDetailType().equals(IllegalDetailType.STOLEN_LOST_CARD))
					.findAny().orElse(null) != null;
		}

		public static boolean isTransactionNotCard(String code) {
			return Arrays.stream(InicisMobileIllegalCode.values())
					.filter(illegal -> illegal.getCode().equals(code)
							&& illegal.getIllegalDetailType().equals(IllegalDetailType.TRANSACTION_NOT_CARD))
					.findAny().orElse(null) != null;
		}
	}

	// PG KCP 부정거래 응답코드
	@Getter
	@RequiredArgsConstructor
	public enum KcpIllegalCode implements CodeCommEnum {
		CODECC43("CC43", "도난카드", IllegalDetailType.STOLEN_LOST_CARD),
		CODEAC11("AC11", "계좌이체|분실/도난 계좌", IllegalDetailType.STOLEN_LOST_CARD),
		CODECC04("CC04", "분실 혹은 거래불가 카드", IllegalDetailType.TRANSACTION_NOT_CARD),
		CODECC39("CC39", "기타거래불가카드", IllegalDetailType.TRANSACTION_NOT_CARD),
		CODECC46("CC46", "기타거래불가카드", IllegalDetailType.TRANSACTION_NOT_CARD),
		CODEAC15("AC15", "계좌이체|기타 거래불가 계좌", IllegalDetailType.TRANSACTION_NOT_CARD),
		;
		private final String code;
		private final String codeName;
		private final IllegalDetailType illegalDetailType;

		public static boolean isStolenLostCard(String code) {
			return Arrays.stream(KcpIllegalCode.values())
					.filter(illegal -> illegal.getCode().equals(code)
							&& illegal.getIllegalDetailType().equals(IllegalDetailType.STOLEN_LOST_CARD))
					.findAny().orElse(null) != null;
		}

		public static boolean isTransactionNotCard(String code) {
			return Arrays.stream(KcpIllegalCode.values())
					.filter(illegal -> illegal.getCode().equals(code)
							&& illegal.getIllegalDetailType().equals(IllegalDetailType.TRANSACTION_NOT_CARD))
					.findAny().orElse(null) != null;
		}
	}
}