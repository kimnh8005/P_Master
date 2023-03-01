package kr.co.pulmuone.v1.comm.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * API Enums
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 9. 21.                손진구          최초작성
 * =======================================================================
 * </PRE>
 */
public class ApiEnums
{

	@Getter
    @RequiredArgsConstructor
    public enum Default implements MessageCommEnum {
		/* API 에서 공통적으로 사용될 Enum 을 제각각 여러번 선언해 둘 필요가 있을까?
		 * 초기에는 규합할 상황이 안되었겠지만, 이후에는 초기 선언된 것 까지 옮기고, 기존 사용한 곳들을 변경 할 필요성이 있어보임.
		 * 같은 Enum은 여기에!
		 * 다른 의견 있으시면 의견제시 해주세요 -박승현 */
		  NO_DATA("-1", "조회된 결과가 없습니다.")
		, HTTP_STATUS_FAIL("-1", "API 통신 실패")
		, RESPONSE_FAIL("-1", "API 결과 상태값 오류")
		, RESPONSEBODY_NO_DATA("-1", "responseBody no data")
		, JSON_PARSING_ERROR("-1", "Json Parsing Error")
        ;

        private final String code;
        private final String message;
    }

	@Getter
	@RequiredArgsConstructor
	public enum EzAdminStatus implements MessageCommEnum {
		  NO_DATA("100", "조회된 결과가 없습니다.")
		, HTTP_STATUS_FAIL("200", "API 통신 실패")
		, RESPONSE_FAIL("300", "API 결과 상태값 오류")
		, RESPONSEBODY_NO_DATA("400", "responseBody no data")
		, JSON_PARSING_ERROR("500", "Json Parsing Error")
		;

		private final String code;
		private final String message;
	}

	// 롯데글로벌로지스 화물상태
	@Getter
	@RequiredArgsConstructor
	public enum LotteGlogisTrackingStatus implements MessageCommEnum
	{
		WEARING("10", "집하")
		, TRACKING_REGISTRATION("12", "운송장등록")
		, SEND("20", "발송(구간/셔틀)")
		, DELIVERY_BEFORE("40", "배달전")
		, DELIVERY_COMPLETED("41", "배달완료")
		, CONSIGNEE_REGISTRATION("45", "인수자등록")
		;

		private final String code;
		private final String message;
	}

	// 롯데글로벌로지스 트래킹 API 결과
	@Getter
	@RequiredArgsConstructor
	public enum LotteGlogisTrackingApiResponse implements MessageCommEnum
	{
		SUCCESS("0", "성공")
		, TRACKING_PARAM_ERROR("-1", "잘못된 운송장번호 입니다.")
		, NO_DATA("-1", "조회된 결과가 없습니다.")
		, JSON_PARSING_ERROR("-1", "Json Parsing Error")
		, TRACKING_NO_DATA("-1", "트래킹 데이터가 없습니다.")
		, RESPONSEBODY_NO_DATA("-1", "responseBody no data")
		;

		private final String code;
		private final String message;
	}

	// 롯데글로벌로지스 거래처 주문 API 결과
	@Getter
	@RequiredArgsConstructor
	public enum LotteGlogisClientOrderApiResponse implements MessageCommEnum
	{
		SUCCESS("0", "성공")
		, HTTP_STATUS_FAIL("-1", "API 통신 실패")
		, NO_DATA("-1", "조회된 결과가 없습니다.")
		, JSON_PARSING_ERROR("-1", "Json Parsing Error")
		, RETURN_NO_DATA("-1", "거래처 주문 결과 데이터가 없습니다.")
		, RESPONSEBODY_NO_DATA("-1", "responseBody no data")
		;

		private final String code;
		private final String message;
	}

	// CJ글로벌로지스 화물상태
	@Getter
	@RequiredArgsConstructor
	public enum CJLogisticsTrackingStatus implements MessageCommEnum
	{
		WEARING("WEARING", "집하처리")
		, UNSTOCKED("UNSTOCKED", "미집하")
		, DELIVERY_LOAD("DELIVERY_LOAD", "간선상차")
		, DELIVERY_GET_OFF("DELIVERY_GET_OFF", "간선하차")
		, DELIVERY_START("DELIVERY_START", "배달출발")
		, UNDELIVERED("UNDELIVERED", "미배달")
		, DELIVERY_COMPLETED("DELIVERY_COMPLETED", "배달완료")
		;

