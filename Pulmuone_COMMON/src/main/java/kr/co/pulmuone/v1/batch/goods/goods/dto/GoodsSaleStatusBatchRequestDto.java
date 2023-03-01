package kr.co.pulmuone.v1.batch.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@ApiModel(description = "goodsSaleStatus")
@Builder
public class GoodsSaleStatusBatchRequestDto {
    @ApiModelProperty(value = "상태")
    private String saleStatus;
    @ApiModelProperty(value = "기준일")
    private LocalDate searchDate;
    @ApiModelProperty(value = "상품타입")
    private String goodsType;
}
