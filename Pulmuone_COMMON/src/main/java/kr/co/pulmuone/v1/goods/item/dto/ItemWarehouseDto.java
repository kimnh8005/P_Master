package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목별 출고처 등록 dto")
public class ItemWarehouseDto {

    /*
     * 품목별 출고처 등록 dto
     */
	private String ilItemWarehouseId;

    private String ilItemCode; // 품목 코드

    private String urSupplierWarehouseId; // 공급처-출고처 PK

    private Boolean unlimitStockYn; // 재고 무제한 ( DB 저장시 => Y: 재고 무제한 )

    private Boolean stockPoYn; // 재고 발주 여부 ( DB 저장시 => Y: 재고 발주 사용 )

    private Boolean preOrderYn; // 선주문 여부 ( DB 저장시 => Y: 선주문 사용 )

    private String preOrderTp; 	// 선주문 유형

    private String memo; // 발주 참조 메모

    private Long createId; // 등록자

    private Long modifyId; // 수정자

    private String updateFlag;

    private String ilPoTpId;


}
