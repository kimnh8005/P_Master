package kr.co.pulmuone.v1.promotion.linkprice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "링크프라이스 주문정보 API DTO")
public class LinkPriceOrderListOrderDto {

    @ApiModelProperty(value = "결제자")
    private String user_name;

    @ApiModelProperty(value = "화폐단위")
    private String currency = PromotionConstants.LP_CURRENCY_CODE;

    @ApiModelProperty(value = "주문총금액")
    private long final_paid_price;

    @ApiModelProperty(value = "주문번호(ODID)")
    private String order_id;

    @JsonIgnore
    @ApiModelProperty(value = "주문번호(OD_ORDER_ID)")
    private String od_order_id;
}
