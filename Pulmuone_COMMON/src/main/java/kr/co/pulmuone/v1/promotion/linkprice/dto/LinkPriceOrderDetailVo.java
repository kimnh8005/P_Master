package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "링크프라이스 주문내역 Vo")
public class LinkPriceOrderDetailVo {

    @ApiModelProperty(value = "주문 PK")
    private String odid;

    @ApiModelProperty(value = "주문번호")
    private long odOrderId;

    @ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문상세 순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "고객 ID")
    private String urUserId;

    @ApiModelProperty(value = "고객 이름")
    private String urUserName;

    @ApiModelProperty(value = "주문상태코드")
    private String orderStatusCd;

    @ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "결제금액")
    private long paidPrice;

    @ApiModelProperty(value = "결제일시")
    private Date paidDt;

    @ApiModelProperty(value = "카테고리코드")
    private String categoryCode;

    @ApiModelProperty(value = "카테고리명")
    private String categoryName;

    @ApiModelProperty(value = "카테고리명 리스트")
    private List<String> categoryNames;

    @ApiModelProperty(value = "브랜드아이디")
    private String urBrandId;

    @ApiModelProperty(value = "할인유형")
    private String discountTp;  /* 할인유형(GOODS_DISCOUNT_TP.EMPLOYEE : 임직원) */

    @ApiModelProperty(value = "배송유형")
    private String deliveryType;  /* 배송유형(DELIVERY_TYPE : DELIVERY_TYPE.DAILY - 일일, DELIVERY_TYPE.REGULAR - 정기) <- 제외대상 */

    @ApiModelProperty(value = "LPINFO쿠키")
    private String lpinfo;

    @ApiModelProperty(value = "user-agent")
    private String userAgent;

    @ApiModelProperty(value = "Client IP Address")
    private String ip;

    @ApiModelProperty(value = "Device Type")
    private String deviceType;

}
