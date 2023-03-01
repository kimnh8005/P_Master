package kr.co.pulmuone.v1.promotion.linkprice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.promotion.linkprice.service.ISO8601DateSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

@ToString
@Setter
@ApiModel(description = "링크프라이스 주문내역 API DTO")
public class LinkPriceOrderListProductsDto {

    @Getter
    @ApiModelProperty(value = "금액")
    private long product_final_price;

    @Getter
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(value = "결제일")
    private Date paid_at;

    @Getter
    @ApiModelProperty(value = "주문수량")
    private int quantity;

    @Getter
    @ApiModelProperty(value = "카테고리")
    private List<String> category_name;

    @ApiModelProperty(value = "카테고리 코드")
    private String category_code;

    @Getter
    @ApiModelProperty(value = "취소일")
    private String canceled_at;

    @Getter
    @ApiModelProperty(value = "상품코드")
    private String product_id;

    @Getter
    @ApiModelProperty(value = "취소일자")
    private String confirmed_at;

    @Getter
    @ApiModelProperty(value = "상품명")
    private String product_name;

    /* 배송유형(DELIVERY_TYPE : DELIVERY_TYPE.DAILY - 일일, DELIVERY_TYPE.REGULAR - 정기) <- 제외대상 */
    @JsonIgnore
    private String delivery_type;

    /* 상품브랜드 ID(미정산 브랜드 : 개발 - 95,99,129,130   상용 - 161,105,106,162) */
    @JsonIgnore
    private String ur_brand_id;

    /* 할인유형(GOODS_DISCOUNT_TP.EMPLOYEE : 임직원) */
    @JsonIgnore
    private String discount_tp;

    /* 사용 적립금 */
    @JsonIgnore
    private int point_price;

    public String getCategory_code() {

        // 1. 미정산 브랜드 체크
        // 2. 임직원 할인 체크
        // 3. 배송유형 체크
        if(PromotionConstants.ignoreBrandList.contains(this.ur_brand_id)    // 베이비밀, 디자인밀, 잇슬림, 녹즙 제외
                || "GOODS_DISCOUNT_TP.EMPLOYEE".equals(this.discount_tp)    // 임직원 할인 제외
                || "DELIVERY_TYPE.DAILY".equals(this.delivery_type)         // 일일배송 제외
                || "DELIVERY_TYPE.REGULAR".equals(this.delivery_type)       // 정기배송 제외
        ) {
            return "0000";
        }

        return this.category_code;
    }
}
