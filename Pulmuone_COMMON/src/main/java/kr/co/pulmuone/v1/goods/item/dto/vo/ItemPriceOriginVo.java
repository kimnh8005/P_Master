package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ItemPriceOriginVo {

    /*
     * 품목별 가격정보 원본 Vo
     */

    private String ilItemCode; // 품목 코드

    private String priceApplyStartDate; // 가격 적용 시작일 ( yyyy-MM-dd 포맷 )

    private String standardPrice; // 원가

    private String recommendedPrice; // 정상가

    private Long createId; // 등록자 ID

    private String systemUpdateYn;  //  시스템 업데이트 유무

    private String managerUpdateYn; // 관리자 업데이트 유무

}
