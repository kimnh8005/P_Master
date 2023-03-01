package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
 *  1.0    2021. 2. 15.                이명수          최초작성
 * =======================================================================
 * </PRE>
 */
public class OrderScheduleEnums {
    // 일일배송 주문불가능 요일
    @Getter
    @RequiredArgsConstructor
    public enum DailyUnAbledWeekDay implements CodeCommEnum {
        SUN("WEEK_CD.SUN", "일"),
        SAT("WEEK_CD.SAT", "토"),
        ;

        private final String code;
        private final String codeName;

    }

	// 스캐줄 사용 여부
	@Getter
	@RequiredArgsConstructor
	public enum ScheduleUseType implements CodeCommEnum
	{
		SCHEDULE_USE_Y("Y", "사용"),
		SCHEDULE_USE_N("N", "미사용");
		;

		private final String code;
		private final String codeName;
	}

	// 스캐줄 사용 여부
	@Getter
	@RequiredArgsConstructor
	public enum ScheduleOrderType implements CodeCommEnum
	{
		SCHEDULE_ORDER_TYPE_ORDER("1", "주문"),
		SCHEDULE_ORDER_TYPE_CANCEL("2", "취소");
		;

		private final String code;
		private final String codeName;
	}

	// 스캐줄 배치 여부
	@Getter
	@RequiredArgsConstructor
	public enum ScheduleBatchType implements CodeCommEnum
	{
		SCHEDULE_BATCH_Y("Y", "배치 완료"),
		SCHEDULE_BATCH_N("N", "배치 미완료");
		;

		private final String code;
		private final String codeName;
	}

    // 스케줄 종류
    @Getter
    @RequiredArgsConstructor
    public enum ScheduleCd implements CodeCommEnum {
        GREENJUICE("GOODS_DAILY_TP.GREENJUICE", "녹즙"),
        EATSSLIM("GOODS_DAILY_TP.EATSSLIM", "잇슬립"),
        BABYMEAL("GOODS_DAILY_TP.BABYMEAL", "베이비밀")
        ;

        private final String code;
        private final String codeName;

    }

    // 스케줄 에러메시지
    @Getter
    @RequiredArgsConstructor
    public enum ScheduleErrMsg implements MessageCommEnum {

    	VALUE_EMPTY("-1", "해당 주문정보가 존재하지 않음"),
    	GOODS_DAILY_TP_EMPTY("-1", "일일상품 유형이 존재하지 않음 "),
    	REQUIRED_GOODS_DAILY_TP("-1", "배송요일이 변경 불가능한 상품유형"),
    	API_COMMUNICATION_FAILED("-1", "API 통신 실패"),
        API_SCHEDULE_SYNC_FAILED("-1", "동기화 실패"),
        API_SCHEDULE_INSERT_FAILED("-1", "API 변경 데이터 존재 리스트 새로고침 필요"),
        API_SCHEDULE_NO_DATA("-1", "스케줄 데이터 없음.")
    	;
    	private final String code;
  		private final String message;
    }

    // 스케줄 도착일자 에러메시지
    @Getter
    @RequiredArgsConstructor
    public enum ScheduleDateErrMsg implements MessageCommEnum {
    	VALUE_EMPTY("-1", "변경 가능일자 정보가 존재하지 않음")
    	;
    	private final String code;
  		private final String message;
    }

    // 스케줄 주문변경 가능일자
    @Getter
    @RequiredArgsConstructor
    public enum ScheduleChangeDate {
          MON("월", "목",-4) /* 배송일이 월요일인 경우 목요일부터 그 이전인 경우 수정가능 */
        , TUE("화", "목",-5) /* 배송일이 화요일인 경우 목요일부터 그 이전인 경우 수정가능 */
        , WEN("수", "일",-3) /* 배송일이 수요일인 경우 일요일부터 그 이전인 경우 수정가능 */
        , THU("목", "월",-3) /* 배송일이 목요일인 경우 월요일부터 그 이전인 경우 수정가능 */
        , FRI("금", "화",-3) /* 배송일이 금요일인 경우 화요일부터 그 이전인 경우 수정가능 */
    	;
    	private final String deliveryDay;
    	private final String changeDay;
  		private final int addDate;

