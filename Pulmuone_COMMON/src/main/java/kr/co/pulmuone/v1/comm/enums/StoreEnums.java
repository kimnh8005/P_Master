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
public class StoreEnums {

    //매장 - 시도 구분 유형
    @Getter
    @RequiredArgsConstructor
    public enum StoreAreaType implements MessageCommEnum {
        SEOUL("SEOUL", "서울"),
        INCHEON("INCHEON", "인천"),
        BUSAN("BUSAN", "부산"),
        DAEGU("DAEGU", "대구"),
        DAEJEON("DAEJEON", "대전"),
        GWANGJU("GWANGJU", "광주"),
        ULSAN("ULSAN", "울산"),
        GYEONGGI("GYEONGGI", "경기"),
        GANGWON("GANGWON", "강원"),
        CHUNGBUK("CHUNGBUK", "충북"),
        CHUNGNAM("CHUNGNAM", "충남"),
        JEONBUK("JEONBUK", "전북"),
        JEONNAM("JEONNAM", "전남"),
        GYEONGBUK("GYEONGBUK", "경북"),
        GYEONGNAM("GYEONGNAM", "경남"),
        JEJU("JEJU", "제주"),
        SEJONG("SEJONG", "세종")
        ;

        private final String code;
        private final String message;
    }

	// 스토어 타입
    @Getter
    @RequiredArgsConstructor
    public enum StoreType implements CodeCommEnum{
    	DIRECT("STORE_TYPE.DIRECT","매장"),
    	BRANCH("STORE_TYPE.BRANCH","가맹점");

        private final String code;
        private final String codeName;
    }

    // 요일코드
    @Getter
    @RequiredArgsConstructor
    public enum WeekCode implements CodeCommEnum{
    	MON("WEEK_CD.MON", "월", 1),
    	TUE("WEEK_CD.TUE", "화", 2),
    	WED("WEEK_CD.WED", "수", 3),
    	THU("WEEK_CD.THU", "목", 4),
    	FRI("WEEK_CD.FRI", "금", 5),
    	SAT("WEEK_CD.SAT", "토", 6),
    	SUN("WEEK_CD.SUN", "일", 7)
        ;

        private final String code;
        private final String codeName;
        private final int weekValue;

		public static WeekCode findByCode(String code) {
			return Arrays.stream(WeekCode.values()).filter(weekCode -> weekCode.getCode().equals(code)).findAny()
					.orElse(null);
		}

		public static WeekCode findByWeekValue(int weekValue) {
			return Arrays.stream(WeekCode.values())
					.filter(weekCode -> weekCode.getWeekValue() == weekValue)
					.findAny()
					.orElse(null);
		}
    }

    // API 입력 시스템 코드값
    @Getter
    @RequiredArgsConstructor
    public enum StoreDeliverySystemCode implements CodeCommEnum{
    	HITOK("HITOK", "하이톡", "4"),
    	ORGAOMS("ORGAOMS", "ORGA OMS", "2")
        ;

        private final String code;
        private final String codeName;
        private final String value;

    }


    // 매장 타입
    @Getter
    @RequiredArgsConstructor
    public enum StoreTypeCode implements CodeCommEnum{
    	DIRECT("STORE_TYPE.DIRECT", "매장"),
    	BRANCH("STORE_TYPE.BRANCH", "가맹점")
        ;

        private final String code;
        private final String codeName;

    }

    // 매장 카테고리 타입
    @Getter
    @RequiredArgsConstructor
    public enum StoreCategoryTypeCode implements CodeCommEnum{
    	DIRECT("DIRECT", "직영" , "STORE_CATEGORY.DIRECT"),
    	SIS("SIS", "샵인샵", "STORE_CATEGORY.SIS"),
    	BY_ORGA("BY_ORGA", "바이올가", "STORE_CATEGORY.BY_ORGA"),
    	FD("FD", "녹즙", "STORE_CATEGORY.FD")
        ;

        private final String code;
        private final String codeName;
        private final String commonCode;
    }

