package kr.co.pulmuone.batch.erp.common.enums;

import lombok.Getter;

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
public class BaseEnums {

    // 사용여부
    @Getter
    public enum UseYn {
        Y("1", true),
        N("0", false);

        private final String numberCode;
        private final boolean primitiveCode;

        UseYn(String numberCode, boolean primitiveCode){
            this.numberCode = numberCode;
            this.primitiveCode = primitiveCode;
        }
    }

}