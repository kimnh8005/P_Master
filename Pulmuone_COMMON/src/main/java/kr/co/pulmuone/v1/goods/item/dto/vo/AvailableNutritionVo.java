package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AvailableNutritionVo {

    /*
     * IL_NUTRITION 테이블에서 조회된 영양정보 분류 코드 데이터 Vo
     */

    private String nutritionCode; // 영앙정보 분류코드

    private String nutritionName; // 영양정보 분류명

    private String nutritionUnit; // 영양정보 분류단위

    private Boolean nutritionPercentYn; // 영양소 기준치 (%) 사용여부 ( DB 조회시 => Y : 사용 )

}
