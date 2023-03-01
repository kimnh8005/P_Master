package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ItemStorePriceLogVo {

    /*
     * 매장품목가격 로그 Vo
     */

    private long ilItemStorePriceLogId; // 매장품목가격 로그 seq

    private String ilItemCd; // 품목코드

    private String urStoreId; // 스토어 코드(ERP 연동)

    private String priceStartDt; // 가격시작일

    private String priceEndDt; // 가격종료일

    private String storeSalePrice; // 매장 판매가
    
    private String priceChangeYn; // 가격변동여부
    
}
