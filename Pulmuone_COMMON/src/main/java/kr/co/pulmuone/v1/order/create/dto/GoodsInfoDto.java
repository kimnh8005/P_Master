package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "GoodsInfoDto")
public class GoodsInfoDto {
    @ApiModelProperty(value = "배송비 정책 pk")
    private long ilGoodsShippingTemplateId;

    @ApiModelProperty(value = "출고처 관리 pk")
    private long urWarehouseId;

    @ApiModelProperty(value = "공급업체 pk")
    private long urSupplierId;

    @ApiModelProperty(value = "예약정보 pk")
    private long ilGoodsReserveOptnId;

    @ApiModelProperty(value = "기획전 pk")
    private long evExhibitId;

    @ApiModelProperty(value = "정상주문상태")
    private String statusCd;

    @ApiModelProperty(value = "판매처주문번호")
    private String sellerOrderId;

    @ApiModelProperty(value = "출고처그룹")
    private String urWarehouseGroupCd;

    @ApiModelProperty(value = "상품유형")
    private String goodsTp;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "판매유형")
    private String saleTp;

    @ApiModelProperty(value = "표준카테고리")
    private int ilCtgryStdId;

    @ApiModelProperty(value = "전시 카테고리")
    private int ilCtgryDisplayId;

    @ApiModelProperty(value = "몰인몰 카테고리")
    private int ilCtgryMallId;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "원가")
    private int standardPrice;
}
