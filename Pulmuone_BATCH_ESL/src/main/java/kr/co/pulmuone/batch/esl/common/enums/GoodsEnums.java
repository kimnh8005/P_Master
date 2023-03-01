package kr.co.pulmuone.batch.esl.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

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
public class GoodsEnums {

    // 식단주기 유형(베이비밀,잇슬림)
    @Getter
    @RequiredArgsConstructor
    public enum GoodsCycleType {
        DAY1_PER_WEEK("GOODS_CYCLE_TP.1DAY_PER_WEEK", "주1일", "1", "" , Arrays.asList()),
        DAYS3_PER_WEEK("GOODS_CYCLE_TP.3DAYS_PER_WEEK", "주3일", "3", "월/수/금", Arrays.asList("WEEK_CD.MON","WEEK_CD.WED","WEEK_CD.FRI")),
        DAYS4_PER_WEEK("GOODS_CYCLE_TP.4DAYS_PER_WEEK", "주4일", "4", "", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU")),
        DAYS5_PER_WEEK("GOODS_CYCLE_TP.5DAYS_PER_WEEK", "주5일", "5", "월~금", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU","WEEK_CD.FRI")),
        DAYS6_PER_WEEK("GOODS_CYCLE_TP.6DAYS_PER_WEEK", "주6일", "6", "월~토", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU","WEEK_CD.FRI")),
        DAYS7_PER_WEEK("GOODS_CYCLE_TP.7DAYS_PER_WEEK", "주7일", "7", "월~일", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU","WEEK_CD.FRI"))
        ;

        private final String code;
        private final String codeName;
        private final String typeQty;
        private final String weekText;
        private final List<String> weekCodeList;

        public static GoodsCycleType findByCode(String code) {
            return Arrays.stream(GoodsCycleType.values())
                    .filter(goodsCycleType -> goodsCycleType.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 식단주기 기간 유형(베이비밀,잇슬림)
    @Getter
    @RequiredArgsConstructor
    public enum GoodsCycleTermType {
        WEEK1("GOODS_CYCLE_TERM_TP.WEEK1", "1주", "1"),
        WEEK2("GOODS_CYCLE_TERM_TP.WEEK2", "2주","2"),
        WEEK4("GOODS_CYCLE_TERM_TP.WEEK4", "4주","4"),
        WEEK8("GOODS_CYCLE_TERM_TP.WEEK8", "8주","8"),
        WEEK12("GOODS_CYCLE_TERM_TP.WEEK12", "12주","12"),
        WEEK16("GOODS_CYCLE_TERM_TP.WEEK16", "16주","16"),
        WEEK20("GOODS_CYCLE_TERM_TP.WEEK20", "20주","20"),
        WEEK24("GOODS_CYCLE_TERM_TP.WEEK24", "24주","24"),
        ;

        private final String code;
        private final String codeName;
        private final String typeQty;

        public static GoodsCycleTermType findByCode(String code) {
            return Arrays.stream(GoodsCycleTermType.values())
                    .filter(goodsCycleTermType -> goodsCycleTermType.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

}