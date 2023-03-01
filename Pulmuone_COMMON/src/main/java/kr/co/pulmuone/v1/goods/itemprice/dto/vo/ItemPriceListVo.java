package kr.co.pulmuone.v1.goods.itemprice.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemPriceListVo {

    /*
     * 품목별 가격정보 Vo
     */
    private int ilItemPriceId; // 품목 가격 ID

    private String ilItemCode; // 품목 코드

    private String startDate; // 가격 적용 시작일

    private String endDate; // 가격 적용 종료일

    private String standardPrice; // 원가

    private String recommendedPrice; // 정상가

    private String createId; // 등록자 ID

    private String modifyId; // 수정자 ID

}
