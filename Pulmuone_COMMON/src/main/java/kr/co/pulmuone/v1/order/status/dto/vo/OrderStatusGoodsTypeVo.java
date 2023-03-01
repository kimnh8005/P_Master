package kr.co.pulmuone.v1.order.status.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문상태 유형 VO")
public class OrderStatusGoodsTypeVo {

    @ApiModelProperty(value = "사용구분")
    private String useType;

    @ApiModelProperty(value = "유형코드")
    private String typeCd;

    @ApiModelProperty(value = "유형명")
    private String typeNm;

}
