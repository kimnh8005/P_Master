package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ItemErpStoreInfoVo {

    @ApiModelProperty(value = "매장품목 정보PK")
    private Long ilItemStoreInfoId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "매장PK")
    private String urStoreId;

    @ApiModelProperty(value = "매장 판매가")
    private String storeSalePrice;

    @ApiModelProperty(value = "연동재고(ERP)")
    private String storeIfStock;

    @ApiModelProperty(value = "현재재고")
    private int storeStock;

    @ApiModelProperty(value = "중계서버 미처리된 주문")
    private int ifUnOrderCnt;

    @ApiModelProperty(value = "풀무원 미연동된 매장 주문")
    private int mallUnOrderCnt;

    @ApiModelProperty(value = "업데이트 이전 매장 판매가")
    private String beforeStoreSalePrice;


}
