package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemWarehouseCodeVo { // 해당 품목코드의 공급업체 PK 에 해당하는 출고처 그룹, 출고처 코드 정보 조회 VO

    private String urSupplierWarehouseId; // 공급업체-출고처 PK

    private String warehouseGroupCode; // 출고처 그룹 Code

    private String warehouseGroupName; // 출고처 그룹명

    private String warehouseName; // 출고처명

    private String warehouseAddress; // 출고처 주소

    private String storeYn; // 매장 (가맹점) 여부

    // (추후 로직 확정되면 구현 예정 ) 화면에서 삭제 가능 여부 : 매장재고가 하나 이상 연동되었을 경우, 삭제 불가
    private Boolean canDeleted;

    private String urWarehouseId;

    private String stockOrderYn;

    // 매장 배송 출고처 여부
    private String storeWarehouseYn;

    // 용인 출고처 여부
    private String yonginWarehouseYn;
}
