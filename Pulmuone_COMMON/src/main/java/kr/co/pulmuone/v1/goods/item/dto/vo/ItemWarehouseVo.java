package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemWarehouseVo {

    /*
     * 품목별 출고처 정보 Vo
     */

    private String ilItemWarehouseId; // 품목별 출고처 PK

    private String ilItemCode; // 품목 코드

    private String urWarehouseId; // 출고처 PK

    private String urSupplierWarehouseId; // 공급처-출고처 PK

    private boolean unlimitStockYn; // 재고 무제한 ( true : 재고 무제한 )

    private boolean stockPoYn; // 재고 발주 ( true : 재고 발주 사용 )

    private boolean preOrderYn; // 선주문 가능 여부 ( true : 선주문 사용 )

    private String preOrderTp; // 선주문 유형 공통코드

    private String memo; // 발주 참조 메모

    private String warehouseGroupCode; // 출고처 그룹 Code

    private String warehouseGroupName; // 출고처 그룹명

    private String warehouseName; // 출고처명

    private String warehouseAddress; // 출고처 주소

    private String storeYn; // 매장 (가맹점) 여부

    private boolean canDeleted; // 삭제 가능 여부 : 해당 품목코드, 출고처로 등록된 상품 존재시 false ( 삭제 불가 )

    private Long createId; // 등록자 ID

    private Long modifyId; // 수정자 ID

    private String updateFlag;

    private String stockOrderYn;

    private String ilPoTpId;

    // 매장 배송 출고처 여부
    private String storeWarehouseYn;

    // 용인 출고처 여부
    private String yonginWarehouseYn;

    // 미연동 재고 수량
    private int notIfStockCnt;

}
