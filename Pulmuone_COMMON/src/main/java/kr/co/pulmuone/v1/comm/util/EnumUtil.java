package kr.co.pulmuone.v1.comm.util;

import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;

public class EnumUtil {

    private EnumUtil() {
    }

    /*
     * 인자로 받은 Enum 클래스 내 해당 코드에 해당하는 Enum 을 반환
     *
     * @Param Class<E> enumClass : CodeCommEnum 인터페이스를 구현한 Enum 클래스
     *
     * @Param String code : 검색할 코드 값
     */
    public static <E extends CodeCommEnum> E getEnum(final Class<E> enumClass, final String code) {
        if (code == null || !enumClass.isEnum()) {
            return null;
        }
        for (final E each : enumClass.getEnumConstants()) {
            if (each.getCode().equals(code)) {
                return each;
            }
        }
        return null;
    }

    /*
     * 인자로 받은 Enum 클래스에서 대소문자 구분없이 해당 코드에 해당하는 Enum 을 반환
     *
     * @Param Class<E> enumClass : CodeCommEnum 인터페이스를 구현한 Enum 클래스
     *
     * @Param String code : 검색할 코드 값 => 해당 메서드 사용시 대소문자 구분하지 않고 Enum 클래스를 검색함
     */
    public static <E extends CodeCommEnum> E getEnumEqualsIgnoreCase(final Class<E> enumClass, final String code) {
        if (code == null || !enumClass.isEnum()) {
            return null;
        }
        for (final E each : enumClass.getEnumConstants()) {
            if (each.getCode().equalsIgnoreCase(code)) {
                return each;
            }
        }
        return null;
    }
}