		private final String code;
		private final String message;
	}

	// CJ글로벌로지스 트래킹 API 결과
	@Getter
	@RequiredArgsConstructor
	public enum CJLogisticsTrackingApiResponse implements MessageCommEnum
	{
		SUCCESS("0", "성공")
		, TRACKING_PARAM_ERROR("-1", "잘못된 운송장번호 입니다.")
		, NO_DATA("-1", "조회된 결과가 없습니다.")
		, JSON_PARSING_ERROR("-1", "Json Parsing Error")
		, TRACKING_NO_DATA("-1", "트래킹 데이터가 없습니다.")
		, RESPONSEBODY_NO_DATA("-1", "responseBody no data")
		;

		private final String code;
		private final String message;
	}

	// EZ Admin API 종류
	@Getter
	@RequiredArgsConstructor
	public enum EZAdminApiAction implements CodeCommEnum
	{
		GET_ORDER_INFO("get_order_info", "주문 조회")
		, GET_PRODUCT_INFO("get_product_info", "상품 조회")
		, GET_STOCK_INFO("get_stock_info", "재고 조회")
		, GET_STOCK_INFO_EXT("get_stock_info_ext", "재고 조회 확장")
		, GET_ETC_INFO("get_etc_info", "기타정보 조회")
		, SET_TRANS_NO("set_trans_no", "송장입력")
		, GET_AUTO_CS_SYNC_DATA("get_auto_cs_sync_data", "문의글 조회")
		, SET_AUTO_CS_SYNC_DATA("set_auto_cs_sync_answer", "문의글 답변")
		;

		private final String code;
		private final String codeName;
	}

	// EZ Admin 주문조회 API 조회일자타입
	@Getter
	@RequiredArgsConstructor
	public enum EZAdminGetOrderInfoDateType implements CodeCommEnum
	{
		COLLECT_DATE("collect_date", "수집일")
		, ORDER_DATE("order_date","주문일")
		, TRANS_DATE("trans_date","송장 입력일")
		, CANCEL_DATE("cancel_date", "취소일")
		, CHANGE_DATE("change_date", "교환일")
		, SHOPSTAT_DATE("shopstat_date", "발주일")
		;

		private final String code;
		private final String codeName;
	}

	// EZ Admin 주문조회 API CS상태
	@Getter
	@RequiredArgsConstructor
	public enum EZAdminGetOrderInfoOrderCs
	{
		ORDER("0" , "정상", "ORDER")
		,TRANS("0" , "송장", "TRANS")
		,CLAIM("1,2,3,4,5,6,7,8" , "클레임", "CLAIM")
		;

		/*
		 * 주문조회 API CS 상태값
		 *	0 정상
			1 배송전 전체 취소
			2 배송전 부분 취소
			3 배송후 전체 취소
			4 배송후 부분 취소
			5 배송전 전체 교환
			6 배송전 부분 교환
			7 배송후 전체 교환
			8 배송후 부분 교환
		 * */

		private final String order_cs;
		private final String status;
		private final String batchTp;
	}

	// 통합 ERP 납품처코드 조회
	@Getter
	@RequiredArgsConstructor
	public enum IfShiptoSrchByErp implements MessageCommEnum
	{
		SUCCESS("0", "사용가능한 코드 입니다.")
		, FAIL("-1", "확인 되지 않은 코드 입니다.")
		;

		private final String code;
		private final String message;
	}

