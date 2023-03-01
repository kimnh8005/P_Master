package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemDiscountVo {

    /*
     * 품목 할인 Vo
     */

    private String ilItemCode; // 품목 코드

    private String discountType; // 상품할인 유형 공통코드 ( GOODS_DISCOUNT_TP - PRIORITY:우선할인, ORGA:올가할인, IMMEDIATE:즉시할인 )

    private String discountStartDate; // 할인 시작일 ( yyyy-MM-dd 포맷 )

    private String discountEndDate; // 할인 종료일 ( yyyy-MM-dd 포맷 )

    private String discountMethodType; // 상품할인 방법 유형 공통코드 ( GOODS_DISCOUNT_METHOD_TP - FIXED_PRICE:고정가할인, FIXED_RATE:정률할인 )

    private String discountSalePrice; // 할인 판매가

    private boolean useYn; // 사용여부 ( Y : 사용 )

    private Long createId; // 등록자 ID

    private String ilItemDiscountId;

    private String discountTp;

    private String discountStartDt;

    private String discountEndDt;

    private String discountMethodTp;

    private String discountRatio;

    private String ilItemCd;

    private String discountTpNm;
    private String discountMethodTpNm;

    private int discountPrice;
}
