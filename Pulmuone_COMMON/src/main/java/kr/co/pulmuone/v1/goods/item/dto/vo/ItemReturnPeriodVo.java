package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemReturnPeriodVo {

    /*
     * 품목별 반품 요청가능 여부 / 반품 가능기간 조회 Vo
     */

    // 반품 요청가능 여부 ( true : 반품 요청 가능 )
    private boolean returnRequestAvailableYn;

    // 반품 가능기간 ( 반품 불가시 null 반환 )
    private Integer returnPeriod;

}
