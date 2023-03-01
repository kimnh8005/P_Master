package kr.co.pulmuone.v1.store.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.WarehouseEnums.UndeliverableType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "출고처 주소 기반 배송 가능 정보 Dto")
public class WarehouseUnDeliveryableInfoDto {

    @ApiModelProperty(value = "출고처 배송 가능여부")
    private boolean isShippingPossibility;

    @ApiModelProperty(value = "출고처 배송불가 enum")
    private UndeliverableType undeliverableType;

    @ApiModelProperty(value = "출고처 배송불가권역유형 array")
    private String[] arrayUndeliverableType;

    @ApiModelProperty(value = "배송불가 내용")
    private String shippingImpossibilityMsg;
}