	// 잇슬림 주문, 배송정보  API 데이터 타입
	@Getter
	@RequiredArgsConstructor
	public enum EatsSlimOrderInfoDateType
	{
		SEARCH_ES_ORDER_LIST			("10.244.3.220", "UM", "R", "12", "searchEsOrderList", "", "", "", ""),
		SEARCH_ES_ORDER_DELIVERY_LIST	("10.244.3.220", "UM", "R", "16", "SearchEsOrderDeliveryList", "ES", "", "", ""),
		UPDATE_ES_ORDER_INFO			("10.244.3.220", "UM", "U", "13", "UpdateOrder", "ES", "풀무원샵", "M", "U")
		;
		private final String currentIp;
		private final String siteCd;
		private final String mode;
		private final String modeSeq;
		private final String serviceCd;
		private final String destsiteCD;
		private final String userNm;
		private final String insertType;
		private final String activeFlag;

	}

	// 베이비밀 주문, 배송정보 조회 API 데이터 타입
	@Getter
	@RequiredArgsConstructor
	public enum BabymealGetOrderInfoDateType
	{
		SEARCH_BM_ORDER_DELIVERY_LIST ("000001", "BM", "SearchOrderInfoDetail", "(알러지대체식단)")
		;
		private final String companyID;
		private final String siteCd;
		private final String serviceCd;
		private final String allergyTypeNm;
	}

	// 베이비밀 배송정보 변경 API 데이터 타입
	@Getter
	@RequiredArgsConstructor
	public enum BabymealPutOrderInfoDateType
	{
		UPDATE_BM_ORDER_DELIVERY_LIST ("KR", "UpdateOrder", "풀무원샵", "BM", "BM", "0001", "HQ", "ko_KR", "10.244.3.220", "GP0900", "KRW", "0001")
		;
		private final String languageCD;
		private final String serviceCd;
		private final String userName;
		private final String destsiteCD;
		private final String siteCd;
		private final String deliveryType;
		private final String spartnerDivCD;
		private final String localeCD;
		private final String currentIp;
		private final String timezoneCD;
		private final String currencyCD;
		private final String giftTypeCd;

	}

	// 베이비밀 배송정보 변경 API 주문구분
	@Getter
	@RequiredArgsConstructor
	public enum BabymealPutOrderInfoKindCd implements CodeCommEnum
	{
		NORMAL("0001", "정상주문"),
		CANCEL("0002", "취소"),
		GIFT("0003", "증정"),
		;
		private final String code;
		private final String codeName;
	}

	// 베이비밀 배송정보 변경 API 저장구분
	@Getter
	@RequiredArgsConstructor
	public enum BabymealPutOrderInfoActiveFlag implements CodeCommEnum
	{
		SAVE("S", "저장"),
		INSERT("I", "추가"),
		UPDATE("U", "수정")
		;
		private final String code;
		private final String codeName;
	}

	// 네이버 로그인 사용자 정보 조회 API 응답코드
	@Getter
	@RequiredArgsConstructor
	public enum naverUserProfileAPIResponseCode implements MessageCommEnum
	{
		SUCCESS("200", "성공")
		, AUTHENTICATION_FAILED("401", "인증 실패")
		, FORBIDDEN("403", "호출 권한 없음")
		, NOT_FOUND("404", "검색결과 없음")
		, INTERNAL_SERVER_ERROR("500", "데이터베이스 오류")
		;

		private final String code;
		private final String message;
	}

	// EZ Admin 문의글 조회 API 조회일자타입
	@Getter
	@RequiredArgsConstructor
	public enum EZAdminGetAutoCsSyncDataDateType implements CodeCommEnum
	{
		REG_DATE("reg_date", "문의글 수집일")
		;

		private final String code;
		private final String codeName;
	}
	
	// 식단 스케쥴관리 > 패턴등록 > 연결상품 추가체크 API 응답코드
	@Getter
	@RequiredArgsConstructor
	public enum checkMealPatternGoodsAPIResponseCode implements MessageCommEnum
	{
		SUCCESS("200", "성공")
		, ALREADY_OR_NOT_EXIST("ALREADY_OR_NOT_EXIST", "이미 등록되었거나 존재하지 않는 상품코드입니다.")
		, NOT_REGISTRATION("NOT_REGISTRATION", "연결할 수 없는 상품입니다.")
		;

		private final String code;
		private final String message;
	}
}