		public static ScheduleChangeDate findByCode(String code) {
			return Arrays.stream(ScheduleChangeDate.values())
					.filter(scheduleChangeDate -> scheduleChangeDate.getDeliveryDay().equals(code))
					.findAny()
					.orElse(null);
		}
    }

    // 스케줄 변경 에러메시지
    @Getter
    @RequiredArgsConstructor
    public enum ScheduleChangeErrMsg implements MessageCommEnum {
    	VALUE_EMPTY("-1", "변경일자 정보가 존재하지 않음"),
    	REQUIRED_CHANGE_FROM_DATE("-1", "기존 배송일자 변경 불가능"),
    	REQUIRED_CHANGE_TO_DATE("-1", "변경요청 배송일자 변경 불가능"),
    	REQUIRED_WEEKEND("-1", "주말 변경 불가능"),
    	REQUIRED_HOLIDAY("-1", "공휴일 변경 불가능"),
    	REQUIRED_STORE_DELIVERY_AREA("-1", "해당 권역 배송 불가능"),
    	REQUIRED_GREENJUICE_SELECT_FAILED("-1", "내맘대로 녹즙 주문건은 불가능"),
    	REQUIRED_GREENJUICE_ORDER_CNT_FAILED("-1", "잘못된 주문 수량"),
    	REQUIRED_VALUE_FAILED("-1", "필수값 누락"),
    	REQUIRED_VALUE_DIFFERENT("-1", "요일과 요일별수량 개수 상이")
    	;
    	private final String code;
  		private final String message;
    }

    // 일일배송 주문변경 시작일(현재일 기준 +4일부터 가능)
    @Getter
    @RequiredArgsConstructor
    public enum ScheduleChangeStartDate {
        START_DATE("START_DATE", 4);
        private final String code;
        private final int addDate;

    }

	// 스캐줄 배치 여부
	@Getter
	@RequiredArgsConstructor
	public enum ScheduleDeliveryType implements CodeCommEnum
	{
		SCHEDULE_DELIVERY_Y("Y", "배송 완료"),
		SCHEDULE_DELIVERY_N("N", "배송 미완료");
		;

		private final String code;
		private final String codeName;
	}

	// 스캐줄 내맘대로 여부
	@Getter
	@RequiredArgsConstructor
	public enum ScheduleSelectType implements CodeCommEnum
	{
		SCHEDULE_SELECT_Y("Y", "내맘대로 녹즙"),
		SCHEDULE_SELECT_N("N", "일반 녹즙");
		;

		private final String code;
		private final String codeName;
	}

	// 일일배송 도착일변경 가능일
	// 녹즙		: 배송일 기준 D-3 출고처 마감시간까지
	// 베이비밀 	: 배송일 기준 D-2 출고처 마감시간까지
	// 잇슬림 	: 배송일 기준 D-3 출고처 마감시간까지
	@Getter
	@RequiredArgsConstructor
	public enum ScheduleChangePossibleDate {
		GREENJUICE_CHANGE_DATE("GOODS_DAILY_TP.GREENJUICE", 3),
		BABYMEAL_CHANGE_DATE("GOODS_DAILY_TP.BABYMEAL", 2),
		EATSSLIM_CHANGE_DATE("GOODS_DAILY_TP.EATSSLIM", 3);
		private final String code;
		private final int changePossibleAddDate;

		public static ScheduleChangePossibleDate findByCode(String code) {
			return Arrays.stream(ScheduleChangePossibleDate.values())
					.filter(ScheduleChangePossibleDate -> ScheduleChangePossibleDate.getCode().equals(code))
					.findAny()
					.orElse(null);
		}
	}

}