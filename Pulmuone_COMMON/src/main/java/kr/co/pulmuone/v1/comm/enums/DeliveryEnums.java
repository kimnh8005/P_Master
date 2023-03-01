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
 *  1.0    2020. 7. 15.                jg          최초작성
 * =======================================================================
 * </PRE>
 */
public class DeliveryEnums {

    //휴일타입
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryType implements CodeCommEnum {
        NORMAL("NORMAL", "일반"),
        DAWN("DAWN", "새벽"),
        DAILY("DAILY","일일");

        private final String code;
        private final String codeName;
    }

    //배송지 타입
    @Getter
    @RequiredArgsConstructor
    public enum ShippingType implements CodeCommEnum {
        RECENT_SHIPPING_TYPE("RECENT_SHIPPING_TYPE", "최근배송지") /* 최근배송지 */
        , BASIC_SHIPPING_TYPE("BASIC_SHIPPING_TYPE", "기본배송지")/* 기본배송지 */
    	, NORMAL_SHIPPING_TYPE("NORMAL_SHIPPING_TYPE", "일반 저장배송지");/* 일반 저장배송지 */

        private final String code;
        private final String codeName;
    }

    //요일타입
    @Getter
    @RequiredArgsConstructor
    public enum WeekType implements CodeCommEnum {
        MON("WEEK_CD.MON", "월") /* 월요일*/
        , TUE("WEEK_CD.TUE", "화") /* 화요일*/
        , WED("WEEK_CD.WED", "수") /* 수요일*/
        , THU("WEEK_CD.THU", "목") /* 목요일*/
        , FRI("WEEK_CD.FRI", "금") /* 금요일*/
        , SAT("WEEK_CD.SAT", "토") /* 토요일*/
        , SUN("WEEK_CD.SUN", "일"); /* 일요일*/

        private final String code;
        private final String codeName;

        public static WeekType findByCodeName(String code) {
            return Arrays.stream(WeekType.values())
                    .filter(weekType -> weekType.getCodeName().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }


    //배송예정일 기본값
    @Getter
    @RequiredArgsConstructor
    public enum BaseWeekDay implements CodeCommEnum {
        WAREHOUSE_DAY("WAREHOUSE_SCHEDULE.DAY_0", "출발예정 0")
        , ARRIVED_DAY("ARRIVED_SCHEDULE.DAY_0", "도착예정 0");

        private final String code;
        private final String codeName;
    }


    // 도착예정일 변경 유효성 체크
    @Getter
    @RequiredArgsConstructor
    public enum ChangeArriveDateValidation implements MessageCommEnum {
        OVER_WAREHOUSE_CUTOFF_TIME("OVER_WAREHOUSE_CUTOFF_TIME", "선택하신 도착예정일의 주문이 마감되었습니다."),
        LACK_STOCK("LACK_STOCK", "선택하신 일자는 재고가 부족하여 주문이 불가합니다. 다른 일자를 선택해주세요."),
        FAIL("FAIL", "오류가 발생하였습니다.<br />동일한 문제가 계속 발생할 경우 고객기쁨센터에 문의해 주세요.");


        private final String code;
        private final String message;
    }
}
