package kr.co.pulmuone.v1.promotion.linkprice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "링크프라이스 주문정보 API DTO")
public class LinkPriceOrderListAPIResponseDto {

    @JsonIgnore
    private String reqtype = "";

    private LinkPriceOrderListDeviceDto linkprice;

    private LinkPriceOrderListOrderDto order;

    private List<LinkPriceOrderListProductsDto> products;
}
