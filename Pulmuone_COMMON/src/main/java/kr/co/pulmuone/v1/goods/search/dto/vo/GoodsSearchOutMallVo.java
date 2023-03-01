package kr.co.pulmuone.v1.goods.search.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsSearchOutMallVo {

    @ApiModelProperty(value = "상품 PK")
    private Long goodsId;

    @ApiModelProperty(value = "품목 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "판매상태 유형")
    private String saleStatus;

    @ApiModelProperty(value = "출고처관리PK")
    private String urWarehouseId;

    @ApiModelProperty(value = "풀무원샵 상품번호 PK")
    private Long goodsNo;

    @ApiModelProperty(value = "외부몰 판매상태 유형")
    private String goodsOutmallSaleStat;

    @ApiModelProperty(value = "외부몰 판매상태 업데이트 여부")
    private String goodsOutmallSaleStatUpdateYn;

    @ApiModelProperty(value = "공급업체 PK")
    private Long urSupplierId;

    @ApiModelProperty(value = "품목 배송불가유형 : 도서산간, 제주")
    private String undeliverableAreaTp;

}
