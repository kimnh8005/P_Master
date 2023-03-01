package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ItemPoTypeCodeVo {

    /*
     * 해당 공급업체 PK 값에 해당하는 발주유형 코드 정보 조회 Vo
     */

    private String poTypeCode; 		// 발주유형 코드

    private String poTypeName; 		// 발주유형명

    private boolean poPerItemYn; 	// 발주일 설정값 품목별 상이 여부 ( true : 품목별 상이 )

    private String erpPoTp;			// ERP 발주유형

    private String templateYn;
}
