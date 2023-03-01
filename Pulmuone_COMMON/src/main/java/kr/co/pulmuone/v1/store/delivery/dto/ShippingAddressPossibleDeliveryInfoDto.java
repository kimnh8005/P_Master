package kr.co.pulmuone.v1.store.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "주소 기반 배송 가능 타입정보 Dto")
public class ShippingAddressPossibleDeliveryInfoDto {

    @ApiModelProperty(value = "매장배송 가능여부")
    private boolean isStoreDelivery;

    @ApiModelProperty(value = "매장 명")
    private List<String> storeName;

    @ApiModelProperty(value = "일일배송 가능여부")
    private boolean isDailyDelivery;

    @ApiModelProperty(value = "일일배송 유형")
    private String dailyDeliveryType;

    @ApiModelProperty(value = "새벽배송 가능여부")
    private boolean isDawnDelivery;

    @ApiModelProperty(value = "택배배송 가능여부")
    private boolean isShippingCompDelivery;

}
