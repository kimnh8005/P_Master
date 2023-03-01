package kr.co.pulmuone.v1.batch.goods.stock.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ItemErpStockVo {

    /*
     * 품목 출고처 재고 Vo
     */

    private long ilItemErpStockId; // 품목 출고처 재고 인터페이스 seq

    private long restDay; //남은일수

    private String urWarehouseId; //출고처ID

    private Long ilItemWarehouseId; // 품목-출고처 PK

    private Long ilItemStockExprId; // 품목 유통기한별 재고PK

    private String ilItemCd; // 품목코드

    private String baseDate; // 재고 기준일 ( yyyyMMdd 형식 )

    private String expirationDate; // 유통기한

    private String stockType; // 재고 타입

    private long stockQuantity; // 재고 수량

    private long createId; // 등록자 ID

    private String createDate; // 등록일시

    private long modifyId; // 수정자 ID

    private String modifyDate; // 수정일시

}
