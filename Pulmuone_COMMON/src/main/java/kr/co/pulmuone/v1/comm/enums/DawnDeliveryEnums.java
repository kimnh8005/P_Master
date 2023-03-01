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
public class DawnDeliveryEnums {

    // 추가 / 삭제 구분
    @Getter
    @RequiredArgsConstructor
    public enum AddDeleteTp implements CodeCommEnum {
        ADD("ADD", "추가"),
        DELETE("DELETE","삭제");

        private final String code;
        private final String codeName;
    }
}
