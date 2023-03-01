package kr.co.pulmuone.v1.order.front.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderInfoFromFeedbackVo {

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "아이템 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "결제완료 일자")
    private String icDate;

    @ApiModelProperty(value = "배송중 일자")
    private String diDate;

    @ApiModelProperty(value = "이벤트 PK - 체험단용")
    private Long evEventId;

    @ApiModelProperty(value = "합 유형")
    private String packType;

    @ApiModelProperty(value = "타이틀")
    private String packTitle;

    @ApiModelProperty(value = "묶음상품 상품 PK")
    private String packGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "상품이미지")
    private String goodsImagePath;

    @ApiModelProperty(value = "건강기능식품 여부")
    private String healthGoodsYn;

}
