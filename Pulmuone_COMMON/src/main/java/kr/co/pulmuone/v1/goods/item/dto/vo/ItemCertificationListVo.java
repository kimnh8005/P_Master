package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemCertificationListVo {

    /*
     * IL_ITEM_CERTIFICATION 테이블에서 조회된 품목별 상품인증정보 Vo
     */

    private String ilItemCode; // 품목 코드

    private String ilCertificationId; // 상품인증정보 ID

    private String certificationDescription; // 품목별 상품 인증 상세 정보

}