    // API 매장상태 코드값
    @Getter
    @RequiredArgsConstructor
    public enum StoreStatusSystemCode implements CodeCommEnum{
    	WAIT("WAIT", "영업대기","STORE_STATUS.WAIT"),
    	USE("USE", "영업중","STORE_STATUS.USE"),
    	NOT_USE("NOT_USE", "폐점","STORE_STATUS.NOT_USE")
        ;

        private final String code;
        private final String codeName;
        private final String commonCode;

    }


    // API 매장상태 코드값
    @Getter
    @RequiredArgsConstructor
    public enum StoreApiDeliveryIntervalCode implements CodeCommEnum{
    	EVERY("EVERY", "매일", "STORE_DELIVERY_INTERVAL.EVERY"),
    	TWO_DAYS("TWO_DAYS", "격일", "STORE_DELIVERY_INTERVAL.TWO_DAYS"),
    	NON_DELV("NON_DELV", "미배송", "STORE_DELIVERY_INTERVAL.NON_DELV"),
    	PARCEL("PARCEL", "택배", "STORE_DELIVERY_INTERVAL.PARCEL")
        ;

        private final String code;
        private final String codeName;
        private final String commonCode;

    }


    // API 배송유형 코드값
    @Getter
    @RequiredArgsConstructor
    public enum StoreApiDeliveryTypeCode implements CodeCommEnum{
    	PICKUP("PICKUP", "매장픽업","STORE_DELIVERY_TYPE.PICKUP"),
    	DIRECT("DIRECT", "매장배송","STORE_DELIVERY_TYPE.DIRECT"),
    	HOME("HD", "홈배송","STORE_DELIVERY_TYPE.HOME"),
    	OFFICE("OD", "오피스배송","STORE_DELIVERY_TYPE.OFFICE"),
		ALL("ALL", "홈/오피스배송","STORE_DELIVERY_TYPE.HD_OD"),
    	HD_OD("MD", "홈/오피스배송","STORE_DELIVERY_TYPE.HD_OD")
        ;

        private final String code;
        private final String codeName;
        private final String commonCode;

        public static StoreApiDeliveryTypeCode findByCode(String code) {
			return Arrays.stream(StoreApiDeliveryTypeCode.values()).filter(dto -> dto.getCode().equals(code)).findAny()
					.orElse(null);
		}
		
		public static StoreApiDeliveryTypeCode findByCommonCode(String code) {
			return Arrays.stream(StoreApiDeliveryTypeCode.values()).filter(dto -> dto.getCode().equals(code)).findAny()
					.orElse(null);
		}
    }


 // API 배송가능 품목유형 코드값
    @Getter
    @RequiredArgsConstructor
    public enum StoreApiDeliverableItemCode implements CodeCommEnum{
    	ALL("ALL", "전체", "STORE_DELIVERABLE_ITEM.ALL"),
    	DM("DM", "DM", "STORE_DELIVERABLE_ITEM.DM"),
    	FD("FD", "녹즙", "STORE_DELIVERABLE_ITEM.FD")
        ;

        private final String code;
        private final String codeName;
        private final String commonCode;

    }


    // 사용유무  타입
    @Getter
    @RequiredArgsConstructor
    public enum UseType implements CodeCommEnum{
    	USE("Y","사용"),
    	NOT_USE("N","미사용");

        private final String code;
        private final String codeName;
    }

    // 스토어 배송권역 배송방식
    @Getter
    @RequiredArgsConstructor
    public enum StoreDeliveryIntervalTpye implements CodeCommEnum{
    	EVERY("EVERY", "매일", "STORE_DELIVERY_INTERVAL.EVERY"),
    	TWO_DAYS("TWO_DAYS", "격일", "STORE_DELIVERY_INTERVAL.TWO_DAYS"),
    	NON_DELV("NON_DELV", "미배송", "STORE_DELIVERY_INTERVAL.NON_DELV"),
    	PARCEL("PARCEL", "택배", "STORE_DELIVERY_INTERVAL.PARCEL")
        ;

        private final String code;
        private final String codeName;
        private final String commonCode;

    }

    // 매장 이미지  타입
    @Getter
    @RequiredArgsConstructor
    public enum storeImageType implements CodeCommEnum{
    	PC("PC","PC"),
    	MOBILE("MOBILE","MOBILE");

        private final String code;
        private final String codeName;
    }

}