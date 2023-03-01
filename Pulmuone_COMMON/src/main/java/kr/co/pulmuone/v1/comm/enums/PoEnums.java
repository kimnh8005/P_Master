package kr.co.pulmuone.v1.comm.enums;

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
 *  1.0    2021. 02. 17. 이성준        최초작성
 * =======================================================================
 * </PRE>
 */
public class PoEnums {

    @Getter
    @RequiredArgsConstructor
    public enum PoType implements CodeCommEnum {

    	PO_TYPE_PRODUCTION("PO_TYPE.PRODUCTION", "생산발주") //
        ;

        private final String code;
        private final String codeName;

    }

    @Getter
    @RequiredArgsConstructor
    public enum PoBatchType implements CodeCommEnum {

    	 PO_BATCH_TP_YONGIN_PF_1440("PO_BATCH_TP.YONGIN_PF_1440", "용인 풀샵 14:40 발주배치")
    	,PO_BATCH_TP_YONGIN_PF_1840("PO_BATCH_TP.YONGIN_PF_1840", "용인 풀샵 18:40 발주배치")
    	,PO_BATCH_TP_YONGIN_OG_R1("PO_BATCH_TP.YONGIN_OG_R1", "용인 올가 R1 15:30 발주배치")
        ,PO_BATCH_TP_YONGIN_OG_R2("PO_BATCH_TP.YONGIN_OG_R2", "용인 올가 R2 15:31 발주배치")
    	,PO_BATCH_TP_BAEKAM_PF_1440("PO_BATCH_TP.BAEKAM_PF_1440", "백암 풀샵 14:40 발주배치")
    	,PO_BATCH_TP_BAEKAM_PF_1840("PO_BATCH_TP.BAEKAM_PF_1840", "백암 풀샵 18:40 발주배치")
        ;

        private final String code;
        private final String codeName;

    }

    @Getter
    @RequiredArgsConstructor
    public enum PoDay implements CodeCommEnum {

    	 PO_DAY_MONDAY("PO_DAY.MONDAY", "월")
        ,PO_DAY_TUESDAY("PO_DAY.TUESDAY", "화")
        ,PO_DAY_WEDNESDAY("PO_DAY.WEDNESDAY", "수")
        ,PO_DAY_THURSDAY("PO_DAY.THURSDAY", "목")
        ,PO_DAY_FRIDAY("PO_DAY.FRIDAY", "금")
        ,PO_DAY_SATURDAY("PO_DAY.SATURDAY", "토")
        ,PO_DAY_SUNDAY("PO_DAY.SUNDAY", "일")
    	;

        private final String code;
        private final String codeName;

    }

}
