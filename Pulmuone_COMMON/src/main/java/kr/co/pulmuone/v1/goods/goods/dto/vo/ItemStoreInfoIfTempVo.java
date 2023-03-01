package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ItemStoreInfoIfTempVo {

    /*
     * 올가매장상품조회 IF 임시테이블 Vo
     */

    private long ilItemStoreInfoIfTempId; // 올가매장상품조회 IF 임시테이블 seq

    private int ifSeq; // 중계서버 DB seq

    private String ilItemCd; // 품목코드

    private String shopCd; // 매장코드

    private int currentStock; // 현재고

    private int recommendedPrice; // 정상가

    private int salePrice; // 판매가

}
