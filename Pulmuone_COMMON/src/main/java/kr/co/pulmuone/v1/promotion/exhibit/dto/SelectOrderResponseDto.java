package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class SelectOrderResponseDto {

    @ApiModelProperty(value = "장바구니 타입")
    private String cartType;

    @ApiModelProperty(value = "카드 PK")
    private Long spCartId;

    @ApiModelProperty(value = "품절상품 리스트")
    private List<SelectOrderNoSaleResponseDto> noSale;

    @ApiModelProperty(value = "재고부족 리스트")
    private List<SelectOrderNoStockResponseDto> noStock;

}
