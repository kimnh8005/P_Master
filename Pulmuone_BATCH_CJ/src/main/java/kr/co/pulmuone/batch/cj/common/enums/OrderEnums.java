package kr.co.pulmuone.batch.cj.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
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
 *  1.0    2021. 05. 12.                이명수          최초작성
 * =======================================================================
 * </PRE>
 */
public class OrderEnums {

    // 택배사코드 CJ : CJ택배, LOTTE : LOTTE택배
    @Getter
    @RequiredArgsConstructor
    public enum LogisticsCd implements CodeCommEnum {
        CJ("93", 		"CJ택배", "CJ"),
        LOTTE("90", 	"LOTTE택배", "LOTTE"),
        ;

        private final String code;
        private final String codeName;
        private final String logisticsCode;

    }

}