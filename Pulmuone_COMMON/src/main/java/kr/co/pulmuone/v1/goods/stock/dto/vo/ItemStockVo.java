package kr.co.pulmuone.v1.goods.stock.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemStockVo {

    /*
     * 출고처별 품목 재고 Vo
     */

    private long ilItemStockId; // 출고처별 품목 재고 seq

    private long ilItemWarehouseId; // 품목 출고처 PK

    private String baseDate; // 재고 기준일 ( yyyyMMdd 형식 )

    /*
     * 재고 수량
     */
    private long d0stockQuantity; // D0 재고 수량

    private long d1stockQuantity; // D1 재고 수량

    private long d2stockQuantity; // D2 재고 수량

    private long d3stockQuantity; // D3 재고 수량

    private long d4stockQuantity; // D4 재고 수량

    private long d5stockQuantity; // D5 재고 수량

    private long d6stockQuantity; // D6 재고 수량

    private long d7stockQuantity; // D7 재고 수량

    private long d8stockQuantity; // D8 재고 수량

    private long d9stockQuantity; // D9 재고 수량

    private long d10stockQuantity; // D10 재고 수량

    /*
     * 주문 수량
     */
    private long d0orderQuantity; // D0 주문 수량

    private long d1orderQuantity; // D1 주문 수량

    private long d2orderQuantity; // D2 주문 수량

    private long d3orderQuantity; // D3 주문 수량

    private long d4orderQuantity; // D4 주문 수량

    private long d5orderQuantity; // D5 주문 수량

    private long d6orderQuantity; // D6 주문 수량

    private long d7orderQuantity; // D7 주문 수량

    private long d8orderQuantity; // D8 주문 수량

    private long d9orderQuantity; // D9 주문 수량

    private long d10orderQuantity; // D10 주문 수량

    private String createId; // 등록자 ID

    private String createDate; // 등록일시

    private String modifyId; // 수정자 ID

    private String modifyDate; // 수정일시

    /*
     * IL_ITEM_WAREHOUSE 테이블의 정보 : ilItemWarehouseId ( 품목 출고처 PK ) 로 매핑됨
     */
    private String ilItemCode; // 품목 코드

    private long urSupplierWarehouseId; // 공급처 출고처 PK

    private Boolean unlimitStockYn; // 재고무제한 ( true : 재고 무제한 )

    private Boolean stockPoYn; // 재고발주 ( true : 재고발주 사용 )

    private Boolean preOrderYn; // 선주문 여부 ( true : 선주문 사용 )

    private String preOrderType; // 선주문 유형 공통코드 ( PRE_ORDER_TP : NOT_ALLOWED, LIMITED_ALLOWED, UNLIMITED_ALLOWED )

    private String memo; // 발주 참조 메모

}
