package kr.co.pulmuone.v1.batch.goods.stock.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErpLinkItemVo {

    private String ilItemCode; // 품목코드 PK

    private Long ilItemWarehouseId; // 품목-출고처 PK

    private String itemType; // 품목유형 (공통, 매장전용, 무형, 렌탈)

    private boolean erpStockIfYn; // ERP 재고연동여부 ( DB 저장시 => 연동, N:미연동 )

    private Long urSupplierId; // 공급업체 PK

    private String urSupplierWarehouseId; // 공급처-출고처 PK

    private Boolean unlimitStockYn; // 재고 무제한 ( DB 저장시 => Y: 재고 무제한 )

    private Boolean stockPoYn; // 재고 발주 여부 ( DB 저장시 => Y: 재고 발주 사용 )

    private Boolean preOrderYn; // 선주문 여부 ( DB 저장시 => Y: 선주문 사용 )

    private String preOrderType; // 선주문 유형 공통코드 ( PRE_ORDER_TP : NOT_ALLOWED, LIMITED_ALLOWED, UNLIMITED_ALLOWED )

    private String memo; // 발주 참조 메모

}
